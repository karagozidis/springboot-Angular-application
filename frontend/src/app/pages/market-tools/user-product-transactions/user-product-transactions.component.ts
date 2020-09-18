import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MarketObject} from '../../../shared/dto/market-object';
import {UserDto} from '../../../shared/dto/user-dto';
import {ActiveProductsDTO} from '../../../shared/dto/activeProducts-dtos';
import {ActivatedRoute} from '@angular/router';
import {ActiveProductsService} from '../../../services/activeProducts.service';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'app-user-product-transactions',
  templateUrl: './user-product-transactions.component.html',
  styleUrls: ['./user-product-transactions.component.scss']
})
export class UserProductTransactionsComponent implements OnInit {

  @Output() public backTolist = new EventEmitter();
  @Input() public showNavigation: Boolean = true;

  marketObjects: MarketObject[];
  marketObjectsBeforeFiltering: MarketObject[];
  currentUser: UserDto;
  productId: any;
  product: ActiveProductsDTO;
  orderIdsAllFilter: string = '';

  constructor(private activatedRoute: ActivatedRoute,
              public activeProductsService: ActiveProductsService,
              public userService: UserService) {
  }

  ngOnInit() {
    this.productId = '';
    this.readproductIdFromPathParams();
    this.loadCurrentUser();
    this.refreshTransactions();
  }

  readProductDetails() {
    this.activeProductsService.getProduct(this.productId).subscribe(data => {
      this.product = data;
    });
  }

  readproductIdFromPathParams() {
    if (this.activatedRoute.snapshot.paramMap.has('id')) {
      this.productId = this.activatedRoute.snapshot.paramMap.get('id');
      this.readProductDetails();
    } else {
      return;
    }
  }

  orderCodesAllFilterRunByIds(orderIdsAllFilter: string) {
    this.orderIdsAllFilter = orderIdsAllFilter;
    this.orderCodesAllFilterRun();
  }

  orderCodesAllFilterRun() {
    this.marketObjects = this.marketObjectsBeforeFiltering;
    if (this.orderIdsAllFilter === '') {
      this.marketObjects = this.marketObjectsBeforeFiltering;
      return false;
    }

    this.marketObjects = [];
    const orderIdsFilter = this.orderIdsAllFilter.split(',');

    for (const marketObject of this.marketObjectsBeforeFiltering) {
      if (marketObject.type === 'ORDER') {
        for (const cId of orderIdsFilter) {
          if (marketObject.singleOrderDTO.id.toString() === cId) {
            this.marketObjects.push(marketObject);
          }
        }
      } else if (marketObject.type === 'TRADE') {
        let found = false;
        for (const cId of orderIdsFilter) {
          if (
              (marketObject.tradeDTO.singleOrder1.id.toString() === cId || marketObject.tradeDTO.singleOrder2.id.toString() === cId)
              && found === false) {
            this.marketObjects.push(marketObject);
            found = true;
          }
        }
      }
    }
  }


  backToList() {
    this.backTolist.emit();
  }

  loadCurrentUser() {
    this.userService.getCurrentUser().subscribe(data => {
      this.currentUser = data;
    });
  }

  refreshTransactionsForProduct(id) {
    this.productId = id;
    this.readProductDetails();
    this.refreshTransactions();
  }


  refreshTransactions() {
    if (this.productId === '') {
      return;
    }

    this.activeProductsService.getUserTransactions(this.productId).subscribe(data => {
      this.marketObjects = data;
      this.marketObjectsBeforeFiltering = data;
      this.orderCodesAllFilterRun();
    });
  }

  showSecondTradeSection(userId) {
    if (this.currentUser.id === userId) {
      return true;
    } else {
      return false;
    }

  }

}
