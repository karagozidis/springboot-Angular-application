import { Component, OnInit, ChangeDetectorRef, Inject, ViewChild } from '@angular/core';
import { AppDataService } from 'app/services/app-data.service';
import { Router } from '@angular/router';
import { MatDialog, MAT_DIALOG_DATA, MatSort, MatPaginator, MatTableDataSource } from '@angular/material';
import { LoadingStateService } from 'app/shared/utils/loading-state.service';
import { InternalMessageService } from 'app/shared/utils/internal-message.service';
import * as moment from 'moment'
import { ConfirmDialogComponent } from 'app/shared/components/confirm-dialog/confirm-dialog.component';
import { forkJoin } from 'rxjs';
import * as fileSaver from 'file-saver';
import { ActiveSubstationHistoryService } from 'app/services/activeSubstation-history.service';
import { AppConstants } from 'app/shared/config/app-constants';
import { ActiveSubstationStateService } from 'app/services/activeSubstationState.service';

@Component({
  selector: 'app-active-substations-history',
  templateUrl: './active-substations-history.component.html',
  styleUrls: ['./active-substations-history.component.scss']
})
export class ActiveSubstationsHistoryComponent implements OnInit {
  historyData: any = null;
  dataSource;
  @ViewChild(MatSort, null) sort: MatSort;
  @ViewChild(MatPaginator, null) paginator: MatPaginator;
  searchKey: string;
  displayedColumns = [ 'actions', 'name', 'description', 'calculation_date', 
  'status', 'location' ]; 
  messageServiceSubscription
  isPopupOpened = true;
  showResultsLoadingButton = false
  constructor(private appDataService: AppDataService,
    private router: Router, private dialog: MatDialog, private loadingStateService: LoadingStateService,
    @Inject(MAT_DIALOG_DATA) public data: any,  private changeDetectorRefs: ChangeDetectorRef,
    private msgService: InternalMessageService, private activeSubstationHistoryService: ActiveSubstationHistoryService, private activeSubstationStateService: ActiveSubstationStateService) { }

  ngOnInit() {
      this.refresh();
  }
  refresh() {
    let resultsLoadingButtonTimer = setTimeout(() => {this.showResultsLoadingButton = true})
    let userGroup = this.appDataService.userDto.activeSubstationUserGroupId;
    this.activeSubstationHistoryService.getHistory(userGroup).subscribe(data => {
    this.historyData = data;
    console.log(data);
    this.dataSource = new MatTableDataSource(data);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

    });
    clearTimeout(resultsLoadingButtonTimer)
    this.showResultsLoadingButton = false
}
downloadFileList(row){
    console.log(row)
    this.loadingStateService.showLoadingIndication()
    this.activeSubstationHistoryService.downloadZip(row['uuid']).subscribe(response => {  
        this.saveFile(response.body, row['uuid'] + '.zip');
        this.loadingStateService.hideLoadingIndication()
    });
}

subscribeToMessageService() {
    const that = this;
    this.messageServiceSubscription = this.msgService.accessMessage('RefreshNavBar').subscribe(
        (msg) => {
            that.refresh();
        });
}
openResultsTab(row){
     this.activeSubstationStateService.historyResult = row['uuid'];
     const a = (<HTMLElement>document.querySelectorAll('.mat-tab-label')[3] ).click();
    /*  clearTimeout(resultsLoadingButtonTimer)
     this.showResultsLoadingButton = false */

}

    formatBackendResponse(response){
        switch(response){
            case AppConstants.ActiveSubstation.BackendEnums.STATUS.EXECUTED:
                return  AppConstants.ActiveSubstation.UILabels.STATUS.EXECUTED
            
            case AppConstants.ActiveSubstation.BackendEnums.STATUS.NOT_EXECUTED:
                return  AppConstants.ActiveSubstation.UILabels.STATUS.NOT_EXECUTED
            
            case AppConstants.ActiveSubstation.BackendEnums.STATUS.ON_EXECUTION:
                return  AppConstants.ActiveSubstation.UILabels.STATUS.ON_EXECUTION

            case AppConstants.ActiveSubstation.BackendEnums.STATUS.OFFER_SENT:
                return  AppConstants.ActiveSubstation.UILabels.STATUS.OFFER_SENT 
        }
    }

    formatLocationResponse(response){
        switch(response){
            case AppConstants.ActiveSubstation.BackendEnums.LOCATION.CRM_CENTRAL:
                return  AppConstants.ActiveSubstation.UILabels.LOCATION.CRM_CENTRAL
            
            case AppConstants.ActiveSubstation.BackendEnums.LOCATION.CRM_LOCAL:
                return  AppConstants.ActiveSubstation.UILabels.LOCATION.CRM_LOCAL
            
            case AppConstants.ActiveSubstation.BackendEnums.LOCATION.SYNC:
                return  AppConstants.ActiveSubstation.UILabels.LOCATION.SYNC
        }
    }

openSpecificResult(selectedRowToPass) {
/* this.historyService.selectedData = selectedRowToPass;
const a = (<HTMLElement>document.querySelectorAll('.mat-tab-label')[2] ).click(); */
}

ngOnDestroy() {
    //this.messageServiceSubscription.unsubscribe();
}

downloadContent(selectedRowToPass) {
    this.loadingStateService.showLoadingIndication()
    /* this.historyService.downloadZip(selectedRowToPass.uuid).subscribe(response => {
        this.saveFile(response.body, selectedRowToPass.uuid + '.zip');
        this.loadingStateService.hideLoadingIndication()
    }); */
}

copyFromCentral(selectedRowToPass) {
    selectedRowToPass.location = 'LOADING';
    this.activeSubstationHistoryService.copyLocally(selectedRowToPass.uuid).subscribe(response => {
        // this.saveFile(response.body, selectedRowToPass.uuid + '.zip');
        selectedRowToPass.location = 'SYNC';
    });
}

getStatusColumnColor(result) {
    switch (result.status) {
        /* case HyppoIOExecutionStatus.EXECUTED:
        return "#005c00";
        case HyppoIOExecutionStatus.RUNNING:
        return "#1b4579";
        case HyppoIOExecutionStatus.FAILED:
        return "#690000"; */
    }
}

formatDate(timestamp: string) {
    return moment(new Date(timestamp)).format('DD/MM/YYYY')
}

saveFile(downloadedData: any, filename?: string) {
    const blob = new Blob([downloadedData], {type: 'application/zip'});
    fileSaver.saveAs(blob, filename);
}

deleteResult(selectedItem){
    let userGroup = this.appDataService.userDto.activeSubstationUserGroupId;
    const resultSelected = selectedItem
    resultSelected.message = 'Are you sure you want to delete ' + resultSelected.name + ' ?';
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        minWidth: '20%',
        minHeight: '25%',
        data: resultSelected,
        disableClose:true
    });

    dialogRef.afterClosed().subscribe(result => {
        console.log(result)
         this.activeSubstationHistoryService.deleteResultInLocal(resultSelected.uuid).subscribe(response => {
            console.log(response)
            if(this.activeSubstationHistoryService.deleteHistory !== true){
                return
            }
            else{
            this.activeSubstationHistoryService.getHistory(userGroup).subscribe(data => {
                this.historyData = data;
                console.log(data);
                this.dataSource = new MatTableDataSource(data);
                this.dataSource.sort = this.sort;
                this.dataSource.paginator = this.paginator;
            
                });
            }
        });
    });
}

}
