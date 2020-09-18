import { Component, OnInit, ViewChild, Renderer2, AfterViewInit, ElementRef, HostListener } from '@angular/core';
import { MatTableDataSource, MatPaginator, MatSort, MatDatepickerInputEvent, DateAdapter, MAT_DATE_LOCALE, MatTable } from '@angular/material';
import { DatePipe } from '@angular/common';
import { FormGroup, FormControl } from '@angular/forms';
import { MomentDateAdapter } from '@angular/material-moment-adapter';

// export interface OrderData {
//   buy_sell: string,
//   quantity: string,
//   price: string,
//   orderTime: number
  
// }
@Component({
  selector: 'market-orders-history',
  templateUrl: './orders-history.component.html',
  styleUrls: ['./orders-history.component.scss'],
  providers: [
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
  ]
})
export class OrdersHistoryComponent implements OnInit {
  
  displayedColumns = ['buy_sell', 'type', 'quantity', 'price', 'delivery', 'status', 'orderTime'];
    buy_sell_Filter = new FormControl();
    quantityFilter = new FormControl();
    priceFilter = new FormControl();
    globalFilter = '';
    filteredValues = {
      buy_sell: '', quantity: '', price: '', orderTime: ''
    };
    filterInDateFlag = false;
    pipe: DatePipe;
    customFilter: any = {};
    filterForm = new FormGroup({
        fromDate: new FormControl(),
        toDate: new FormControl(),
    });
    epochFromTime = 1;
    epochToTime = ((new Date).getTime()) + 24 * 60  * 60 * 1000;
    fromEvents: string[] = [];
    toEvents: string[] = [];
    
  

    get fromDate() { return this.filterForm.get('fromDate').value; }
  get toDate() { return this.filterForm.get('toDate').value; }

dataSource = new MatTableDataSource<OrderData>(DATA);


@ViewChild(MatSort, null) sort: MatSort;
@ViewChild(MatPaginator, null) paginator: MatPaginator;




ngOnInit() {
  // this.dataSource.paginator = this.paginator;
  // this.dataSource.sort = this.sort;
  
  

  this.buy_sell_Filter.valueChanges.subscribe((buysellFilterValue) => {
    this.filteredValues['buy_sell'] = buysellFilterValue;
      this.dataSource.filter = JSON.stringify(this.filteredValues);
      

  });
  this.quantityFilter.valueChanges.subscribe((quantityFilterValue) => {
    this.filteredValues['quantity'] =quantityFilterValue;
    this.dataSource.filter = JSON.stringify(this.filteredValues);
  });

  this.priceFilter.valueChanges.subscribe((priceFilterValue) => {
    this.filteredValues['price'] = priceFilterValue;
    this.dataSource.filter = JSON.stringify(this.filteredValues);
  });

  this.dataSource.filterPredicate = this.customFilterPredicate();

  
}

applyFilter(filter) {
  this.globalFilter = filter;
  this.dataSource.filter = JSON.stringify(this.filteredValues);
}

customFilterPredicate() {
  
  const myFilterPredicate = (orderData: OrderData, filter: string): boolean => {
    var globalMatch = !this.globalFilter;
    if (this.globalFilter) {
      // search all text fields
      globalMatch = orderData.buy_sell.toString().trim().toLowerCase().indexOf(this.globalFilter.toLowerCase()) !== -1;
    }
    if (!globalMatch) {
      return;
    }
    let searchString = JSON.parse(filter);
    return  orderData.buy_sell.toString().trim().toLowerCase().indexOf(searchString.buy_sell.toLowerCase()) !== -1 &&
    orderData.quantity.toString().trim().indexOf(searchString.quantity) !== -1 &&
    orderData.price.toString().trim().indexOf(searchString.price) !== -1
    // && new Date(orderData.orderTime).valueOf() >= this.epochFromTime
    // && new Date(orderData.orderTime).valueOf() <= this.epochToTime;
      ;
  }
  return myFilterPredicate;
}

addFromEvent(type: string, event: MatDatepickerInputEvent<Date>){
  const selectedEpochFromTime = new Date(event.value).valueOf();
  this.epochFromTime = selectedEpochFromTime;
  this.applyDateFilter();
}
addToEvent(type: string, event: MatDatepickerInputEvent<Date>){
 
  const selectedEpochToTime = new Date(event.value).valueOf();
  this.epochToTime = selectedEpochToTime + (23 * 60 * 60 * 1000) + (59 * 60 * 1000);
  this.applyDateFilter();
  
}

applyDateFilter() {
  this.filteredValues['ordertTime'] = Math.random().toString()
  this.dataSource.filter = JSON.stringify(this.filteredValues);

}


}

export interface OrderData {
  buy_sell: string;
  type: string;
  quantity: string;
  price: string;
  delivery: string;
  status: string;
  orderTime: string;
}

const DATA: OrderData[] = [
  // tslint:disable-next-line:max-line-length
  { buy_sell: 'Buy', type: 'limit', quantity: '400' , price: '55.00 ', delivery: 'Today 14:00 - 14:15' , status: 'Queued', orderTime:'27/09/19 13:00'},
  // tslint:disable-next-line:max-line-length
  {  buy_sell: 'Buy', type: 'limit', quantity: '400' , price: '52.00' , delivery: 'Today 17:00 - 18:00' , status: 'Queued', orderTime:'27/09/19 14:00' },
  // tslint:disable-next-line:max-line-length
  { buy_sell: 'Sell', type: 'basket', quantity: '200' , price: '60.00' , delivery: 'Today 18:00 - 19:00' , status: 'Accepted', orderTime:'28/09/19 11:00' },
  // tslint:disable-next-line:max-line-length
  {  buy_sell: 'Sell', type:'IoC', quantity: '300' , price: '55.42' , delivery: 'Tomorrow 11:00 - 11:15' , status: 'Active-Not Accepted', orderTime:'28/09/19 14:00' },
  // tslint:disable-next-line:max-line-length
  {  buy_sell: 'Buy', type:'basket', quantity: '1000' , price: '186.31' , delivery: 'Tomorrow 18:00 - 19:00' , status: 'Accepted', orderTime:'28/09/19 16:00' }
 
];

