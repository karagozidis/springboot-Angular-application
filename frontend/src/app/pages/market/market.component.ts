import { Component, OnInit, Input, EventEmitter } from '@angular/core';
import { AppDataService } from 'app/services/app-data.service';
import {AppType, UpdateSignals, AppConstants, MarketCountryType} from 'app/shared/config/app-constants';
import { OrderStateService } from 'app/services/orderState.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';
import { UserService } from 'app/services/user.service';
import { UserDto } from 'app/shared/dto/user-dto';
import { Subscription } from 'rxjs';

@Component({
    selector: 'app-market',
    templateUrl: './market.component.html',
    styleUrls: ['./market.component.scss']
})
export class MarketComponent implements OnInit {
    @Input() marketEnabled: true;
    tabIndex = 0 ;
    userDto: UserDto;
    updateEmmiterSubscriptionId: Subscription
    updateEmmiter = new EventEmitter();


    constructor(private appDataService: AppDataService,
        private orderStateService: OrderStateService,
        private userService: UserService) {
            this.updateEmmiterSubscriptionId =
            this.appDataService.updateEmmiter.subscribe(event => {
                if (event === UpdateSignals.USER_DTO || event === UpdateSignals.CRM_LOCAL_ACTIVE_STATE ||
                    event === UpdateSignals.APP_TYPE) {
                    this.onUpdateSignal()
                }
            })
        }

    changeTab(event){
        this.tabIndex = event.index;
    }
    onUpdateSignal(){
        console.log(this.appDataService.userDto)
        if(this.appDataService.userDto === null){
            return;
        }
        else{
            this.appDataService.defaultMarket = this.appDataService.userDto.defaultMarket
            if(this.appDataService.defaultMarket=== MarketCountryType.DEFAULT_MARKET_BULGARIA){
                this.orderStateService.country =  MarketCountryType.DEFAULT_MARKET_BULGARIA
            }
            else{
                this.orderStateService.country =  MarketCountryType.DEFAULT_MARKET_CYPRUS
            }
        }
    }

    ngOnInit() {
        this.appDataService.setAppType(AppType.MARKET)
        this.appDataService.setSideBarVisible(false)
    }

}
