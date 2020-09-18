import { Injectable } from '@angular/core';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { InternalMessageService } from 'app/shared/utils/internal-message.service'
import { NotificationToastService } from '.././shared/utils/notification-toast-service';
import {AppConstants} from 'app/shared/config/app-constants'

const webSocketUrl = AppConstants.CRM_LOCAL_URL + '/sockjs'

@Injectable({
  providedIn: 'root'
})
export class CrmLocalWebSocketsService {
    private stompClient;

    constructor(
        private msgService: InternalMessageService,
        private notificationToastService: NotificationToastService ) { }

        initializeConnections() {
            const ws = new SockJS(webSocketUrl);
            this.stompClient = Stomp.over(ws);
            const that = this;
            this.stompClient.connect({}, (frame) => {
                setTimeout(() => {
                    that.stompClient.subscribe('/topic/newBackendEvents', (message) => {
                        that.msgService.publishMessage('hyppoIo', message.body);
                        that.msgService.publishMessage('RefreshNavBar', message.body);

                        that.notificationToastService.showNotification(
                            '<h4>HyppoIo execution finished!</h4> Check your notifications.',
                            'notifications_active',
                            'top',
                            'center',
                            'info');
                        });
                    }, 6000);
                });
        }
}
