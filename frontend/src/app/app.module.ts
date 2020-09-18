import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {Router, RouterModule} from '@angular/router';
import {ConfirmDialogComponent} from './shared/components/confirm-dialog/confirm-dialog.component';
import {AppRoutingModule, routes} from './app.routing';
import {ComponentsModule} from './shared/components/components.module';
import {AppComponent} from './app.component';
import {DialogListComponent} from './shared/components/dialog-list/dialog-list.component';
import {AdminLayoutComponent} from './layouts/admin-layout/admin-layout.component';
import {BrowserModule} from '@angular/platform-browser';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {JwtModule} from '@auth0/angular-jwt';
import {
  MAT_DIALOG_DATA,
  MatAutocompleteModule,
  MatButtonModule,
  MatCardModule,
  MatCheckboxModule,
  MatDialogRef,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatMenuModule,
  MatNativeDateModule,
  MatOptionModule,
  MatProgressBarModule,
  MatProgressSpinnerModule,
  MatSelectModule,
  MatSortModule,
  MatTableModule,
  MatToolbarModule,
  MatTooltipModule,
  MatStepperModule
} from '@angular/material';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {CommonModule, DatePipe} from '@angular/common';
import {UserProfileModule} from './pages/user-profile/user-profile.module';

import {DashboardModule} from './pages/dashboard/dashboard.module';
import {LoginModule} from './pages/login/login.modules';
import {ContactsModule} from './pages/contacts/contacts.module';
import {UserGroupsModule} from './pages/user-groups/user-groups.module';
import {AppConstants} from 'app/shared/config/app-constants'
import {UserService} from './services/user.service';
import {UserGroupService} from './services/user-group.service';
import {AuthInterceptor} from 'app/shared/interceptors/auth-interceptor'
import {ModulesModule} from 'app/pages/modules/modules.module';
import {HistoryService} from './services/history.service';
import {NgxJsonViewerModule} from 'ngx-json-viewer';
import {MatExpansionModule} from '@angular/material/expansion';
import {FileUploadModule} from 'ng2-file-upload';
import * as PlotlyJS from 'plotly.js/dist/plotly.js';
import {PlotlyModule} from 'angular-plotly.js';
import {DataModule} from './pages/data/data.module';
import {MatMomentDateModule} from '@angular/material-moment-adapter';
import {GeHyppoService} from './services/ge-hyppo.service';
import {FileUploadRegistryService} from './services/FileUploadRegistry.service';
import {ResultService} from './services/results.service';
// import { MarketComponent } from './pages/market/market.component';
import {MarketModule} from './pages/market/market.module';
import {NgxMaterialTimepickerModule} from 'ngx-material-timepicker';
import {GraphService} from './services/graph.service';
// import { ModalService } from './services/modal.service';
import {SideBarToggleService} from './services/sidebar-toggle.service';
import {MyOrdersService} from './services/myOrders.service';
import {ActiveProductsService} from './services/activeProducts.service';
import {MyTradesService} from './services/myTrades.service';
import {BasketDataSourceChangedService} from './services/basketDataSourceChanged.service';
import {OrderStateService} from './services/orderState.service';
import {UsersComponent} from './pages/users/users.component';
import {UsersFormComponent} from './pages/users/users-form/users-form.component';
import {DataSourceChangedService} from './services/dataSourceChanged.service';
import {BasketService} from './services/basket.service';
import {OrdersByProductService} from './services/ordersByProduct.service';
import {NotificationMessagesComponent} from './pages/notification-messages/notification-messages.component';
import {NotificationsDialogComponent} from './pages/notifications/notifications-dialog/notifications-dialog.component';
import {NotificationToastService} from './shared/utils/notification-toast-service';
import {MarketDemoDesignComponent} from './pages/market-demo-design/market-demo-design.component';
import {FileUploaderComponent} from './pages/file-uploader/file-uploader.component';
import {UploadComponent} from './pages/upload/upload.component';
import {CrmCentralWebSocketsService} from './services/crm-central-web-sockets.service';
import {CrmLocalWebSocketsService} from './services/crm-local-web-sockets.service';
import {AppDataService} from './services/app-data.service';
import {WeatherImportComponent} from './pages/weather-import/weather-import.component';
import {MarketNotificationMessagesComponent} from './pages/market/market-notification-messages/market-notification-messages.component';
import {AuthenticationHeaderInterceptor} from './shared/interceptors/authentication-header-interceptor';
import {MatRadioModule} from '@angular/material/radio';
import {MarketScenarioComponent} from './pages/market-scenario/market-scenario.component';
import { HistoricalMarketDataComponent } from './pages/historical-market-data/historical-market-data.component';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { ActiveSubstationsManagementComponent } from './pages/modules/active-substations-management/active-substations-management.component';
import { ActiveSubstationsMainComponent } from './pages/modules/active-substations-main/active-substations-main.component';
import { ActiveSubstationsHistoryComponent } from './pages/modules/active-substations-history/active-substations-history.component';
import { ActiveSubstationHistoryService } from './services/activeSubstation-history.service';
import { ActiveSubstationsUploadForecastedDataComponent } from './pages/modules/active-substations-upload-forecasted-data/active-substations-upload-forecasted-data.component';
import { ActiveSubstationsResultsComponent } from './pages/modules/active-substations-results/active-substations-results.component';

