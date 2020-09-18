import { Component, OnInit, Inject, ChangeDetectorRef, ViewChild, AfterViewInit, ElementRef, NgZone } from '@angular/core';

import {MatTableModule} from '@angular/material/table';
import {MatTableDataSource, MatSort, MatDialog, MAT_DIALOG_DATA, MatDialogRef, MatPaginator, MatAutocomplete, MatAutocompleteSelectedEvent, MatChipInputEvent} from '@angular/material';
import { DataSource, SelectionModel } from '@angular/cdk/collections';
import {Observable} from 'rxjs';
import { Router } from '@angular/router';
// import { routerTransition } from 'app/router.animations';
import { UserService } from 'app/services/user.service';
import { UserGroupService } from 'app/services/user-group.service';
import { UserDto } from 'app/shared/dto/user-dto';
import { ContactsComponent } from 'app/pages/contacts/contacts.component';
import { UserGroupFormComponent } from 'app/pages/user-groups/user-group-form/user-group-form.component';
import { ContactService } from 'app/services/contact.service'
import { FormControl } from '@angular/forms';
import { ENTER, COMMA } from '@angular/cdk/keycodes';
import { startWith, map, take } from 'rxjs/operators';
import { CdkTextareaAutosize } from '@angular/cdk/text-field';
import { AppDataService } from 'app/services/app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';
import { ActiveSubstationService } from 'app/services/activeSubstation.service';


export interface PeriodicElement {
  id: string;
  username: string;
  email: string;
}

@Component({
    selector: 'app-dialog-list',
    templateUrl: './dialog-list.component.html',
    styleUrls: ['./dialog-list.component.css'],
    // animations: [routerTransition()]
})
export class DialogListComponent implements OnInit {

    highlighted?: boolean;
    hovered?: boolean;
    isPopupOpened = true;
    employeeData = [];
    displayedData: any = null;
    onFilterMode = false;
    @ViewChild(MatSort, null) sort: MatSort;
    @ViewChild(MatPaginator, null) paginator: MatPaginator;
    searchKey: string;
    dataSource;
    contactDTO = new Array();
    displayedColumns = [];
    columns = [];
    selectionArray: Object[] = [];
    selection = new SelectionModel<PeriodicElement>(true, []);
    visible = true;
    selectable = true;
    removable = true;
    addOnBlur = true;
    separatorKeysCodes: number[] = [ENTER, COMMA];
    partnerCtrl = new FormControl();
    filteredPartners: Observable<string[]>;
    partners: string[] = [];
    allPartners: string[] = [];
  @ViewChild('partnerInput', {static: false}) partnerInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto', {static: false}) matAutocomplete: MatAutocomplete;
  @ViewChild('autosize', {static: false}) autosize: CdkTextareaAutosize;

    constructor(private userService: UserService, private contactService: ContactService,
                private appDataService: AppDataService,
                private dialogRef: MatDialogRef<ContactsComponent, UserGroupFormComponent>,
                @Inject(MAT_DIALOG_DATA) public data: any, private _ngZone: NgZone, private activeSubstationService: ActiveSubstationService) { dialogRef.disableClose = true;
                    this.filteredPartners = this.partnerCtrl.valueChanges.pipe(
                        startWith(null),
                        map((partner: string | null) => partner ? this._filter(partner) : this.allPartners.slice())); }

    onSearchClear() {
        this.searchKey = '';
        this.applyFilter();
        this.onFilterMode = false;
    }
    onNoClick(): void {
      this.activeSubstationService.acceptOffer = false;
      this.activeSubstationService.rejectOffer = false;
      this.activeSubstationService.sendOffer = false  
        this.dialogRef.close();
    }
    
    responseNo(){
      this.activeSubstationService.acceptOffer = false;
      this.activeSubstationService.rejectOffer = false;
      this.activeSubstationService.sendOffer = false  
      this.dialogRef.close();
    }
    focusUsersListInput(){
      this.partnerInput.nativeElement.blur();
      this.partnerInput.nativeElement.focus();
    }

    acceptOffer(){
      this.activeSubstationService.acceptOffer = true;
      this.dialogRef.close();
    }
    rejectOffer(){
      this.activeSubstationService.rejectOffer = true;
      this.dialogRef.close();
    }

    sendOffer(){
      this.activeSubstationService.sendOffer = true;
      this.dialogRef.close();
    }

    triggerResize() {
      // Wait for changes to be applied, then trigger textarea resize.
      this._ngZone.onStable.pipe(take(1))
          .subscribe(() => this.autosize.resizeToFitContent(true));
    }

    applyFilter() {
        this.onFilterMode = true;
        this.dataSource.filter = this.searchKey.trim().toLowerCase();
    }

    onSelect(element: PeriodicElement) {
        if (element['checked'] === false) {
            this.selectionArray = this.selectionArray.filter(updatedList => updatedList['id'] !== element.id);
        } else {
            this.selectionArray.push(element);
        }
    }

    ngOnInit() {
       
        if (this.data.compAction === 'invite') {

            this.displayedColumns = ['checked','username', 'email'];
            this.userService.getAllUsers().subscribe(data => {
                for(let i=0;i<data.length;i++){
                  this.allPartners.push(data[i]['username'])
                }
                this.displayedData = data;
                this.dataSource = new MatTableDataSource(data);
                this.dataSource.sort = this.sort;
                this.dataSource.paginator = this.paginator;
            });


        } else if (this.data.compAction ==='createUsrGrp') {
            if (this.data.alreadyCheckedObject.length > 0) {
                this.selectionArray = this.data.alreadyCheckedObject
            }
            this.columns = this.data.columns

            this.displayedColumns = this.data.displayedColumns;


                this.dataSource = new MatTableDataSource(this.data.creationData);
                this.dataSource.sort = this.sort;
                this.dataSource.paginator = this.paginator;

        } else if (this.data.compAction === 'editUsrGrp') {
            this.columns = this.data.columns;

            this.displayedColumns = this.data.displayedColumns;
                this.selectionArray = this.data.selectedUsersToEdit;
                this.dataSource = new MatTableDataSource(this.data.dataToPass);
                this.dataSource.sort = this.sort;
                this.dataSource.paginator = this.paginator;

        }
    }

    postUsers() {
      if (this.data.compAction === 'invite') {
          this.formContactDTO();
          this.inviteContacts();
      }
  }
  formContactDTO(){
    this.contactDTO = this.partners;
  }

   inviteContacts(){
     this.contactService.postContacts(this.contactDTO).subscribe(
      response => {
          this.contactService.getAllContacts().subscribe(result => {
              this.dialogRef.close()
          });
      });
   }

  add(event: MatChipInputEvent): void {
    // Add partner only when MatAutocomplete is not open
    // To make sure this does not conflict with OptionSelected Event
    if (!this.matAutocomplete.isOpen) {
      const input = event.input;
      const value = event.value;

      // Add our partner
      if ((value || '').trim()) {
        this.partners.push(value.trim());
      }

      // Reset the input value
      if (input) {
        input.value = '';
      }

      this.partnerCtrl.setValue(null);
    }
  }

  remove(partner: string): void {
    const index = this.partners.indexOf(partner);

    if (index >= 0) {
      this.partners.splice(index, 1);
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.partners.push(event.option.viewValue);
    this.partnerInput.nativeElement.value = '';
    this.partnerCtrl.setValue(null);
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.allPartners.filter(partner => partner.toLowerCase().indexOf(filterValue) === 0);
  }
}

export interface Element {
    checkedLine:boolean;
    highlighted: boolean;
    hovered: boolean;
}
