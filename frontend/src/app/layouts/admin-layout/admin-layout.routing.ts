import { Routes } from '@angular/router';

import { DashboardComponent } from 'app/pages/dashboard/dashboard.component';
import { UserProfileComponent } from 'app/pages/user-profile/user-profile.component';
import { DialogListComponent } from 'app/shared/components/dialog-list/dialog-list.component';
import { UserGroupsComponent } from 'app/pages/user-groups/user-groups.component';
import { NotificationsComponent } from 'app/pages/notifications/notifications.component';
import { LoginComponent } from 'app/pages/login/login.component';
import { AdminLayoutComponent } from './admin-layout.component';
import { ContactsComponent } from 'app/pages/contacts/contacts.component';
import { UserGroupFormComponent } from 'app/pages/user-groups/user-group-form/user-group-form.component';
import { ModulesComponent } from 'app/pages/modules/modules.component';
import { DataComponent } from 'app/pages/data/data.component';
import { MarketComponent } from 'app/pages/market/market.component';
import { NotificationFormComponent } from 'app/pages/notifications/notification-form/notification-form.component';
import { UploadComponent } from 'app/pages/upload/upload.component';
import { MarketNotificationFormComponent } from 'app/pages/market/components/market-notification-form/market-notification-form.component';
import { GeneralNotificationFormComponent } from 'app/pages/market/general-notification-form/general-notification-form.component';
import { MarketNotificationMessagesComponent } from 'app/pages/market/market-notification-messages/market-notification-messages.component';
import { UsersFormComponent } from 'app/pages/users/users-form/users-form.component';
import { RoleGuard } from 'app/shared/guards/role.guard';
import { ActiveSubstationsManagementComponent } from 'app/pages/modules/active-substations-management/active-substations-management.component';
import { ActiveSubstationsMainComponent } from 'app/pages/modules/active-substations-main/active-substations-main.component';


export const AdminLayoutRoutes: Routes = [

        {
          path: '',
          children: [ {
            path: 'user-profile',
            component: UserProfileComponent
        }]},

        {
          path: '',
          children: [ {
          path: 'dialog-list',
          component: DialogListComponent
        }]},

        {
          path: '',
          children: [ {
            path: 'contacts',
            component: ContactsComponent
        }]},

        {
          path: '',
          children: [ {
            path: 'userGroups',
            component: UserGroupsComponent
        }]},

        {
          path: '',
          children: [ {
            path: 'users',
            component: UsersFormComponent,
            canActivate:[RoleGuard]
        }]},

        {
          path: '',
          children: [ {
            path: 'userGroupForm',
            component: UserGroupFormComponent
        }]},

        {
          path: '',
          children: [ {
            path: 'partnerModules',
            component: ModulesComponent
        }]},

        {
          path: '',
          children: [ {
            path: 'data',
            component: DataComponent
        }]},
        {
          path: '',
          children: [ {
            path: 'market',
            component: MarketComponent
        }]},

        {
          path: '',
          children: [ {
            path: 'dashboard',
            component: DashboardComponent
        }]},

        {
          path: '',
          children: [ {
            path: 'notificationForm/:id',
            component: NotificationFormComponent
        }]},

        {
          path: '',
          children: [ {
            path: 'marketNotificationForm/:id',
            component: MarketNotificationFormComponent
        }]},

        {
          path: '',
          children: [ {
            path: 'market-notification-messages',
            component: MarketNotificationMessagesComponent
        }]},

        {
          path: '',
          children: [ {
            path: 'generalNotificationForm/:id',
            component: GeneralNotificationFormComponent
        }]},

        {
          path: '',
          children: [ {
            path: 'upload',
            component: UploadComponent
        }]},

        {
          path: '',
          children: [ {
            path: 'active-substations-management',
            component: ActiveSubstationsMainComponent  
        }]},
        {
          path: '',
          children: [ {
            path: 'system-operator',
            component: ActiveSubstationsMainComponent  
        }]},
        {
          path: '',
          children: [ {
            path: 'substation-operator',
            component: ActiveSubstationsMainComponent  
        }]},
        {
          path: '',
          children: [ {
            path: 'import-forecasted-data',
            component: ActiveSubstationsMainComponent  
        }]},
        {
          path: '',
          children: [ {
            path: 'substation-optimization',
            component: ActiveSubstationsMainComponent  
        }]},
                
                

    { path: 'dashboard',      component: DashboardComponent },
    { path: 'user-profile',   component: UserProfileComponent },
    { path: 'dialog-list',     component: DialogListComponent },
    { path: 'userGroupForm',     component: UserGroupFormComponent},
    { path: 'contacts',       component: ContactsComponent },
    { path: 'userGroups',      component: UserGroupsComponent },
    { path: 'notifications/:id',  component: NotificationsComponent },
    { path: 'partnerModules',  component: ModulesComponent },
    {path: 'data', component: DataComponent},
    {path: 'market', component: MarketComponent},
    {path: 'notificationForm/:id', component: NotificationFormComponent},
    {path: 'upload', component: UploadComponent},
    {path: 'marketNotificationForm/:id', component: MarketNotificationFormComponent},
    {path: 'generalNotificationForm/:id', component: GeneralNotificationFormComponent},
    {path: 'market-notification-messages', component: MarketNotificationMessagesComponent},
    {path: 'active-substations-management', component:ActiveSubstationsMainComponent},
    {path: 'system-operator', component:ActiveSubstationsMainComponent},
    {path: 'substation-operator', component:ActiveSubstationsMainComponent},
    {path: 'import-forecasted-data', component:ActiveSubstationsMainComponent},
    {path: 'substation-optimization', component:ActiveSubstationsMainComponent},
    
                  ];
