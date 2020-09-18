import {Component, OnInit, ViewChild} from '@angular/core';
import {ProductTransactionsComponent} from '../product-transactions/product-transactions.component';
import {ProductsListComponent} from "../products-list/products-list.component";
import { AppDataService } from 'app/services/app-data.service';
import { AppType } from 'app/shared/config/app-constants';

@Component({
  selector: 'app-admin-market-stats',
  templateUrl: './admin-market-stats.component.html',
  styleUrls: ['./admin-market-stats.component.scss']
})
export class AdminMarketStatsComponent implements OnInit {

  @ViewChild('apt', {static: false}) apt: ProductTransactionsComponent;
  @ViewChild('productsListObject', { static: false} ) productsListObject: ProductsListComponent;



  public productsListHidden: boolean;
  public productsTimelineHidden: boolean;

  constructor(private appDataService:AppDataService) {
  }

  ngOnInit() {
    this.appDataService.setAppType(AppType.MARKET)
    this.productsListHidden = false;
    this.productsTimelineHidden = true;
    setTimeout(() => {
      if(this.productsListObject !=null){
        this.productsListObject.refreshProducts();
      }
    }, 1000);
  }

  showProductsList() {
    this.productsListHidden = false;
    this.productsTimelineHidden = true;
  }

  showTimeline(productId) {
    this.apt.refreshTransactionsForProduct(productId);
    this.productsListHidden = true;
    this.productsTimelineHidden = false;
  }
}
