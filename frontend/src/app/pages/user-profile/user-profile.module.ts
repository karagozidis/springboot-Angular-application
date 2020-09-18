import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';


import {MatTableModule} from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule, MatIconModule, MatInputModule } from '@angular/material';
import { FormsModule } from '@angular/forms';
import { UserProfileComponent } from './user-profile.component';

@NgModule({
    imports: [CommonModule,
        MatTableModule,
        MatDialogModule,
        MatPaginatorModule,
        MatFormFieldModule,
        MatSortModule,
        MatIconModule,
        FormsModule,
        MatInputModule

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
            UserProfileComponent
          ],
    declarations: [UserProfileComponent],




})
export class UserProfileModule {}
