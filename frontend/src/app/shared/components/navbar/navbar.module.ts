import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';


import {MatTableModule} from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule, MatIconModule, MatInputModule, MatMenuModule, MatSelectModule, MatListItem } from '@angular/material';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from './navbar.component';
import { ComponentsModule } from '../components.module';



import {  MatNativeDateModule,
     // tslint:disable-next-line:max-line-length
     MatCheckboxModule, MatTabsModule,  MatOptionModule, MatExpansionModule, MatTooltipModule, MatCardModule, MatButtonToggleModule, MatChipsModule, MatAutocompleteModule} from '@angular/material';
import { ReactiveFormsModule } from '@angular/forms';
import {MatButtonModule} from '@angular/material';
import { MarketModule } from 'app/pages/market/market.module';


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
        FormsModule,
        MatFormFieldModule,
        ComponentsModule,
        MatSelectModule,
        MatMenuModule,
        CommonModule,
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
        MatNativeDateModule,
        MatTableModule,
        MatDialogModule,
        MatPaginatorModule,
        MatFormFieldModule,
        MatSortModule,
        MatIconModule,
        FormsModule,
        MatInputModule,
        MatButtonModule,
        MatCheckboxModule,
        MatTabsModule,
        MatSelectModule,
        MatOptionModule,
        MatExpansionModule,
        MatMenuModule,
        MatTooltipModule,
        MatCardModule,
        MatButtonToggleModule,
        MatChipsModule,
        MatAutocompleteModule
        
     
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
            FormsModule,
            MatFormFieldModule,
            MatMenuModule,
            MatSelectModule,
            MatTableModule,
            MatPaginatorModule,
            MatDialogModule,
            MatSortModule,
            MatFormFieldModule,
            MatIconModule,
            FormsModule,
            MatInputModule,
            MatButtonModule,
            ReactiveFormsModule,
            
            MatNativeDateModule,
            
            MatTableModule,
            MatDialogModule,
            MatPaginatorModule,
            MatFormFieldModule,
            MatSortModule,
            MatIconModule,
            FormsModule,
            MatInputModule,
            MatButtonModule,
            MatCheckboxModule, 
            MatTabsModule,
            MatSelectModule,
            MatOptionModule,
            MatExpansionModule,
            MarketModule,
            MatTooltipModule,
            MatCardModule,
            MatButtonToggleModule,
            MatChipsModule,
            MatAutocompleteModule
            
          ],
    declarations: [],




})
export class NavbarModule {}
