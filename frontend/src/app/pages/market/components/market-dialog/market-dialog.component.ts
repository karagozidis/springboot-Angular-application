import { Component, OnInit, Inject, Input, HostListener } from '@angular/core';

import { Router } from '@angular/router';
import {
  MatDialog,
  MAT_DIALOG_DATA,
  MatDialogRef,
  MatTableDataSource,
  MAT_TOOLTIP_DEFAULT_OPTIONS,
  MatTooltipDefaultOptions
} from '@angular/material';

// import { UserGroupsComponent } from '../user-groups/user-groups.component';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { DialogListComponent } from 'app/shared/components/dialog-list/dialog-list.component';
import { MarketTableComponent } from '../market-table/market-table.component';
import { OrderDTO } from 'app/shared/dto/order-dto';
import { ProductDTO } from 'app/shared/dto/product-dto';
import { OrderMetadataDTO } from 'app/shared/dto/orderMetadataDTO';
import { OrderStateService, LastOrderData } from 'app/services/orderState.service';
import { MarketConfirmationDialogComponent } from '../market-confirmation-dialog/market-confirmation-dialog.component';
import { BasketDataSourceChangedService } from 'app/services/basketDataSourceChanged.service';
import { ActiveProductsService } from 'app/services/activeProducts.service';
import { DataSourceChangedService } from 'app/services/dataSourceChanged.service';
import { MyOrdersService } from 'app/services/myOrders.service';
import { AppConstants } from 'app/shared/config/app-constants';
import { BasketService } from 'app/services/basket.service';
import { NotificationToastService } from 'app/shared/utils/notification-toast-service';
import * as moment from 'moment'
import { AppDataService } from 'app/services/app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';

export const myCustomTooltipDefaults: MatTooltipDefaultOptions = {
  showDelay: 700,
  hideDelay: 0,
  touchendHideDelay: 1000,
};

@Component({
  selector: 'app-market-diaolog',
  templateUrl: './market-dialog.component.html',
  styleUrls: ['./market-dialog.component.scss'],
  providers: [
    { provide: MAT_TOOLTIP_DEFAULT_OPTIONS, useValue: myCustomTooltipDefaults }
  ],

})
export class MarketDialogComponent implements OnInit {
  @Input() selectionArray: any[];
  isPopupOpened = true;
  selectedMarketChoice = '';
  aquamarine = true;
  redColor = false;
  basketTypeDisabled = true;
  firstParamDisabled = true;
  secondParamDisabled = true;
  thirdParamDisabled = true;
  firstParamOpacity = 0;
  secondParamOpacity = 0;
  thirdParamOpacity = 0;
  basketTypeOpacity = 0;
  firstParamPlaceholder = '';
  secondParamPlaceholder = '';
  thirdParamPlaceholder = '';
  selectedMarketTypeChoice = '';
  selectedBasketTypeChoice = '';
  // basketTypes = AppConstants['Market']['OrderBackendLabels']['BasketTypeExclusive']
  orderDirectionTypes = AppConstants.Market.BackendEnums.OrderDirection
  basketTypes = AppConstants.Market.BackendEnums.BasketType
  orderTypes = AppConstants.Market.BackendEnums.OrderType;


  orderMetadataObject: OrderMetadataDTO = new OrderMetadataDTO();

  orderDialogData = new OrderDialogData();

