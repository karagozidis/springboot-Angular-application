import {Component, OnInit} from '@angular/core';
import {AppConstants, AppType} from '../../shared/config/app-constants';
import {CrmCentralWebSocketsService} from '../../services/crm-central-web-sockets.service';
import {AppDataService} from '../../services/app-data.service';
import {Router} from '@angular/router';
import {InternalMessageService} from '../../shared/utils/internal-message.service';
import * as moment from 'moment';
import {MatTableDataSource} from '@angular/material/table';
import {NotificationService} from '../../services/notification.service';
import {NotificationDto} from '../../shared/dto/notification-dto';
import { UserDto } from 'app/shared/dto/user-dto';


declare interface ModulesInfo {
    path: string;
    title: string;
    icon: string;
    rolesCrm: string[];
    moduleCrm: string;
    rolesMarket: string[];
    moduleMarket: string;
}

export const ModulesInfos: ModulesInfo[] = [
    {
        path: '/dashboard',
        title: 'Dashboard',
        icon: 'dashboard',
        rolesCrm: ['admin', 'user'],
        moduleCrm: null,
        rolesMarket: null,
        moduleMarket: null,
    },
    {
        path: '/market',
        title: 'Market',
        icon: 'swap_horiz',
        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: ['admin'],
        moduleMarket: 'market',
    },
    {
        path: '/data',
        title: 'Data',
        icon: 'notes',
        rolesCrm: ['admin'],
        moduleCrm: 'data',
        rolesMarket: null,
        moduleMarket: null,
    },
    {
        path: '/partnerModules',
        title: 'HyppoIo',
        icon: 'blur_on',
        rolesCrm: [],
        moduleCrm: 'hyppoio',
        rolesMarket: [],
        moduleMarket: null,
    },
    {
        path: '/file-uploader',
        title: 'Upload',
        icon: 'cloud_upload',
        rolesCrm: [],
        moduleCrm: 'upload-file',
        rolesMarket: null,
        moduleMarket: null,
    },
    {
        path: '/userGroups',
        title: 'UserGroups',
        icon: 'supervised_user_circle',
        rolesCrm: ['admin'],
        moduleCrm: 'user-groups',
        rolesMarket: null,
        moduleMarket: null,
    },
    {
        path: '/users',
        title: 'Users',
        icon: 'group',
        rolesCrm: ['admin'],
        moduleCrm: 'users',
        rolesMarket: null,
        moduleMarket: null,
    },
    {
        path: '/contacts',
        title: 'Contacts',
        icon: 'contacts',
        rolesCrm: ['admin'],
        moduleCrm: 'contacts',
        rolesMarket: null,
        moduleMarket: null
    },
    {
        path: '/notification-messages',
        title: 'Notifications',
        icon: 'notification_important',
        rolesCrm: ['admin'],
        moduleCrm: 'notifications',
        rolesMarket: null,
        moduleMarket: null
    },
    {
        path: '/market-data',
        title: 'Market Data',
        icon: 'import_export',
        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: ['admin'],
        moduleMarket: 'market-imports',
    },
    {
        path: '/weather-data',
        title: 'Weather Data',
        icon: 'import_export',
        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: ['admin'],
        moduleMarket: 'weather-imports',
    },
    {
        path: '/market-tools',
        title: 'Market tools',
        icon: 'settings',
        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: ['admin'],
        moduleMarket: 'market-tools'
    },
    {
        path: '/market-stats',
        title: 'Market stats',
        icon: 'today',
        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: ['admin'],
        moduleMarket: 'market-stats'
    },
    {
        path: '/market-scenario',
        title: 'Market Scenarios',
        icon: 'settings',
        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: ['admin'],
        moduleMarket: 'market-scenarios'
    },
    {
        path: '/active-substations-management',
        title: 'Active Substations Management',
        icon: 'charging_station',
        rolesCrm: [],
        moduleCrm: 'active-substations-management',
        rolesMarket: [],
        moduleMarket: null,
    },
];


@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

    dataSource: any[];
    modulesInfos: ModulesInfo[];

    constructor(
        private crmCentralWebSocketService: CrmCentralWebSocketsService,
        private appDataService: AppDataService,
        private router: Router,
        private msgService: InternalMessageService,
        private notificationService: NotificationService
    ) {
    }

    ngOnInit() {
        this.modulesInfos = ModulesInfos;
        this.refresh();
    }

    getUserDto() : UserDto {
        return this.appDataService.userDto
    }

    refresh() {

        this.notificationService.getPage(0, 5, 'createdOn',
            'desc').subscribe(data => {
            data.content.forEach(function (row) {
                row.descriptionPreview = row.description;
                row.createdOn = moment(new Date(row.createdOn)).format('HH:mm  DD/MM/YYYY');
                if (row.description.length > 500) {
                    row.descriptionPreview = row.description.substring(0, 500) + ' .....';
                }

            });

            this.dataSource = data.content;

        });
    }

    onLoggedout() {
        this.crmCentralWebSocketService.unsubscribeUserFromTopics();
        localStorage.removeItem(AppConstants.StorageLabels.JWT_STORAGE_NAME);
        this.appDataService.setAppType(AppType.CRM)
        this.router.navigateByUrl(`/login`);
        this.msgService.unsubscribeAll();
    }

    userHasRightsOnMenu(menuRolesCrm, menuModuleCrm, menuRolesMarket, menuModuleMarket) {

        const userRolesCrm = this.appDataService.userDto.rolesCrm.split(',');
        const userModulesCrm = this.appDataService.userDto.modulesCrm.split(',');
        const userRolesMarket = this.appDataService.userDto.rolesMarket.split(',');
        const userModulesMarket = this.appDataService.userDto.modulesMarket.split(',');

        /*
         *
         * Check on current menu, if, at least one Menu Role is assigned to the User Roles.
         * If it is then return true
         * Otherwise check the modules
         *
         */
        if (menuRolesCrm !== undefined && menuRolesCrm !== null) {
            for (const userRoleCrm of userRolesCrm) {
                if (menuRolesCrm.indexOf(userRoleCrm) !== -1) {
                    return true;
                }
            }
        }

        /*
        *
        * Check if current Menu Module is assigned to the User Modules.
        * If it is then return true
        *
        */
        if (menuModuleCrm !== undefined && menuModuleCrm !== null) {
            for (const userModuleCrm of userModulesCrm) {
                if (userModuleCrm.match(menuModuleCrm)) {
                    return true;
                }
            }
        }

        /*
        *
        * Check on current menu, if, at least one Menu Role is assigned to the User Roles.
        * If it is then return true
        * Otherwise check the modules
        *
        */
        if (menuRolesMarket !== undefined && menuRolesMarket !== null) {
            for (const userRoleMarket of userRolesMarket) {
                if (menuRolesMarket.indexOf(userRoleMarket) !== -1) {
                    return true;
                }
            }
        }

        /*
        *
        * Check if current Menu Module is assigned to the User Modules.
        * If it is then return true
        *
        */
        if (menuModuleMarket !== undefined && menuModuleMarket !== null) {
            for (const userModuleMarket of userModulesMarket) {
                if (userModuleMarket.match(menuModuleMarket)) {
                    return true;
                }
            }
        }

        /*
        *
        * if No matched Role - Module assignements were found
        * User has no writes on this menu & return value is false
        *
        */
        return false;
    }

}
