import {ChangeDetectorRef, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatPaginator,
  MatSort,
  MatTableDataSource
} from '@angular/material';
import {Router} from '@angular/router';
import {UserGroupService} from 'app/services/user-group.service';
import {UserGroupFormComponent} from './user-group-form/user-group-form.component';
import {UserGroupDTO} from 'app/shared/dto/user-group-dto';
import {ConfirmDialogComponent} from 'app/shared/components/confirm-dialog/confirm-dialog.component';
import { AppDataService } from 'app/services/app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';


@Component({
  selector: 'app-user-groups',
  templateUrl: './user-groups.component.html',
  styleUrls: ['./user-groups.component.css'],
})
export class UserGroupsComponent implements OnInit {
  isPopupOpened = true;
  partnerData: any = null;
  onFilterMode = false;
  compAction: string;
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  searchKey: string;
  usernamesToPass: Object;
  buttonLabel: string;
  addedUserGroup = [];
  clonedDTO = new UserGroupDTO('', '', null);
  dataSource;
  displayedColumns = ['actions', 'name', 'description', 'members'];
  totalElements: any;

  constructor(
      private userGroupService: UserGroupService,
      private router: Router, private dialog: MatDialog,
      private appDataService: AppDataService,
      @Inject(MAT_DIALOG_DATA) public data: any,
      private changeDetectorRefs: ChangeDetectorRef) {

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

  ngOnInit() {
    this.paginator.pageSize = 100;
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

    this.userGroupService.getAllUserGroupsPage(this.paginator.pageIndex, pageSize, sort,
        direction).subscribe(data => {
      this.partnerData = data;
      console.log(data);
      this.dataSource = new MatTableDataSource(data.content);
      this.dataSource.sort = this.sort;
      this.totalElements = data.totalElements;
    });

  }

  createUserGroup() {
    this.processUserDTORequest(new UserGroupDTO('', '', null), 'createUserGroup');

  }

  editUserGroup(selectedItem) {
    this.isPopupOpened = true;
    const userGroupRowSelected = this.partnerData.find(c => c.name === selectedItem.name);
    console.log(userGroupRowSelected);
    this.userGroupService.idOfDTOToUpdate = userGroupRowSelected['id']
    this.processUserDTORequest(userGroupRowSelected, 'editUserGroup')
  }

  processUserDTORequest(userGroupDTOToProcess: UserGroupDTO, action: string) {
    if (action === 'createUserGroup') {
      const dialogRef = this.dialog.open(UserGroupFormComponent, {
        data: {
          compAction: 'createUserGroup',
          addedUserGroup: UserGroupDTO
        }
      });

      dialogRef.afterClosed().subscribe(result => {
        this.userGroupService.getAllUserGroups().subscribe(data => {
          this.partnerData = data;
          this.dataSource = new MatTableDataSource(data);
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        });
        this.isPopupOpened = false;
      });
    } else {

      const myClonedObject = Object.assign({}, userGroupDTOToProcess);
      const clonedDtoToPass = new UserGroupDTO(
          // tslint:disable-next-line:max-line-length
          userGroupDTOToProcess['name'], userGroupDTOToProcess['description'],
          Object.assign([], userGroupDTOToProcess['users']))
          const dialogRef = this.dialog.open(UserGroupFormComponent, {
              data: {
              userDTOToUpdate: clonedDtoToPass,
              userGroupDTOId: this.userGroupService.idOfDTOToUpdate
              }
          });
      dialogRef.afterClosed().subscribe(result => {
          this.userGroupService.getAllUserGroups().subscribe(data => {
          this.partnerData = data;
          this.dataSource = new MatTableDataSource(data);
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        });
      });
    }
  }

  deleteUserGroup(row) {
    row.message = 'Are you sure you want to delete ' + row.name + ' ?';
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: row
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userGroupService.deleteUserGroup(result.id).subscribe(response => {
          this.userGroupService.getAllUserGroups().subscribe(data => {
            this.partnerData = data;
            this.dataSource = new MatTableDataSource(data);
            this.dataSource.sort = this.sort;
            this.dataSource.paginator = this.paginator;
          });
        })
      }
    });
  }
}

export class PartnerDataSource {
  constructor() {
  }
}