  orderForm: FormGroup;
  productTitleInDialog = '';
  deliveryPeriodInDialog = '';
  dateInDialog = '';
  deliveryTimeStartInDialog = '';
  updatedBasketDataSource = new MatTableDataSource([]);
  disabledStatus = false;
  scenario1TestArray = []
  

 

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    public orderStateService: OrderStateService,
    public activeProductsService: ActiveProductsService,
    public basketDataSourceChanged: BasketDataSourceChangedService,
    public myOrdersService: MyOrdersService,
    public basketService: BasketService,
    private appDataService: AppDataService,
    public dataSourceChanged: DataSourceChangedService,
    private dialogRef: MatDialogRef<MarketTableComponent>,
    private notificationToastService: NotificationToastService,
    @Inject(MAT_DIALOG_DATA) public dialogAnyData: any,
  ) {
    this.getCurrentOrderTypes()
  }

  ngOnInit() {
    //this.getCurrentOrderTypes()
    if (this.dialogAnyData.arrayViewInDialog === 'basket_orders') {
      this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.BASKET
    }
    if (this.dialogAnyData.compAction === 'updateOrder') {
      this.getSelectedOrder();
    } else {
      this.initializeGeneralInformation();
    }
  }
  onEnterMethod() {
    if (this.dialogAnyData.tableIdToPass === 3) {
      this.onUpdateButtonClick();
    }
    else if (this.selectedMarketTypeChoice !== 'BASKET' && this.dialogAnyData.tableIdToPass === 1) {
      this.placeOrder();
    }
    else {
      this.addOrderToBasket()
    }

  }
  getCurrentOrderTypes() {
    this.orderStateService.getCurrentOrderTypeStatus().subscribe(response => {
      this.scenario1TestArray = response.scenario.split(",");
    },
      onError => {
        this.notificationToastService.showNotification(
          'There is no available market scenario. Please provide some order Type(s) first.', 'error', 'top',
          'center', 'danger')
      }
    )
  }

  initializeCommonInformation() {
    this.productTitleInDialog = this.dialogAnyData.title;
    this.orderDialogData.productName = this.productTitleInDialog;
    console.log(this.orderStateService.getLastOrderData())
    this.orderDialogData.orderDirection = this.selectedMarketChoice
    let today = new Date();
    let createdOnDate = new Date(this.dialogAnyData.date);

    if (createdOnDate.getDay() === today.getDay()) {
      this.dateInDialog = 'Today'
    }
    else {
      this.dateInDialog = 'Tomorrow'
    }

    this.orderDialogData.date = this.dateInDialog;
    const deliveryTimeStartDate = new Date(this.dialogAnyData.deliveryTimeStart);
    var deliveryTimeStartHours = deliveryTimeStartDate.getHours().toString();

    if (deliveryTimeStartHours === '24') {
      deliveryTimeStartHours = '00'
    }

    let deliveryTimeStartMinutes;
    let closesAtDateMinutes;

    if (deliveryTimeStartDate.getMinutes() < 10) {
      deliveryTimeStartMinutes = '0' + deliveryTimeStartDate.getMinutes().toString()
    }
    else {
      deliveryTimeStartMinutes = deliveryTimeStartDate.getMinutes().toString()
    }

    this.deliveryTimeStartInDialog = deliveryTimeStartHours.toString() + ':' + deliveryTimeStartMinutes;
    this.orderDialogData.deliveryTimeStart = this.dialogAnyData.deliveryTimeStart;

    if (this.dialogAnyData.deliveryPeriod === AppConstants.Market.BackendEnums.Product.ONE_HOUR_PERIOD) {
      this.deliveryPeriodInDialog = AppConstants.Market.UILabels.Product.ONE_HOUR_PERIOD
    }
    else {
      this.deliveryPeriodInDialog = AppConstants.Market.UILabels.Product.FIFTEEN_MINUTES_PERIOD
    }

    this.orderDialogData.period = this.deliveryPeriodInDialog;
  }

  initializeGeneralInformation() {
    if (this.orderStateService.processMode === AppConstants.Market.UILabels.ProcessMode.UPDATE_BASKET_FROM_ORDERS_ARRAY) {
      this.initializeInfoFromBasketOrderUpdateProcess()
    }
    else {
      this.selectedMarketTypeChoice = this.orderStateService.orderType;
      this.selectedBasketTypeChoice = this.orderStateService.getBasketType();
      console.log('Last Basket Type:' + this.selectedBasketTypeChoice)
      console.log(this.orderStateService.getLastOrderData())
      this.selectedMarketChoice = this.orderStateService.getLastOrderData().orderDirection

      if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.BUY) {
        this.redColor = true;
        this.aquamarine = false;
      }
      else if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.SELL) {
        this.aquamarine = true;
        this.redColor = false;
      }
      // Here is the logic when we open a mat-dialog and we have already placed an order(simple order) previously.
      if (this.orderStateService.getLastOrderData().orderType !== AppConstants.Market.BackendEnums.OrderType.BASKET && this.orderStateService.getLastOrderData().orderType !== undefined) {
        this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE
        if (this.orderStateService.getLastOrderData().orderType === AppConstants.Market.BackendEnums.OrderType.ICEBERG) {
          this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.ICEBERG;
          this.clearMetadataInputValues();
          console.log('param1 & param2');
          this.firstParamDisabled = false;
          this.secondParamDisabled = false;
          this.firstParamOpacity = 1;
          this.secondParamOpacity = 1;
          this.firstParamPlaceholder = 'Maximum Iceberg Quantity(MW)';
          this.secondParamPlaceholder = 'Price Change(€/MWh)';
          this.orderMetadataObject = JSON.parse(this.orderStateService.getLastOrderData()['metadata']);
          console.log(this.orderMetadataObject)
          this.orderDialogData.metadata.param1 = this.orderMetadataObject['icebergQuantity'];
          console.log(this.orderDialogData.metadata.param1)
          this.orderDialogData.metadata.param2 = this.orderMetadataObject['icebergPriceDelta'];
        }
        if (this.orderStateService.getLastOrderData().orderType === AppConstants.Market.BackendEnums.OrderType.AON) {
          this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.AON;
        }
        if (this.orderStateService.getLastOrderData().orderType === AppConstants.Market.BackendEnums.OrderType.IOC) {
          this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.IOC
        }
        if (this.orderStateService.getLastOrderData().orderType === AppConstants.Market.BackendEnums.OrderType.FOK) {
          this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.FOK
        }
        if (this.orderStateService.getLastOrderData().orderType === AppConstants.Market.BackendEnums.OrderType.LIMIT) {
          this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.LIMIT
        }
      }
      // Here is the logic when we open a mat-dialog and we have already placed an order(basket order) previously.
      else if (this.orderStateService.getLastOrderData().orderType === AppConstants.Market.BackendEnums.OrderType.BASKET) {
        this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.BASKET
        if (this.orderStateService.getLastOrderData().basket.basketType === AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED) {
          this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED;
          this.firstParamDisabled = false;
          this.basketTypeOpacity = 1;
          this.basketTypeDisabled = false;
          this.firstParamOpacity = 1;
          this.firstParamPlaceholder = 'Total Quantity(MW)';
          this.orderMetadataObject = JSON.parse(this.orderStateService.getLastOrderData()['basket']['metadata']);
          console.log(this.orderMetadataObject)
          console.log(Object.keys(this.orderMetadataObject)[0])
          if (Object.keys(this.orderMetadataObject)[0] === 'param1') {
            this.orderDialogData.metadata.param1 = this.orderMetadataObject['param1'];
          }
          else {
            this.orderDialogData.metadata.param1 = this.orderMetadataObject['totalQuantity'];
          }
        }
        else if (this.orderStateService.getLastOrderData()['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED) {
          this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED;
          this.basketTypeDisabled = false;
          this.firstParamDisabled = false;
          this.secondParamDisabled = false;
          this.thirdParamDisabled = false;
          this.basketTypeOpacity = 1;
          this.firstParamOpacity = 1;
          this.secondParamOpacity = 1;
          this.thirdParamOpacity = 1;
          this.firstParamPlaceholder = 'Maximum Capacity';
          this.secondParamPlaceholder = 'Initial Charge';
          this.thirdParamPlaceholder = 'Rated Power';
          this.orderMetadataObject = JSON.parse(this.orderStateService.getLastOrderData()['basket']['metadata']);
          if (Object.keys(this.orderMetadataObject)[0] === 'param1') {
            this.orderDialogData.metadata.param1 = this.orderMetadataObject['param1'];
            this.orderDialogData.metadata.param2 = this.orderMetadataObject['param2'];
            this.orderDialogData.metadata.param3 = this.orderMetadataObject['param3'];
          }
          else {
            this.orderDialogData.metadata.param1 = this.orderMetadataObject['maxCapacity'];
            this.orderDialogData.metadata.param2 = this.orderMetadataObject['initialCharge'];
            this.orderDialogData.metadata.param3 = this.orderMetadataObject['ratedPower'];
          }
        }
        else if (this.orderStateService.getLastOrderData()['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE) {
          this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE;
          this.firstParamDisabled = false;
          this.basketTypeOpacity = 1;
          this.basketTypeDisabled = false;
          this.firstParamOpacity = 1;
          this.firstParamPlaceholder = 'Max Matched Orders';
          this.orderMetadataObject = JSON.parse(this.orderStateService.getLastOrderData()['basket']['metadata']);
          console.log(this.orderMetadataObject)
          console.log(Object.keys(this.orderMetadataObject)[0])
          if (Object.keys(this.orderMetadataObject)[0] === 'param1') {
            this.orderDialogData.metadata.param1 = this.orderMetadataObject['param1'];
          }
          else {
            this.orderDialogData.metadata.param1 = this.orderMetadataObject['maxMatchedOrders'];
          }
        }
        else if (this.orderStateService.getLastOrderData()['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.LINKED) {
          this.basketTypeDisabled = false;
          this.basketTypeOpacity = 1;
          this.basketTypeDisabled = false;
          this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.LINKED
        }

      }
      else {
        // Here is the logic for the first time we open a mat-dialog in order to create an order.
        this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE
      }
      this.initializeCommonInformation();
    }
  }
  initializeInfoFromBasketOrderUpdateProcess() {
    this.selectedMarketTypeChoice = this.orderStateService.orderType;
    this.selectedBasketTypeChoice = this.orderStateService.getBasketType();
    console.log('Last Basket Type:' + this.selectedBasketTypeChoice)
    console.log(this.orderStateService.getLastOrderData())
    this.selectedMarketChoice = this.orderStateService.getLastOrderData().orderDirection

    if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.BUY) {
      this.redColor = true;
      this.aquamarine = false;
    } else if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.SELL) {
      this.aquamarine = true;
      this.redColor = false;
    }
    // Here is the logic when we open a mat-dialog and we have already placed an order(simple order) previously.
    if (this.orderStateService.getLastOrderData().orderType !== AppConstants.Market.BackendEnums.OrderType.BASKET && this.orderStateService.getLastOrderData().orderType !== undefined) {
      this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE
      // this.selectedMarketTypeChoice = this.orderStateService.orderType;
      if (this.orderStateService.getLastOrderData().orderType === AppConstants.Market.BackendEnums.OrderType.ICEBERG) {
        this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.ICEBERG;
        this.clearMetadataInputValues();
        console.log('param1 & param2');
        this.firstParamDisabled = false;
        this.secondParamDisabled = false;
        this.firstParamOpacity = 1;
        this.secondParamOpacity = 1;
        this.firstParamPlaceholder = 'Maximum Iceberg Quantity(MW)';
        this.secondParamPlaceholder = 'Price Change(€/MWh)';
        this.orderMetadataObject = JSON.parse(this.orderStateService.getLastOrderData()['metadata']);
        console.log(this.orderMetadataObject)
        this.orderDialogData.metadata.param1 = this.orderMetadataObject['icebergQuantity'];
        console.log(this.orderDialogData.metadata.param1)
        this.orderDialogData.metadata.param2 = this.orderMetadataObject['icebergPriceDelta'];
      }
      if (this.orderStateService.getLastOrderData().orderType === AppConstants.Market.BackendEnums.OrderType.AON) {
        this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.AON;
      }
      if (this.orderStateService.getLastOrderData().orderType === AppConstants.Market.BackendEnums.OrderType.IOC) {
        this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.IOC
      }
      if (this.orderStateService.getLastOrderData().orderType === AppConstants.Market.BackendEnums.OrderType.FOK) {
        this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.FOK
      }
      if (this.orderStateService.getLastOrderData().orderType === AppConstants.Market.BackendEnums.OrderType.LIMIT) {
        this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.LIMIT
      }

    }
    else if (
      // Here is the logic when we open a mat-dialog and we have already placed an order(basket order) previously.
      this.orderStateService.getLastOrderData().orderType === AppConstants.Market.BackendEnums.OrderType.BASKET) {
      this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.BASKET
      if (this.orderStateService.getLastOrderData()['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED) {
        this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED;
        this.firstParamDisabled = false;
        this.basketTypeOpacity = 1;
        this.basketTypeDisabled = false;
        this.firstParamOpacity = 1;
        this.firstParamPlaceholder = 'Total Quantity(MW)';
        this.orderMetadataObject = JSON.parse(this.orderStateService.getLastOrderData()['basket']['metadata']);
        console.log(this.orderMetadataObject)
        if (Object.keys(this.orderMetadataObject)[0] === 'param1') {
          this.orderDialogData.metadata.param1 = this.orderMetadataObject['param1'];
        }
        else {
          this.orderDialogData.metadata.param1 = this.orderMetadataObject['totalQuantity'];
        }
      }
      else if (
        this.orderStateService.getLastOrderData()['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED) {
        this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED;
        this.basketTypeDisabled = false;
        this.firstParamDisabled = false;
        this.secondParamDisabled = false;
        this.thirdParamDisabled = false;
        this.basketTypeOpacity = 1;
        this.firstParamOpacity = 1;
        this.secondParamOpacity = 1;
        this.thirdParamOpacity = 1;
        this.firstParamPlaceholder = 'Maximum Capacity';
        this.secondParamPlaceholder = 'Initial Charge';
        this.thirdParamPlaceholder = 'Rated Power';
        this.orderMetadataObject = JSON.parse(this.orderStateService.getLastOrderData()['basket']['metadata']);
        if (Object.keys(this.orderMetadataObject)[0] === 'param1') {
          this.orderDialogData.metadata.param1 = this.orderMetadataObject['param1'];
          this.orderDialogData.metadata.param2 = this.orderMetadataObject['param2'];
          this.orderDialogData.metadata.param3 = this.orderMetadataObject['param3'];
        }
        else {
          this.orderDialogData.metadata.param1 = this.orderMetadataObject['maxCapacity'];
          this.orderDialogData.metadata.param2 = this.orderMetadataObject['initialCharge'];
          this.orderDialogData.metadata.param3 = this.orderMetadataObject['ratedPower'];
        }
      }
      else if (this.orderStateService.getLastOrderData()['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE) {
        this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE;
        this.firstParamDisabled = false;
        this.basketTypeOpacity = 1;
        this.basketTypeDisabled = false;
        this.firstParamOpacity = 1;
        this.firstParamPlaceholder = 'Max Matched Orders';
        this.orderMetadataObject = JSON.parse(this.orderStateService.getLastOrderData()['basket']['metadata']);
        console.log(this.orderMetadataObject)
        if (Object.keys(this.orderMetadataObject)[0] === 'param1') {
          this.orderDialogData.metadata.param1 = this.orderMetadataObject['param1'];
        }
        else {
          this.orderDialogData.metadata.param1 = this.orderMetadataObject['maxMatchedOrders'];
        }
      }
      else if (this.orderStateService.getLastOrderData()['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.LINKED) {
        this.basketTypeDisabled = false;
        this.basketTypeOpacity = 1;
        this.basketTypeDisabled = false;
        this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.LINKED
      }

    }
    else {
      // Here is the logic for the first time we open a mat-dialog in order to create an order.
      this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE
      console.log(this.selectedMarketTypeChoice);
      console.log(this.selectedBasketTypeChoice);
    }
    this.initializeCommonInformation();
  }

  openRemoveDialog() {
    if (this.orderStateService.orderMode === 'getBasketOrder') {
      const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
        data: {
          basketItemIndex: this.dialogAnyData.rowIndex,
          userCommand: 'clearSpecificRow'
        }
      });

      dialogRef.afterClosed().subscribe(result => {
        if (this.orderStateService.getResponse() === 'no') {
          return;
        }
        console.log('The dialog was closed');

        if (result !== null) {
          this.basketDataSourceChanged.basketChangedEvent.emit('basket changed');
          this.updatedBasketDataSource = new MatTableDataSource(
            this.orderStateService.getBasketTableArray()
          );
          this.orderStateService.setDataSourceToUpdate(
            this.updatedBasketDataSource
          );

          console.log(this.updatedBasketDataSource);
          this.isPopupOpened = false;
        }

        this.isPopupOpened = false;
        this.dialogRef.close();
      });
    }
    else {
      console.log(this.dialogAnyData.orderToUpdate)
      if (this.dialogAnyData.orderToUpdate.orderType === AppConstants.Market.BackendEnums.OrderType.BASKET) {
        this.deactivateBasketOrders();
      }
      else {
        const idToUpdate = this.dialogAnyData.orderToUpdate.id
        const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
          data: {
            orderItemIndex: this.dialogAnyData.rowIndex,
            userCommand: 'clearSpecificOrderFromList'
          }
        });

        dialogRef.afterClosed().subscribe(result => {
          if (this.orderStateService.getResponse() === 'no') {
            return;
          }
          console.log('The dialog was closed');
          this.myOrdersService.removeOrderFromList(idToUpdate).subscribe(updatedData => {
          console.log(updatedData)
          this.myOrdersService.getmyOrders().subscribe(ordersData => {
          console.log(ordersData);
              this.dialogRef.close();
            });
            this.isPopupOpened = false;

          })
          this.isPopupOpened = false;
          this.dialogRef.close();
        });
      }
    }

  }
  getSelectedOrder() {
    if (this.dialogAnyData.arrayViewInDialog === 'my_orders') {
      this.getIndividualOrderInfo();
    } else {
      this.getSelectedBasketOrderInfo();
    }
    console.log(this.orderMetadataObject);
  }
  deactivateBasketOrders() {
    console.log('Here')
    const basketIdToDelete = this.dialogAnyData.basketIds
    console.log(basketIdToDelete)
    const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
      data: {
        userCommand: 'deactivateBasketFromList'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (this.orderStateService.getResponse() === 'no') {
        return;
      }
      console.log('The dialog was closed');
      this.basketService.deactivateBasket(basketIdToDelete).subscribe(updatedData => {
      console.log(updatedData)
        this.myOrdersService.getmyOrders().subscribe(ordersData => {
          console.log(ordersData);
          this.dialogRef.close();
        });
        this.isPopupOpened = false;

      })
      this.isPopupOpened = false;
      this.dialogRef.close();
    });
  }

  getSelectedBasketOrderInfo() {
    console.log(this.dialogAnyData.orderToUpdate);
    console.log(this.orderStateService.processMode);
    this.basketTypeOpacity = 1;
    this.basketTypeDisabled = false;
    this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.BASKET;
    this.disabledStatus = true;
    if (this.dialogAnyData.orderToUpdate['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED) {
      this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED;
    } else if (
      this.dialogAnyData.orderToUpdate['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED) {
      this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED
    } else if (this.dialogAnyData.orderToUpdate['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE) {
      this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE;
    } else if (
      this.dialogAnyData.orderToUpdate['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.LINKED
    ) {
      this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.LINKED
    }

    this.productTitleInDialog = this.dialogAnyData.orderToUpdate['product']['name'];
    if (this.dialogAnyData.orderToUpdate['product']['period'] === AppConstants.Market.BackendEnums.Product.FIFTEEN_MINUTES_PERIOD) {
      this.deliveryPeriodInDialog = AppConstants.Market.UILabels.Product.FIFTEEN_MINUTES_PERIOD
    }
    else {
      this.deliveryPeriodInDialog = AppConstants.Market.UILabels.Product.ONE_HOUR_PERIOD
    }

    this.dateInDialog = this.dialogAnyData.orderToUpdate['date'];
    this.deliveryTimeStartInDialog = moment(new Date(this.dialogAnyData.orderToUpdate['product']['deliveryTimeStart'])).format('HH:mm  DD/MM/YYYY');
    this.orderDialogData.quantity = this.dialogAnyData.orderToUpdate.quantity;
    this.orderDialogData.price = this.dialogAnyData.orderToUpdate.price;
    if (this.dialogAnyData.orderToUpdate['orderDirection'] === AppConstants.Market.BackendEnums.OrderDirection.BUY) {
      this.selectedMarketChoice = AppConstants.Market.BackendEnums.OrderDirection.BUY;
      this.redColor = true;
      this.aquamarine = false;
    } else {
      this.selectedMarketChoice = AppConstants.Market.BackendEnums.OrderDirection.SELL;
      this.aquamarine = true;
      this.redColor = false;
    }
    if (this.orderStateService.processMode === AppConstants.Market.UILabels.ProcessMode.ADD_TO_BASKET) {
      switch (this.selectedBasketTypeChoice) {
        case AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED: {
          this.firstParamOpacity = 1;
          this.firstParamDisabled = false;
          this.firstParamPlaceholder = 'Total Quantity(MW)';
          // this.orderMetadataObject = this.orderStateService.getBasketParameters()
          // this.orderDialogData.metadata.param1 = this.orderMetadataObject['param1']
          console.log((this.orderStateService.getBasketParameters()));
          console.log((this.orderStateService.getBasketParameters()['totalQuantity']));
          this.orderDialogData.metadata.param1 = this.orderStateService.getBasketParameters()[
            'totalQuantity'
          ];

          break;
        }
        case AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED: {
          this.firstParamOpacity = 1;
          this.secondParamOpacity = 1;
          this.thirdParamOpacity = 1;
          this.firstParamDisabled = false;
          this.secondParamDisabled = false;
          this.thirdParamDisabled = false;
          this.firstParamPlaceholder = 'Maximum Capacity';
          this.secondParamPlaceholder = 'Initial Charge';
          this.thirdParamPlaceholder = 'Rated Power';
          // this.orderMetadataObject = this.dialogAnyData.orderToUpdate['metadata']
          // this.orderDialogData.metadata.param1 = this.orderMetadataObject['param1']
          // this.orderDialogData.metadata.param2 = this.orderMetadataObject['param2']
          // this.orderDialogData.metadata.param3 = this.orderMetadataObject['param3']
          this.orderDialogData.metadata.param1 = this.orderStateService.getBasketParameters()['maxCapacity'];
          this.orderDialogData.metadata.param2 = this.orderStateService.getBasketParameters()['initialCharge'];
          this.orderDialogData.metadata.param3 = this.orderStateService.getBasketParameters()['ratedPower'];

          break;
        }
        case AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE: {
          this.firstParamOpacity = 1;
          this.firstParamDisabled = false;
          this.firstParamPlaceholder = 'Max Matched Orders';
          // this.orderMetadataObject = this.orderStateService.getBasketParameters()
          // this.orderDialogData.metadata.param1 = this.orderMetadataObject['param1']
          console.log((this.orderStateService.getBasketParameters()));
          console.log((this.orderStateService.getBasketParameters()['maxMatchedOrders']));
          this.orderDialogData.metadata.param1 = this.orderStateService.getBasketParameters()[
            'maxMatchedOrders'
          ];

          break;
        }
        default:
          {
            this.firstParamOpacity = 0;
            this.secondParamOpacity = 0;
            this.thirdParamOpacity = 0;
            this.firstParamDisabled = true;
            this.secondParamDisabled = true;
            this.thirdParamDisabled = true;
            break;
          }
          if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.BUY) {
            this.redColor = true;
            this.aquamarine = false;
          } else if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.SELL) {
            this.aquamarine = true;
            this.redColor = false;
            console.log(this.selectedMarketChoice);
          }
      }
    }
    else {
      switch (this.selectedBasketTypeChoice) {
        case AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED: {
          this.firstParamOpacity = 1;
          this.firstParamDisabled = false;
          this.firstParamPlaceholder = 'Total Quantity(MW)';
          // this.orderMetadataObject = this.orderStateService.getBasketParameters()
          // this.orderDialogData.metadata.param1 = this.orderMetadataObject['param1']
          console.log((this.orderStateService.getBasketParameters()));
          console.log((this.orderStateService.getBasketParameters()['totalQuantity']));
          this.orderDialogData.metadata.param1 = this.orderStateService.getBasketParameters()[
            'totalQuantity'
          ];

          break;
        }
        case AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED: {
          this.firstParamOpacity = 1;
          this.secondParamOpacity = 1;
          this.thirdParamOpacity = 1;
          this.firstParamDisabled = false;
          this.secondParamDisabled = false;
          this.thirdParamDisabled = false;
          this.firstParamPlaceholder = 'Maximum Capacity';
          this.secondParamPlaceholder = 'Initial Charge';
          this.thirdParamPlaceholder = 'Rated Power';
          // this.orderMetadataObject = this.dialogAnyData.orderToUpdate['metadata']
          // this.orderDialogData.metadata.param1 = this.orderMetadataObject['param1']
          // this.orderDialogData.metadata.param2 = this.orderMetadataObject['param2']
          // this.orderDialogData.metadata.param3 = this.orderMetadataObject['param3']
          this.orderDialogData.metadata.param1 = this.orderStateService.getBasketParameters()['maxCapacity'];
          this.orderDialogData.metadata.param2 = this.orderStateService.getBasketParameters()['initialCharge'];
          this.orderDialogData.metadata.param3 = this.orderStateService.getBasketParameters()['ratedPower'];

          break;
        }
        case AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE: {
          this.firstParamOpacity = 1;
          this.firstParamDisabled = false;
          this.firstParamPlaceholder = 'Max Matched Orders';
          // this.orderMetadataObject = this.orderStateService.getBasketParameters()
          // this.orderDialogData.metadata.param1 = this.orderMetadataObject['param1']
          console.log((this.orderStateService.getBasketParameters()));
          console.log((this.orderStateService.getBasketParameters()['maxMatchedOrders']));
          this.orderDialogData.metadata.param1 = this.orderStateService.getBasketParameters()[
            'maxMatchedOrders'
          ];

          break;
        }
        default:
          {
            this.firstParamOpacity = 0;
            this.secondParamOpacity = 0;
            this.thirdParamOpacity = 0;
            this.firstParamDisabled = true;
            this.secondParamDisabled = true;
            this.thirdParamDisabled = true;
            break;
          }
          if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.BUY) {
            this.redColor = true;
            this.aquamarine = false;
          } else if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.SELL) {
            this.aquamarine = true;
            this.redColor = false;
            console.log(this.selectedMarketChoice);
          }
      }
    }
    console.log(this.selectedMarketTypeChoice);
    console.log(this.selectedBasketTypeChoice);
    console.log(this.orderDialogData);
  }

  getIndividualOrderInfo() {
    console.log(this.dialogAnyData.orderToUpdate);
    this.productTitleInDialog = this.dialogAnyData.orderToUpdate['product'][
      'name'
    ];
    if (this.dialogAnyData.orderToUpdate['product']['period'] === 'MINUTES60') {
      this.deliveryPeriodInDialog = '1 Hour';
    } else {
      this.deliveryPeriodInDialog = '15 minutes';
    }

    var today = new Date();
    var deliveryTimeStartDate = new Date(
      this.dialogAnyData.orderToUpdate['product']['deliveryTimeStart']
    );
    console.log(deliveryTimeStartDate.getDay());
    if (deliveryTimeStartDate.getDay() === today.getDay()) {
      this.dateInDialog = 'Today'
    } else {
      this.dateInDialog = 'Tomorrow';
    }

    var deliveryTimeStartDate = new Date(
      this.dialogAnyData.orderToUpdate['product']['deliveryTimeStart']
    );
    // tslint:disable-next-line:max-line-length
    var deliveryTimeStartHours = deliveryTimeStartDate.getHours().toString();
    var deliveryTimeStartDateMinutes = deliveryTimeStartDate
      .getMinutes()
      .toString();
    if (deliveryTimeStartDate.getMinutes() < 10) {
      deliveryTimeStartDateMinutes =
        '0' + deliveryTimeStartDate.getMinutes().toString();
    } else {
      deliveryTimeStartDateMinutes = deliveryTimeStartDate
        .getMinutes()
        .toString();
    }

    if (deliveryTimeStartHours === '24') {
      deliveryTimeStartHours = '00'
    }
    this.deliveryTimeStartInDialog = deliveryTimeStartHours + ':' + deliveryTimeStartDateMinutes;
    this.orderDialogData.quantity = this.dialogAnyData.orderToUpdate.quantity;
    this.orderDialogData.price = this.dialogAnyData.orderToUpdate.price;
    console.log(this.dialogAnyData.orderToUpdate['orderDirection']);
    if (this.dialogAnyData.orderToUpdate['orderDirection'] === AppConstants.Market.BackendEnums.OrderDirection.BUY) {
      this.selectedMarketChoice = AppConstants.Market.BackendEnums.OrderDirection.BUY;
    } else {
      this.selectedMarketChoice = AppConstants.Market.BackendEnums.OrderDirection.SELL
    }
    if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.BUY) {
      this.redColor = true;
      this.aquamarine = false;
    } else if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.SELL) {
      this.aquamarine = true;
      this.redColor = false;
      console.log(this.selectedMarketChoice);
    }
    // this.selectedMarketTypeChoice = this.dialogAnyData.orderToUpdate['orderType']
    // tslint:disable-next-line:max-line-length
    // this.selectedMarketTypeChoice = this.selectedMarketTypeChoice.charAt(0).toUpperCase() + this.selectedMarketTypeChoice.slice(1).toLowerCase();
    this.selectedMarketTypeChoice = this.dialogAnyData.orderToUpdate[
      'orderType'
    ];
    switch (this.dialogAnyData.orderToUpdate['orderType']) {
      case AppConstants.Market.BackendEnums.OrderType.LIMIT:
        // tslint:disable-next-line:max-line-length
        this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.LIMIT
        break;
      case AppConstants.Market.BackendEnums.OrderType.ICEBERG:
        // tslint:disable-next-line:max-line-length
        this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.ICEBERG
        break;
      case AppConstants.Market.BackendEnums.OrderType.IOC:
        this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.IOC;
        break;
      case AppConstants.Market.BackendEnums.OrderType.FOK:
        this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.FOK;
        break;
      case AppConstants.Market.BackendEnums.OrderType.AON:
        this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.AON;
        break;
      case AppConstants.Market.BackendEnums.OrderType.BASKET:
        // tslint:disable-next-line:max-line-length
        this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.BASKET
        break;
      default:
        this.selectedMarketTypeChoice = '';
        break;
    }
    console.log(this.selectedMarketTypeChoice);
    console.log(this.dialogAnyData.metadata);

    // tslint:disable-next-line:max-line-length
    if (
      this.dialogAnyData.metadata === undefined &&
      this.selectedMarketTypeChoice === AppConstants.Market.BackendEnums.OrderType.ICEBERG
    ) {
      this.firstParamDisabled = false;
      this.secondParamDisabled = false;
      this.thirdParamDisabled = true;
      this.basketTypeDisabled = true;
      this.firstParamPlaceholder = 'Maximum Iceberg Quantity(MW)';
      this.secondParamPlaceholder = 'Price Change(€/MWh)';
      this.firstParamOpacity = 1;
      this.secondParamOpacity = 1;
      this.thirdParamOpacity = 0;
      this.basketTypeOpacity = 0;
      console.log(JSON.parse(this.dialogAnyData.orderToUpdate['metadata']));
      this.orderMetadataObject = JSON.parse(
        this.dialogAnyData.orderToUpdate['metadata']
      );
      console.log(this.orderMetadataObject)
      this.orderDialogData.metadata.param1 = this.orderMetadataObject[
        'icebergQuantity'
      ];
      this.orderDialogData.metadata.param2 = this.orderMetadataObject[
        'icebergPriceDelta'
      ];
      console.log(this.orderMetadataObject);
      console.log(this.orderDialogData.metadata.param1);
      console.log(this.orderDialogData.metadata.param2);
    } else if (
      this.dialogAnyData.metadata === undefined &&
      this.selectedMarketTypeChoice === AppConstants.Market.BackendEnums.OrderType.BASKET
    ) {
      this.basketTypeDisabled = false;
      this.basketTypeOpacity = 1;
      // tslint:disable-next-line:max-line-length
      this.selectedBasketTypeChoice = this.dialogAnyData.orderToUpdate['basket']['basketType'];
      switch (this.selectedBasketTypeChoice) {
        case AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE:
          this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE
          this.firstParamDisabled = false;
          this.firstParamPlaceholder = 'Max Matched Orders';
          this.firstParamOpacity = 1;
          this.orderMetadataObject = JSON.parse(
            this.dialogAnyData.orderToUpdate['basket']['metadata']
          );
          console.log(this.orderMetadataObject);
          this.orderDialogData.metadata.param1 = this.orderMetadataObject[
            'maxMatchedOrders'
          ];
          break;
        case AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED:
          this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED;
          this.firstParamDisabled = false;
          this.firstParamPlaceholder = 'Total Quantity(MW)';
          this.firstParamOpacity = 1;
          this.orderMetadataObject = JSON.parse(
            this.dialogAnyData.orderToUpdate['basket']['metadata']
          );
          console.log(this.orderMetadataObject);
          this.orderDialogData.metadata.param1 = this.orderMetadataObject[
            'totalQuantity'
          ];
          break;
        case AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED:
          this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED;
          this.firstParamDisabled = false;
          this.secondParamDisabled = false;
          this.thirdParamDisabled = false;
          this.firstParamPlaceholder = 'Maximum Capacity';
          this.secondParamPlaceholder = 'Initial Charge';
          this.thirdParamPlaceholder = 'Rated Power';
          this.firstParamOpacity = 1;
          this.secondParamOpacity = 1;
          this.thirdParamOpacity = 1;
          this.orderMetadataObject = JSON.parse(
            this.dialogAnyData.orderToUpdate['basket']['metadata']
          );
          console.log(this.orderMetadataObject);
          this.orderDialogData.metadata.param1 = this.orderMetadataObject['maxCapacity'];
          this.orderDialogData.metadata.param2 = this.orderMetadataObject['initialCharge'];
          this.orderDialogData.metadata.param3 = this.orderMetadataObject['ratedPower'];
          break;
          break;
        case AppConstants.Market.BackendEnums.BasketType.LINKED:
          // tslint:disable-next-line:max-line-length
          this.selectedBasketTypeChoice = AppConstants.Market.BackendEnums.BasketType.LINKED
          this.firstParamDisabled = true;
          this.secondParamDisabled = true;
          this.thirdParamDisabled = true;
          this.firstParamOpacity = 0;
          this.secondParamOpacity = 0;
          this.thirdParamOpacity = 0;

          break;
        default:
          this.selectedBasketTypeChoice = '';
          break;
      }
    } else {
      this.firstParamDisabled = true;
      this.secondParamDisabled = true;
      this.thirdParamDisabled = true;
      this.basketTypeDisabled = true;
      this.firstParamOpacity = 0;
      this.secondParamOpacity = 0;
      this.thirdParamOpacity = 0;
      this.basketTypeOpacity = 0;
    }
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  getOrderTypeTooltip(orderType) {
    let displayedData = ''
    if (orderType === AppConstants.Market.BackendEnums.OrderType.AON) {
      displayedData = AppConstants.Market.Descriptions.OrderType.AON
    }
    else if (orderType === AppConstants.Market.BackendEnums.OrderType.FOK) {
      displayedData = AppConstants.Market.Descriptions.OrderType.FOK
    }
    else if (orderType === AppConstants.Market.BackendEnums.OrderType.ICEBERG) {
      displayedData = AppConstants.Market.Descriptions.OrderType.ICEBERG
    }
    else if (orderType === AppConstants.Market.BackendEnums.OrderType.IOC) {
      displayedData = AppConstants.Market.Descriptions.OrderType.IOC
    }
    else if (orderType === AppConstants.Market.BackendEnums.OrderType.LIMIT) {
      displayedData = AppConstants.Market.Descriptions.OrderType.LIMIT
    }

    return displayedData;

  }
  getBasketTypeTooltip(basketType) {
    let displayedData = ''
    if(this.basketTypeDisabled == true){
      return
    }
    if (basketType === AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE) {
      displayedData = AppConstants.Market.Descriptions.BasketType.EXCLUSIVE
    }
    else if (basketType === AppConstants.Market.BackendEnums.BasketType.LINKED) {
      displayedData = AppConstants.Market.Descriptions.BasketType.LINKED
    }
    else if (basketType === AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED) {
      displayedData = AppConstants.Market.Descriptions.BasketType.VOLUME_CONSTRAINED
    }
    else if (basketType === AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED) {
      displayedData = AppConstants.Market.Descriptions.BasketType.CUMULATIVE_VOLUME_CONSTRAINED
    }

    return displayedData;

  }
  getParametersTooltip(placeHolder){
    let parametersDisplayedData = ''
    if (placeHolder === AppConstants.Market.BackendEnums.Parameters.IcebergParameters.MAXIMUM_ICEBERG_QUANTITY) {
      parametersDisplayedData = AppConstants.Market.Descriptions.Parameters.ICEBERG_MAXIMUM_QUANTITY
    }
    else if (placeHolder === AppConstants.Market.BackendEnums.Parameters.IcebergParameters.PRICE_CHANGE) {
      parametersDisplayedData = AppConstants.Market.Descriptions.Parameters.ICEBERG_PRICE_CHANGE
    }
    else if (placeHolder === AppConstants.Market.BackendEnums.Parameters.ExclusiveParameters.MAX_MATCHED_ORDERS) {
      parametersDisplayedData= AppConstants.Market.Descriptions.Parameters.EXCLUSIVE_MAX_MATCHED_ORDERS
    }
    else if (placeHolder === AppConstants.Market.BackendEnums.Parameters.VolumeConstrainedParameters.TOTAL_QUANTITY) {
      parametersDisplayedData = AppConstants.Market.Descriptions.Parameters.VOLUME_CONSTRAINED_TOTAL_QUANTITY
    }
    else if (placeHolder === AppConstants.Market.BackendEnums.Parameters.CumulativeVolumeConstrainedParameters.MAXIMUM_CAPACITY) {
      parametersDisplayedData = AppConstants.Market.Descriptions.Parameters.CUMULATIVE_VOLUME_CONSTRAINED_MAXIMUM_QUANTITY
    }
    else if (placeHolder === AppConstants.Market.BackendEnums.Parameters.CumulativeVolumeConstrainedParameters.INITIAL_CHARGE) {
      parametersDisplayedData = AppConstants.Market.Descriptions.Parameters.CUMULATIVE_VOLUME_CONSTRAINED_INITIAL_CHARGE
    }
    else if (placeHolder === AppConstants.Market.BackendEnums.Parameters.CumulativeVolumeConstrainedParameters.RATED_POWER) {
      parametersDisplayedData = AppConstants.Market.Descriptions.Parameters.CUMULATIVE_VOLUME_CONSTRAINED_RATED_POWER
    }

    return parametersDisplayedData;

  }

  marketChoiceChanged(marketPreference) {
    this.selectedMarketChoice = marketPreference.value;
    if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.BUY) {
      this.redColor = true;
      this.aquamarine = false;
    }
    if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.SELL) {
      this.aquamarine = true;
      this.redColor = false
      console.log(this.selectedMarketChoice);
    }
  }

  changeOrderType(eventData) {
    // this.initializeGeneralInformation()
    this.selectedMarketTypeChoice = eventData;
    console.log(this.selectedMarketChoice)
    if (this.selectedMarketTypeChoice === AppConstants.Market.BackendEnums.OrderType.BASKET) {
      if (this.selectedBasketTypeChoice === AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE) {
        this.clearMetadataInputValues();
        this.basketTypeDisabled = false;
        this.basketTypeOpacity = 1;
        this.firstParamDisabled = false;
        this.secondParamDisabled = true;
        this.thirdParamDisabled = true;
        this.firstParamOpacity = 1;
        this.secondParamOpacity = 0;
        this.thirdParamOpacity = 0;
        this.firstParamPlaceholder = 'Max Matched Orders';
        if (this.orderStateService.getBasketTableArray().length > 0) {
          console.log("------"+ this.orderDialogData.metadata.param1)
          this.orderDialogData.metadata.param1 = this.orderStateService.getBasketParameters()['param1'];
        }
      }
      else if (this.selectedBasketTypeChoice === AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED) {
        this.basketTypeDisabled = false;
        this.basketTypeOpacity = 1;
        // this.clearMetadataInputValues();
        this.firstParamDisabled = false;
        this.secondParamDisabled = true;
        this.thirdParamDisabled = true;
        this.firstParamOpacity = 1;
        this.secondParamOpacity = 0;
        this.thirdParamOpacity = 0;
        this.firstParamPlaceholder = 'Total Quantity(MW)';
        if (this.orderStateService.getBasketTableArray().length > 0) {
          console.log("------" + this.orderDialogData.metadata.param1)
          this.orderDialogData.metadata.param1 = this.orderStateService.getBasketParameters()['param1'];
        }
      }
      else if (this.selectedBasketTypeChoice === AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED) {
        this.basketTypeDisabled = false;
        this.basketTypeOpacity = 1;
        // this.clearMetadataInputValues();
        this.firstParamDisabled = false;
        this.secondParamDisabled = false;
        this.thirdParamDisabled = false;
        this.firstParamOpacity = 1;
        this.secondParamOpacity = 1;
        this.thirdParamOpacity = 1;
        this.firstParamPlaceholder = 'Maximum Capacity';
        this.secondParamPlaceholder = 'Initial Charge';
        this.thirdParamPlaceholder = 'Rated Power';
        if (this.orderStateService.getBasketTableArray().length > 0) {
          this.orderDialogData.metadata.param1 = this.orderStateService.getBasketParameters()[
            'param1'
          ];
          this.orderDialogData.metadata.param2 = this.orderStateService.getBasketParameters()[
            'param2'
          ];
          this.orderDialogData.metadata.param3 = this.orderStateService.getBasketParameters()[
            'param3'
          ];
        }
      } else {
        this.basketTypeDisabled = false;
        this.basketTypeOpacity = 1;
        this.clearMetadataInputValues();
        this.firstParamDisabled = true;
        this.secondParamDisabled = true;
        this.thirdParamDisabled = true;
        this.firstParamOpacity = 0;
        this.secondParamOpacity = 0;
        this.thirdParamOpacity = 0;
      }
    } else if (this.selectedMarketTypeChoice !== AppConstants.Market.BackendEnums.OrderType.BASKET) {
      this.basketTypeDisabled = true;
      this.basketTypeOpacity = 0;
      console.log('--Here--')
      if (this.selectedMarketTypeChoice === AppConstants.Market.BackendEnums.OrderType.ICEBERG) {
        console.log('--Here in Iceberg--')
        this.clearMetadataInputValues();
        this.firstParamDisabled = false;
        this.secondParamDisabled = false;
        this.firstParamOpacity = 1;
        this.secondParamOpacity = 1;
        this.thirdParamOpacity = 0;
        if (this.orderStateService.getLastOrderData().hasOwnProperty('metadata') !== false && this.orderStateService.getLastOrderData()['metadata'] !== null) {
          console.log('--iceberg with params--')
          this.orderMetadataObject = JSON.parse(this.orderStateService.getLastOrderData()['metadata']);
          console.log(this.orderMetadataObject)
          this.orderDialogData.metadata.param1 = this.orderMetadataObject['icebergQuantity'];
          console.log(this.orderDialogData.metadata.param1)
          this.orderDialogData.metadata.param2 = this.orderMetadataObject['icebergPriceDelta'];
        }

        this.firstParamPlaceholder = 'Maximum Iceberg Quantity(MW)';
        this.secondParamPlaceholder = 'Price Change(€/MWh)';
      } else {
        this.clearMetadataInputValues();
        this.firstParamDisabled = true;
        this.secondParamDisabled = true;
        this.thirdParamDisabled = true;
        this.firstParamOpacity = 0;
        this.secondParamOpacity = 0;
        this.thirdParamOpacity = 0;
      }
    }
  }

  changeBasketType(basketEvent) {
    this.selectedBasketTypeChoice = basketEvent;
    this.selectedMarketTypeChoice = AppConstants.Market.BackendEnums.OrderType.BASKET

    if (this.selectedBasketTypeChoice === AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE) {
      this.clearMetadataInputValues();
      this.firstParamDisabled = false;
      this.secondParamDisabled = true;
      this.thirdParamDisabled = true;
      this.firstParamOpacity = 1;
      this.secondParamOpacity = 0;
      this.thirdParamOpacity = 0;
      this.firstParamPlaceholder = 'Max Matched Orders';
      if (this.selectedBasketTypeChoice === this.orderStateService.getBasketType()) {
        this.orderDialogData.metadata.param1 = this.orderStateService.getBasketParameters()[
          'param1'
        ];
      }
    }
    if (this.selectedBasketTypeChoice === AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED) {
      this.clearMetadataInputValues();
      this.firstParamDisabled = false;
      this.secondParamDisabled = true;
      this.thirdParamDisabled = true;
      this.firstParamOpacity = 1;
      this.secondParamOpacity = 0;
      this.thirdParamOpacity = 0;
      this.firstParamPlaceholder = 'Total Quantity(MW)';
      if (this.selectedBasketTypeChoice === this.orderStateService.getBasketType()) {
        this.orderDialogData.metadata.param1 = this.orderStateService.getBasketParameters()[
          'param1'
        ];
      }
    }
    if (this.selectedBasketTypeChoice === AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED) {
      this.clearMetadataInputValues();
      this.firstParamDisabled = false;
      this.secondParamDisabled = false;
      this.thirdParamDisabled = false;
      this.firstParamOpacity = 1;
      this.secondParamOpacity = 1;
      this.thirdParamOpacity = 1;
      this.firstParamPlaceholder = 'Maximum Capacity';
      this.secondParamPlaceholder = 'Initial Charge';
      this.thirdParamPlaceholder = 'Rated Power';
      if (this.selectedBasketTypeChoice === this.orderStateService.getBasketType()) {
        this.orderDialogData.metadata.param1 = this.orderStateService.getBasketParameters()[
          'param1'
        ];
        this.orderDialogData.metadata.param2 = this.orderStateService.getBasketParameters()[
          'param2'
        ];
        this.orderDialogData.metadata.param3 = this.orderStateService.getBasketParameters()[
          'param3'
        ];
      }
    }
    if (this.selectedBasketTypeChoice === AppConstants.Market.BackendEnums.BasketType.LINKED) {
      this.clearMetadataInputValues();
      this.firstParamDisabled = true;
      this.secondParamDisabled = true;
      this.thirdParamDisabled = true;
      this.firstParamOpacity = 0;
      this.secondParamOpacity = 0;
      this.thirdParamOpacity = 0;
    }
  }

  onUpdateButtonClick() {
    const idToUpdate = this.dialogAnyData.orderToUpdate.id
    console.log("Entered updateOrder")
    let orderDtoToUpdate: OrderDTO = this.dialogAnyData.orderToUpdate
    console.log(orderDtoToUpdate)
    console.log("-----" + this.selectedMarketChoice)
    if (this.orderStateService.orderMode === 'getBasketOrder') {
      if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.BUY) {
        orderDtoToUpdate.orderDirection = AppConstants.Market.BackendEnums.OrderDirection.BUY
      }
      else {
        orderDtoToUpdate.orderDirection = AppConstants.Market.BackendEnums.OrderDirection.SELL
      }
      orderDtoToUpdate.metadata = JSON.stringify(this.orderDialogData.metadata)
      orderDtoToUpdate.quantity = this.orderDialogData.quantity
      orderDtoToUpdate.price = this.orderDialogData.price
      if (
        + orderDtoToUpdate.price <=0  ||
        + orderDtoToUpdate.quantity <=0

      ) {
        this.notificationToastService.showNotification('The value of  Quantity(MW)/Price(€/MWh) should be greater than 0.',
          'error',
          'top',
          'center',
          'danger');
        return;
      }
      this.orderStateService.setLastOrderDto(orderDtoToUpdate)
      this.orderStateService.setBasketArray(orderDtoToUpdate, this.dialogAnyData.rowIndex)
      this.orderStateService.setLastOrderDto(orderDtoToUpdate)
      console.log(orderDtoToUpdate.metadata)
      if (this.orderStateService.processMode === AppConstants.Market.UILabels.ProcessMode.ADD_TO_BASKET) {
        if (orderDtoToUpdate['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE) {
          orderDtoToUpdate.metadata = orderDtoToUpdate.metadata.replace('param1', 'maxMatchedOrders');
          orderDtoToUpdate['basket']['metadata'] = orderDtoToUpdate.metadata
          const updatedMaxMatchedOrdersValue = JSON.parse(orderDtoToUpdate['basket']['metadata'])['maxMatchedOrders']
          if (updatedMaxMatchedOrdersValue === '' || orderDtoToUpdate.metadata === null || orderDtoToUpdate.metadata === undefined
            || orderDtoToUpdate.price === '' || orderDtoToUpdate.quantity === '') {
            this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh)/Order Parameter(if needed) are required',
              'error',
              'top',
              'center',
              'danger');
            return;
          }
          if (+ updatedMaxMatchedOrdersValue <=0) {
            this.notificationToastService.showNotification('The value of MaxMatchedOrders parameter should be greater than 0.',
              'error',
              'top',
              'center',
              'danger');
            return;
          }

          const basketParametersUpdatedObject = JSON.parse(orderDtoToUpdate['basket']['metadata'])
          this.orderStateService.setBasketParameters(basketParametersUpdatedObject);
        }
        else if (orderDtoToUpdate['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED) {
          orderDtoToUpdate.metadata = orderDtoToUpdate.metadata.replace('param1', 'totalQuantity');
          orderDtoToUpdate['basket']['metadata'] = orderDtoToUpdate.metadata
          const updatedTotalQuantityValue = JSON.parse(orderDtoToUpdate['basket']['metadata'])['totalQuantity']
          if (updatedTotalQuantityValue === '' || orderDtoToUpdate.metadata === null || orderDtoToUpdate.metadata === undefined
            || orderDtoToUpdate.price === '' || orderDtoToUpdate.quantity === '') {
            this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh)/Order Parameter(if needed) are required',
              'error',
              'top',
              'center',
              'danger');
            return;
          }
          if (+updatedTotalQuantityValue <=0) {
            this.notificationToastService.showNotification('The value of TotalQuantity(MW) parameter should be greater than 0.',
              'error',
              'top',
              'center',
              'danger');
            return;
          }

          const basketParametersUpdatedObject = JSON.parse(orderDtoToUpdate['basket']['metadata'])
          this.orderStateService.setBasketParameters(basketParametersUpdatedObject);
        }
        else if (orderDtoToUpdate['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED) {
          orderDtoToUpdate.metadata = orderDtoToUpdate.metadata.replace('param1', 'maxCapacity');
          orderDtoToUpdate.metadata = orderDtoToUpdate.metadata.replace('param2', 'initialCharge');
          orderDtoToUpdate.metadata = orderDtoToUpdate.metadata.replace('param3', 'ratedPower');
          orderDtoToUpdate['basket']['metadata'] = orderDtoToUpdate.metadata
          const updatedMaxCapacityValue = JSON.parse(orderDtoToUpdate['basket']['metadata'])['maxCapacity']
          const updatedInitialChargeValue = JSON.parse(orderDtoToUpdate['basket']['metadata'])['initialCharge']
          const updateRatedPowerValue = JSON.parse(orderDtoToUpdate['basket']['metadata'])['ratedPower']
          if (updatedMaxCapacityValue === '' || updatedInitialChargeValue === '' || updateRatedPowerValue === '' || orderDtoToUpdate.metadata === null || orderDtoToUpdate.metadata === undefined
            || orderDtoToUpdate.price === '' || orderDtoToUpdate.quantity === '') {
            this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh)/Order Parameter(if needed) are required',
              'error',
              'top',
              'center',
              'danger');
            return;
          }
          if (+updatedMaxCapacityValue <=0) {
            this.notificationToastService.showNotification('The value of MaxCapacity parameter should be greater than 0.',
              'error',
              'top',
              'center',
              'danger');
            return;
          }
          if (+updateRatedPowerValue <=0) {
            this.notificationToastService.showNotification('The value of RatedPower parameter should be greater than 0.',
              'error',
              'top',
              'center',
              'danger');
            return;
          }

          const basketParametersUpdatedObject = JSON.parse(orderDtoToUpdate['basket']['metadata'])
          this.orderStateService.setBasketParameters(basketParametersUpdatedObject);
        }
        console.log(this.orderStateService.getBasketParameters())
      }
      else {
        if (orderDtoToUpdate['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE) {
          orderDtoToUpdate.metadata = orderDtoToUpdate.metadata.replace('param1', 'maxMatchedOrders');
          console.log(orderDtoToUpdate.metadata)
          console.log(orderDtoToUpdate['basket']['metadata'])
          console.log(JSON.parse(orderDtoToUpdate['basket']['metadata'])['maxMatchedOrders'])
          orderDtoToUpdate['basket']['metadata'] = orderDtoToUpdate.metadata
          const updatedMaxMatchedOrdersValue = JSON.parse(orderDtoToUpdate['basket']['metadata'])['maxMatchedOrders']
          if (updatedMaxMatchedOrdersValue === '' || updatedMaxMatchedOrdersValue === null || updatedMaxMatchedOrdersValue === undefined
            || orderDtoToUpdate.price === '' || orderDtoToUpdate.quantity === '') {
            this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh)/Order Parameter(if needed) are required',
              'error',
              'top',
              'center',
              'danger');
            return;
          }
          if (+updatedMaxMatchedOrdersValue <=0) {
            this.notificationToastService.showNotification('The value of MaxMatchedOrders parameter should be greater than 0.',
              'error',
              'top',
              'center',
              'danger');
            return;
          }

          const basketParametersUpdatedObject = JSON.parse(orderDtoToUpdate['basket']['metadata'])
          this.orderStateService.setBasketParameters(basketParametersUpdatedObject);
        }
        else if (orderDtoToUpdate['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED) {
          orderDtoToUpdate.metadata = orderDtoToUpdate.metadata.replace('param1', 'totalQuantity');
          console.log(orderDtoToUpdate.metadata)
          console.log(orderDtoToUpdate['basket']['metadata'])
          console.log(JSON.parse(orderDtoToUpdate['basket']['metadata'])['totalQuantity'])
          orderDtoToUpdate['basket']['metadata'] = orderDtoToUpdate.metadata
          const updatedTotalQuantityValue = JSON.parse(orderDtoToUpdate['basket']['metadata'])['totalQuantity']
          if (
            updatedTotalQuantityValue === '' || updatedTotalQuantityValue === null || updatedTotalQuantityValue === undefined
            || orderDtoToUpdate.price === '' || orderDtoToUpdate.quantity === ''
          ) {
            this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh)/Order Parameter(if needed) are required',
              'error',
              'top',
              'center',
              'danger');
            return;
          }
          if (+updatedTotalQuantityValue <=0) {
            this.notificationToastService.showNotification('The value of TotalQuantity(MW) parameter should be greater than 0.',
              'error',
              'top',
              'center',
              'danger');
            return;
          }
          const basketParametersUpdatedObject = JSON.parse(orderDtoToUpdate['basket']['metadata'])
          this.orderStateService.setBasketParameters(basketParametersUpdatedObject);
        }
        else if (orderDtoToUpdate['basket']['basketType'] === AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED) {
          orderDtoToUpdate.metadata = orderDtoToUpdate.metadata.replace('param1', 'maxCapacity');
          orderDtoToUpdate.metadata = orderDtoToUpdate.metadata.replace('param2', 'initialCharge');
          orderDtoToUpdate.metadata = orderDtoToUpdate.metadata.replace('param3', 'ratedPower');
          orderDtoToUpdate['basket']['metadata'] = orderDtoToUpdate.metadata
          const updatedMaxCapacityValue = JSON.parse(orderDtoToUpdate['basket']['metadata'])['maxCapacity']
          const updatedInitialChargeValue = JSON.parse(orderDtoToUpdate['basket']['metadata'])['initialCharge']
          const updateRatedPowerValue = JSON.parse(orderDtoToUpdate['basket']['metadata'])['ratedPower']
          if (
            updatedMaxCapacityValue === '' || updatedInitialChargeValue === '' ||
            updateRatedPowerValue === '' || orderDtoToUpdate.metadata === null || orderDtoToUpdate.metadata === undefined
            || orderDtoToUpdate.price === '' || orderDtoToUpdate.quantity === ''
          ) {
            this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh)/Order Parameter(if needed) are required',
              'error',
              'top',
              'center',
              'danger');
            return;
          }
          if (+updatedMaxCapacityValue <=0) {
            this.notificationToastService.showNotification('The value of MaxCapacity parameter should be greater than 0.',
              'error',
              'top',
              'center',
              'danger');
            return;
          }
          if (+updateRatedPowerValue <=0) {
            this.notificationToastService.showNotification('The value of RatedPower parameter should be greater than 0.',
              'error',
              'top',
              'center',
              'danger');
            return;
          }

          const basketParametersUpdatedObject = JSON.parse(orderDtoToUpdate['basket']['metadata'])
          this.orderStateService.setBasketParameters(basketParametersUpdatedObject);
        }
      }
      console.log(this.orderStateService.getBasketParameters())
      this.basketDataSourceChanged.basketChangedEvent.emit('basket changed');
      this.updatedBasketDataSource = new MatTableDataSource(this.orderStateService.getBasketTableArray());
      this.orderStateService.setDataSourceToUpdate(this.updatedBasketDataSource);
      this.isPopupOpened = false;
      this.dialogRef.close();
      // });
    }
    else if (this.orderStateService.orderMode === 'getSingleOrder') {
      const updateSingleOrderData = new UpdateSingleOrderData();
      updateSingleOrderData.id = this.dialogAnyData.orderToUpdate.id;
      //  this.selectedMarketChoice = this.dialogAnyData.orderToUpdate.orderDirection
      console.log(this.selectedMarketChoice)
      if (this.selectedMarketChoice === AppConstants.Market.BackendEnums.OrderDirection.BUY) {
        updateSingleOrderData.orderDirection = AppConstants.Market.BackendEnums.OrderDirection.BUY
      }
      else {
        updateSingleOrderData.orderDirection = AppConstants.Market.BackendEnums.OrderDirection.SELL
      }

      updateSingleOrderData.orderType = this.dialogAnyData.orderToUpdate.orderType.toUpperCase();
      updateSingleOrderData.price = this.orderDialogData.price.toString()
      updateSingleOrderData.product = this.dialogAnyData.orderToUpdate.product
      updateSingleOrderData.quantity = this.orderDialogData.quantity.toString()

      if (updateSingleOrderData.orderType === AppConstants.Market.BackendEnums.OrderType.ICEBERG) {
        const orderParameters = new Object();
        orderParameters['icebergQuantity'] = this.orderDialogData.metadata.param1;
        orderParameters['icebergPriceDelta'] = this.orderDialogData.metadata.param2;
        if (+updateSingleOrderData.quantity > +orderParameters['icebergQuantity']) {
          this.notificationToastService.showNotification('Quantity chunk(MW) parameter value must be less than or equal to quantity(MW) value.',
            'error',
            'top',
            'center',
            'danger');
          return;
        }
        updateSingleOrderData.metadata = JSON.stringify(orderParameters);
        console.log(orderParameters)
        /* this.orderStateService.getLastOrderData()['metadata'] = JSON.stringify(orderParameters)  */
      }
      else {
        updateSingleOrderData.metadata = ''
      }
      if (
        this.orderDialogData.metadata.param1 === '' || this.orderDialogData.metadata.param2 === ''
        || updateSingleOrderData.price === '' || updateSingleOrderData.quantity === ''
      ) {
        this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh)/Order Parameter(if needed) are required',
          'error',
          'top',
          'center',
          'danger');
        return;
      }
      if (
        +updateSingleOrderData.price <=0  ||
        +updateSingleOrderData.quantity <=0

      ) {
        this.notificationToastService.showNotification('The value of  Quantity(MW)/Price(€/MWh) should be greater than 0.',
          'error',
          'top',
          'center',
          'danger');
        return;
      }
      console.log(this.orderDialogData.orderStatus)

      console.log(updateSingleOrderData)
      const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
        data: {
          updatedDTO: updateSingleOrderData,
          userCommand: 'updateSpecificRow'
        }
      });

      dialogRef.afterClosed().subscribe(result => {

        if (this.orderStateService.getResponse() === 'no') {
          return;
        }
        if(updateSingleOrderData.orderType === AppConstants.Market.BackendEnums.OrderType.ICEBERG ){
          this.orderStateService.getLastOrderData()['metadata'] = updateSingleOrderData.metadata
        }
        console.log(result);
        this.updateSingleOrderOnBackend(updateSingleOrderData, idToUpdate)
        this.orderStateService.getLastOrderData()['orderType'] = updateSingleOrderData.orderType
        this.isPopupOpened = false;
        this.dialogRef.close();
      });
      // }
    }
  }

  updateSingleOrderOnBackend(orderObjectToUpdate: UpdateSingleOrderData, indexToUpdate: number) {

    console.log(orderObjectToUpdate)
    console.log(indexToUpdate)
    this.myOrdersService.updateOrder(orderObjectToUpdate, indexToUpdate).subscribe(
      response => {
        console.log(response);
        this.myOrdersService.getmyOrders().subscribe(ordersData => {
          console.log(ordersData);
          this.dialogRef.close();
        });
        this.dialogRef.close();
      })

  }

  placeOrder() {
    console.log(this.selectedMarketTypeChoice);
    console.log(this.selectedBasketTypeChoice);
    this.orderStateService.setDataSourceNameToUpdate('dataSource3');
    if (this.selectedMarketTypeChoice === AppConstants.Market.BackendEnums.OrderType.ICEBERG) {
      // tslint:disable-next-line:max-line-length
      if (
        this.orderDialogData.price === undefined ||
        this.orderDialogData.quantity === undefined ||
        this.orderDialogData.quantity === '' ||
        this.orderDialogData.price === '' ||
        this.orderDialogData.metadata.param1 === '' ||
        this.orderDialogData.metadata.param2 === ''
      ) {
        this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh)/Order Parameter(if needed) are required',
          'error',
          'top',
          'center',
          'danger');
        return;
      }
    }
    if (
      this.orderDialogData.price === undefined ||
      this.orderDialogData.quantity === undefined ||
      this.orderDialogData.quantity === '' ||
      this.orderDialogData.price === ''
    ) {
      this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh)/Order Parameter(if needed) are required',
        'error',
        'top',
        'center',
        'danger');
      return;
    }
    if (
      +this.orderDialogData.price <=0  ||
      +this.orderDialogData.quantity <=0

    ) {
      this.notificationToastService.showNotification('The value of  Quantity(MW)/Price(€/MWh) should be greater than 0.',
        'error',
        'top',
        'center',
        'danger');
      return;
    }
     else {
      // tslint:disable-next-line:prefer-const
      // let newProductDTO = new ProductDTO();
      // tslint:disable-next-line:prefer-const
      // let newOrderDTO = new OrderDTO();

      this.orderDialogData.orderDirection = this.selectedMarketChoice;
      this.orderDialogData.orderType = this.selectedMarketTypeChoice;
      this.orderDialogData.basketType = this.selectedBasketTypeChoice
      console.log(this.orderDialogData)
      const newOrderDTO = OrderDTO.createFromOrderDialogData(this.dialogAnyData.productId, this.orderDialogData)
      if (newOrderDTO.id === '-') {
        newOrderDTO.id = ''
      }
      //newProductDTO.id = this.dialogAnyData.productId;
      // newOrderDTO.product = newProductDTO;
      // newOrderDTO.quantity = this.orderDialogData.quantity;
      // newOrderDTO.price = this.orderDialogData.price;
      // newOrderDTO.orderDirection = this.orderStateService.mapUiLabelsToBackendFormat(this.selectedMarketChoice)
      // newOrderDTO.orderType = this.orderStateService.mapUiLabelsToBackendFormat(this.selectedMarketTypeChoice)
      // newOrderDTO.basketType = AppConstants.Market.OrderBackendLabels.BasketTypeNone;

      console.log(this.selectedMarketChoice);


      // newOrderDTO.orderStatus = 'ACTIVE';
      console.log(this.orderDialogData);
      console.log(newOrderDTO);

      const myClonedObject = Object.assign({}, newOrderDTO);
      this.orderStateService.setLastOrderDto(myClonedObject);
      if (newOrderDTO.orderType === AppConstants.Market.BackendEnums.OrderType.ICEBERG) {
        console.log(+newOrderDTO.quantity)
        let metadataObject = JSON.parse(newOrderDTO['metadata'])
        console.log(metadataObject['icebergQuantity'])
        if (+newOrderDTO.quantity > +metadataObject['icebergQuantity']) {
          this.notificationToastService.showNotification('Quantity chunk(MW) parameter value must be less than or equal to quantity(MW) value.',
            'error',
            'top',
            'center',
            'danger');
          return;
        }
        if (newOrderDTO.metadata === null || newOrderDTO.metadata === undefined || newOrderDTO.metadata === '{}' || metadataObject['icebergQuantity'] === '' ||
          metadataObject['icebergPriceDelta'] === '') {
          this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh)/Order Parameter(if needed) are required',
            'error',
            'top',
            'center',
            'danger');
          return;
        }


        // let orderParameters = new Object();
        // orderParameters[ 'icebergQuantity'] = newOrderDTO.quantity;
        // orderParameters['icebergPriceDelta'] = this.orderDialogData.metadata.param2;
        // newOrderDTO.metadata = JSON.stringify(orderParameters);
        // newOrderDTO.quantity = (this.orderDialogData.metadata.param1);



      }
      console.log(newOrderDTO);
      console.log('Saved DTO: ' + this.orderStateService.getLastOrderData())
      // this.orderStateService.setLastOrderDto(newOrderDTO);

      this.activeProductsService.placeOrder(newOrderDTO).subscribe(response => {
        if (response !== null) {
          console.log(response);
          this.orderStateService.orderType = this.selectedMarketTypeChoice;
          // this.orderStateService.basketType = AppConstants.Market.BackendEnums.BasketType.NONE;
          this.orderStateService.orderMode = 'orderPlaced';
          // this.myOrdersService.getmyOrders().subscribe(ordersData => {
          //   console.log(ordersData);
          // });
          this.dialogRef.close();
        }
      });
      this.appDataService.orderAndTradesTableSelection = AppConstants.Market.Tables.OrdersAndTrades.State.ORDERS
      this.appDataService.setOrdersAndTradesTableSelection(this.appDataService.orderAndTradesTableSelection)
    }
  }

  addOrderToBasket() {
    console.log("Entered updateOrder")
    this.orderDialogData.orderType = this.selectedMarketTypeChoice
    this.orderDialogData.basketType = this.selectedBasketTypeChoice
    this.orderDialogData.orderDirection = this.selectedMarketChoice;

    this.orderDialogData.orderStatus = 'Queued';
    this.orderDialogData.date = this.dateInDialog;
    console.log(this.orderDialogData);
    Auxiliary.logInformation(this.orderDialogData);
    if (
      this.orderDialogData.price === undefined ||
      this.orderDialogData.quantity === undefined ||
      this.orderDialogData.quantity === '' ||
      this.orderDialogData.price === ''
    ) {
      this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh) are required',
        'error',
        'top',
        'center',
        'danger');
      return;
    }
    if (
      +this.orderDialogData.price <=0  ||
      +this.orderDialogData.quantity <= 0
    ) {
      this.notificationToastService.showNotification('The value of  Quantity(MW)/Price(€/MWh) should be greater than 0',
        'error',
        'top',
        'center',
        'danger');
      return;
    }

    let orderDtoToAddInBasketTable = new OrderDTO();
    const newProductDTO = new ProductDTO();
    newProductDTO.id = this.dialogAnyData.productId;
    orderDtoToAddInBasketTable = OrderDTO.createFromOrderDialogData(this.dialogAnyData.productId, this.orderDialogData)
    // orderDtoToAddInBasketTable.date = '\xa0';
    orderDtoToAddInBasketTable.orderStatus = 'Queued';
    // orderDtoToAddInBasketTable.metadata = JSON.stringify(
    //   this.orderDialogData.metadata
    // );

    console.log(orderDtoToAddInBasketTable);
    console.log(this.orderDialogData.basketType);
    if (this.orderDialogData.basketType === AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE) {
      // tslint:disable-next-line:max-line-length
      if (
        this.orderDialogData.price === undefined ||
        this.orderDialogData.quantity === undefined ||
        this.orderDialogData.metadata.param1 === '' ||
        orderDtoToAddInBasketTable['basket']['metadata'] === '{}'
      ) {
        this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh)/Basket Parameter(s)(if needed) are required',
          'error',
          'top',
          'center',
          'danger');
        return;
      }
      if ( +this.orderDialogData.metadata.param1 <=0) {
        this.notificationToastService.showNotification('The value of  MaxMatchedOrders parameter should be greater than 0.',
          'error',
          'top',
          'center',
          'danger');
        return;
      }
    }
    else if (this.orderDialogData.basketType === AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED) {
      // tslint:disable-next-line:max-line-length
      if (
        this.orderDialogData.price === undefined ||
        this.orderDialogData.quantity === undefined ||
        this.orderDialogData.metadata.param1 === '' ||
        orderDtoToAddInBasketTable['basket']['metadata'] === '{}'
      ) {
        this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh)/Basket Parameter(s)(if needed) are required',
          'error',
          'top',
          'center',
          'danger');
        return;
      }
      if (+this.orderDialogData.metadata.param1 <=0) {
        this.notificationToastService.showNotification('The value of  TotalQuantity(MW) parameter should be greater than 0.',
          'error',
          'top',
          'center',
          'danger');
        return;
      }

    }
    else if (
      this.orderDialogData.basketType === AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED
    ) {
      // tslint:disable-next-line:max-line-length
      if (
        this.orderDialogData.price === undefined ||
        this.orderDialogData.quantity === undefined ||
        this.orderDialogData.metadata.param1 === '' ||
        this.orderDialogData.metadata.param2 === '' ||
        this.orderDialogData.metadata.param3 === '' ||
        orderDtoToAddInBasketTable['basket']['metadata'] === '{}'
      ) {
        this.notificationToastService.showNotification('The fields Quantity(MW)/Price(€/MWh)/Basket Parameter(s)(if needed) are required',
          'error',
          'top',
          'center',
          'danger');
        return;
      }
      if (
        +this.orderDialogData.metadata.param1 <= 0) {
        this.notificationToastService.showNotification('The value of MaximumCapacity should be greater than 0',
          'error',
          'top',
          'center',
          'danger');
        return;
      }
      if (
        +this.orderDialogData.metadata.param3 <= 0) {
        this.notificationToastService.showNotification('The value of RatedPower should be greater than 0',
          'error',
          'top',
          'center',
          'danger');
        return;
      }
    }



    if (this.orderStateService.getBasketTableArray().length > 0) {
      // const usedOrderType = this.orderDialogData.basketType.toUpperCase();
      const usedOrderType = this.orderDialogData.basketType
      console.log(this.orderStateService.getBasketParameters())
      if (this.orderStateService.getBasketType() !== usedOrderType) {
        console.log('mat dialog open')
        const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
          data: {
            updatedDTO: orderDtoToAddInBasketTable,
            basketItemIndex: this.dialogAnyData.rowIndex,
            userCommand: 'modifyBasketType'
          }
        });

        dialogRef.afterClosed().subscribe(result => {
          if (this.orderStateService.getResponse() === 'no') {
            return;
          }
          this.orderStateService.basketType = this.orderDialogData.basketType;
          console.log(result);
          console.log('The dialog was closed');
          this.orderStateService.setLastOrderDto(orderDtoToAddInBasketTable);
          this.orderStateService.setBasketParameters(JSON.parse(orderDtoToAddInBasketTable.metadata));
          if (result !== null) {
            this.basketDataSourceChanged.basketChangedEvent.emit('basket changed');
            this.updatedBasketDataSource = new MatTableDataSource(this.orderStateService.getBasketTableArray());
            console.log(this.updatedBasketDataSource);
            this.orderStateService.setDataSourceToUpdate(this.updatedBasketDataSource);

            console.log(this.updatedBasketDataSource);
            this.isPopupOpened = false;
          }
          this.orderStateService.basketType = this.orderStateService.getBasketTableArray()[0]['basket']['basketType'];
          this.isPopupOpened = false;
          this.dialogRef.close();
        })
      } else {
        this.orderStateService.addToBasket(orderDtoToAddInBasketTable);
        this.orderStateService.setLastOrderDto(orderDtoToAddInBasketTable);
        this.orderStateService.setBasketParameters(JSON.parse(orderDtoToAddInBasketTable.metadata));
        this.orderStateService.orderMode = 'addToBasket';
        // this.orderStateService.processMode = AppConstants.Market.UILabels.ProcessMode.ADD_TO_BASKET;
        this.dialogRef.close();
        // this.appDataService.orderAndTradesTableSelection = AppConstants.Market.Tables.OrdersAndTrades.State.BASKET
        this.appDataService.setOrdersAndTradesTableSelection(AppConstants.Market.Tables.OrdersAndTrades.State.BASKET)

      }
    } else {
      this.orderStateService.addToBasket(orderDtoToAddInBasketTable);
      this.orderStateService.setLastOrderDto(orderDtoToAddInBasketTable);
      this.orderStateService.setBasketParameters(JSON.parse(orderDtoToAddInBasketTable.metadata));
      this.orderStateService.orderType = AppConstants.Market.BackendEnums.OrderType.BASKET;
      this.orderStateService.basketType = this.orderStateService.getBasketTableArray()[0]['basket'][
        'basketType'
      ];
      this.orderStateService.orderMode = 'addToBasket';
      // this.orderStateService.processMode = AppConstants.Market.UILabels.ProcessMode.ADD_TO_BASKET;
      this.dialogRef.close(this.dialogAnyData.orderReceived);
    }
    
       
    this.appDataService.setOrdersAndTradesTableSelection(AppConstants.Market.Tables.OrdersAndTrades.State.BASKET)
    console.log(this.orderStateService.getBasketParameters());

    console.log(this.orderStateService.basketType);
    console.log(this.orderStateService.orderType);
  }

  onSubmit() { }

  clearMetadataInputValues() {
    this.orderDialogData.metadata.param1 = '';
    this.orderDialogData.metadata.param2 = '';
    this.orderDialogData.metadata.param3 = '';
  }
}

export class OrderDialogData {
  productName: string;
  date: string;
  deliveryTimeStart: string;
  period: string;
  quantity: string;
  price: string;
  orderStatus: string;
  orderDirection: string;
  orderType: string;
  basketType: string;
  metadata: OrderParameters = new OrderParameters();
}

export class UpdateSingleOrderData {
  id: string;
  product: ProductDTO;
  price: string;
  quantity: string;
  orderDirection: string;
  orderType: string;
  metadata: string;
}


export class BasketData {
  basketType: string;
  orderData: OrderDialogData[];
  metadata: BasketParameters = new BasketParameters();
}

export class OrderParameters {
  public param1: string;
  public param2: string;
  public param3: string;
}

export class BasketParameters {
  public param1: string;
  public param2: string;
  public param3: string;
}
