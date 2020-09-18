import {MarketStatsComponent} from './pages/market-tools/market-stats/market-stats.component';
import {ProductTransactionsComponent} from './pages/market-tools/product-transactions/product-transactions.component';
import {MarketToolsComponent} from './pages/market-tools/market-tools.component';
import {MarketDataImporterComponent} from './pages/MarketDataImporter/MarketDataImporter.component';
import {NgModule} from '@angular/core';
import {CommonModule,} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {RouterModule, Routes} from '@angular/router';

import {AdminLayoutComponent} from './layouts/admin-layout/admin-layout.component';
import {LoginComponent} from './pages/login/login.component';
import {UserProfileComponent} from './pages/user-profile/user-profile.component';
import {DialogListComponent} from './shared/components/dialog-list/dialog-list.component';
import {DashboardComponent} from './pages/dashboard/dashboard.component';
import {AuthGuard} from './shared/guards/auth.guard';
import {RoleGuard} from './shared/guards/role.guard';

import {UserGroupsComponent} from './pages/user-groups/user-groups.component';
import {UsersComponent} from './pages/users/users.component';
import {UsersFormComponent} from './pages/users/users-form/users-form.component';
import {NotificationMessagesComponent} from './pages/notification-messages/notification-messages.component';
import {ContactsComponent} from './pages/contacts/contacts.component';
import {UserGroupFormComponent} from './pages/user-groups/user-group-form/user-group-form.component';
import {NotificationFormComponent} from './pages/notifications/notification-form/notification-form.component';
import {ModulesComponent} from './pages/modules/modules.component';
import {DataComponent} from './pages/data/data.component';
import {MarketComponent} from './pages/market/market.component';
import {MarketDemoDesignComponent} from './pages/market-demo-design/market-demo-design.component';
import {FileUploaderComponent} from './pages/file-uploader/file-uploader.component';
import {UploadComponent} from './pages/upload/upload.component';
import {WeatherImportComponent} from './pages/weather-import/weather-import.component';
import {BmeCommandInspectorComponent} from './pages/market-tools/bme-command-inspector/bme-command-inspector.component';
import {MarketNotificationFormComponent} from './pages/market/components/market-notification-form/market-notification-form.component';
import {GeneralNotificationFormComponent} from './pages/market/general-notification-form/general-notification-form.component';
import {NotificationsComponent} from './pages/notifications/notifications.component';
import {MarketNotificationMessagesComponent} from './pages/market/market-notification-messages/market-notification-messages.component';
import {MarketScenarioComponent} from './pages/market-scenario/market-scenario.component';
import {AdminMarketStatsComponent} from './pages/market-tools/admin-market-stats/admin-market-stats.component';
import {TradingExeCommandInspectorComponent} from './pages/market-tools/trading-exe-command-inspector/trading-exe-command-inspector.component';
import {HistoricalMarketDataComponent} from "./pages/historical-market-data/historical-market-data.component";
import { ActiveSubstationsManagementComponent } from './pages/modules/active-substations-management/active-substations-management.component';
import { ActiveSubstationsMainComponent } from './pages/modules/active-substations-main/active-substations-main.component';


