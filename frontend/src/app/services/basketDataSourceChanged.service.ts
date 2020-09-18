import {Injectable, EventEmitter} from '@angular/core';

@Injectable({providedIn: 'root'})
export class BasketDataSourceChangedService  {

  basketChangedEvent = new EventEmitter();


  constructor() {}

  }
