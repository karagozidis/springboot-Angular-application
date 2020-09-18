import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NotificationService } from 'app/services/notification.service';
import { InternalMessageService } from 'app/shared/utils/internal-message.service'
import * as moment from 'moment'
import { NotificationDto } from 'app/shared/dto/notification-dto';
import { AppDataService } from 'app/services/app-data.service';
import { UpdateSignals, AppType } from 'app/shared/config/app-constants';
import { Auxiliary } from 'app/shared/utils/auxiliary';

@Component({
  selector: 'app-market-notification-form',
  templateUrl: './market-notification-form.component.html',
  styleUrls: ['./market-notification-form.component.scss']
})
export class MarketNotificationFormComponent implements OnInit {
  data;
  appType 
  AppType = AppType;
  notificationName = '';
  notificationDate = ''
  notificationDescription = ''

  constructor(private notificationService: NotificationService,
    private activatedRoute: ActivatedRoute,
    private msgService: InternalMessageService,
    private appDataService: AppDataService,
    ) { 
    
    }

  ngOnInit() {
    this.appDataService.setAppType(AppType.MARKET)   
  }
 
  public updateStatus(id) {
    this.notificationService.editStatus(id, 'viewed').subscribe();
  }

  notifyNavbar() {
    this.msgService.publishMessage('RefreshNavBar', 'FromNotificationMessages');
  }

  refresh(id) {
    this.notificationService.getNotification(id).subscribe(data => {
      this.data = data;
      this.notificationName = data.name;
      this.notificationDate =  moment(new Date(data.createdOn)).format('HH:mm  DD/MM/YYYY')
      this.notificationDescription = data.description
      // this.data.createdOn = moment(new Date(this.data.createdOn)).format('HH:mm, DD/MM/YYYY')
    });
}

}
