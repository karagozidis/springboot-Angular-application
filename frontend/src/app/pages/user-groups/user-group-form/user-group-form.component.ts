   import { Component, OnInit, Inject, Input, ElementRef, ViewChild } from '@angular/core';
   import { UserService } from 'app/services/user.service';
   import { UserGroupService } from 'app/services/user-group.service';
   import { Router } from '@angular/router';
   // tslint:disable-next-line:max-line-length
   import { MatDialog, MAT_DIALOG_DATA, MatDialogRef,MatChipsModule, MatAutocomplete, MatAutocompleteSelectedEvent, MatChipInputEvent, MatAutocompleteTrigger } from '@angular/material';
   import { UserGroupDTO } from 'app/shared/dto/user-group-dto';
   // import { UserGroupsComponent } from '../user-groups/user-groups.component';
   import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
   import { DialogListComponent } from 'app/shared/components/dialog-list/dialog-list.component';
   import { UserGroupsComponent } from '../user-groups.component';
   import { ContactService } from 'app/services/contact.service';
   import { ENTER, COMMA } from '@angular/cdk/keycodes';
   import { Observable } from 'rxjs';
   import { startWith, map } from 'rxjs/operators';
   import { UserDto } from 'app/shared/dto/user-dto';
   import { MessagesService } from 'app/services/messages.service';
   import { InternalMessageService } from 'app/shared/utils/internal-message.service';
