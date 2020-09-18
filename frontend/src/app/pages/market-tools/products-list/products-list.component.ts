import { Component, OnInit, ViewChild, Output, Input, EventEmitter } from '@angular/core';
import { DatePipe } from '@angular/common'
import { ActiveProductsService } from '../../../services/activeProducts.service';
import { MyOrdersService } from '../../../services/myOrders.service';
import {MatTableDataSource, MatSort, MatPaginator} from '@angular/material';
import { NotificationToastService } from '../../../shared/utils/notification-toast-service';
import { MarketCountryType, UpdateSignals } from 'app/shared/config/app-constants';
import { AppDataService } from 'app/services/app-data.service';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-products-list',
  templateUrl: './products-list.component.html',
  styleUrls: ['./products-list.component.scss']
})
export class ProductsListComponent implements OnInit {

  public showProductDateFrom: Date;
  public showProductDateTo: Date;
  public productsViewMarketCode: string = MarketCountryType.DEFAULT_MARKET_CYPRUS;
  updateEmmiterSubscriptionId: Subscription
  updateEmmiter = new EventEmitter();

  @Output() public productIdSelected = new EventEmitter();
  @Input() public mode: string;

  productsData: any = null;
  @ViewChild(MatSort, null) sort: MatSort;
  @ViewChild(MatPaginator, null) paginator: MatPaginator;
  productsDisplayedColumns = [ 'actions', 'id', 'name', 'deliveryTimeStart', 'deliveryTimeEnd' , 'closesAt' , 'period', 'productStatus', 'totalOrders' ];

  constructor(    public activeProductsService: ActiveProductsService,
    public notificationToastService: NotificationToastService,
    public myOrdersService: MyOrdersService,
    public datePipe: DatePipe,
    public appDataService: AppDataService
    ) { 
      this.updateEmmiterSubscriptionId =
                this.appDataService.updateEmmiter.subscribe(event => {
                  if (event === UpdateSignals.USER_DTO || event === UpdateSignals.CRM_LOCAL_ACTIVE_STATE ||
                      event === UpdateSignals.APP_TYPE) {
                      this.onUpdateSignal()
                  }
                })
    }

  ngOnInit() {
    this.showProductDateFrom = new Date();
    this.showProductDateTo =  new Date();
    this.onUpdateSignal()
  }
  onUpdateSignal(){
    if(this.appDataService.userDto === null){
      return;
    }
    else{
        this.appDataService.defaultMarket = this.appDataService.userDto.defaultMarket
        if(this.appDataService.defaultMarket=== MarketCountryType.DEFAULT_MARKET_BULGARIA){
          this.productsViewMarketCode =  MarketCountryType.DEFAULT_MARKET_BULGARIA
        }
        else{
          this.productsViewMarketCode =  MarketCountryType.DEFAULT_MARKET_CYPRUS
        } 
    }
  }

  refreshProducts() {

      const from = this.datePipe.transform(this.showProductDateFrom, 'yyyy-MM-dd') + 'T00:00:00Z';
      const to = this.datePipe.transform(this.showProductDateTo, 'yyyy-MM-dd') + 'T23:59:00Z';

      if (this.mode === 'ADMIN') {
        this.activeProductsService.getProducts(from, to, this.productsViewMarketCode ).subscribe(data => {

          this.productsData = new MatTableDataSource(data);
          this.productsData.sort = this.sort;
          this.productsData.paginator = this.paginator;
      });
    }
    else {
      this.activeProductsService.getUsersProducts(from, to, this.productsViewMarketCode ).subscribe(data => {

        this.productsData = new MatTableDataSource(data);
        this.productsData.sort = this.sort;
        this.productsData.paginator = this.paginator;
    });
    }
  }

  onFromSelect(event) {
    this.showProductDateFrom = event;
    if ( this.showProductDateFrom > this.showProductDateTo ) {
      this.showProductDateTo = this.showProductDateFrom;
    }
  }

  onToSelect(event) {
    this.showProductDateTo = event;
  }

  productTilelineSelected(id){
    this.productIdSelected.emit(id);
  }


}

