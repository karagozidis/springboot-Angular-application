import { HttpClient } from "@angular/common/http";
import { Injectable, OnInit } from "@angular/core";
import { JwtHelperService } from "@auth0/angular-jwt";
import { MatTableDataSource } from "@angular/material";
import 'rxjs/add/operator/map';
import { ProductDTO } from "app/shared/dto/product-dto";
import { OrderDTO } from "app/shared/dto/order-dto";
import * as TableStructure from 'app/pages/market/components/market-table/table-structure/table-structure';
import {  BasketParameters } from 'app/pages/market/components/market-dialog/market-dialog.component';
import { AppConstants, MarketCountryType } from "app/shared/config/app-constants";
import * as moment from 'moment'
import { BasketDTO } from "app/shared/dto/basket-dto";
import { Observable } from "rxjs";
import { AppDataService } from "./app-data.service";
import { Auxiliary } from "app/shared/utils/auxiliary";
/**
 * A service providing functionality for the user of the application, including authentication,
 * authorisation and session management.
 */
@Injectable({
  providedIn: "root"
})
export class OrderStateService   {
    // tslint:disable-next-line:max-line-length
    productObject = new ProductDTO()
    // tslint:disable-next-line:max-line-length
    orderDtoUsed = new OrderDTO();
    basketTableArray: any = [];
    activeProductsArray: any = [];
    myOrdersArray: any = [];
    orderMode = '';
    processMode = AppConstants.Market.UILabels.ProcessMode.ADD_TO_BASKET
    myOrderTableDataList: TableStructure.MyOrdersTableData[] = [];
    dataSourceToUpdate;
    dataSourceName;
    basketParameters = {};
    selectedArrayToView = '';
    enableOrdersByProductArray = false;
    productId: number;
    requestFullfilled = '';
    country = MarketCountryType.DEFAULT_MARKET_CYPRUS
    from = ''
    to = new Date().toString()
    lastOrderData = new OrderDTO();
    basketIdFromBasketDTOInOrdersArray: number;
    // basketType: string;
    orderType = AppConstants.Market.BackendEnums.OrderType.LIMIT;
    basketType = AppConstants.Market.BackendEnums.BasketType.NONE;
    getOrderTypeURL = AppConstants.CRM_CENTRAL_URL + '/market-scenario'

    constructor(
        private http: HttpClient,
        private localHttp: HttpClient,
        private appDataService: AppDataService,
        private jwtService: JwtHelperService
    ) {
        this.lastOrderData.orderDirection = AppConstants.Market.BackendEnums.OrderDirection.BUY
        this.lastOrderData.orderType = AppConstants.Market.BackendEnums.OrderType.LIMIT
        // this.lastOrderData.basket.basketType = AppConstants.Market.BackendEnums.BasketType.NONE


        let fromDay = new Date();
        let toDay = new Date();

        fromDay.setDate(fromDay.getDate() - 2)
        fromDay.setHours(0, 0, 0, 0)
        this.from = moment(fromDay).toISOString()
        this.to =moment(toDay.setHours(23, 59, 59, 999)).toISOString();
    }

    addToBasket(orderDTO: OrderDTO){
        this.basketTableArray.push(orderDTO);
    }

    getBasketType() {
        return this.basketType
        // if (this.orderType === AppConstants.Market.BackendEnums.OrderType.BASKET) {
        // } else {
        //     return AppConstants.Market.BackendEnums.BasketType.NONE
        // }
    }

    setLastOrderDto(lastOrderObject: OrderDTO){
        this.lastOrderData = lastOrderObject;

    }
    getCurrentOrderTypeStatus(): Observable<any>{
        return this.http.get<any>(this.getOrderTypeURL);
    }

    getLastOrderData(){
        return this.lastOrderData;
    }

    setResponse(response: string){
        this.requestFullfilled = response;
    }

    getResponse(){
        return this.requestFullfilled;
    }

    setBasketArray(orderDTO: OrderDTO, index: number){
        this.basketTableArray[index] = orderDTO;
    }

    removeOrderFromBasketArray( index: number) {
        this.basketTableArray.splice(index, 1);
    }

