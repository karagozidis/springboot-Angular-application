import { Component, OnInit, EventEmitter, ViewChild, ElementRef, Input, Output } from '@angular/core';
import { AppDataService } from 'app/services/app-data.service';
import { NotificationToastService } from 'app/shared/utils/notification-toast-service';
import { OrderUploadService } from 'app/services/OrderUpload.service';
import { MatDialog, MatSort, MatPaginator, MatTableDataSource, MatDatepickerInputEvent, DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material';
import * as moment from 'moment'
import {FileUploader} from 'ng2-file-upload';
import { FileUploadRegistryService } from 'app/services/FileUploadRegistry.service';
import { UserGroupService } from 'app/services/user-group.service';
import * as fileSaver from 'file-saver';
import { ConfirmDialogComponent } from 'app/shared/components/confirm-dialog/confirm-dialog.component';
import { AppConstants } from 'app/shared/config/app-constants';
import { ActiveSubstationForecastUploadDTO } from 'app/shared/dto/activeSubstationForecastUpload-dto';
import { DatePipe } from '@angular/common';
import { MomentDateAdapter } from '@angular/material-moment-adapter';


export const MY_FORMATS = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'LL',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};


const uri = AppConstants.CRM_CENTRAL_URL + '/modules/active-substation-forecasts/files';
@Component({
  selector: 'app-active-substations-upload-forecasted-data',
  templateUrl: './active-substations-upload-forecasted-data.component.html',
  styleUrls: ['./active-substations-upload-forecasted-data.component.scss'],
  providers: [
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},

      {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ]
})
export class ActiveSubstationsUploadForecastedDataComponent implements OnInit {
    @ViewChild('chooseEnergyFileToEncrypt', {static: false})
    chooseEnergyFileToEncrypt: ElementRef;

    dataSource;
    displayedColumns = ['actions', 'filename', 'fromDate', 'toDate', 'tag'];

    @ViewChild(MatSort, null) sort: MatSort;
    @ViewChild(MatPaginator, null) paginator: MatPaginator;
    uploader: FileUploader = new FileUploader({url: uri});

    usergroups: any = [];
    selectedUserGroup = 0;
    displayValue = new Date();
    displayFromValue = new Date();
    displayToValue = new Date();
    displayEventsValue = new Date();
    selectedTag = 'cmrPrice'
    selectedHistoricalChoice = 'measurements'
    activeSubstationForecastFileDTO = new ActiveSubstationForecastUploadDTO()
    uploadHistoricalDataDescription = ''
    uploadEventsDescription = ''
    selectedHistoricalTag = 'historicalCmrPrice'

    constructor(
        private fileUploadRegistryService: FileUploadRegistryService,
        private dialog: MatDialog,
        private userGroupService: UserGroupService,
        private notificationToastService: NotificationToastService,
        private  appDataService: AppDataService,
        private dp: DatePipe,
    ) {
    }

