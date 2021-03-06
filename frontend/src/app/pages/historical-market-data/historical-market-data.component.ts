import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDialog, MatPaginator, MatSort, MatTableDataSource} from "@angular/material";
import {FileUploader} from "ng2-file-upload";
import {FileUploadRegistryService} from "../../services/FileUploadRegistry.service";
import {UserGroupService} from "../../services/user-group.service";
import {NotificationToastService} from "../../shared/utils/notification-toast-service";
import {AppDataService} from "../../services/app-data.service";
import * as moment from "moment";
import {ConfirmDialogComponent} from "../../shared/components/confirm-dialog/confirm-dialog.component";
import * as fileSaver from 'file-saver';
import {AppConstants} from "../../shared/config/app-constants";

const uri = AppConstants.CRM_CENTRAL_URL + +'/modules/file-uploader/files';

@Component({
  selector: 'app-historical-market-data',
  templateUrl: './historical-market-data.component.html',
  styleUrls: ['./historical-market-data.component.scss']
})
export class HistoricalMarketDataComponent implements OnInit {


    dataSource;
    displayedColumns = ['actions', 'filename', 'createdOn', 'createdBy', 'userGroupName'];

    @ViewChild(MatSort, null) sort: MatSort;
    @ViewChild(MatPaginator, null) paginator: MatPaginator;
    uploader: FileUploader = new FileUploader({url: uri});

    usergroups: any = [];
    selectedUserGroup = 0;


    constructor(
        private fileUploadRegistryService: FileUploadRegistryService,
        private dialog: MatDialog,
        private userGroupService: UserGroupService,
        private notificationToastService: NotificationToastService,
        private  appDataService: AppDataService
    ) { }

  ngOnInit() {
      this.refresh();
      this.refreshUserGroups();
  }

    refreshUserGroups() {
        this.userGroupService.getAllUserGroups().subscribe(data => {
            this.usergroups = data;
            for (let i = 0; i < this.usergroups.length; i++) {
                if (this.usergroups[i]['name'] === 'me') {
                    // this.geHyppoService.setDefaultUserGroup(this.usergroups[i]['name']);
                    this.selectedUserGroup = this.usergroups[i]['id']
                }
            }
        });
    }


    refresh() {
        this.fileUploadRegistryService.getHistoricalMarketData().subscribe(data => {
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
            this.dataSource.paginator = this.paginator;
        });
    }

    uploadFile(files: FileList) {
        this.fileUploadRegistryService.uploadHistoricalDataToCrmCentral(files.item(0), this.selectedUserGroup).subscribe(data => {
            this.refresh();
        }, error => {
        });
    }

    uploadFileEncrypted(files: FileList) {

        if (this.selectedUserGroup == 0) {
            this.notificationToastService.showNotification(
                'User group field must not be empty for encrypted file uploads',
                'delete',
                'top',
                'center',
                'danger');

            return;
        }

        this.fileUploadRegistryService.uploadHistoricalDataFileToCrmLocal(files.item(0), this.selectedUserGroup).subscribe(data => {
            this.refresh();
        }, error => {
            this.notificationToastService.showNotification(
                'Error uploading file',
                'delete',
                'top',
                'center',
                'danger');
        });
    }

    formatDate(timestamp: string) {
        return moment(new Date(timestamp)).format('HH:mm  DD/MM/YYYY')
    }

    download(row) {

        if(row.tag === 'historical_market_data'){
            this.fileUploadRegistryService.downloadFromCrmCentral(row.id, row.metadata).subscribe(response => {
                const blob = new Blob([response.body], {type: 'application/' + row.metadata});
                fileSaver.saveAs(blob, row.name);
            });
        } else {
            this.fileUploadRegistryService.downloadFromCrmLocal(row.uniqueId, row.metadata).subscribe(response => {
                const blob = new Blob([response.body], {type: 'application/' + row.metadata});
                fileSaver.saveAs(blob, row.name);
            });
        }

    }

    delete(selectedRowToPass) {
        selectedRowToPass.message = 'Are you sure you want to delete ' + selectedRowToPass.name + ' ?';
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
            data: selectedRowToPass
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.fileUploadRegistryService.delete(result.id).subscribe(response => {
                    this.refresh();
                })
            }
        });
    }

}
