import {AfterViewInit, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Location, PopStateEvent} from '@angular/common';
import 'rxjs/add/operator/filter';
import {NavigationEnd, NavigationStart, Router} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import * as $ from 'jquery';
import {AppType, UpdateSignals} from 'app/shared/config/app-constants';
import {AppDataService} from 'app/services/app-data.service';
import {Auxiliary} from 'app/shared/utils/auxiliary';

@Component({
    selector: 'app-admin-layout',
    templateUrl: './admin-layout.component.html',
    styleUrls: ['./admin-layout.component.scss'],

})
export class AdminLayoutComponent implements OnInit, AfterViewInit {
    private _router: Subscription;
    private lastPoppedUrl: string;
    private yScrollStack: number[] = [];
    marketEnabled: any;
    fullScreenSet = false
    screenWithSidebarSet = true;
    appType: AppType = AppType.CRM
    AppType = AppType
    updateEmmiterSubscriptionId: Subscription
    sideBarVisible = true;

    // tslint:disable-next-line:whitespace
    // tslint:disable-next-line:max-line-length
    constructor(public location: Location,
                private router: Router,
                private cdref: ChangeDetectorRef,
                private appDataService: AppDataService) {

                this.updateEmmiterSubscriptionId =
                this.appDataService.updateEmmiter.subscribe(event => {
                    if (event === UpdateSignals.APP_TYPE || event === UpdateSignals.SIDEBAR_VISIBILITY_STATE) {
                        this.onUpdateSignal()
                    }
                })
    }

    ngOnInit() {
        this.onUpdateSignal()
        // this.isSidebarHidden = this.sidebarToggleService.isSidebarHidden
        // this.sidebarToggleService.toggleEvent.subscribe(i =>  this.isSidebarHidden = this.sidebarToggleService.isSidebarHidden);
        const isWindows = navigator.platform.indexOf('Win') > -1 ? true : false;
        if (isWindows && !document.getElementsByTagName('body')[0].classList.contains('sidebar-mini')) {
            // if we are on windows OS we activate the perfectScrollbar function

            //document.getElementsByTagName('body')[0].classList.add('perfect-scrollbar-on');
        } else {
            document.getElementsByTagName('body')[0].classList.remove('perfect-scrollbar-off');
        }
        const elemMainPanel = <HTMLElement>document.querySelector('.main-panel');
        const elemSidebar = <HTMLElement>document.querySelector('.sidebar .sidebar-wrapper');

        this.location.subscribe((ev: PopStateEvent) => {
            this.lastPoppedUrl = ev.url;
        });
        this.router.events.subscribe((event: any) => {


            if (event instanceof NavigationStart) {

                if (event.url != this.lastPoppedUrl) {
                    this.yScrollStack.push(window.scrollY);
                }
            } else if (event instanceof NavigationEnd) {
                if (event.url == this.lastPoppedUrl) {
                    this.lastPoppedUrl = undefined;
                    window.scrollTo(0, this.yScrollStack.pop());
                } else {
                    window.scrollTo(0, 0);
                }
            }
        });
        this._router = this.router.events.filter(event => event instanceof NavigationEnd).subscribe((event: NavigationEnd) => {
            elemMainPanel.scrollTop = 0;
            elemSidebar.scrollTop = 0;
        });
        //   if (window.matchMedia(`(min-width: 960px)`).matches && !this.isMac()) {
        //       let ps = new PerfectScrollbar(elemMainPanel);
        //       ps = new PerfectScrollbar(elemSidebar);
        //   }

        const window_width = $(window).width();
        let $sidebar = $('.sidebar');


        let $sidebar_responsive = $('body > .navbar-collapse');
        let $sidebar_img_container = $sidebar.find('.sidebar-background');


        if (window_width > 767) {
            if ($('.fixed-plugin .dropdown').hasClass('show-dropdown')) {
                $('.fixed-plugin .dropdown').addClass('open');
            }

        }

        $('.fixed-plugin a').click(function (event) {
            //if we click on switch, stop propagation of the event, so the dropdown will not be hide, otherwise we set the  section active
            if ($(this).hasClass('switch-trigger')) {
                if (event.stopPropagation) {
                    event.stopPropagation();
                } else if (window.event) {
                    window.event.cancelBubble = true;
                }
            }
        });

        $('.fixed-plugin .badge').click(function () {
            let $full_page_background = $('.full-page-background');


            $(this).siblings().removeClass('active');
            $(this).addClass('active');

            var new_color = $(this).data('color');

            if ($sidebar.length !== 0) {
                $sidebar.attr('data-color', new_color);
            }

            if ($sidebar_responsive.length != 0) {
                $sidebar_responsive.attr('data-color', new_color);
            }
        });

        $('.fixed-plugin .img-holder').click(function () {
            let $full_page_background = $('.full-page-background');

            $(this).parent('li').siblings().removeClass('active');
            $(this).parent('li').addClass('active');


            var new_image = $(this).find('img').attr('src');

            if ($sidebar_img_container.length != 0) {
                $sidebar_img_container.fadeOut('fast', function () {
                    $sidebar_img_container.css('background-image', 'url("' + new_image + '")');
                    $sidebar_img_container.fadeIn('fast');
                });
            }

            if ($full_page_background.length != 0) {

                $full_page_background.fadeOut('fast', function () {
                    $full_page_background.css('background-image', 'url("' + new_image + '")');
                    $full_page_background.fadeIn('fast');
                });
            }

            if ($sidebar_responsive.length != 0) {
                $sidebar_responsive.css('background-image', 'url("' + new_image + '")');
            }
        });
    }

    onMarket() {
        const found = this.router.url.includes('generalNotificationForm');
        return found
    }

    onCrm() {
        const found = this.router.url.includes('userGroups');
        return found
    }

    onUpdateSignal() {
        this.appType = this.appDataService.appType
        this.sideBarVisible = this.appDataService.sideBarVisible;
        this.cdref.detectChanges();
    }


    ngAfterViewInit() {
        this.runOnRouteChange();
    }

    isMaps(path) {
        var titlee = this.location.prepareExternalUrl(this.location.path());

        titlee = titlee.slice(1);
        if (path == titlee) {
            return false;
        } else {
            return true;
        }
    }

    runOnRouteChange(): void {
        if (window.matchMedia(`(min-width: 960px)`).matches && !this.isMac()) {
            const elemMainPanel = <HTMLElement>document.querySelector('.main-panel');
        }
    }

    isMac(): boolean {
        let bool = false;
        if (navigator.platform.toUpperCase().indexOf('MAC') >= 0 || navigator.platform.toUpperCase().indexOf('IPAD') >= 0) {
            bool = true;
        }
        return bool;
    }

    ngOnDestroy() {
        this.updateEmmiterSubscriptionId.unsubscribe()
    }

}
