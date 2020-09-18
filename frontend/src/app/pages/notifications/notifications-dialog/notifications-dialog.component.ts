import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { UserGroupFormComponent } from 'app/pages/user-groups/user-group-form/user-group-form.component';
import { ContactsComponent } from 'app/pages/contacts/contacts.component';

@Component({
  selector: 'app-notifications-dialog',
  templateUrl: './notifications-dialog.component.html',
  styleUrls: ['./notifications-dialog.component.scss']
})
export class NotificationsDialogComponent implements OnInit {

  constructor(
    private dialogRef: MatDialogRef<ContactsComponent, UserGroupFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any)  { 
        }

  ngOnInit() {
  }

}
