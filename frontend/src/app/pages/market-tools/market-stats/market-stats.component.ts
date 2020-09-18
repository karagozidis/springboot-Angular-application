import { ProductTransactionsComponent } from './../product-transactions/product-transactions.component';
import { Component, OnInit, ViewChild, EventEmitter } from '@angular/core';
import { DatePipe } from '@angular/common'
import { ActiveProductsService } from '../../../services/activeProducts.service';
import { MyOrdersService } from '../../../services/myOrders.service';
import {MatTableDataSource, MatSort, MatDialog, MAT_DIALOG_DATA, MatDialogRef, MatPaginator} from '@angular/material';
import { NotificationToastService } from '../../../shared/utils/notification-toast-service';
import { MarketCountryType, UpdateSignals, AppType } from 'app/shared/config/app-constants';
import { Subscription } from 'rxjs';
import { AppDataService } from 'app/services/app-data.service';
import {ProductsListComponent} from "../products-list/products-list.component";


@Component({
  selector: 'app-market-stats',
  templateUrl: './market-stats.component.html',
  styleUrls: ['./market-stats.component.scss']
})
export class MarketStatsComponent implements OnInit {


  public productDate: Date;
  public productDelivaryPeriod: string ;

  public orderTotal: number;
  public orderMarketCode: string = MarketCountryType.DEFAULT_MARKET_CYPRUS;

  public productsGenerateMarketCode: string = MarketCountryType.DEFAULT_MARKET_CYPRUS;

  public productsListHidden: boolean ;
  public productsTimelineHidden: boolean ;
  updateEmmiterSubscriptionId: Subscription
  updateEmmiter = new EventEmitter();
  productsData: any = null;
  @ViewChild(MatSort, null) sort: MatSort;
  @ViewChild(MatPaginator, null) paginator: MatPaginator;

  @ViewChild('apt', { static: false} ) apt: ProductTransactionsComponent;
  @ViewChild('productsListObject', { static: false} ) productsListObject: ProductsListComponent;

  productsDisplayedColumns = [ 'actions', 'id', 'name', 'deliveryTimeStart', 'deliveryTimeEnd' , 'closesAt' , 'period', 'productStatus', 'totalOrders' ];

  public productForTimeline: string;
  

  constructor(public appDataService: AppDataService) {
    this.updateEmmiterSubscriptionId =
            this.appDataService.updateEmmiter.subscribe(event => {
                if (event === UpdateSignals.USER_DTO || event === UpdateSignals.CRM_LOCAL_ACTIVE_STATE ||
                    event === UpdateSignals.APP_TYPE) {
                    this.onUpdateSignal()
                }
            })
   }

  ngOnInit() {
    this.appDataService.setAppType(AppType.MARKET)
    this.productForTimeline = '';
    this.productsListHidden = false ;
    this.productsTimelineHidden = true ;
    setTimeout(() => {
      if(this.productsListObject !=null){
        this.productsListObject.refreshProducts();
      }
    }, 1000);
  }

  onUpdateSignal(){
    if(this.appDataService.userDto === null){
      return;
    }
    else{
        this.appDataService.defaultMarket = this.appDataService.userDto.defaultMarket
        if(this.appDataService.defaultMarket=== MarketCountryType.DEFAULT_MARKET_BULGARIA){
            this.orderMarketCode =  MarketCountryType.DEFAULT_MARKET_BULGARIA
            this.productsGenerateMarketCode =  MarketCountryType.DEFAULT_MARKET_BULGARIA
        }
        else{
            this.orderMarketCode = MarketCountryType.DEFAULT_MARKET_CYPRUS
            this.productsGenerateMarketCode =  MarketCountryType.DEFAULT_MARKET_CYPRUS
        } 
    }
  }

  showProductsList() {
   this.productsListHidden = false ;
   this.productsTimelineHidden = true ;
  }

  showTimeline(productId) {
   this.apt.refreshTransactionsForProduct(productId);
   this.productsListHidden = true ;
   this.productsTimelineHidden = false ;
  }





}
