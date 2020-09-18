import { Component, OnInit } from '@angular/core';
import { InternalMessageService } from 'app/shared/utils/internal-message.service';
import { MarketService } from 'app/services/market.service';
import { AppDataService } from 'app/services/app-data.service';
import { AppType } from 'app/shared/config/app-constants';



@Component({
  selector: 'app-bme-command-inspector',
  templateUrl: './bme-command-inspector.component.html',
  styleUrls: ['./bme-command-inspector.component.scss']
})
export class BmeCommandInspectorComponent implements OnInit {

  messageServiceSubscription
  bmeCommands: string[] = [];
  connectionState: boolean;

  constructor(
    private msgService: InternalMessageService,
    private marketService: MarketService,
    private appDataService: AppDataService
    ) {
     
    }

  ngOnInit() {
    this.appDataService.setAppType(AppType.MARKET)
    this.subscribeToMessageService();
    this.getConnectionState();
  }

  getConnectionState() {
    this.marketService.get().subscribe((connectionState) => {
      this.connectionState = connectionState;
    })
  }

  setConnectionState() {
    if (this.connectionState === false) {
      this.connectionState = true;
    } else {
      this.connectionState = false;
    }
    this.marketService.put(this.connectionState).subscribe( (connectionState) => {
      this. getConnectionState();
    });
  }

  subscribeToMessageService() {
    const that = this;
    this.messageServiceSubscription = this.msgService.accessMessage('bme-commands').subscribe(
        (data) => {
          this.bmeCommands.push( data);
        });
  }

  ngOnDestroy() {
      this.messageServiceSubscription.unsubscribe();
  }

  deleteData() {
      this.bmeCommands  = [];
  }

}
