import { Component, OnInit, ChangeDetectorRef, Inject, ViewChild } from '@angular/core';
import { UserGroupService } from 'app/services/user-group.service';
import { Router } from '@angular/router';
import { MAT_DIALOG_DATA, MatDialog, MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { HistoryService } from 'app/services/history.service';
import * as fileSaver from 'file-saver';
import { MatProgressSpinnerModule } from '@angular/material';
import { LoadingStateService } from 'app/shared/utils/loading-state.service';
import { HyppoIOExecutionStatus } from 'app/shared/config/app-constants';
import { ConfirmDialogComponent } from 'app/shared/components/confirm-dialog/confirm-dialog.component';
import { InternalMessageService } from 'app/shared/utils/internal-message.service';
import { Observable } from 'rxjs';
import { forkJoin } from 'rxjs';
import * as moment from 'moment'
import { AppDataService } from 'app/services/app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';
import { NotificationToastService } from 'app/shared/utils/notification-toast-service';

@Component({
  selector: 'ge-hyppo-io-history',
  templateUrl: './ge-hyppo-io-history.component.html',
  styleUrls: ['./ge-hyppo-io-history.component.scss']
})
export class GeHyppoIoHistoryComponent implements OnInit {
  empData: any = null;
  dataSource;
  executionStatus = HyppoIOExecutionStatus
  @ViewChild(MatSort, null) sort: MatSort;
  @ViewChild(MatPaginator, null) paginator: MatPaginator;
  searchKey: string;
  displayedColumns = [ 'actions', 'name', 'description', 'calculation_date', 'senderUsername', 'userGroupName', 'location',
  'status', 'size' ];
  messageServiceSubscription

  constructor(private userGroupService: UserGroupService, private historyService: HistoryService,
    private appDataService: AppDataService,
    private router: Router, private dialog: MatDialog, private loadingStateService: LoadingStateService,
    @Inject(MAT_DIALOG_DATA) public data: any,  private changeDetectorRefs: ChangeDetectorRef,
    private msgService: InternalMessageService, private notificationToastService: NotificationToastService) {}

    ngOnInit() {
        this.refresh()
        this.subscribeToMessageService()
    }

    refresh() {
        this.loadingStateService.showLoadingIndication()
        this.historyService.getHistory().subscribe(data => {
        this.empData = data;
        console.log(data);
        this.dataSource = new MatTableDataSource(data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;

        this.loadingStateService.hideLoadingIndication()
        }, onError =>{

            this.notificationToastService.showNotification('Crm Local not connected.', 'error', 'top',
            'center', 'danger');
            this.loadingStateService.hideLoadingIndication()

        }
        );
    }

    subscribeToMessageService() {
        const that = this;
        this.messageServiceSubscription = this.msgService.accessMessage('RefreshNavBar').subscribe(
            (msg) => {
                that.refresh();
            });
        }

  openSpecificResult(selectedRowToPass) {
    this.historyService.selectedData = selectedRowToPass;
    const a = (<HTMLElement>document.querySelectorAll('.mat-tab-label')[2] ).click();
    }

    ngOnDestroy() {
        this.messageServiceSubscription.unsubscribe();
    }

    downloadContent(selectedRowToPass) {
        this.loadingStateService.showLoadingIndication()
        this.historyService.downloadZip(selectedRowToPass.uuid).subscribe(response => {
            this.saveFile(response.body, selectedRowToPass.uuid + '.zip');
            this.loadingStateService.hideLoadingIndication()
        });
    }

    copyFromCentral(selectedRowToPass) {
        selectedRowToPass.location = 'LOADING';
        this.historyService.copyLocally(selectedRowToPass.uuid).subscribe(response => {
            // this.saveFile(response.body, selectedRowToPass.uuid + '.zip');
            selectedRowToPass.location = 'SYNC';
        });
    }

    getStatusColumnColor(result) {
        switch (result.status) {
            case HyppoIOExecutionStatus.EXECUTED:
            return "#005c00";
            case HyppoIOExecutionStatus.RUNNING:
            return "#1b4579";
            case HyppoIOExecutionStatus.FAILED:
            return "#690000";
        }
    }

    formatDate(timestamp: string) {
        return moment(new Date(timestamp)).format('HH:mm  DD/MM/YYYY')
    }

    saveFile(downloadedData: any, filename?: string) {
        const blob = new Blob([downloadedData], {type: 'application/zip'});
        fileSaver.saveAs(blob, filename);
    }

    deleteResult(selectedItem){
        const resultSelected = selectedItem
        resultSelected.message = 'Are you sure you want to delete ' + resultSelected.name + ' ?';
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
            data: resultSelected
        });

        dialogRef.afterClosed().subscribe(result => {
            console.log(result)
            forkJoin(
            this.historyService.deleteResultInLocal(result.uuid),
            this.historyService.deleteResultInCentral(result.uuid)
            )
            .subscribe(response => {
                console.log(response)
                this.historyService.getHistory().subscribe(data => {
                    console.log(data);
                    this.dataSource = new MatTableDataSource(data);
                    this.dataSource.sort = this.sort;
                    this.dataSource.paginator = this.paginator;
                });
            });
        });
    }

}
