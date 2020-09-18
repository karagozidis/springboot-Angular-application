import {MarketToolsComponent} from './../market-tools/market-tools.component';
import {MarketStatsComponent} from './../market-tools/market-stats/market-stats.component';
import {MarketDataImporterComponent} from './../MarketDataImporter/MarketDataImporter.component';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';


import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatSortModule} from '@angular/material/sort';
import {MatDialogModule} from '@angular/material/dialog';
import {
    MatAutocompleteModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatNativeDateModule,
    MatOptionModule,
    MatSelectModule,
    MatTabsModule,
    MatTooltipModule
} from '@angular/material';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatMomentDateModule} from '@angular/material-moment-adapter';
import {MarketComponent} from './market.component';
import {FileUploadModule} from 'ng2-file-upload';
import {OrdersComponent} from './orders/orders.component';
import {OrdersHistoryComponent} from './orders-history/orders-history.component';
import {NgxMaterialTimepickerModule} from 'ngx-material-timepicker';
import {MarketTableComponent} from './components/market-table/market-table.component';
import {MarketDialogComponent} from './components/market-dialog/market-dialog.component';
import {MarketConfirmationDialogComponent} from './components/market-confirmation-dialog/market-confirmation-dialog.component';
import {ProductTransactionsComponent} from '../market-tools/product-transactions/product-transactions.component';
import {ProductsListComponent} from '../market-tools/products-list/products-list.component';
import {NumericDirective} from './components/market-dialog/numeric.directive';
import {BmeCommandInspectorComponent} from '../market-tools/bme-command-inspector/bme-command-inspector.component';
import {OrderGeneratorComponent} from '../market-tools/order-generator/order-generator.component';
import {ProductGeneratorComponent} from '../market-tools/product-generator/product-generator.component';
import {MarketNotificationFormComponent} from './components/market-notification-form/market-notification-form.component';
import {NotificationFormComponent} from '../notifications/notification-form/notification-form.component';
import {RouterModule} from '@angular/router';
// import { GeneralNotificationFormComponent } from './general-notification-form/general-notification-form.component';
import {NotificationsComponent} from '../notifications/notifications.component';
import {GeneralNotificationFormComponent} from './general-notification-form/general-notification-form.component';
import {UserProductTransactionsComponent} from '../market-tools/user-product-transactions/user-product-transactions.component';
import {PlotlyModule} from 'angular-plotly.js';
import {AdminMarketStatsComponent} from '../market-tools/admin-market-stats/admin-market-stats.component';
import {TradingExeCommandInspectorComponent} from '../market-tools/trading-exe-command-inspector/trading-exe-command-inspector.component';
import { DeleteByFileDialogComponent } from './components/delete-by-file-dialog/delete-by-file-dialog.component';
import { ScrollingModule } from '@angular/cdk/scrolling';


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
        FileUploadModule,
        MatTooltipModule,
        MatCardModule,
        ScrollingModule,
        MatButtonToggleModule,
        MatChipsModule,
        MatAutocompleteModule,
        NgxMaterialTimepickerModule,
        RouterModule, PlotlyModule


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
        ReactiveFormsModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatMomentDateModule,
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
        FileUploadModule,
        MatTooltipModule,
        MatCardModule,
        MatButtonToggleModule,
        MatChipsModule,
        MatAutocompleteModule,
        NgxMaterialTimepickerModule,
        NumericDirective
     
        //  GeneralNotificationFormComponent


    ],
    declarations: [MarketComponent,
        OrdersComponent,
        OrdersHistoryComponent,
        MarketTableComponent,
        MarketDialogComponent,
        MarketConfirmationDialogComponent,
        DeleteByFileDialogComponent,
        MarketDataImporterComponent,
        MarketToolsComponent,
        AdminMarketStatsComponent,
        ProductTransactionsComponent,
        UserProductTransactionsComponent,
        OrderGeneratorComponent,
        ProductGeneratorComponent,
        BmeCommandInspectorComponent,
        ProductsListComponent,
        MarketStatsComponent,
        NumericDirective,
        TradingExeCommandInspectorComponent,
        //   GeneralNotificationFormComponent,
        NotificationsComponent,
        NotificationFormComponent,
        MarketNotificationFormComponent,
        GeneralNotificationFormComponent,
        DeleteByFileDialogComponent
    ],
    entryComponents: [MarketDialogComponent, MarketConfirmationDialogComponent, DeleteByFileDialogComponent]


})
export class MarketModule {
}
