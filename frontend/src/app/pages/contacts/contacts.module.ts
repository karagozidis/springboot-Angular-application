import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';


import {MatTableModule} from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule, MatIconModule, MatInputModule, MatTooltipModule } from '@angular/material';
import { FormsModule } from '@angular/forms';
import {MatButtonModule} from '@angular/material';
import { ContactsComponent } from './contacts.component';


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
        MatTooltipModule
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
            MatButtonModule,
            ContactsComponent,
            MatTooltipModule
          ],
    declarations: [ContactsComponent],




})
export class ContactsModule {}
