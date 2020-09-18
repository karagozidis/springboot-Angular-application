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
  selector: 'app-notification-form',
  templateUrl: './notification-form.component.html',
  styleUrls: ['./notification-form.component.scss']
})
export class NotificationFormComponent implements OnInit {
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
    this.appType = this.appDataService.appType;
    console.log(this.appType)
    this.activatedRoute.params.subscribe(params => {
      this.refresh( params['id'] );
      this.updateStatus( params['id'] );
      this.notifyNavbar();
      });
     
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
    });
}

}
