import {ChangeDetectorRef, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {Router} from '@angular/router';
import {UserService} from 'app/services/user.service';
import {UserDto} from 'app/shared/dto/user-dto';
import {ConfirmDialogComponent} from 'app/shared/components/confirm-dialog/confirm-dialog.component';
import {NotificationToastService} from 'app/shared/utils/notification-toast-service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {

  isPopupOpened = true;
  partnerData: any = null;
  onFilterMode = false;
  compAction: string;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  searchKey: string;
  usernamesToPass: Object;
  buttonLabel: string;
  addedUserGroup = [];
  clonedDTO = new UserDto();
  dataSource;
  displayedColumns = ['actions', 'username', 'email', 'status', 'createdOn'];
  totalElements: any ;


  constructor(
      private userService: UserService,
      private router: Router,
      private dialog: MatDialog,
      @Inject(MAT_DIALOG_DATA) public data: any,
      private changeDetectorRefs: ChangeDetectorRef,
      private notificationToastService: NotificationToastService
  ) {}

  ngOnInit() {
    this.paginator.pageSize = 100;
    this.refresh();
  }

  handshake() {
    this.notificationToastService.showNotification(
        'Crm local to Crm central setup started.',
        'notifications',
        'top',
        'center',
        'warning');

    this.userService.handshake().subscribe(data => {
      this.refresh();
      this.notificationToastService.showNotification(
          'Crm local to Crm central setup finished.',
          'notifications',
          'top',
          'center',
          'success');

    });
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
    if (this.paginator.pageSize !== undefined){
      pageSize = this.paginator.pageSize ;
    }

    this.userService.getAllUsersPageable(this.paginator.pageIndex, pageSize, sort,
        direction).subscribe((data: any) => {
      this.dataSource = new MatTableDataSource(data.content);
       this.dataSource.sort = this.sort;
      this.dataSource.data  = data.content;
      this.totalElements = data.totalElements;
    });
  }

  deleteUser(selectedItem) {

    selectedItem.message = 'Are you sure, you want to delete user ' + selectedItem.email + ' ?';
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: selectedItem
    });

    dialogRef.afterClosed().subscribe(result => {
      this.userService.delete(selectedItem.id).subscribe(() => {
        this.refresh();
      });
    });
  }

  editUser(selectedItem) {

  }

  createUser() {
  }

}
