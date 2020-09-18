import {Component, ElementRef, EventEmitter, OnInit, ViewChild} from '@angular/core';
import {Location, LocationStrategy} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import {SideBarToggleService} from 'app/services/sidebar-toggle.service';
import {InternalMessageService} from 'app/shared/utils/internal-message.service';
import {NotificationService} from 'app/services/notification.service';
import {AppConstants, AppType, UpdateSignals, UserAccess, MarketCountryType} from 'app/shared/config/app-constants';
import {UserDto} from 'app/shared/dto/user-dto';
import {MatMenuTrigger} from '@angular/material';
import {OrderStateService} from 'app/services/orderState.service';
import {DataSourceChangedService} from 'app/services/dataSourceChanged.service';
import {UserService} from 'app/services/user.service';
import {CrmCentralWebSocketsService} from '../../../services/crm-central-web-sockets.service'
import {AppDataService} from 'app/services/app-data.service';
import {Subscription} from 'rxjs';
import {Auxiliary} from 'app/shared/utils/auxiliary';
import {RoleGuard} from '../../guards/role.guard';
import {AuthGuard} from '../../guards/auth.guard';
import {TradingExeCommandInspectorComponent} from '../../../pages/market-tools/trading-exe-command-inspector/trading-exe-command-inspector.component';

declare interface RouteInfo {
    path: string;
    title: string;
    icon: string;
    class: string;
}

