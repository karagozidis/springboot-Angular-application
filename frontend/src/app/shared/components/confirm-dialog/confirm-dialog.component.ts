import {
  Component,
  OnInit,
  Inject,
  ChangeDetectorRef,
  ElementRef,
  ViewChild
} from '@angular/core';
import { UserService } from 'app/services/user.service';
import { Router } from '@angular/router';
import { UserGroupService } from 'app/services/user-group.service';
import { UserGroupFormComponent } from 'app/pages/user-groups/user-group-form/user-group-form.component';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ContactService } from 'app/services/contact.service';
import { UserGroupsComponent } from 'app/pages/user-groups/user-groups.component';
import { ActiveSubstationService } from 'app/services/activeSubstation.service';
import { ActiveSubstationHistoryService } from 'app/services/activeSubstation-history.service';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent implements OnInit {
  constructor(
    private userService: UserService,
    private userGroupService: UserGroupService,
    private contactService: ContactService,
    private router: Router,
    private dialog: MatDialog,
    private dialogRef: MatDialogRef<UserGroupsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private changeDetectorRefs: ChangeDetectorRef,private activeSubstationHistoryService: ActiveSubstationHistoryService
  ) {}

  ngOnInit() {}

  onNoClick(): void {
      this.activeSubstationHistoryService.deleteHistory = false;
      this.dialogRef.close();
  }

  closeDialog() {
    this.dialogRef.close();
    this.activeSubstationHistoryService.deleteHistory = false;
  }

  doAction(data) {
    this.dialogRef.close(data);
    this.activeSubstationHistoryService.deleteHistory = true;
  }
}
