
import {Injectable, EventEmitter} from '@angular/core';

@Injectable({providedIn: 'root'})
export class SideBarToggleService  {

  isSidebarHidden = false;
  isGoToMarketHidden = false;
  isReturnToCrmHidden = false;
  isResponsiveNavButtonHidden  = false;
  toggleEvent = new EventEmitter();


  constructor() {

  }

  setSideBarHidden(isHidden: boolean){
    this.isSidebarHidden = isHidden
    this.toggleEvent.emit('sidebar toggled');
  }

}