import { NotificationToastService } from 'app/shared/utils/notification-toast-service';
import { AppDataService } from 'app/services/app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';

   @Component({
   selector: 'app-user-group-form',
   templateUrl: './user-group-form.component.html',
   styleUrls: ['./user-group-form.component.scss']
   })
   export class UserGroupFormComponent implements OnInit {
       @Input() selectionArray: any[];
       messageServiceSubscription;
       isPopupOpened = true;
       userGroupModel = new UserGroupDTO('', '', new Array);
       userGroupDTO = new UserGroupDTO('', '', new Array);
       userGroupDTOToUpdate = new UserGroupDTO('', '',new Array)
        userGroupInterfaceData = new UserGroupInterfaceData();
       userGroupForm: FormGroup;
       columns:  Object[] = [];
       displayedColumns;
       usernamesToPass: any[] = [];
       selectedUsersToPass: Object[] = [];
       dialogData: any = null;
       selectedIdsToGet: number[] = [] ;
       idOfSelectedGroup: number;
       alreadyChecked: number[] = [] ;
       alreadyCheckedObject: Object[] = [];
       selectedUsernamesToShowInUi: String[] = [];
       visible = true;
       selectable = true;
       removable = true;
       addOnBlur = true;
       separatorKeysCodes: number[] = [ENTER, COMMA];
       partnerCtrl = new FormControl();
       filteredPartners: Observable<string[]>;
       partners: string[] = [];
       allPartners: string[] = [];
       usersIdsArray: UserDto = new UserDto();
       userIdsList = [];
       usersObjects: UserDto[]

       @ViewChild('partnerInput', {static: false}) partnerInput: ElementRef<HTMLInputElement>;
       @ViewChild('auto', {static: false}) matAutocomplete: MatAutocomplete;
       

       constructor(private formBuilder: FormBuilder, private userService: UserService, private dialog: MatDialog,
           private userGroupService: UserGroupService, private router: Router, private contactService: ContactService,
           private appDataService: AppDataService,
           private dialogRef: MatDialogRef<UserGroupsComponent, DialogListComponent>,
           public msgService: InternalMessageService,
           public notificationToastService: NotificationToastService,
           @Inject(MAT_DIALOG_DATA) public data: any) {
               dialogRef.disableClose = true;
               this.filteredPartners = this.partnerCtrl.valueChanges.pipe(
                   startWith(null),
                   map((partner: string | null) => partner ? this._filter(partner) : this.allPartners.slice()));
       }

       ngOnInit() {

           this.contactService.getApprovedContacts().subscribe(usersReceived => {
               this.usersObjects = usersReceived
               this.contactService.setContactsArrayFromServer(usersReceived)
               for(let i=0; i<usersReceived.length;i++){
                   this.allPartners.push(usersReceived[i]['username'])
               }
           })
           if (this.data.compAction !== 'createUserGroup') {
               this.editUserGroup();
           }
           
       }

       add(event: MatChipInputEvent): void {
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

         focusMemberInput(){
             this.partnerInput.nativeElement.blur();
             this.partnerInput.nativeElement.focus()
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


       editUserGroup() {
           console.log(this.data.userDTOToUpdate)
           this.userGroupModel.name = this.data.userDTOToUpdate['name'];
           this.userGroupModel.description = this.data.userDTOToUpdate['description']

           for(let _i = 0;_i < this.data.userDTOToUpdate['users'].length; _i++) {
               this.partners.push(this.data.userDTOToUpdate['users'][_i]['username'])
           }

       }

       onNoClick(): void {
           this.dialogRef.close();
       }

       onSubmit() {
           if (this.data.compAction !== 'createUserGroup') {
               this.formUserGroupDTO();
               console.log(this.userGroupDTO)
               this.updateUserGroup(this.userGroupDTO, this.data.userGroupDTOId );
           } else {
               this.formUserGroupDTO();
               this.createUserGroup(this.userGroupDTO);
               this.data.addedUserGroup = this.userGroupDTO;
           }
       }
       getIdsFromSelectedUsers(){
       const userDTOArray = [];
       const allContactsNames = [];
       const falseNames = [];
       for(let m=0; m <this.usersObjects.length; m++){
           allContactsNames.push(this.usersObjects[m]['username'])
       }
       for(let m=0 ;m <this.partners.length; m++){
           console.log(this.partners)
           if(allContactsNames.includes(this.partners[m]) === false){
               falseNames.push(this.partners[m])

           }


       }
       if(falseNames.length >0){
           console.log(falseNames)
           if(falseNames.length === 1){
                 this.notificationToastService.showNotification(
                   falseNames + ' is not found in your Contacts.',
                   'error',
                   'top',
                   'center',
                   'warning');
               }else{
                   this.notificationToastService.showNotification(
                       falseNames + ' are not found in your Contacts.',
                       'error',
                       'top',
                       'center',
                       'warning');
               }
           return;
       }

       for (let i = 0; i < this.usersObjects.length; i++) {
           for(let k = 0; k < this.partners.length; k++) {
               const userDtoIdObject = new UserDto();
               console.log(this.partners[k])
               console.log(this.usersObjects[i])
               if(this.partners[k] === this.usersObjects[i]['username']){
                   userDtoIdObject.id = (this.usersObjects[i].id);
                   userDTOArray.push(userDtoIdObject)
               }
           }
          }
          console.log(userDTOArray)
          return userDTOArray;

       }

       formUserGroupDTO(){
           this.userGroupInterfaceData.name = this.userGroupModel.name;
           this.userGroupInterfaceData.description = this.userGroupModel.description;
           this.userGroupInterfaceData.users = this.partners;
           this.userGroupDTO.name = this.userGroupInterfaceData.name;
           this.userGroupDTO.description = this.userGroupInterfaceData.description;
           this.userGroupDTO.users = this.getIdsFromSelectedUsers()
           if(this.userGroupDTO.users === undefined){
               return
           }
           console.log(this.userGroupDTO)

       }

       createUserGroup(userGroup: UserGroupDTO) {

           this.userGroupService.createUserGroups(userGroup)
           .subscribe(
               response => {
                   console.log(response);
                   this.userGroupService.getAllUserGroups().subscribe(result => {
                       console.log(result)
                       this.data.addedUserGroup = response;
                       this.dialogRef.close(this.data.addedUserGroup)
                   });
               });
       }

       updateUserGroup(userGroup: UserGroupDTO, index: number) {
           if(userGroup['users']===undefined){
               return;
           }
           this.userGroupService.editUserGroup(userGroup, index)
           .subscribe(
               response => {
                   console.log(response)
               this.data = response['users']
                   this.dialogRef.close(this.data)
               });
       }
   }

   export class UserGroupInterfaceData {
       public name: string;
       public description: string;
       public users: Array<String>
       constructor() {}
   }
