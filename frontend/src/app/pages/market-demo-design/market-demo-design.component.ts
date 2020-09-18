import { Component, OnInit } from '@angular/core';

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  {position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H'},
  {position: 2, name: 'Helium', weight: 4.0026, symbol: 'He'},
  {position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li'},
  {position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be'},
  {position: 5, name: 'Boron', weight: 10.811, symbol: 'B'},
  {position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C'},
  {position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N'},
  {position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O'},
  {position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'}
];


export interface DemoProducts {
  name: string;
  price: number;
  quantity: number;
  latestBuy: number;
  latestSell: number;
  deliveryDate: string;
  weight: number;
  symbol: string;
}

const DEMO_PRODUCT_DATA: DemoProducts[] = [
  {price: 1.2,  quantity: 1.2,  latestBuy: 1.2,  latestSell: 1.2,  deliveryDate: '10/10/2029',  name: 'prod-1' , 	weight: 1.0079, symbol: 'H'},
  {price: 2.3,  quantity: 2.3,  latestBuy: 2.3,  latestSell: 2.3,  deliveryDate: '10/10/2029',  name: 'prod-2' , 	weight: 4.0026, symbol: 'He'},
  {price: 33.2, quantity: 33.2, latestBuy: 33.2, latestSell: 33.2, deliveryDate: '10/10/2029',  name: 'prod-3' , 	weight: 6.941, symbol: 'Li'},
  {price: 4.2,  quantity: 4.2,  latestBuy: 4.2,  latestSell: 4.2,  deliveryDate: '10/10/2029',  name: 'prod-4' , 	weight: 9.0122, symbol: 'Be'},
  {price: 1.5,  quantity: 1.5,  latestBuy: 1.5,  latestSell: 1.5,  deliveryDate: '10/10/2029',  name: 'prod-5' , 	weight: 10.811, symbol: 'B'},
  {price: 2.6,  quantity: 2.6,  latestBuy: 2.6,  latestSell: 2.6,  deliveryDate: '10/10/2029',  name: 'prod-6' , 	weight: 12.0107, symbol: 'C'},
  {price: 1.7,  quantity: 1.7,  latestBuy: 1.7,  latestSell: 1.7,  deliveryDate: '10/10/2029',  name: 'prod-7' , 	weight: 14.0067, symbol: 'N'},
  {price: 8.2,  quantity: 8.2,  latestBuy: 8.2,  latestSell: 8.2,  deliveryDate: '10/10/2029',  name: 'prod-8' , 	weight: 15.9994, symbol: 'O'},
  {price: 9.1,  quantity: 9.1,  latestBuy: 9.1,  latestSell: 9.1,  deliveryDate: '10/10/2029',  name: 'prod-9' , 	weight: 18.9984, symbol: 'F'},
  {price: 10.2, quantity: 10.2, latestBuy: 10.2, latestSell: 10.2, deliveryDate: '10/10/2029',  name: 'prod-10',  	weight: 20.1797, symbol: 'Ne'},
  {price: 12.1, quantity: 12.1, latestBuy: 12.1, latestSell: 12.1, deliveryDate: '10/10/2029',  name: 'prod-11',  	weight: 20.1797, symbol: 'Ne'},
  {price: 3.1,  quantity: 3.1,  latestBuy: 3.1,  latestSell: 3.1,  deliveryDate: '10/10/2029',  name: 'prod-12', weight: 20.1797, symbol: 'Ne'},
  {price: 2.5,  quantity: 2.5,  latestBuy: 2.5,  latestSell: 2.5,  deliveryDate: '10/10/2029',  name: 'prod-13', weight: 20.1797, symbol: 'Ne'},
  {price: 4.8,  quantity: 4.8,  latestBuy: 4.8,  latestSell: 4.8,  deliveryDate: '10/10/2029',  name: 'prod-14', weight: 20.1797, symbol: 'Ne'},
  {price: 7.3,  quantity: 7.3,  latestBuy: 7.3,  latestSell: 7.3,  deliveryDate: '10/10/2029',  name: 'prod-15', weight: 20.1797, symbol: 'Ne'},
  {price: 9.9,  quantity: 9.9,  latestBuy: 9.9,  latestSell: 9.9,  deliveryDate: '10/10/2029',  name: 'prod-16', weight: 20.1797, symbol: 'Ne'},
  {price: 0.5,  quantity: 0.5,  latestBuy: 0.5,  latestSell: 0.5,  deliveryDate: '10/10/2029',  name: 'prod-17', weight:	20.1797, symbol: 'Ne'},
  {price: 1.2,  quantity: 1.2,  latestBuy: 1.2,  latestSell: 1.2,  deliveryDate: '10/10/2029',  name: 'prod-18',  	weight: 1.0079, symbol: 'H'},
  {price: 2.3,  quantity: 2.3,  latestBuy: 2.3,  latestSell: 2.3,  deliveryDate: '10/10/2029',  name: 'prod-19',  	weight: 4.0026, symbol: 'He'},
  {price: 33.2, quantity: 33.2, latestBuy: 33.2, latestSell: 33.2, deliveryDate: '10/10/2029',  name: 'prod-20',  	weight: 6.941, symbol: 'Li'},
  {price: 4.2,  quantity: 4.2,  latestBuy: 4.2,  latestSell: 4.2,  deliveryDate: '10/10/2029',  name: 'prod-21',  	weight: 9.0122, symbol: 'Be'},
  {price: 1.5,  quantity: 1.5,  latestBuy: 1.5,  latestSell: 1.5,  deliveryDate: '10/10/2029',  name: 'prod-22',  	weight: 10.811, symbol: 'B'},
  {price: 2.6,  quantity: 2.6,  latestBuy: 2.6,  latestSell: 2.6,  deliveryDate: '10/10/2029',  name: 'prod-23',  	weight: 12.0107, symbol: 'C'},
  {price: 1.7,  quantity: 1.7,  latestBuy: 1.7,  latestSell: 1.7,  deliveryDate: '10/10/2029',  name: 'prod-24',  	weight: 14.0067, symbol: 'N'},
  {price: 8.2,  quantity: 8.2,  latestBuy: 8.2,  latestSell: 8.2,  deliveryDate: '10/10/2029',  name: 'prod-25',  	weight: 15.9994, symbol: 'O'},
  {price: 9.1,  quantity: 9.1,  latestBuy: 9.1,  latestSell: 9.1,  deliveryDate: '10/10/2029',  name: 'prod-26',  	weight: 18.9984, symbol: 'F'},
  {price: 10.2, quantity: 10.2, latestBuy: 10.2, latestSell: 10.2, deliveryDate: '10/10/2029',  name: 'prod-27',  	weight: 20.1797, symbol: 'Ne'},
  {price: 12.1, quantity: 12.1, latestBuy: 12.1, latestSell: 12.1, deliveryDate: '10/10/2029',  name: 'prod-28',  	weight: 20.1797, symbol: 'Ne'},
  {price: 3.1,  quantity: 3.1,  latestBuy: 3.1,  latestSell: 3.1,  deliveryDate: '10/10/2029',  name: 'prod-29', weight: 20.1797, symbol: 'Ne'},
  {price: 2.5,  quantity: 2.5,  latestBuy: 2.5,  latestSell: 2.5,  deliveryDate: '10/10/2029',  name: 'prod-30', weight: 20.1797, symbol: 'Ne'},
  {price: 4.8,  quantity: 4.8,  latestBuy: 4.8,  latestSell: 4.8,  deliveryDate: '10/10/2029',  name: 'prod-31', weight: 20.1797, symbol: 'Ne'},
  {price: 7.3,  quantity: 7.3,  latestBuy: 7.3,  latestSell: 7.3,  deliveryDate: '10/10/2029',  name: 'prod-32', weight: 20.1797, symbol: 'Ne'},
  {price: 9.9,  quantity: 9.9,  latestBuy: 9.9,  latestSell: 9.9,  deliveryDate: '10/10/2029',  name: 'prod-33', weight: 20.1797, symbol: 'Ne'},
  {price: 0.5,  quantity: 0.5,  latestBuy: 0.5,  latestSell: 0.5,  deliveryDate: '10/10/2029',  name: 'prod-34', weight:	20.1797, symbol: 'Ne'},
  {price: 1.2,  quantity: 1.2,  latestBuy: 1.2,  latestSell: 1.2,  deliveryDate: '10/10/2029',name: 'prod-35'  , weight: 1.0079, symbol: 'H'},
  {price: 2.3,  quantity: 2.3,  latestBuy: 2.3,  latestSell: 2.3,  deliveryDate: '10/10/2029',  name: 'prod-36',  	weight: 4.0026, symbol: 'He'},
  {price: 33.2, quantity: 33.2, latestBuy: 33.2, latestSell: 33.2, deliveryDate: '10/10/2029',  name: 'prod-37',  	weight: 6.941, symbol: 'Li'},
  {price: 4.2,  quantity: 4.2,  latestBuy: 4.2,  latestSell: 4.2,  deliveryDate: '10/10/2029',  name: 'prod-38',  	weight: 9.0122, symbol: 'Be'},
  {price: 1.5,  quantity: 1.5,  latestBuy: 1.5,  latestSell: 1.5,  deliveryDate: '10/10/2029',  name: 'prod-39',  	weight: 10.811, symbol: 'B'},
  {price: 2.6,  quantity: 2.6,  latestBuy: 2.6,  latestSell: 2.6,  deliveryDate: '10/10/2029',  name: 'prod-40',  	weight: 12.0107, symbol: 'C'},
  {price: 1.7,  quantity: 1.7,  latestBuy: 1.7,  latestSell: 1.7,  deliveryDate: '10/10/2029',  name: 'prod-41',  	weight: 14.0067, symbol: 'N'},
  {price: 8.2,  quantity: 8.2,  latestBuy: 8.2,  latestSell: 8.2,  deliveryDate: '10/10/2029',  name: 'prod-42',  	weight: 15.9994, symbol: 'O'},
  {price: 9.1,  quantity: 9.1,  latestBuy: 9.1,  latestSell: 9.1,  deliveryDate: '10/10/2029',  name: 'prod-43',  	weight: 18.9984, symbol: 'F'},
  {price: 10.2, quantity: 10.2, latestBuy: 10.2, latestSell: 10.2, deliveryDate: '10/10/2029',  name: 'prod-44',  	weight: 20.1797, symbol: 'Ne'},
  {price: 12.1, quantity: 12.1, latestBuy: 12.1, latestSell: 12.1, deliveryDate: '10/10/2029',  name: 'prod-45',  	weight: 20.1797, symbol: 'Ne'},
  {price: 3.1,  quantity: 3.1,  latestBuy: 3.1,  latestSell: 3.1,  deliveryDate: '10/10/2029',  name: 'prod-46', weight: 20.1797, symbol: 'Ne'},
  {price: 2.5,  quantity: 2.5,  latestBuy: 2.5,  latestSell: 2.5,  deliveryDate: '10/10/2029',  name: 'prod-47', weight: 20.1797, symbol: 'Ne'},
  {price: 4.8,  quantity: 4.8,  latestBuy: 4.8,  latestSell: 4.8,  deliveryDate: '10/10/2029',  name: 'prod-48', weight: 20.1797, symbol: 'Ne'},
  {price: 7.3,  quantity: 7.3,  latestBuy: 7.3,  latestSell: 7.3,  deliveryDate: '10/10/2029',  name: 'prod-49', weight: 20.1797, symbol: 'Ne'},
  {price: 9.9,  quantity: 9.9,  latestBuy: 9.9,  latestSell: 9.9,  deliveryDate: '10/10/2029',  name: 'prod-50', weight: 20.1797, symbol: 'Ne'},
  {price: 0.5,  quantity: 0.5,  latestBuy: 0.5,  latestSell: 0.5,  deliveryDate: '10/10/2029',  name: 'prod-51', weight:	20.1797, symbol: 'Ne'},
  {price: 4.8,  quantity: 4.8,  latestBuy: 4.8,  latestSell: 4.8,  deliveryDate: '10/10/2029',  name: 'prod-31', weight: 20.1797, symbol: 'Ne'},
  {price: 7.3,  quantity: 7.3,  latestBuy: 7.3,  latestSell: 7.3,  deliveryDate: '10/10/2029',  name: 'prod-32', weight: 20.1797, symbol: 'Ne'},
  {price: 9.9,  quantity: 9.9,  latestBuy: 9.9,  latestSell: 9.9,  deliveryDate: '10/10/2029',  name: 'prod-33', weight: 20.1797, symbol: 'Ne'},
  {price: 0.5,  quantity: 0.5,  latestBuy: 0.5,  latestSell: 0.5,  deliveryDate: '10/10/2029',  name: 'prod-34', weight:	20.1797, symbol: 'Ne'},
  {price: 1.2,  quantity: 1.2,  latestBuy: 1.2,  latestSell: 1.2,  deliveryDate: '10/10/2029',name: 'prod-35'  , weight: 1.0079, symbol: 'H'},
  {price: 2.3,  quantity: 2.3,  latestBuy: 2.3,  latestSell: 2.3,  deliveryDate: '10/10/2029',  name: 'prod-36',  	weight: 4.0026, symbol: 'He'},
  {price: 33.2, quantity: 33.2, latestBuy: 33.2, latestSell: 33.2, deliveryDate: '10/10/2029',  name: 'prod-37',  	weight: 6.941, symbol: 'Li'},
  {price: 4.2,  quantity: 4.2,  latestBuy: 4.2,  latestSell: 4.2,  deliveryDate: '10/10/2029',  name: 'prod-38',  	weight: 9.0122, symbol: 'Be'},
  {price: 1.5,  quantity: 1.5,  latestBuy: 1.5,  latestSell: 1.5,  deliveryDate: '10/10/2029',  name: 'prod-39',  	weight: 10.811, symbol: 'B'},
  {price: 2.6,  quantity: 2.6,  latestBuy: 2.6,  latestSell: 2.6,  deliveryDate: '10/10/2029',  name: 'prod-40',  	weight: 12.0107, symbol: 'C'},
  {price: 1.7,  quantity: 1.7,  latestBuy: 1.7,  latestSell: 1.7,  deliveryDate: '10/10/2029',  name: 'prod-41',  	weight: 14.0067, symbol: 'N'},
  {price: 8.2,  quantity: 8.2,  latestBuy: 8.2,  latestSell: 8.2,  deliveryDate: '10/10/2029',  name: 'prod-42',  	weight: 15.9994, symbol: 'O'},
  {price: 9.1,  quantity: 9.1,  latestBuy: 9.1,  latestSell: 9.1,  deliveryDate: '10/10/2029',  name: 'prod-43',  	weight: 18.9984, symbol: 'F'},
  {price: 10.2, quantity: 10.2, latestBuy: 10.2, latestSell: 10.2, deliveryDate: '10/10/2029',  name: 'prod-44',  	weight: 20.1797, symbol: 'Ne'},
  {price: 12.1, quantity: 12.1, latestBuy: 12.1, latestSell: 12.1, deliveryDate: '10/10/2029',  name: 'prod-45',  	weight: 20.1797, symbol: 'Ne'},
  {price: 3.1,  quantity: 3.1,  latestBuy: 3.1,  latestSell: 3.1,  deliveryDate: '10/10/2029',  name: 'prod-46', weight: 20.1797, symbol: 'Ne'},
  {price: 2.5,  quantity: 2.5,  latestBuy: 2.5,  latestSell: 2.5,  deliveryDate: '10/10/2029',  name: 'prod-47', weight: 20.1797, symbol: 'Ne'},
  {price: 4.8,  quantity: 4.8,  latestBuy: 4.8,  latestSell: 4.8,  deliveryDate: '10/10/2029',  name: 'prod-48', weight: 20.1797, symbol: 'Ne'},
  {price: 7.3,  quantity: 7.3,  latestBuy: 7.3,  latestSell: 7.3,  deliveryDate: '10/10/2029',  name: 'prod-49', weight: 20.1797, symbol: 'Ne'},
  {price: 9.9,  quantity: 9.9,  latestBuy: 9.9,  latestSell: 9.9,  deliveryDate: '10/10/2029',  name: 'prod-50', weight: 20.1797, symbol: 'Ne'},
  {price: 4.8,  quantity: 4.8,  latestBuy: 4.8,  latestSell: 4.8,  deliveryDate: '10/10/2029',  name: 'prod-31', weight: 20.1797, symbol: 'Ne'},
  {price: 7.3,  quantity: 7.3,  latestBuy: 7.3,  latestSell: 7.3,  deliveryDate: '10/10/2029',  name: 'prod-32', weight: 20.1797, symbol: 'Ne'},
  {price: 9.9,  quantity: 9.9,  latestBuy: 9.9,  latestSell: 9.9,  deliveryDate: '10/10/2029',  name: 'prod-33', weight: 20.1797, symbol: 'Ne'},
  {price: 0.5,  quantity: 0.5,  latestBuy: 0.5,  latestSell: 0.5,  deliveryDate: '10/10/2029',  name: 'prod-34', weight:	20.1797, symbol: 'Ne'},
  {price: 1.2,  quantity: 1.2,  latestBuy: 1.2,  latestSell: 1.2,  deliveryDate: '10/10/2029',name: 'prod-35'  , weight: 1.0079, symbol: 'H'},
  {price: 2.3,  quantity: 2.3,  latestBuy: 2.3,  latestSell: 2.3,  deliveryDate: '10/10/2029',  name: 'prod-36',  	weight: 4.0026, symbol: 'He'},
  {price: 33.2, quantity: 33.2, latestBuy: 33.2, latestSell: 33.2, deliveryDate: '10/10/2029',  name: 'prod-37',  	weight: 6.941, symbol: 'Li'},
  {price: 4.2,  quantity: 4.2,  latestBuy: 4.2,  latestSell: 4.2,  deliveryDate: '10/10/2029',  name: 'prod-38',  	weight: 9.0122, symbol: 'Be'},
  {price: 1.5,  quantity: 1.5,  latestBuy: 1.5,  latestSell: 1.5,  deliveryDate: '10/10/2029',  name: 'prod-39',  	weight: 10.811, symbol: 'B'},
  {price: 2.6,  quantity: 2.6,  latestBuy: 2.6,  latestSell: 2.6,  deliveryDate: '10/10/2029',  name: 'prod-40',  	weight: 12.0107, symbol: 'C'},
  {price: 1.7,  quantity: 1.7,  latestBuy: 1.7,  latestSell: 1.7,  deliveryDate: '10/10/2029',  name: 'prod-41',  	weight: 14.0067, symbol: 'N'},
  {price: 8.2,  quantity: 8.2,  latestBuy: 8.2,  latestSell: 8.2,  deliveryDate: '10/10/2029',  name: 'prod-42',  	weight: 15.9994, symbol: 'O'},
  {price: 9.1,  quantity: 9.1,  latestBuy: 9.1,  latestSell: 9.1,  deliveryDate: '10/10/2029',  name: 'prod-43',  	weight: 18.9984, symbol: 'F'},
  {price: 10.2, quantity: 10.2, latestBuy: 10.2, latestSell: 10.2, deliveryDate: '10/10/2029',  name: 'prod-44',  	weight: 20.1797, symbol: 'Ne'},
  {price: 12.1, quantity: 12.1, latestBuy: 12.1, latestSell: 12.1, deliveryDate: '10/10/2029',  name: 'prod-45',  	weight: 20.1797, symbol: 'Ne'},
  {price: 3.1,  quantity: 3.1,  latestBuy: 3.1,  latestSell: 3.1,  deliveryDate: '10/10/2029',  name: 'prod-46', weight: 20.1797, symbol: 'Ne'},
  {price: 2.5,  quantity: 2.5,  latestBuy: 2.5,  latestSell: 2.5,  deliveryDate: '10/10/2029',  name: 'prod-47', weight: 20.1797, symbol: 'Ne'},
  {price: 4.8,  quantity: 4.8,  latestBuy: 4.8,  latestSell: 4.8,  deliveryDate: '10/10/2029',  name: 'prod-48', weight: 20.1797, symbol: 'Ne'},
  {price: 7.3,  quantity: 7.3,  latestBuy: 7.3,  latestSell: 7.3,  deliveryDate: '10/10/2029',  name: 'prod-49', weight: 20.1797, symbol: 'Ne'},
  {price: 9.9,  quantity: 9.9,  latestBuy: 9.9,  latestSell: 9.9,  deliveryDate: '10/10/2029',  name: 'prod-50', weight: 20.1797, symbol: 'Ne'},
  {price: 4.8,  quantity: 4.8,  latestBuy: 4.8,  latestSell: 4.8,  deliveryDate: '10/10/2029',  name: 'prod-31', weight: 20.1797, symbol: 'Ne'},
  {price: 7.3,  quantity: 7.3,  latestBuy: 7.3,  latestSell: 7.3,  deliveryDate: '10/10/2029',  name: 'prod-32', weight: 20.1797, symbol: 'Ne'},
  {price: 9.9,  quantity: 9.9,  latestBuy: 9.9,  latestSell: 9.9,  deliveryDate: '10/10/2029',  name: 'prod-33', weight: 20.1797, symbol: 'Ne'},
  {price: 0.5,  quantity: 0.5,  latestBuy: 0.5,  latestSell: 0.5,  deliveryDate: '10/10/2029',  name: 'prod-34', weight:	20.1797, symbol: 'Ne'},
  {price: 1.2,  quantity: 1.2,  latestBuy: 1.2,  latestSell: 1.2,  deliveryDate: '10/10/2029',name: 'prod-35'  , weight: 1.0079, symbol: 'H'},
  {price: 2.3,  quantity: 2.3,  latestBuy: 2.3,  latestSell: 2.3,  deliveryDate: '10/10/2029',  name: 'prod-36',  	weight: 4.0026, symbol: 'He'},
  {price: 33.2, quantity: 33.2, latestBuy: 33.2, latestSell: 33.2, deliveryDate: '10/10/2029',  name: 'prod-37',  	weight: 6.941, symbol: 'Li'},
  {price: 4.2,  quantity: 4.2,  latestBuy: 4.2,  latestSell: 4.2,  deliveryDate: '10/10/2029',  name: 'prod-38',  	weight: 9.0122, symbol: 'Be'},
  {price: 1.5,  quantity: 1.5,  latestBuy: 1.5,  latestSell: 1.5,  deliveryDate: '10/10/2029',  name: 'prod-39',  	weight: 10.811, symbol: 'B'},
  {price: 2.6,  quantity: 2.6,  latestBuy: 2.6,  latestSell: 2.6,  deliveryDate: '10/10/2029',  name: 'prod-40',  	weight: 12.0107, symbol: 'C'},
  {price: 1.7,  quantity: 1.7,  latestBuy: 1.7,  latestSell: 1.7,  deliveryDate: '10/10/2029',  name: 'prod-41',  	weight: 14.0067, symbol: 'N'},
  {price: 8.2,  quantity: 8.2,  latestBuy: 8.2,  latestSell: 8.2,  deliveryDate: '10/10/2029',  name: 'prod-42',  	weight: 15.9994, symbol: 'O'},
  {price: 9.1,  quantity: 9.1,  latestBuy: 9.1,  latestSell: 9.1,  deliveryDate: '10/10/2029',  name: 'prod-43',  	weight: 18.9984, symbol: 'F'},
  {price: 10.2, quantity: 10.2, latestBuy: 10.2, latestSell: 10.2, deliveryDate: '10/10/2029',  name: 'prod-44',  	weight: 20.1797, symbol: 'Ne'},
  {price: 12.1, quantity: 12.1, latestBuy: 12.1, latestSell: 12.1, deliveryDate: '10/10/2029',  name: 'prod-45',  	weight: 20.1797, symbol: 'Ne'},
  {price: 3.1,  quantity: 3.1,  latestBuy: 3.1,  latestSell: 3.1,  deliveryDate: '10/10/2029',  name: 'prod-46', weight: 20.1797, symbol: 'Ne'},
  {price: 2.5,  quantity: 2.5,  latestBuy: 2.5,  latestSell: 2.5,  deliveryDate: '10/10/2029',  name: 'prod-47', weight: 20.1797, symbol: 'Ne'},
  {price: 4.8,  quantity: 4.8,  latestBuy: 4.8,  latestSell: 4.8,  deliveryDate: '10/10/2029',  name: 'prod-48', weight: 20.1797, symbol: 'Ne'},
  {price: 7.3,  quantity: 7.3,  latestBuy: 7.3,  latestSell: 7.3,  deliveryDate: '10/10/2029',  name: 'prod-49', weight: 20.1797, symbol: 'Ne'},
  {price: 9.9,  quantity: 9.9,  latestBuy: 9.9,  latestSell: 9.9,  deliveryDate: '10/10/2029',  name: 'prod-50', weight: 20.1797, symbol: 'Ne'}
];

/**
 * Data source
 */
import { DataSource } from '@angular/cdk/collections';
import { BehaviorSubject, Observable } from 'rxjs';

const PAGESIZE = 20;
const ROW_HEIGHT = 48;

@Component({
  selector: 'app-market-demo-design',
  templateUrl: './market-demo-design.component.html',
  styleUrls: ['./market-demo-design.component.scss']
})
export class MarketDemoDesignComponent implements OnInit {

topLeftVisibilityStatus: Boolean;
topRightVisibilityStatus: Boolean;
bottomLeftVisibilityStatus: Boolean;
bottomRightVisibilityStatus: Boolean;



displayedColumns: string[] = ['position', 'name', 'weight', 'symbol'];
dataSource = ELEMENT_DATA;

porductColumns: string[] = ['actions', 'name', 'price', 'quantity', 'latestBuy' , 'latestSell', 'deliveryDate', 'weight', 'symbol'];
productDataSource = DEMO_PRODUCT_DATA;

  ngOnInit() {
    this.topLeftVisibilityStatus = true;
    this.topRightVisibilityStatus = true;
    this.bottomLeftVisibilityStatus = true;
    this.bottomRightVisibilityStatus = true;

  }


  blockMoreCornersHide(){
    
    let countInvisibleCorners = 0;
    if( this.topLeftVisibilityStatus == false ) countInvisibleCorners ++;
    if( this.topRightVisibilityStatus == false )  countInvisibleCorners ++;
    if( this.bottomLeftVisibilityStatus == false ) countInvisibleCorners ++;
    if( this.bottomRightVisibilityStatus == false ) countInvisibleCorners ++;

    if( countInvisibleCorners >=3 ) return true;
    else return false;
  }

  hideProductsTable() {

      if(this.topLeftVisibilityStatus == true && !this.blockMoreCornersHide()) this.topLeftVisibilityStatus = false;
      else this.topLeftVisibilityStatus = true; 
  }

  hideOrdersByProductTable() {

      if(this.topRightVisibilityStatus == true && !this.blockMoreCornersHide())this.topRightVisibilityStatus = false;
      else this.topRightVisibilityStatus = true;
  }


  hideOrdersAndTradesTable() {

      if(this.bottomLeftVisibilityStatus == true && !this.blockMoreCornersHide()) this.bottomLeftVisibilityStatus = false;
      else this.bottomLeftVisibilityStatus = true;
  }

  hideWeatherTable() {

      if(this.bottomRightVisibilityStatus == true && !this.blockMoreCornersHide()) this.bottomRightVisibilityStatus = false;
      else this.bottomRightVisibilityStatus = true;
  }   





  getTopLeftVisibility(){ 
     if( this.topLeftVisibilityStatus == true &&  this.topRightVisibilityStatus == true ) return 'side';
     else if( this.topLeftVisibilityStatus == true &&  this.topRightVisibilityStatus == false ) return 'wide';
     else return 'hidden';
  }

  getTopRightVisibility(){ 
     if( this.topRightVisibilityStatus == true &&  this.topLeftVisibilityStatus == true ) return 'side';
     else if( this.topRightVisibilityStatus == true &&  this.topLeftVisibilityStatus == false ) return 'wide';
     else return 'hidden';
  }

  getBottomLeftVisibility(){ 
     if( this.bottomLeftVisibilityStatus == true &&  this.bottomRightVisibilityStatus == true ) return 'side';
     else if( this.bottomLeftVisibilityStatus == true &&  this.bottomRightVisibilityStatus == false ) return 'wide';
     else return 'hidden';
  }


  getBottomRightVisibility(){ 
     if( this.bottomRightVisibilityStatus == true &&  this.bottomLeftVisibilityStatus == true ) return 'side';
     else if( this.bottomRightVisibilityStatus == true &&  this.bottomLeftVisibilityStatus == false ) return 'wide';
     else return 'hidden';
  }   

  getTopVisibility(){ 
     if( (this.topLeftVisibilityStatus == true || this.topRightVisibilityStatus == true) && 
         (this.bottomLeftVisibilityStatus == true || this.bottomRightVisibilityStatus == true)  ) return 'side';
     else if( (this.topLeftVisibilityStatus == true || this.topRightVisibilityStatus == true) && 
         (this.bottomLeftVisibilityStatus == false && this.bottomRightVisibilityStatus == false)  ) return 'wide';

     else return 'hidden';
  }

  getBottomVisibility(){ 
     if(  (this.bottomRightVisibilityStatus == true || this.bottomLeftVisibilityStatus == true) && 
          (this.topLeftVisibilityStatus == true || this.topRightVisibilityStatus == true) ) return 'side';
     else if(  (this.bottomRightVisibilityStatus == true || this.bottomLeftVisibilityStatus == true) && 
          (this.topLeftVisibilityStatus == false && this.topRightVisibilityStatus == false) ) return 'wide';
     else return 'hidden';
  } 

  



}
