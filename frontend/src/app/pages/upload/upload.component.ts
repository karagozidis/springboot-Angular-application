import { Component, OnInit, ViewChild } from '@angular/core';
import { ConfirmDialogComponent } from 'app/shared/components/confirm-dialog/confirm-dialog.component';
import { MatTableDataSource, MatPaginator, MatSort, MatDialog } from '@angular/material';
import { FileUploader,  FileItem } from 'ng2-file-upload';
import { FileUploadRegistryService } from 'app/services/FileUploadRegistry.service';
import * as moment from 'moment'
import * as fileSaver from 'file-saver';
import { AppConstants } from 'app/shared/config/app-constants';
import { AppDataService } from 'app/services/app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';

const uri = AppConstants.CRM_CENTRAL_URL  + '/modules/file-uploader/files';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss']
})
export class UploadComponent implements OnInit {

  dataSource;
  displayedColumns = ['actions', 'filename', 'createdOn', 'progressBar'];
  fileUploadFlag = false;
  

  @ViewChild(MatSort, null) sort: MatSort;
  @ViewChild(MatPaginator, null) paginator: MatPaginator;
  uploader: FileUploader = new FileUploader({url: uri});
  progressVal;

  constructor(
    private fileUploadRegistryService: FileUploadRegistryService,
    private appDataService: AppDataService,
    private dialog: MatDialog,
    ) {
      this.uploader.onProgressItem = (progress: any) => {
        this.progressVal = progress['progress']
        console.log(progress['progress']);
      // this.uploader_.onProgressItem = (progress_: any) => {
      //     console.log(progress_['progress']);
      // }
    };
    this.uploader.onCompleteItem = (item: any, response: any , status: any, headers: any) => {
      this.fileUploadFlag = false;
    }
    }

  ngOnInit() {
    this.refresh();
    }


  refresh() {
    this.fileUploadRegistryService.getAll().subscribe(data => {
        this.dataSource = new MatTableDataSource(data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      });
  }


  uploadFile(files: FileList) {
     this.fileUploadRegistryService.uploadFileToCrmCentral(files.item(0), 0).subscribe(data => {
      this.refresh();
      }, error => {
      });

  }

  formatDate(timestamp: string) {
    return moment(new Date(timestamp)).format('HH:mm  DD/MM/YYYY')
  }

  download(selectedRowToPass) {
    this.fileUploadRegistryService.downloadFromCrmCentral(selectedRowToPass.id, selectedRowToPass.metadata).subscribe(response => {
        const blob = new Blob([response.body], {type: 'application/' + selectedRowToPass.metadata});
        fileSaver.saveAs(blob, selectedRowToPass.name);
    });
  }


  delete(selectedRowToPass) {

    selectedRowToPass.message = 'Are you sure you want to delete ' + selectedRowToPass.name + ' ?';
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: selectedRowToPass
  });


  dialogRef.afterClosed().subscribe(result => {

    if (result) {
                this.fileUploadRegistryService.delete(result.id).subscribe(response =>{
                  this.refresh();
                })
            }

    });


  }


}
