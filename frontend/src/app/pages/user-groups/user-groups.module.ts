import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';


import {MatTableModule} from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatDialogModule } from '@angular/material/dialog';
// tslint:disable-next-line:max-line-length
import { MatFormFieldModule, MatIconModule, MatInputModule, MatAutocompleteModule, MatChipsModule, MatSelectModule } from '@angular/material';
import {MatButtonModule} from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserGroupsComponent } from './user-groups.component';
import { UserGroupFormComponent } from './user-group-form/user-group-form.component';


@NgModule({
    imports: [CommonModule,
        MatTableModule,
        MatDialogModule,
        MatPaginatorModule,
        MatFormFieldModule,
        MatSortModule,
        MatIconModule,
        FormsModule,
        MatInputModule,
        MatButtonModule,
        MatChipsModule,
        MatSelectModule,
        MatAutocompleteModule,
        ReactiveFormsModule

        ],
    exports: [
            MatTableModule,
            MatPaginatorModule,
            MatDialogModule,
            MatSortModule,
            MatFormFieldModule,
            MatIconModule,
            FormsModule,
            MatInputModule,
            MatChipsModule,
            MatSelectModule,
            MatButtonModule,
            UserGroupsComponent,
            MatAutocompleteModule,
            ReactiveFormsModule
          ],
    declarations: [UserGroupsComponent, UserGroupFormComponent],




})
export class UserGroupsModule {}
