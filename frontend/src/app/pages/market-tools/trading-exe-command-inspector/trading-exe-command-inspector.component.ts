import { Component, OnInit } from '@angular/core';
import { AppDataService } from 'app/services/app-data.service';
import { AppType } from 'app/shared/config/app-constants';

@Component({
  selector: 'app-trading-exe-command-inspector',
  templateUrl: './trading-exe-command-inspector.component.html',
  styleUrls: ['./trading-exe-command-inspector.component.scss']
})
export class TradingExeCommandInspectorComponent implements OnInit {

  constructor(private appDataService: AppDataService) { }

  ngOnInit() {
    this.appDataService.setAppType(AppType.MARKET)
  }

}
