import {UserDto} from './../../../shared/dto/user-dto';
import {UserService} from './../../../services/user.service';
import {MarketObject} from './../../../shared/dto/market-object';
import {ActiveProductsService} from './../../../services/activeProducts.service';
import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ActiveProductsDTO} from 'app/shared/dto/activeProducts-dtos';

@Component({
  selector: 'app-product-transactions',
  templateUrl: './product-transactions.component.html',
  styleUrls: ['./product-transactions.component.scss']
})
export class ProductTransactionsComponent implements OnInit {

  @Output() public backTolist = new EventEmitter();
  @Input() public showNavigation: Boolean = true;


  public graph = {
    data: [],
    autosize: true,
    layout: {
      title: 'Product Transactions',
      hovermode: 'closest',
      autosize: true,
      width: window.innerWidth - 420,
      xaxis: {
        title: {
          text: 'Bid Time'
        },
      },
      yaxis: {
        title: {
          text: 'Bid Price',
        }
      }
    }, config: {
      toImageButtonOptions: {
        format: 'png',
        filename: 'test',
        height: 900,
        width: 1500
      }
    }

  };

  marketObjects: MarketObject[];
  marketObjectsBeforeFiltering: MarketObject[];
  currentUser: UserDto;
  productId: any;
  product: ActiveProductsDTO;
  orderIdsAllFilter = '';
  graphHidden = true;

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

  refreshGraph() {

    this.graph.layout.width = window.outerWidth - 420;
    this.graph.data = [];
    this.activeProductsService.getGraph(this.productId).subscribe(response => {
      for (let i = 0; i < response.length; i++) {
        this.graph.data.push(response[i]);
        this.graphHidden = true;
      }
    });
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

  changeGraphVisibility() {
    if (this.graphHidden) {
      this.graphHidden = false;
    } else {
      this.refreshGraph();
    }
  }

  refreshTransactions() {
    if (this.productId === '') {
      return;
    }

    if (!this.graphHidden) {
      this.refreshGraph();
    }

    // if (this.mode === 'ADMIN') {
    this.activeProductsService.getTransactions(this.productId).subscribe(data => {
      this.marketObjects = data;
      this.marketObjectsBeforeFiltering = data;
      this.orderCodesAllFilterRun();
    });
    // } else {
    //       this.activeProductsService.getUserTransactions( this.productId ).subscribe(data => {
    //         this.marketObjects = data;
    //         this.marketObjectsBeforeFiltering = data;
    //         this.orderCodesAllFilterRun();
    //       });
    // }
  }


  // showSecondTradeSectionForAdmin(userId){
  //   if ( this.mode === 'ADMIN') {
  //     return true;
  //   } else if ( this.mode !== 'ADMIN' && this.currentUser.id === userId ) {
  //     return true;
  //   }
  //   else {
  //     return false;
  //   }
  //
  //
  // }


}
