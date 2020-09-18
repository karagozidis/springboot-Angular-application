import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {MatTableModule} from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule, MatIconModule, MatInputModule, MatButtonModule,
         MatCheckboxModule, MatTabsModule, MatOptionModule, MatSelectModule, MatTooltipModule, MatAutocompleteModule, MatChipsModule } from '@angular/material';
import {MatExpansionModule} from '@angular/material/expansion';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GEHyppoIOComponent } from './ge-hyppo-io.component';
import { GEHyppoIONewComponent } from './ge-hyppo-io-new/ge-hyppo-io-new.component';
import { GeHyppoIoHistoryComponent } from './ge-hyppo-io-history/ge-hyppo-io-history.component';
import { GeHyppoIoResultsComponent } from './ge-hyppo-io-results/ge-hyppo-io-results.component';
import { NgxJsonViewerModule } from 'ngx-json-viewer';
import { FileUploadModule } from 'ng2-file-upload';
import { GeHyppoIoGraphComponent } from './ge-hyppo-io-graph/ge-hyppo-io-graph.component';
import * as PlotlyJS from 'plotly.js/dist/plotly.js';
import { PlotlyModule } from 'angular-plotly.js';


PlotlyModule.plotlyjs = PlotlyJS;


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
        MatCheckboxModule,
        MatTabsModule,
        MatSelectModule,
        MatOptionModule,
        NgxJsonViewerModule,
        MatExpansionModule,
        FileUploadModule,
        PlotlyModule,
        MatTooltipModule,
        MatAutocompleteModule,
        ReactiveFormsModule,
        MatChipsModule
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
        MatCheckboxModule,
        MatTabsModule,
        MatSelectModule,
        MatOptionModule,
        NgxJsonViewerModule,
        MatExpansionModule,
        FileUploadModule,
        PlotlyModule,
        MatTooltipModule,
        MatAutocompleteModule,
        ReactiveFormsModule,
        MatChipsModule,
        GEHyppoIOComponent
    ],
    declarations: [GEHyppoIOComponent, GEHyppoIONewComponent, GeHyppoIoHistoryComponent, GeHyppoIoResultsComponent,
        GeHyppoIoGraphComponent],
    })
    export class GeHyppoIOModule {}
