import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';


import {MatTableModule} from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule, MatIconModule, MatInputModule, MatNativeDateModule} from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatButtonModule} from '@angular/material';
// import { ContactsComponent } from '../contacts/contacts.component';
import { DataComponent } from './data.component';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatMomentDateModule, MomentDateAdapter } from '@angular/material-moment-adapter';
import {MatCardModule} from '@angular/material/card';


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
        ReactiveFormsModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatMomentDateModule,
        MatCardModule
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
          DataComponent,
            ReactiveFormsModule,
            MatDatepickerModule,
            MatNativeDateModule,
            MatMomentDateModule
          ],
    declarations: [DataComponent],




})
export class DataModule {}
