import {ChangeDetectorRef, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatPaginator,
  MatSort,
  MatTableDataSource
} from '@angular/material';
import {UserGroupDTO} from 'app/shared/dto/user-group-dto';
import {Router} from '@angular/router';
import {MessagesService} from 'app/services/messages.service';
import {FormControl, FormGroup} from '@angular/forms';
import {DatePipe} from '@angular/common';
import {MomentDateAdapter} from '@angular/material-moment-adapter';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material/core';
import {HistoryService} from 'app/services/history.service';
import * as fileSaver from 'file-saver';
import {LoadingStateService} from 'app/shared/utils/loading-state.service';
import {ConfirmDialogComponent} from 'app/shared/components/confirm-dialog/confirm-dialog.component';
import {FileUploadRegistryService} from '../../services/FileUploadRegistry.service';
import { RoleGuard } from 'app/shared/guards/role.guard';
import { NotificationToastService } from 'app/shared/utils/notification-toast-service';

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

export interface MessagesData {
  name: string,
  tag: string,
  createdOn: number,
  userGroupId: string,
  senderId: string
}

@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.scss'],
  providers: [
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ]
})
export class DataComponent implements OnInit {

  public dateFrom: Date;
  public dateTo: Date;

  isPopupOpened = true;
  partnerData: any = null;
  onFilterMode = false;
  compAction: string;
  @ViewChild(MatSort, null) sort: MatSort;
  @ViewChild(MatPaginator, null) paginator: MatPaginator;
  searchKey: string;
  usernamesToPass: Object;
  buttonLabel: string;
  addedUserGroup = [];
  currentFilteredData: any;
  clonedDTO = new UserGroupDTO('', '', null);
  dataSource;
  updatedFilteredData: any
  updatedDataSource;
  displayedColumns = ['actions', 'name', 'tag', 'timeSent', 'userGroupId', 'senderId'];
  nameFilter: any;
  tagFilter: any;
  timeSentFilter = new FormControl();
  globalFilter = '';
  filteredValues = {
    name: '', tag: '', createdOn: '', userGroupId: '', senderId: ''
  };
  filterInDateFlag = false;
  pipe: DatePipe;
  customFilter: any = {};
  filterForm = new FormGroup({
    fromDate: new FormControl(),
    toDate: new FormControl(),
  });
  epochFromTime = 1;
  epochToTime = ((new Date).getTime()) + 24 * 60 * 60 * 1000;
  fromEvents: string[] = [];
  toEvents: string[] = [];
  totalElements: any;

  onFromSelect(event) {
    this.dateFrom = event;
    if (this.dateFrom > this.dateTo) {
      this.dateTo = this.dateFrom;
    }
  }

  onToSelect(event) {
    this.dateTo = event;
  }


  constructor(private messagesService: MessagesService, private historyService: HistoryService,
              private router: Router, private dialog: MatDialog, private loadingStateService: LoadingStateService,
              @Inject(
                  MAT_DIALOG_DATA) public data: any,
              private changeDetectorRefs: ChangeDetectorRef,
              private fileUploadRegistryService: FileUploadRegistryService,
              public datePipe: DatePipe, private notificationToastService: NotificationToastService) {
  }

  ngOnInit() {
    this.dateFrom = new Date();
    this.dateFrom.setDate(this.dateFrom.getDate() - 30);
    this.dateTo = new Date();
    this.nameFilter = '';
    this.tagFilter = '';
    this.paginator.pageSize = 50;
    this.refresh();
  }

  refresh() {

    let sort = '';
    let direction = 'desc';
    if (this.sort.active !== undefined) {
      this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
      sort = this.sort.active;
      direction = this.sort.direction;
    }
    let pageSize = 5;
    if (this.paginator.pageSize !== undefined) {
      pageSize = this.paginator.pageSize;
    }

    this.loadingStateService.showLoadingIndication();
    const from = this.datePipe.transform(this.dateFrom, 'yyyy-MM-dd') + 'T00:00:00Z';
    const to = this.datePipe.transform(this.dateTo, 'yyyy-MM-dd') + 'T23:59:00Z';

    this.messagesService.getPage(this.nameFilter, this.tagFilter, from, to,
        this.paginator.pageIndex, pageSize, sort,
        direction).subscribe(data => {
      // this.partnerData = data;

      this.dataSource = new MatTableDataSource(data.content);
      this.dataSource.sort = this.sort;
      this.totalElements = data.totalElements;
      this.loadingStateService.hideLoadingIndication();
    }, onError =>{

     /*  this.notificationToastService.showNotification('Crm Local not connected.', 'error', 'top',
      'center', 'danger'); */
      this.loadingStateService.hideLoadingIndication()

  });
  }

  downloadMessage(row) {
    if (row.tag === 'hippoio-results') {
      this.downloadHyppoIo(row);
    } else if (row.tag === 'file_upload' ||
        row.tag === 'order_upload' ||
        row.tag === 'weather_file') {
      this.downloadUserData(row);
    }
  }

  downloadUserData(row) {
    this.loadingStateService.showLoadingIndication()
    this.fileUploadRegistryService.downloadFromCrmCentral(row.id, row.metadata).subscribe(response => {
      const blob = new Blob([response.body], {type: 'application/' + row.metadata});
      fileSaver.saveAs(blob, row.name);
      this.loadingStateService.hideLoadingIndication();
    });
  }

  downloadHyppoIo(row) {
    this.loadingStateService.showLoadingIndication()
    this.historyService.downloadHyppoIoData(row.uniqueId).subscribe(response => {
      this.saveFile(response.body, row.name + '.zip');
      this.loadingStateService.hideLoadingIndication();
    });
  }

  saveFile(downloadedData: any, filename?: string) {
    const blob = new Blob([downloadedData], {type: 'application/zip'});
    fileSaver.saveAs(blob, filename);
  }

  deleteMessage(selectedItem) {
    const messageSelected = selectedItem
    messageSelected.message = 'Are you sure you want to delete ' + messageSelected.name + ' ?';
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: messageSelected
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.messagesService.deleteMessage(result.id).subscribe(response => {
        })
      }

    });
  }
}
