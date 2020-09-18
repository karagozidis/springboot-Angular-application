import { Component, OnInit } from '@angular/core';
import { AppDataService } from 'app/services/app-data.service';
import { AppType } from 'app/shared/config/app-constants';

@Component({
  selector: 'app-market-notification-messages',
  templateUrl: './market-notification-messages.component.html',
  styleUrls: ['./market-notification-messages.component.scss']
})
export class MarketNotificationMessagesComponent implements OnInit {
  AppType = AppType
  constructor(private appDataService: AppDataService) { }

  ngOnInit() {
    this.appDataService.setAppType(AppType.MARKET)   
  }

}
