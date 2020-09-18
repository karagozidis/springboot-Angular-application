import {Component, OnInit} from '@angular/core';
import {UserDto} from 'app/shared/dto/user-dto';
import {LoadingStateService} from 'app/shared/utils/loading-state.service';
import {AppType, UpdateSignals} from 'app/shared/config/app-constants';
import {AppDataService} from 'app/services/app-data.service';
import {Subscription} from 'rxjs';
import {RoleGuard} from '../../guards/role.guard';
import {AuthGuard} from '../../guards/auth.guard';
import {ProductTransactionsComponent} from '../../../pages/market-tools/product-transactions/product-transactions.component';

declare const $: any;

declare interface RouteInfo {
    id: string;
    path: string;
    title: string;
    icon: string;
    class: string;
    rolesCrm: string[];
    moduleCrm: string;
    rolesMarket: string[];
    moduleMarket: string;
    submenus: RouteInfo[];
}

export const ROUTES: RouteInfo[] = [
    {
        id: '0',
        path: '/dashboard',
        title: 'Dashboard',
        icon: 'dashboard',
        class: '',
        rolesCrm: null,
        moduleCrm: 'dashboard',
        rolesMarket: null,
        moduleMarket: null,
        submenus: null
    },
    {
        id: '0',
        path: '/market',
        title: 'Market',
        icon: 'swap_horiz',
        class: '',
        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: null,
        moduleMarket: 'market',
        submenus: null
    },
    {
        id: '1',
        path: '/data',
        title: 'Data',
        icon: 'notes',
        class: '',
        rolesCrm: null,
        moduleCrm: 'data',
        rolesMarket: null,
        moduleMarket: null,
        submenus: null
    },
    {
        id: '2',
        path: '#',
        title: 'Modules',
        icon: 'view_module',
        class: '',
        rolesCrm: null,
        moduleCrm: 'modules',
        rolesMarket: null,
        moduleMarket: null,
        submenus: [
            {
                id: '1-1',
                path: '/partnerModules',
                title: 'HyppoIo',
                icon: 'blur_on',
                class: '',
                rolesCrm: null,
                moduleCrm: 'hyppoio',
                rolesMarket: null,
                moduleMarket: null,
                submenus: null
            },
            {
                id: '2-1',
                path: '/file-uploader',
                title: 'Upload',
                icon: 'cloud_upload',
                class: '',
                rolesCrm: null,
                moduleCrm: 'upload-file',
                rolesMarket: null,
                moduleMarket: null,
                submenus: null
            },
            {
                id: '3-1',
                path: '/active-substations-management',
                title: 'Substation Management',
                icon: 'charging_station',
                class: '',
                rolesCrm: null,
                moduleCrm: 'active-substations-management', 
                rolesMarket: null,
                moduleMarket: null,
                submenus: null     
            }
        ]
    },
    {
        id: '3',
        path: '/userGroups',
        title: 'UserGroups',
        icon: 'supervised_user_circle',
        class: '',
        rolesCrm: null,
        moduleCrm: 'user-groups',
        rolesMarket: null,
        moduleMarket: null,
        submenus: null,
    },
    {
        id: '4',
        path: '/users',
        title: 'Users',
        icon: 'group',
        class: '',
        rolesCrm: null,
        moduleCrm: 'users',
        rolesMarket: null,
        moduleMarket: null,
        submenus: null,
    },
    {
        id: '5',
        path: '/contacts',
        title: 'Contacts',
        icon: 'contacts',
        class: '',
        rolesCrm: null,
        moduleCrm: 'contacts',
        rolesMarket: null,
        moduleMarket: null,
        submenus: null,
    },
    {
        id: '7',
        path: '/notification-messages',
        title: 'Notifications',
        icon: 'notification_important',
        class: '',

        rolesCrm: null,
        moduleCrm: 'notifications',
        rolesMarket: null,
        moduleMarket: null,
        submenus: null,
    },

    {
        id: '10',
        path: '#',
        title: 'Imports',
        icon: 'view_module',
        class: '',
        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: null,
        moduleMarket: 'imports',
        submenus: [
            {
                id: '10-1',
                path: '/market-data',
                title: 'Market Data',
                icon: 'import_export',
                class: '',
                rolesCrm: null,
                moduleCrm: null,
                rolesMarket: null,
                moduleMarket: 'market-imports',
                submenus: null,

            },
            {
                id: '10-2',
                path: '/weather-data',
                title: 'Weather Data',
                icon: 'import_export',
                class: '',
                rolesCrm: null,
                moduleCrm: null,
                rolesMarket: null,
                moduleMarket: 'weather-imports',
                submenus: null,

            }
        ],

    },
    {
        id: '11',
        path: '/market-tools',
        title: 'Market tools',
        icon: 'settings',
        class: '',
        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: null,
        moduleMarket: 'market-tools',
        submenus: null,
    },
    {
        id: '12',
        path: '/market-stats',
        title: 'Market stats',
        icon: 'today',
        class: '',
        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: null,
        moduleMarket: 'market-stats',
        submenus: null,
    },
    {
        id: '13',
        path: '/admin-market-stats',
        title: 'Admin Market stats',
        icon: 'today',
        class: '',
        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: null,
        moduleMarket: 'admin-market-stats',
        submenus: null,
    },
    {
        id: '14',
        path: '/market-scenario',
        title: 'Market Scenarios',
        icon: 'settings',
        class: '',
        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: null,
        moduleMarket: 'market-scenarios',
        submenus: null,
    },
    {
        id: '14',
        path: '/bme-commands-inspector',
        title: 'Trading.exe Inspector',
        icon: 'settings',
        class: '',
        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: null,
        moduleMarket: 'bme-commands-inspector',
        submenus: null,
    },
    {
        id: '15',
        path: '/market-notification-messages',
        title: 'Notifications',
        icon: 'notification_important',
        class: '',

        rolesCrm: null,
        moduleCrm: null,
        rolesMarket: null,
        moduleMarket:'market-notifications',
        submenus: null,
    },
    {
        id: '16',
        path: '/historical-market-data',
        title: 'Historical market data',
        icon: 'cloud_upload',
        class: '',
        rolesCrm: null,
        moduleCrm: 'historical-market-data',
        rolesMarket: null,
        moduleMarket: null ,
        submenus: null
    }
    


];

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
    menuItems: any[];
    userDto: UserDto = null;
    loadingIndicationHidden = true;
    appType
    AppType = AppType
    updateEmmiterSubscriptionId: Subscription

    constructor(private loadingStateService: LoadingStateService, private appDataService: AppDataService) {
        this.updateEmmiterSubscriptionId =
            this.appDataService.updateEmmiter.subscribe(event => {
                if (event == UpdateSignals.USER_DTO || event == UpdateSignals.APP_TYPE) {
                    this.onUpdateSignal()
                }
            })
    }

    ngOnInit() {

       
       
        

        // const jSonUser = localStorage.getItem(AppConstants.User);
        // this.userDto = JSON.parse(jSonUser);

        this.loadingStateService.loadingStateEmmiter.subscribe(event => {
            if (event === UpdateSignals.SHOW_LOADING_INDICATION) {
                this.loadingIndicationHidden = false;
            } else if (event === UpdateSignals.HIDE_LOADING_INDICATION) {
                this.loadingIndicationHidden = true;
            }
        });
        this.onUpdateSignal()
    }

    onUpdateSignal() {
        this.userDto = this.appDataService.userDto
        this.appType = this.appDataService.appType
        this.updateSideBarLabels();
    }

    updateSideBarLabels(){
        if(this.appType === AppType.MARKET){
            this.menuItems = ROUTES.filter(menuItem =>(menuItem.moduleMarket!=null) && (menuItem.moduleMarket.length>0));
        }
        else{
            this.menuItems = ROUTES.filter(menuItem => (menuItem.moduleCrm!=null) && (menuItem.moduleCrm.length>0));
        }
    }

    OnDestroy() {
        this.updateEmmiterSubscriptionId.unsubscribe()
    }

    isParentMenu(menuPath) {
        if (menuPath.path.match('#')) {
            for (const submenu of menuPath.submenus) {
                const submenuHasRights = this.userHasRightsOnMenu(submenu.rolesCrm, submenu.moduleCrm, submenu.rolesMarket, submenu.moduleMarket);
                if (submenuHasRights === true) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    userHasRightsOnMenu(menuRolesCrm, menuModuleCrm, menuRolesMarket, menuModuleMarket) {
        
        if (this.userDto == null || this.userDto == undefined) {
            return false;
        }

        /* const userRolesCrm = this.userDto.rolesCrm.split(','); */
        const userModulesCrm = this.userDto.modulesCrm.split(',');
       /*  const userRolesMarket = this.userDto.rolesMarket.split(','); */
        const userModulesMarket = this.userDto.modulesMarket.split(',');

        /*
         *
         * Check on current menu, if, at least one Menu Role is assigned to the User Roles.
         * If it is then return true
         * Otherwise check the modules
         *
         */
        /* if (menuRolesCrm !== undefined && menuRolesCrm !== null) {
            for (const userRoleCrm of userRolesCrm) {
                if (menuRolesCrm.indexOf(userRoleCrm) !== -1) {
                    return true;
                }
            }
        } */

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
       /*  if (menuRolesMarket !== undefined && menuRolesMarket !== null) {
            for (const userRoleMarket of userRolesMarket) {
                if (menuRolesMarket.indexOf(userRoleMarket) !== -1) {
                    return true;
                }
            }
        } */

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

    isMobileMenu() {
        if ($(window).width() > 991) {

            return false;
        }

        return true;
    }

}
