import {Component, OnInit, EventEmitter} from '@angular/core';
import {RandomOrderGenerationSettingsDTO} from '../../../shared/dto/random-order-generation-settings-dto';
import {ActiveProductsService} from '../../../services/activeProducts.service';
import {NotificationToastService} from '../../../shared/utils/notification-toast-service';
import {MyOrdersService} from '../../../services/myOrders.service';
import {DatePipe} from '@angular/common';
import { MarketCountryType, UpdateSignals, AppType } from 'app/shared/config/app-constants';
import { Subscription } from 'rxjs';
import { AppDataService } from 'app/services/app-data.service';

@Component({
  selector: 'app-order-generator',
  templateUrl: './order-generator.component.html',
  styleUrls: ['./order-generator.component.scss']
})
export class OrderGeneratorComponent implements OnInit {

  public orderTotal: number;
  public randomOrderGenerationSettingsDTO: RandomOrderGenerationSettingsDTO;
  updateEmmiterSubscriptionId: Subscription
  updateEmmiter = new EventEmitter();

  constructor(public activeProductsService: ActiveProductsService,
              public notificationToastService: NotificationToastService,
              public myOrdersService: MyOrdersService,
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
   
    this.randomOrderGenerationSettingsDTO = new RandomOrderGenerationSettingsDTO();
    this.onUpdateSignal()
    //this.randomOrderGenerationSettingsDTO.marketCode = MarketCountryType.DEFAULT_MARKET_CYPRUS;
  }
  onUpdateSignal(){
    if(this.appDataService.userDto === null){
      return;
    }
    else{
        this.appDataService.defaultMarket = this.appDataService.userDto.defaultMarket
        if(this.appDataService.defaultMarket=== MarketCountryType.DEFAULT_MARKET_BULGARIA){
          this.randomOrderGenerationSettingsDTO.marketCode =  MarketCountryType.DEFAULT_MARKET_BULGARIA
        }
        else{
          this.randomOrderGenerationSettingsDTO.marketCode =  MarketCountryType.DEFAULT_MARKET_CYPRUS
        } 
    }
  }


  generateOrders() {

    this.randomOrderGenerationSettingsDTO.orderTypes =
        this.randomOrderGenerationSettingsDTO.orderTypes.filter(orderType => orderType !== '');

    this.randomOrderGenerationSettingsDTO.orderDirections =
        this.randomOrderGenerationSettingsDTO.orderDirections.filter(orderDirection => orderDirection !== '');

    if (!this.verifySettingsDTOData()) {
      this.notificationToastService.showNotification(
          '<h4>There are empty fields or numerical range errors on this form</h4> Fill data, correct errors & click Run again!',
          'info',
          'top',
          'center',
          'danger');
      return;
    }

    this.myOrdersService.generateRandom(this.randomOrderGenerationSettingsDTO).subscribe(() => {
          this.notificationToastService.showNotification(
              '<h4>Process executed.</h4>Random orders generation finished',
              'info',
              'top',
              'center',
              'success');

        }, error => {
          this.notificationToastService.showNotification(
              'Error! <br> <h4>Error: ' + error.error.error + ' <br> Message: ' + error.error.message + ' </h4>   ',
              'info',
              'top',
              'center',
              'danger');
        }
    );
  }

  hideIcebergFields() {
    if (this.randomOrderGenerationSettingsDTO.orderTypes.includes('ICEBERG')) {
      return false;
    } else {
      return true;
    }
  }

  verifySettingsDTOData() {

    if (this.randomOrderGenerationSettingsDTO.totalOrders === undefined ||
        this.randomOrderGenerationSettingsDTO.marketCode === undefined ||
        this.randomOrderGenerationSettingsDTO.minPrice === undefined ||
        this.randomOrderGenerationSettingsDTO.maxPrice === undefined ||
        this.randomOrderGenerationSettingsDTO.minQuantity === undefined ||
        this.randomOrderGenerationSettingsDTO.maxQuantity === undefined) {
      return false;
    }

    if (this.randomOrderGenerationSettingsDTO.totalOrders <= 0 ||
        this.randomOrderGenerationSettingsDTO.marketCode === '' ||
        this.randomOrderGenerationSettingsDTO.minPrice <= 0 ||
        this.randomOrderGenerationSettingsDTO.maxPrice <= 0 ||
        this.randomOrderGenerationSettingsDTO.minQuantity <= 0 ||
        this.randomOrderGenerationSettingsDTO.maxQuantity <= 0 ||
        this.randomOrderGenerationSettingsDTO.maxPrice < this.randomOrderGenerationSettingsDTO.minPrice ||
        this.randomOrderGenerationSettingsDTO.maxQuantity < this.randomOrderGenerationSettingsDTO.minQuantity) {
      return false;
    }

    if (this.randomOrderGenerationSettingsDTO.orderTypes.includes('ICEBERG') &&
        (this.randomOrderGenerationSettingsDTO.minIcebergQuantity === undefined ||
            this.randomOrderGenerationSettingsDTO.maxIcebergQuantity === undefined ||
            this.randomOrderGenerationSettingsDTO.minIcebergPrice === undefined ||
            this.randomOrderGenerationSettingsDTO.maxIcebergPrice === undefined ||
            this.randomOrderGenerationSettingsDTO.minIcebergQuantity <= 0 ||
            this.randomOrderGenerationSettingsDTO.maxIcebergQuantity <= 0 ||
            this.randomOrderGenerationSettingsDTO.minIcebergPrice < 0 ||
            this.randomOrderGenerationSettingsDTO.maxIcebergPrice < 0 ||
            this.randomOrderGenerationSettingsDTO.maxIcebergQuantity < this.randomOrderGenerationSettingsDTO.minIcebergQuantity ||
            this.randomOrderGenerationSettingsDTO.maxIcebergPrice < this.randomOrderGenerationSettingsDTO.minIcebergPrice)) {
      return false;
    }

    return true;
  }

}