export const ROUTES_: RouteInfo[] = [
    {path: '^\/user-profile$', title: 'Account', icon: 'person', class: ''},
    {path: '^\/contacts$', title: 'Contacts', icon: 'contacts', class: ''},
    {path: '^\/userGroups$', title: 'UserGroups', icon: 'supervised_user_circle', class: ''},
    {path: '^\/partnerModules$', title: 'Modules', icon: 'view_module', class: ''},
    {path: '^\/active-substations-management$', title: 'Substation Management', icon: 'charging_station', class: ''},
    {path: '^\/data$', title: 'Data', icon: 'notes', class: ''},
    {path: '^\/market$', title: 'Market', icon: 'swap_horiz', class: ''},
    {path: '^\/users$', title: 'Users', icon: 'group', class: ''},
    {path: '^\/user-form\/[0-9]+$', title: 'User', icon: 'person', class: ''},

    {
        path: '^\/notification-messages$',
        title: 'Notifications',
        icon: 'notification_important',
        class: ''
    },
    {
        path: '^\/upload$',
        title: 'Upload',
        icon: 'cloud_upload',
        class: ''
    },
    {
        path: '^\/file-uploader$',
        title: 'File Upload',
        icon: 'cloud_upload',
        class: ''
    },

    {
        path: '^\/historical-market-data$',
        title: 'Historical Market Data',
        icon: 'cloud_upload',
        class: ''
    },
    {
        path: '^\/dashboard$',
        title: 'Dashboard',
        icon: 'dashboard',
        class: ''
    },
    {
        path: '#',
        title: 'Imports$',
        icon: 'cloud_upload',
        class: ''
    },
    {
        path: '^\/notificationForm\/[0-9]+$',
        title: 'Notification',
        icon: 'notification_important',
        class: ''
    },
    {
        path: '^\/marketNotificationForm\/[0-9]+$',
        title: 'Notification',
        icon: 'notification_important',
        class: ''
    },
    {
        path: '^\/market-notification-messages$',
        title: 'Notifications',
        icon: 'notification_important',
        class: ''
    },
    {
        path: '^\/market-data',
        title: 'Market Data',
        icon: 'cloud_upload',
        class: ''
    },
    {
        path: '^\/weather-data$',
        title: 'Weather Data',
        icon: 'cloud_upload',
        class: ''
    },

    {
        path: '^\/market-tools$',
        title: 'Market Tools',
        icon: 'settings',
        class: ''
    },
    {
        path: '^\/market-stats$',
        title: 'Market Stats',
        icon: 'today',
        class: ''
    },
    {
        path: '^\/market-scenario$',
        title: 'Market Scenarios',
        icon: 'settings',
        class: ''
    },
    {
        path: '^\/bme-commands-inspector',
        title: 'Trading exe Inspector',
        icon: 'settings',
        class: ''
    },
    {
        path: '^\/admin-market-stats',
        title: 'Admin Market Stats',
        icon: 'today',
        class: ''
    }



];

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
    messageServiceSubscription;
    private listTitles: any[];
    location: Location;
    mobile_menu_visible: any = 0;
    private toggleButton: any;
    sidebarHidden = false;
    crmMenuHidden = true;
    private sidebarVisible: boolean;
    notificationsData: any = null;
    totalNotifications: Number = 0;
    userDto: UserDto;
    userNameVariable = '';
    selectedCountry = ''
    isCrmLocalActive
    appType
    userAccess
    AppType = AppType
    UserAccess = UserAccess
    updateEmmiterSubscriptionId: Subscription
    updateEmmiter = new EventEmitter();


    // isGoToMarketHidden = false
    // isReturnToCrmHidden = false;
    // isNavResponsiveHidden = false;

    @ViewChild('menu', null) menu: MatMenuTrigger

    constructor(
        location: Location,
        private notificationService: NotificationService,
        private element: ElementRef,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private sidebarToggleService: SideBarToggleService,
        private msgService: InternalMessageService,
        private orderStateService: OrderStateService,
        private userService: UserService,
        private dataSourceChanged: DataSourceChangedService,
        private crmCentralWebSocketService: CrmCentralWebSocketsService,
        private appDataService: AppDataService,
        private url: LocationStrategy
    ) {
        this.location = location;
        this.sidebarVisible = false;
        // tslint:disable-next-line:max-line-length
        // tslint:disable-next-line:max-line-length
        // this.sidebarToggleService.toggleEvent.subscribe(i =>  this.updateNavBarButtonsState(this.isGoToMarketHidden,
        // this.isReturnToCrmHidden, this.isNavResponsiveHidden));
        this.updateEmmiterSubscriptionId =
            this.appDataService.updateEmmiter.subscribe(event => {
                if (event === UpdateSignals.USER_DTO || event === UpdateSignals.CRM_LOCAL_ACTIVE_STATE ||
                    event === UpdateSignals.APP_TYPE) {
                    this.onUpdateSignal()
                }
            })
    }

    isOnMarket() {
        return this.url.path() === '/market';
    }

    hideProductsTable() {
        this.msgService.publishMessage('topLeftVisibilityUpdate', 'doUpdate');
    }

    hideOrdersByProductTable() {
        this.msgService.publishMessage('topRightVisibilityUpdate', 'doUpdate');
    }

    hideOrdersAndTradesTable() {
        this.msgService.publishMessage('bottomLeftVisibilityUpdate', 'doUpdate');
    }

    hideWeatherTable() {
        this.msgService.publishMessage('bottomRightVisibilityUpdate', 'doUpdate');
    }

    onLoggedout() {
        this.crmCentralWebSocketService.unsubscribeUserFromTopics();
        localStorage.removeItem(AppConstants.StorageLabels.JWT_STORAGE_NAME);
        this.appDataService.setAppType(AppType.CRM)
        //   this.appDataService.setResponsiveButtonState(true)
        // sessionStorage.removeItem(jwtString)
        this.router.navigateByUrl(`/login`);
        this.msgService.unsubscribeAll()
    }


    onMarket() {
        return this.router.url.match(`/market$`);
    }

    setMarketCountry(country: string) {
        this.selectedCountry = country;
        if (this.selectedCountry === MarketCountryType.COUNTRY_CYPRUS) {
            this.orderStateService.country = MarketCountryType.DEFAULT_MARKET_CYPRUS
            this.orderStateService.productId = null
        } else {
            this.orderStateService.country =MarketCountryType.DEFAULT_MARKET_BULGARIA
            this.orderStateService.productId = null
        }

        this.dataSourceChanged.dataSourceChangedEvent.emit('dataSource changed');
    }

    showAlert(type: string) {
        if (type === 'f') {
            alert(
                'Date 03-10-2019 17:40:49\nHyppoIo hello11223345 execution log file\nHyppoIo hello11223345 execution log file\nThe battery will be designed by the tool, please remove custom values or change design mode\nThe base load and primary reserve values will be designed by the tool, please remove custom values or change design mode\nFunction get_data_from_csv called 1 times.\nExecution time max: 0.001, average: 0.001\nTraceback (most recent call last):\nFile F:/onCloudApp-crm-local-4/onCloudApp-crm-local/modules/hyppo-io/src/hyppo_io/main.py, line 64, in <module>\nstate[pricing_df] = data_import.get_pricing(CONFIG)\nFile F: onCloudApp-crm-local-4 onCloudApp-crm-local moduleshyppo-iosrchyppo_iodata_import.py, line 157, in get_pricing\nfile_path = os.path.join(data_folder_path, file_name[0])\nIndexError: list index out of range\n'
            );
        } else {
            alert(
                'Date 03-10-2019 17:40:49\nHyppoIo hello11223345 execution log file\nThe battery will be designed by the tool, please remove custom values or change design mode\nThe base load and primary reserve values will be designed by the tool, please remove custom values or change design mode\nFunction get_data_from_csv called 1 times.\nExecution time max: 0.001, average: 0.001'
            );
        }
    }

    ngOnInit() {
        console.log('before updateCrmLocal')
        this.appDataService.updateCrmLocalActiveState()
        console.log(this.selectedCountry)
        this.listTitles = ROUTES_.filter(listTitle => listTitle);
        const navbar: HTMLElement = this.element.nativeElement;
        this.toggleButton = navbar.getElementsByClassName('navbar-toggler')[0];
        this.router.events.subscribe(event => {
            this.sidebarClose();
            const $layer: any = document.getElementsByClassName('close-layer')[0];
            if ($layer) {
                $layer.remove();
                this.mobile_menu_visible = 0;
            }
        });

        this.subscribeToMessageService();
        this.refresh();
        this.onUpdateSignal()
    }

    onUpdateSignal() {
        this.userDto = this.appDataService.userDto
        this.isCrmLocalActive = this.appDataService.crmLocalActiveState
        this.appType = this.appDataService.appType
        console.log(this.appDataService.crmLocalActiveState)
        this.isCrmLocalActive = this.appDataService.crmLocalActiveState
        this.userAccess = this.appDataService.userAccess
        if (this.appDataService.defaultMarket === MarketCountryType.DEFAULT_MARKET_BULGARIA) {
            this.selectedCountry = MarketCountryType.COUNTRY_BULGARIA
        } else {
            this.selectedCountry = MarketCountryType.COUNTRY_CYPRUS
        }
        this.initializeNavBar()
        console.log(this.userAccess)
        console.log(this.appType)
    }

    initializeNavBar() {

        if (this.appDataService.userDto == null || this.appDataService.userDto === undefined) {
            return
        }
        this.userNameVariable = this.userDto['username']
    }

    updateNavBarButtonsState(goToMarketHidden: boolean, returnToCrmHidden: boolean, navResponsiveHidden: boolean) {
    }

    refresh() {
        this.notificationService.getPending().subscribe(data => {
            console.log(data)
            this.notificationsData = data;
            this.totalNotifications = data.length;
        });
    }

    userHasAccessToMarket() {
        if (this.userDto === null || this.userDto === undefined) {
            return false
        }

        if ((this.userDto.rolesMarket.length > 0) && (this.userDto.rolesCrm.length > 0) &&
            (this.appDataService.crmLocalActiveState === true)) {
            return true;
        } else {
            return false;
        }
    }

    isUserAdmin() {
        if (this.userDto === null || this.userDto === undefined) {
            return false
        }

        if (this.userDto.rolesCrm === 'admin') {
            return true;
        } else {
            return false
        }
    }

    redirectToPage(appType: AppType) {
        switch (appType) {
            case AppType.MARKET:
                this.appDataService.setAppType(AppType.MARKET)      
                console.log('Market-----------------')
                this.router.navigateByUrl('/market');
                break;
            case AppType.CRM:
                this.appDataService.setAppType(AppType.CRM)
                this.router.navigateByUrl('/data');

                break;
        }
    }

    setSideBarVisibilityState() {
        /* this.appDataService.updateEmmiter.emit(UpdateSignals.SHOW_HIDE_SIDEMENU); */
        this.appDataService.setSideBarVisible(!this.appDataService.sideBarVisible)

    }

    subscribeToMessageService() {
        this.messageServiceSubscription = this.msgService
            .accessMessage('RefreshNavBar')
            .subscribe(msg => {
                this.refresh();
            });
    }

    OnDestroy() {
        this.messageServiceSubscription.unsubscribe();
        this.updateEmmiterSubscriptionId.unsubscribe()
    }

    originalCrmInterface() {
        const $sidebar = $('.sidebar');
        $sidebar.toggle();

        this.sidebarToggleService.setSideBarHidden(false)

        // document.getElementById('returnToCrm').style.display = 'none';
        // document.getElementById('goToMarket').style.display = 'block';
        this.updateNavBarButtonsState(false, true, false)

    }

    sidebarOpen() {
        const toggleButton = this.toggleButton;
        const body = document.getElementsByTagName('body')[0];
        setTimeout(function () {
            toggleButton.classList.add('toggled');
        }, 0);

        body.classList.add('nav-open');

        this.sidebarVisible = true;
    }

    sidebarClose() {
        const body = document.getElementsByTagName('body')[0];
        this.toggleButton.classList.remove('toggled');
        this.sidebarVisible = false;
        body.classList.remove('nav-open');
    }

    sidebarToggle() {
        const $toggle = document.getElementsByClassName('navbar-toggler')[0];

        if (this.sidebarVisible === false) {
            this.sidebarOpen();
        } else {
            this.sidebarClose();
        }
        const body = document.getElementsByTagName('body')[0];

        const $layer = document.createElement('div');
        $layer.setAttribute('class', 'close-layer');

        if (this.mobile_menu_visible === 1) {
            // $('html').removeClass('nav-open');
            body.classList.remove('nav-open');
            if ($layer) {
                $layer.remove();
            }
            setTimeout(function () {
                $toggle.classList.remove('toggled');
            }, 400);

            this.mobile_menu_visible = 0;
        } else {
            setTimeout(function () {
                $toggle.classList.add('toggled');
            }, 430);

            if (body.querySelectorAll('.main-panel')) {
                document.getElementsByClassName('main-panel')[0].appendChild($layer);
            } else if (body.classList.contains('off-canvas-sidebar')) {
                document.getElementsByClassName('wrapper-full-page')[0].appendChild($layer);
            }

            setTimeout(function () {
                $layer.classList.add('visible');
            }, 100);

            $layer.onclick = function () {
                body.classList.remove('nav-open');
                this.mobile_menu_visible = 0;
                $layer.classList.remove('visible');
                setTimeout(function () {
                    $layer.remove();
                    $toggle.classList.remove('toggled');
                }, 400);
            }.bind(this);

            body.classList.add('nav-open');
            this.mobile_menu_visible = 1;

        }
    }

    getTitle() {
        let currentPath = this.location.prepareExternalUrl(this.location.path());
        if (currentPath.charAt(0) === '#') {
            currentPath = currentPath.slice(1);
        }
        for (let item = 0; item < this.listTitles.length; item++) {
            if (currentPath.match(this.listTitles[item].path)) {
                return this.listTitles[item].title;
                break;
            }
        }
        return 'Dashboard'
    }

    getHeaderIcon() {
        let currentPath = this.location.prepareExternalUrl(this.location.path());
        if (currentPath.charAt(0) === '#') {
            currentPath = currentPath.slice(1);
        }
        for (let item = 0; item < this.listTitles.length; item++) {
            if (currentPath.match(this.listTitles[item].path)) {
                return this.listTitles[item].icon;
                break;
            }
        }

        return 'view_module'
    }


    notificationClicked(notificationDtoId) {
        console.log(this.appType)
        if (this.appType === AppType.CRM) {
            this.appDataService.setAppType(AppType.CRM)
            this.router.navigateByUrl('/notificationForm/' + notificationDtoId)

        } else {
            this.appDataService.setAppType(AppType.MARKET)
            this.router.navigateByUrl('/marketNotificationForm/' + notificationDtoId)
        }
        this.updateStatus(notificationDtoId);
        this.notifyNavbar();
        this.subscribeToMessageService();
        this.refresh();
    }

    public updateStatus(id) {

    this.notificationService.editStatus(id, 'viewed').subscribe(() =>{
              /* this.notifyNavbar(); */ 
              this.refresh();
            });
    }

    notifyNavbar() {
        this.msgService.publishMessage('RefreshNavBar', 'FromNotificationMessages');
    }


}