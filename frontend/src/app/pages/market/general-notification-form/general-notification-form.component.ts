import { Component, OnInit, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from 'app/services/notification.service';
import { InternalMessageService } from 'app/shared/utils/internal-message.service'
import * as moment from 'moment'
import { NotificationDto } from 'app/shared/dto/notification-dto';
import { AppDataService } from 'app/services/app-data.service';
import { UpdateSignals, AppType } from 'app/shared/config/app-constants';
import { Auxiliary } from 'app/shared/utils/auxiliary';

@Component({
  selector: 'app-general-notification-form',
  templateUrl: './general-notification-form.component.html',
  styleUrls: ['./general-notification-form.component.scss']
})
export class GeneralNotificationFormComponent implements OnInit {
  data;
  appType 
  AppType = AppType;
  updateEmmiter = new EventEmitter();
  

  constructor(private notificationService: NotificationService, private router: Router,
    private activatedRoute: ActivatedRoute,
    private msgService: InternalMessageService,
    private appDataService: AppDataService,
    ) { 
      // this.appDataService.setAppType(AppType.MARKET)
    }

  ngOnInit() {
    console.log(this.appType)
    // this.appDataService.setAppType(AppType.MARKET)
    this.appDataService.appType = AppType.MARKET
    this.appType  = this.appDataService.appType
    this.activatedRoute.params.subscribe(params => {
      this.refresh( params['id'] );
      this.updateStatus( params['id'] );
      this.notifyNavbar();
      
      });
      
     
  }
 
  onMarket(){
    const found = this.router.url.includes('marketNotificationForm');
    return found
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
      this.data.createdOn = moment(new Date(this.data.createdOn)).format('HH:mm  DD/MM/YYYY')
    }); 
}

}
