import {Component, OnInit, EventEmitter} from '@angular/core';
import {ActiveProductsService} from '../../../services/activeProducts.service';
import {NotificationToastService} from '../../../shared/utils/notification-toast-service';
import {DatePipe} from '@angular/common';
import { MarketCountryType, UpdateSignals } from 'app/shared/config/app-constants';
import { Subscription } from 'rxjs';
import { AppDataService } from 'app/services/app-data.service';

export interface ProductDeliveryPeriod {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-product-generator',
  templateUrl: './product-generator.component.html',
  styleUrls: ['./product-generator.component.scss']
})
export class ProductGeneratorComponent implements OnInit {

  public productDate: Date;
  public productDelivaryPeriod: string;
  public productsGenerateMarketCode: string;
  productPeriods: ProductDeliveryPeriod[] = [
    {value: 'MINUTES60', viewValue: 'Hourly Products'},
    {value: 'MINUTES15', viewValue: 'Quarter-hourly Products'}
  ];
  updateEmmiterSubscriptionId: Subscription
  updateEmmiter = new EventEmitter();

  constructor(public activeProductsService: ActiveProductsService,
              public notificationToastService: NotificationToastService,
              public datePipe: DatePipe,
              public appDataService: AppDataService) {
                this.updateEmmiterSubscriptionId =
                this.appDataService.updateEmmiter.subscribe(event => {
                if (event === UpdateSignals.USER_DTO || event === UpdateSignals.CRM_LOCAL_ACTIVE_STATE ||
                    event === UpdateSignals.APP_TYPE) {
                    this.onUpdateSignal()
                }
                })
  }

  ngOnInit() {
    this.onUpdateSignal()
  }

  onUpdateSignal(){
    if(this.appDataService.userDto === null){
      return;
    }
    else{
        this.appDataService.defaultMarket = this.appDataService.userDto.defaultMarket
        if(this.appDataService.defaultMarket=== MarketCountryType.DEFAULT_MARKET_BULGARIA){
          this.productsGenerateMarketCode =  MarketCountryType.DEFAULT_MARKET_BULGARIA
        }
        else{
          this.productsGenerateMarketCode =  MarketCountryType.DEFAULT_MARKET_CYPRUS
        } 
    }
  }

  generateProducts() {
    if (this.productDelivaryPeriod === undefined || this.productDelivaryPeriod === null ||
        this.productDate === undefined || this.productDate === null) {

      this.notificationToastService.showNotification(
          '<h4>There are empty fields on the Products generator form</h4> Fill them & click run again.',
          'info',
          'top',
          'center',
          'danger');
    }

    const atDate = this.datePipe.transform(this.productDate, 'yyyy-MM-dd') + 'T00:00:00Z';

    this.activeProductsService.generateDay(atDate, this.productDelivaryPeriod, this.productsGenerateMarketCode).subscribe(() => {

      this.notificationToastService.showNotification(
          '<h4>Process executed.</h4>Products generation finished',
          'info',
          'top',
          'center',
          'success');
    });
  }

}
