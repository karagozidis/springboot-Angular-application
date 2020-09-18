import {Injectable, EventEmitter} from '@angular/core';

@Injectable({providedIn: 'root'})
export class DataSourceChangedService  {

  dataSourceChangedEvent = new EventEmitter();


  constructor() {}

  }
