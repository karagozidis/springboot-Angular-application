import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AdminLayoutRoutes } from './admin-layout.routing';
import { DashboardComponent } from 'app/pages/dashboard/dashboard.component';
import { UserProfileComponent } from 'app/pages/user-profile/user-profile.component';
import { DialogListComponent } from 'app/shared/components/dialog-list/dialog-list.component';
import { UserGroupsComponent } from 'app/pages/user-groups/user-groups.component';
import { NotificationsComponent } from 'app/pages/notifications/notifications.component';

import {
  MatButtonModule,
  MatInputModule,
  MatRippleModule,
  MatFormFieldModule,
  MatTooltipModule,
  MatSelectModule,
  MatDialogModule
} from '@angular/material';
import { UserProfileModule } from 'app/pages/user-profile/user-profile.module';
import { LoginComponent } from 'app/pages/login/login.component';
// import { LoginModule } from 'app/login/login.module';
import { DashboardModule } from 'app/pages/dashboard/dashboard.module';
// import { ContactsComponent } from 'app/pages/contacts/contacts.component';
// import { UserGroupFormComponent } from 'app/pages/user-groups/user-group-form/user-group-form.component';
// import { DataComponent } from 'app/pages/data/data.component';
// import { MarketComponent } from 'app/pages/market/market.component';
import { MarketNotificationFormComponent } from 'app/pages/market/components/market-notification-form/market-notification-form.component';
import { MarketModule } from 'app/pages/market/market.module';
import { NotificationFormComponent } from 'app/pages/notifications/notification-form/notification-form.component';
import { GeneralNotificationFormComponent } from 'app/pages/market/general-notification-form/general-notification-form.component';
@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AdminLayoutRoutes),
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatRippleModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatTooltipModule,
    MatDialogModule,
    UserProfileModule,
    DashboardModule,
    MarketModule,
    // GeneralNotificationFormComponent
  ],
  declarations: [
    // MarketNotificationFormComponent,
    // NotificationFormComponent,
    // GeneralNotificationFormComponent
    // DashboardComponent,
    // UserProfileComponent,
    // NotificationsComponent,
    // UserGroupFormComponent,
    // MarketComponent
    // DialogListComponent,
    // LoginComponent
  ]
})

export class AdminLayoutModule {}
