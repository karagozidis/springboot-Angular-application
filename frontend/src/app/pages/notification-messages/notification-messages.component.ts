import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {NotificationService} from 'app/services/notification.service';
import {MAT_DIALOG_DATA, MatDialog, MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {InternalMessageService} from 'app/shared/utils/internal-message.service'
import * as moment from 'moment'
import {Router} from '@angular/router';
import {LoadingStateService} from 'app/shared/utils/loading-state.service';
import {AppDataService} from 'app/services/app-data.service';
import {AppType} from 'app/shared/config/app-constants';

@Component({
  selector: 'app-notification-messages',
  templateUrl: './notification-messages.component.html',
  styleUrls: ['./notification-messages.component.scss']
})
export class NotificationMessagesComponent implements OnInit {
  data: any = null;
  appType
  AppType = AppType;
  dataSource;
  isPopupOpened = true;
  @ViewChild(MatSort, null) sort: MatSort;
  @ViewChild(MatPaginator, null) paginator: MatPaginator;
  displayedColumns = ['actions', 'createdOn', 'name', 'description'];
  totalElements: any;

  constructor(private notificationService: NotificationService,
              private msgService: InternalMessageService,
              private router: Router,
              @Inject(MAT_DIALOG_DATA) public anyData: any,
              private dialog: MatDialog,
              private loadingStateService: LoadingStateService,
              private appDataService: AppDataService) {
  }

  ngOnInit() {
    this.appType = this.appDataService.appType;
    this.paginator.pageSize = 50;
    this.refresh();
  }

  public updateStatus(id, event) {
    event.stopPropagation();
    this.notificationService.editStatus(id, 'viewed').subscribe(any => {
      this.refresh();
      this.notifyNavbar();
    });
  }

  markAllAsRead() {
    this.notificationService.editAllStatuses('viewed').subscribe(any => {
      this.refresh();
      this.notifyNavbar();
    });
  }

  openDialog(row) {
    this.notificationService.editStatus(row.id, 'viewed').subscribe(any => {
      this.refresh();
      this.notifyNavbar();
    });
    if (this.appType === AppType.MARKET) {
      this.router.navigate(['/marketNotificationForm/' + row.id])
    } else {
      this.router.navigate(['/notificationForm/' + row.id])
    }

  }

  onRowClicked(row) {
    this.openDialog(row)
  }

  openNotification(row) {
    this.openDialog(row)
  }

  notifyNavbar() {
    this.msgService.publishMessage('RefreshNavBar', 'FromNotificationMessages');
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

    this.notificationService.getPage(this.paginator.pageIndex, pageSize, sort,
        direction).subscribe(data => {
      data.content.forEach(function (row) {
        row.descriptionPreview = row.description;
        row.createdOn = moment(new Date(row.createdOn)).format('HH:mm  DD/MM/YYYY');
        if (row.description.length > 100) {
          row.descriptionPreview = row.description.substring(0, 100) + ' .....';
        }
      });

      this.dataSource = new MatTableDataSource(data.content);
      this.dataSource.sort = this.sort;
      this.totalElements = data.totalElements;
      this.loadingStateService.hideLoadingIndication();
    });
  }


}