  ngOnInit() {
    this.refresh();
  
  }
  refreshUserGroups() {
    /* this.userGroupService.getAllUserGroups().subscribe(data => {
        this.usergroups = data;
        for (let i = 0; i < this.usergroups.length; i++) {
            if (this.usergroups[i]['name'] === 'me') {
                // this.geHyppoService.setDefaultUserGroup(this.usergroups[i]['name']);
                this.selectedUserGroup = this.usergroups[i]['id']
            }
        }
    }); */
}
onChangeHour(event) {
  console.log('event', event);
}

refresh() {
    this.fileUploadRegistryService.getForecastFile().subscribe(data => {
      console.log(data)
        this.dataSource = new MatTableDataSource(data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
    });
}

uploadFile(files: FileList) {
    this.fileUploadRegistryService.uploadFileToCrmCentral(files.item(0), this.selectedUserGroup).subscribe(data => {
        this.refresh();
    }, error => {
    });
}

catchDateEvent(type: string, event: MatDatepickerInputEvent<Date>,dataFileVar){
  if(dataFileVar ==='displayValue'){
    this.displayValue = new Date(event.value)
  }
  if(dataFileVar ==='displayFromValue'){
    this.displayFromValue = new Date(event.value)
  }
  if(dataFileVar ==='displayToValue'){
    this.displayToValue = new Date(event.value)
  }
  if(dataFileVar ==='ddisplayEventsValue'){
    this.displayEventsValue = new Date(event.value)
  }
  
}

uploadFileEncrypted(files: FileList) {

    this.activeSubstationForecastFileDTO.file = files
    this.activeSubstationForecastFileDTO.usergroup = this.appDataService.userDto.activeSubstationUserGroupId;
    let foreCastDate = this.dp.transform(this.displayValue, 'yyyy-MM-ddThh:mm:ss') + 'Z'
    this.activeSubstationForecastFileDTO.forecastDate =foreCastDate
    this.activeSubstationForecastFileDTO.tag = this.selectedTag
    this.activeSubstationForecastFileDTO.dateTo = foreCastDate
    this.activeSubstationForecastFileDTO.type = '';
    this.activeSubstationForecastFileDTO.res = '0';
    console.log(this.activeSubstationForecastFileDTO)

    this.fileUploadRegistryService.uploadForecastFileToCrmLocal(files.item(0),this.activeSubstationForecastFileDTO).subscribe(data => {
        this.refresh();
    }, error => {
      this.notificationToastService.showNotification(
          'Error uploading file',
          'delete',
          'top',
          'center',
          'danger');
    });

    this.chooseEnergyFileToEncrypt.nativeElement.value='';
}

uploadHistoricalData(files: FileList){
  this.activeSubstationForecastFileDTO.file = files
  this.activeSubstationForecastFileDTO.usergroup = this.appDataService.userDto.activeSubstationUserGroupId;
  let foreCastFromDate = this.dp.transform(this.displayFromValue, 'yyyy-MM-ddThh:mm:ss') + 'Z'
  this.activeSubstationForecastFileDTO.forecastDate =foreCastFromDate
  let forecastToDate = this.dp.transform(this.displayToValue, 'yyyy-MM-ddThh:mm:ss') + 'Z'
  this.activeSubstationForecastFileDTO.dateTo =forecastToDate
  this.activeSubstationForecastFileDTO.description = this.uploadHistoricalDataDescription
  this.activeSubstationForecastFileDTO.type = this.selectedHistoricalChoice
  this.activeSubstationForecastFileDTO.tag = this.selectedHistoricalTag

  console.log(this.activeSubstationForecastFileDTO)
  this.fileUploadRegistryService.uploadForecastFileToCrmLocal(files.item(0),this.activeSubstationForecastFileDTO).subscribe(data => {
    this.refresh();
}, error => {
  this.notificationToastService.showNotification(
      'Error uploading file',
      'delete',
      'top',
      'center',
      'danger');
});

this.chooseEnergyFileToEncrypt.nativeElement.value='';

}

uploadEvents(files: FileList){
  this.activeSubstationForecastFileDTO.file = files
  this.activeSubstationForecastFileDTO.usergroup = this.appDataService.userDto.activeSubstationUserGroupId;
  let foreCastEventsDate = this.dp.transform(this.displayEventsValue, 'yyyy-MM-ddThh:mm:ss') + 'Z'
  this.activeSubstationForecastFileDTO.forecastDate =foreCastEventsDate
  this.activeSubstationForecastFileDTO.description = this.uploadEventsDescription
  /* this.activeSubstationForecastFileDTO.dateTo = this.dp.transform(new Date(), 'yyyy-MM-ddThh:mm:ss') + 'Z' */
  this.activeSubstationForecastFileDTO.dateTo = foreCastEventsDate
  this.activeSubstationForecastFileDTO.tag = 'event'
  this.activeSubstationForecastFileDTO.type = '';
  this.activeSubstationForecastFileDTO.res = '0';
  
  console.log(this.activeSubstationForecastFileDTO)
  this.fileUploadRegistryService.uploadForecastFileToCrmLocal(files.item(0),this.activeSubstationForecastFileDTO).subscribe(data => {
    this.refresh();
}, error => {
  this.notificationToastService.showNotification(
      'Error uploading file',
      'delete',
      'top',
      'center',
      'danger');
});

this.chooseEnergyFileToEncrypt.nativeElement.value='';
}

formatDate(timestamp: string) {
    return moment(new Date(timestamp)).format('DD/MM/YYYY')
}

download(row) {
// alert(row.uniqueId);
    this.fileUploadRegistryService.downloadForecastDataFromCrmLocal(row.uniqueId, 'csv').subscribe(response => {
      const blob = new Blob([response.body], {type: 'application/csv'  });
      fileSaver.saveAs(blob, row.originalFileName);
    });
  
}

delete(selectedRowToPass) {
    console.log(selectedRowToPass)
    selectedRowToPass.message = 'Are you sure you want to delete ' + selectedRowToPass.originalFileName + ' ?';
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        data:{
          mode: 'deleteUploadedForecastFile',
          message: selectedRowToPass.message,
          rowUniqueId: selectedRowToPass.uniqueId
        } 
        
    });

    dialogRef.afterClosed().subscribe(result => {
        console.log(result)
        if (result!=undefined) {
              this.fileUploadRegistryService.deleteForecastData(result.rowUniqueId).subscribe(response => {
                this.refresh();
            })  
        }
    });
}
  
}