    resetBasketArray(){
        this.basketTableArray = [];
    }

    setMyOrdersArray(arrayFromServer: any[]){
        this.myOrdersArray = arrayFromServer;
    }

    setActiveProductsArray(arrayFromServer: ProductDTO[]){
        this.activeProductsArray = arrayFromServer;
    }

    getActiveProductsArray(){
        return this.activeProductsArray;
    }

    getMyOrdersArray() {
        return this.myOrdersArray;
    }

    getBasketTableArray() {
        return this.basketTableArray;
    }

    setDataSourceToUpdate(dataSource: MatTableDataSource<[]>){
        this.dataSourceToUpdate = dataSource;
    }

    setDataSourceNameToUpdate(dataSource: string){

        this.dataSourceName = dataSource;
    }

    getDataSourceToUpdate(){
        return this.dataSourceToUpdate;
    }

    getFormattedOrdersDataSource(){
        return this.getFormattedOrdersDataSource;
    }

    setBasketParameters(basketParams: BasketParameters){
        this.basketParameters = basketParams;
    }

    getBasketParameters(){
        return this.basketParameters;
    }

    //  mapUiLabelsToBackendFormat(label: String){
    //   switch(label){
    //   case AppConstants.Market.OrderUILabels.Buy:
    //       return AppConstants.Market.OrderBackendLabels.Buy;
    //       break;
    //   case AppConstants.Market.OrderUILabels.Sell:
    //       return AppConstants.Market.OrderBackendLabels.Sell;
    //       break;
    //   case AppConstants.Market.OrderUILabels.Today:
    //       return AppConstants.Market.OrderBackendLabels.Today;
    //       break;
    //   case AppConstants.Market.OrderUILabels.TypeAON:
    //       return AppConstants.Market.OrderBackendLabels.TypeAON;
    //       break;
    //   case AppConstants.Market.OrderUILabels.TypeBasket:
    //       return AppConstants.Market.OrderBackendLabels.TypeBasket;
    //       break;
    //   case AppConstants.Market.OrderUILabels.TypeFoK:
    //       return AppConstants.Market.OrderBackendLabels.TypeFoK;
    //       break;
    //   case AppConstants.Market.OrderUILabels.TypeIoC:
    //       return AppConstants.Market.OrderBackendLabels.TypeIoC;
    //       break;
    //   case AppConstants.Market.OrderUILabels.TypeLimit:
    //       return AppConstants.Market.OrderBackendLabels.TypeLimit;
    //       break;
    //   case AppConstants.Market.OrderUILabels.TypeIceberg:
    //       return AppConstants.Market.OrderBackendLabels.TypeIceberg;
    //       break;
    //   case AppConstants.Market.OrderUILabels.BasketTypeNone:
    //       return AppConstants.Market.OrderBackendLabels.BasketTypeNone;
    //       break;
    //   case AppConstants.Market.OrderUILabels.BasketTypeExclusive:
    //       return AppConstants.Market.OrderBackendLabels.BasketTypeExclusive;
    //       break;
    //   case AppConstants.Market.OrderUILabels.BasketTypeLinkedGroup:
    //       return AppConstants.Market.OrderBackendLabels.BasketTypeLinkedGroup;
    //       break;
    //   case AppConstants.Market.OrderUILabels.BasketTypeVolumeConstrained:
    //       return AppConstants.Market.OrderBackendLabels.BasketTypeVolumeConstrained;
    //       break;
    //   case AppConstants.Market.OrderUILabels.BasketTypeCumulativeVolumeConstrained:
    //       return AppConstants.Market.OrderBackendLabels.BasketTypeCumulativeVolumeConstrained;
    //       break;
    //   default:
    //     return '';
    //     break;

    //   }
    // }
}


export class LastOrderData {
    public product: ProductDTO ;
    // public productName: string;
    // public date: string;
    // public deliveryTimeStart: string;
    // public period: string;
    public quantity: string;
    public price: string;
    public orderStatus: string;
    public orderDirection: string
    public orderType: string
    public basket: string
    public metadata: string
}
