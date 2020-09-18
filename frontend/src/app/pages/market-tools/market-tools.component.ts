import {ProductTransactionsComponent} from './product-transactions/product-transactions.component';
import {Component, OnInit, ViewChild} from '@angular/core';
import {DatePipe} from '@angular/common'
import {ActiveProductsService} from '../../services/activeProducts.service';
import {MyOrdersService} from '../../services/myOrders.service';
import {NotificationToastService} from '../../shared/utils/notification-toast-service';
import { AppType } from 'app/shared/config/app-constants';
import { AppDataService } from 'app/services/app-data.service';




@Component({
  selector: 'app-market-tools',
  templateUrl: './market-tools.component.html',
  styleUrls: ['./market-tools.component.scss']
})
export class MarketToolsComponent implements OnInit {

  @ViewChild('apt', {static: false}) apt: ProductTransactionsComponent;
  public productsListHidden: boolean;
  public productsTimelineHidden: boolean;
  public productsGeneratorHidden: boolean;
  public ordersGeneratorHidden: boolean;
  public transactionInspectorHidden: boolean;

  productsGeneratorShowHide() {
    if (this.productsGeneratorHidden === false) {
      this.productsGeneratorHidden = true;
    } else {
      this.productsGeneratorHidden = false;
    }
  }

  ordersGeneratorShowHide() {
    if (this.ordersGeneratorHidden === false) {
      this.ordersGeneratorHidden = true;
    } else {
      this.ordersGeneratorHidden = false;
    }
  }

  productsViewerShowHide() {
    if (this.productsListHidden === false || this.productsTimelineHidden === false) {
      this.productsListHidden = true;
      this.productsTimelineHidden = true;
    } else {
      this.productsListHidden = false;
    }

  }

  transactionInspectorShowHide() {
    if (this.transactionInspectorHidden === false) {
      this.transactionInspectorHidden = true;
    } else {
      this.transactionInspectorHidden = false;
    }
  }

  constructor(
      public activeProductsService: ActiveProductsService,
      public notificationToastService: NotificationToastService,
      public myOrdersService: MyOrdersService,
      public datePipe: DatePipe,
      private appDataService: AppDataService) {
  }

  ngOnInit() {
    this.appDataService.setAppType(AppType.MARKET)
    this.productsListHidden = false;
    this.productsTimelineHidden = true;
    this.productsGeneratorHidden = false;
    this.ordersGeneratorHidden = false;
    this.transactionInspectorHidden = false;

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