PlotlyModule.plotlyjs = PlotlyJS;

export function getJwtToken(): string {
  return localStorage.getItem(AppConstants.StorageLabels.JWT_STORAGE_NAME);
}

@NgModule({
  imports: [
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatMenuModule,
    MatIconModule,
    MatRadioModule,
    MatToolbarModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSortModule,
    MatTableModule,
    MatOptionModule,
    UserProfileModule,
    FormsModule,
    MatInputModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    ComponentsModule,
    RouterModule.forRoot(routes),
    AppRoutingModule,
    LoginModule,
    DashboardModule,
    ContactsModule,
    DataModule,
    UserGroupsModule,
    MatOptionModule,
    ModulesModule,
    NgxJsonViewerModule,
    MatExpansionModule,
    FileUploadModule,
    PlotlyModule,
    MatTooltipModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatMomentDateModule,
    MarketModule,
    NgxMaterialTimepickerModule,
    MatAutocompleteModule,
    MatCheckboxModule,
    MatProgressBarModule,
    MatMenuModule,
    ReactiveFormsModule,
    MatStepperModule,
    JwtModule.forRoot({
      config: {
        tokenGetter: getJwtToken,
        whitelistedDomains: new Array(new RegExp('.*'))
      }
    })
  ],
  declarations: [
    AppComponent,
    ConfirmDialogComponent,
    AdminLayoutComponent,
    DialogListComponent,
    UsersComponent,
    UsersFormComponent,
    NotificationMessagesComponent,
    NotificationsDialogComponent,
    MarketNotificationMessagesComponent,
    MarketDemoDesignComponent,
    UploadComponent,
    FileUploaderComponent,
    WeatherImportComponent,
    MarketScenarioComponent,
    HistoricalMarketDataComponent,
    ActiveSubstationsManagementComponent,
    ActiveSubstationsMainComponent,
    ActiveSubstationsHistoryComponent,
    ActiveSubstationsUploadForecastedDataComponent,
    ActiveSubstationsResultsComponent
  ],
  entryComponents: [ConfirmDialogComponent, NotificationsDialogComponent],
  providers: [
    DatePipe,
    NotificationToastService,
    UserService, UserGroupService,
    HistoryService,
    GeHyppoService,
    FileUploadRegistryService,
    ResultService, GraphService,
    SideBarToggleService,
    MyOrdersService,
    MyTradesService,
    ActiveProductsService,
    OrderStateService,
    BasketDataSourceChangedService,
    BasketService,
    CrmCentralWebSocketsService,
    CrmLocalWebSocketsService,
    AppDataService,
    OrdersByProductService,
    DataSourceChangedService,
    ActiveSubstationHistoryService,
    {provide: MatDialogRef, useValue: {}},
    {provide: MAT_DIALOG_DATA, useValue: []},
    {
      provide: HTTP_INTERCEPTORS,
      useFactory: function (router: Router) {
        return new AuthInterceptor(router);
      },
      multi: true,
      deps: [Router]
    },
    {provide: HTTP_INTERCEPTORS, useClass: AuthenticationHeaderInterceptor, multi: true}

  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
