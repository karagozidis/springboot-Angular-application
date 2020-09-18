import {Injectable, EventEmitter} from '@angular/core';

@Injectable({providedIn: 'root'})
export class DisplayDropDownOptionService  {

  displayDropDownOptionEvent = new EventEmitter();


  constructor() {}

  }