export const routes: Routes = [

  {path: 'admin', component: AdminLayoutComponent},
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',
    component: AdminLayoutComponent,
    children: [{
      path: 'dashboard',
      component: DashboardComponent,
      canActivate: [RoleGuard, AuthGuard],
      data: {
        rolesCrm: ['admin'],
        moduleCrm: 'dashboard'
      }

    },
      {
        path: 'market-demo-design',
        component: MarketDemoDesignComponent,
      },
      {
        path: 'user-profile',
        component: UserProfileComponent,
      },
      {
        path: 'dialog-list',
        canActivate: [RoleGuard, AuthGuard],
        component: DialogListComponent,
      },
      {
        path: 'contacts',
        component: ContactsComponent,
        canActivate: [RoleGuard, AuthGuard],
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'contacts'
        }
      },
      {
        path: 'userGroups',
        canActivate: [RoleGuard, AuthGuard],
        component: UserGroupsComponent,
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'user-groups'
        }
      },
      {
        path: 'notification-messages',
        canActivate: [RoleGuard, AuthGuard],
        component: NotificationMessagesComponent,
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'notifications'
        }
      },

      {
        path: 'userGroupForm',
        canActivate: [RoleGuard, AuthGuard],
        component: UserGroupFormComponent,
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'user-groups'
        }
      },
      {
        path: 'notificationForm/:id',
        canActivate: [RoleGuard, AuthGuard],
        component: NotificationFormComponent,
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'notifications'
        }
      },
      /* {
        path: 'marketNotificationForm/:id',
        canActivate: [RoleGuard, AuthGuard],
        component: MarketNotificationFormComponent,
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'notifications'
        }
      },
      {
        path: 'market-notification-messages',
        canActivate: [RoleGuard, AuthGuard],
        component: MarketNotificationMessagesComponent,
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'notifications'
        }
      }, */
      {
        path: 'generalNotificationForm/:id',
        canActivate: [RoleGuard, AuthGuard],
        component: GeneralNotificationFormComponent,
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'notifications'
        }
      },
      {
        path: 'notifications/:id',
        canActivate: [RoleGuard, AuthGuard],
        component: NotificationsComponent,
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'notifications'
        }
      },
      {
        path: 'users',
        component: UsersComponent,
        canActivate: [RoleGuard, AuthGuard],
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'users'
        }
      },
      {
        path: 'user-form/:id',
        canActivate: [RoleGuard, AuthGuard],
        component: UsersFormComponent,
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'users'
        }
      },
      {
        path: 'partnerModules',
        component: ModulesComponent,
        canActivate: [RoleGuard, AuthGuard],
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'hyppoio'
        }

      },
      {
        path: 'data',
        canActivate: [RoleGuard, AuthGuard],
        component: DataComponent,
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'data'
        }
      },
      {
        path: 'file-uploader',
        canActivate: [RoleGuard, AuthGuard],
        component: FileUploaderComponent,
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'upload-file'
        }
      },

      {
            path: 'historical-market-data',
            canActivate: [RoleGuard, AuthGuard],
            component: HistoricalMarketDataComponent,
            data: {
                rolesCrm: ['admin'],
                moduleCrm: 'historical-market-data'
            }
      },


        {
        path: 'market',
        canActivate: [RoleGuard, AuthGuard],
        component: MarketComponent,
        data: {
          rolesMarket: ['admin'],
          moduleMarket: 'market'
        }
      },
      {
        path: 'upload',
        canActivate: [RoleGuard, AuthGuard],
        component: UploadComponent,
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'upload-file'
        }
      },
      {
        path: 'market-tools',
        canActivate: [RoleGuard, AuthGuard],
        component: MarketToolsComponent,
        data: {
          rolesMarket: ['admin'],
          moduleMarket: 'market-tools'
        }
      },

      {
        path: 'product-transactions/:id',
        canActivate: [RoleGuard, AuthGuard],
        component: ProductTransactionsComponent,
        data: {
          rolesMarket: ['admin'],
          moduleMarket: 'market-tools'
        }
      },
      {
        path: 'bme-commands-inspector',
        canActivate: [RoleGuard, AuthGuard],
        component: TradingExeCommandInspectorComponent,
        data: {
          rolesMarket: ['admin'],
          moduleMarket: 'bme-commands-inspector'
        }
      },

      {
        path: 'market-stats',
        canActivate: [RoleGuard, AuthGuard],
        component: MarketStatsComponent,
        data: {
          rolesMarket: ['admin'],
          moduleMarket: 'market-stats'
        }
      },
      {
        path: 'admin-market-stats',
        canActivate: [RoleGuard, AuthGuard],
        component: AdminMarketStatsComponent,
        data: {
          rolesMarket: ['admin'],
          moduleMarket: 'admin-market-stats'
        }
      },
      {
        path: 'market-data',
        canActivate: [RoleGuard, AuthGuard],
        component: MarketDataImporterComponent,
        data: {
          rolesMarket: ['admin'],
          moduleMarket: 'market-imports'
        }
      },
      {
        path: 'weather-data',
        component: WeatherImportComponent,
        canActivate: [RoleGuard, AuthGuard],
        data: {
          rolesMarket: ['admin'],
          moduleMarket: 'weather-imports'
        }
      },
      {
        path: 'market-scenario',
        canActivate: [RoleGuard, AuthGuard],
        component: MarketScenarioComponent,
        data: {
          rolesMarket: ['admin'],
          moduleMarket: 'market-scenarios'
        }
      },
      {
        path: 'marketNotificationForm/:id',
        canActivate: [RoleGuard, AuthGuard],
        component: MarketNotificationFormComponent,
        data: {
          rolesCrm: ['admin'],
          moduleMarket: 'market-notifications'
        }
      },
      {
        path: 'market-notification-messages',
        canActivate: [RoleGuard, AuthGuard],
        component: MarketNotificationMessagesComponent,
        data: {
          rolesCrm: ['admin'],
          moduleMarket: 'market-notifications'
        }
      },
      {
        path: 'active-substations-management',
        canActivate: [RoleGuard, AuthGuard],
        component: ActiveSubstationsMainComponent, 
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'active-substations-management'
        }
      },
      {
        path: 'active-substations-management',
        canActivate: [RoleGuard, AuthGuard],
        component: ActiveSubstationsMainComponent, 
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'system-operator'
        }
      },
      {
        path: 'active-substations-management',
        canActivate: [RoleGuard, AuthGuard],
        component: ActiveSubstationsMainComponent, 
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'substation-operator'
        }
      },
      {
        path: 'active-substations-management',
        canActivate: [RoleGuard, AuthGuard],
        component: ActiveSubstationsMainComponent, 
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'import-forecasted-data'
        }
      },
      {
        path: 'active-substations-management',
        canActivate: [RoleGuard, AuthGuard],
        component: ActiveSubstationsMainComponent, 
        data: {
          rolesCrm: ['admin'],
          moduleCrm: 'substation-optimization'
        }
      },

    ]
  }


];

@NgModule({
  imports: [
    CommonModule,
    BrowserModule,
    RouterModule.forRoot(routes, {
      useHash: true
    })
  ],
  exports: [],
})
export class AppRoutingModule {
}
