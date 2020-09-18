import {ChangeDetectorRef, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {Router} from '@angular/router';
// import { routerTransition } from 'app/router.animations';
import {DialogListComponent} from 'app/shared/components/dialog-list/dialog-list.component';
import {UserService} from 'app/services/user.service'
import {ContactService} from 'app/services/contact.service';
import {ConfirmDialogComponent} from 'app/shared/components/confirm-dialog/confirm-dialog.component';
import { AppDataService } from 'app/services/app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';


@Component({
  selector: 'app-contacts',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.css'],
  // animations: [routerTransition()]
})
export class ContactsComponent implements OnInit {

  idOfJSON: any;
  public deleteResponse: any;
  public updatedPartnerData: any;
  specificPartnerData: any = null;
  isPopupOpened = true;
  employeeData = [];
  updatedPartnerDataInArray = [];
  empData: any = null;
  deletedData: any = null;
  onFilterMode = false;
  public changedState = false;
  @ViewChild(MatSort, null) sort: MatSort;
  @ViewChild(MatPaginator, null) paginator: MatPaginator;
  searchKey: string;
  dataSource;
  compAction: string;
  displayedColumns = ['action', 'status', 'username'];
  totalElements: any;

  constructor(private contactService: ContactService, private router: Router,
              private dialog: MatDialog, 
              @Inject(MAT_DIALOG_DATA) public data: any, 
              private changeDetectorRefs: ChangeDetectorRef,
              private userService: UserService,
              private appDataService: AppDataService) {
  }


  onSearchClear() {
    this.searchKey = '';
    this.applyFilter();
    this.onFilterMode = false;
  }

  applyFilter() {
    this.onFilterMode = true;
    this.dataSource.filter = this.searchKey.trim().toLowerCase();

  }


  inviteUser() {
    // tslint:disable-next-line:max-line-length

    const dialogRef = this.dialog.open(DialogListComponent, {
      minWidth: '40%',
      minHeight: '80%',
      data: {
        compAction: 'invite',
        contactsToAdd: [],
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.refresh();
      this.isPopupOpened = false;
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
    if (this.paginator.pageSize !== undefined) {
      pageSize = this.paginator.pageSize;
    }

    this.contactService.getAllContactsPage(this.paginator.pageIndex, pageSize, sort, direction).subscribe(data => {
      this.dataSource = new MatTableDataSource(data.content);
      this.dataSource.sort = this.sort;
      this.totalElements = data.totalElements;
    });
  }

  deleteContact(row) {
    row.message = 'Are you sure you want to delete ' + row.username + ' contact  ?';
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        contactToDelete: row,
        message: 'deletePartner'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.contactService.deleteContact(row.id).subscribe(response => {
        this.refresh();
      })

    });
  }

  ngOnInit() {
    this.paginator.pageSize = 100;
    this.refresh();
  }

  delay(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  acceptInvitation(row) {
    this.contactService.setInvitationApproved(row).subscribe(result => {
      this.refresh();
    });
  }

  clearInvitation(row) {

    row.message = 'Are you sure you want to reject ' + row.username + ' ?';

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: row
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.contactService.setInvitationRejected(row).subscribe(response => {
          this.refresh();
        })
      }

    });
  }


}

export class PartnerDataSource {
  constructor() {
  }


}
