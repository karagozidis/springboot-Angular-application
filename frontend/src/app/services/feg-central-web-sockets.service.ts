import {UserService} from './user.service';
import {Injectable} from '@angular/core';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import {InternalMessageService} from 'app/shared/utils/internal-message.service'
import {NotificationToastService} from '.././shared/utils/notification-toast-service';
import {AppConstants, UpdateSignals} from 'app/shared/config/app-constants'
import {AppDataService} from './app-data.service';
import {UserDto} from 'app/shared/dto/user-dto';
import { Auxiliary } from 'app/shared/utils/auxiliary';

const webSocketUrl = AppConstants.CRM_CENTRAL_URL + '/sockjs'

@Injectable({
    providedIn: 'root'
})
export class CrmCentralWebSocketsService {
    private stompClient;
    userDto: UserDto = null;
    subscriptionIds = new Array();

    constructor(
        private msgService: InternalMessageService,
        private notificationToastService: NotificationToastService,
        private appDataService: AppDataService) {
        console.log('4.1.1  CrmCentralWebSocketsService constructor');
        console.log(this.appDataService);
        this.appDataService.updateEmmiter.subscribe(event => {
            console.log('4.1.  event no event');
            console.log('4.1.  event'+ event);
            if (event == UpdateSignals.USER_DTO) {
                console.log('5.  this.onUpdateSignal()');
                this.onUpdateSignal()
            }
        })
        this.userDto = this.appDataService.userDto
    }

    onUpdateSignal() {
        this.userDto = this.appDataService.userDto
        console.log('6.  this.onUpdateSignal()');
        this.unsubscribeUserFromTopics();
        this.subscribeToOwnNotificationTopic();
        if (this.userDto.rolesMarket !== '') {
            this.subscribeToOwnMarketTradeTopic();
            this.subscribeToMarketOrderTopic();
            this.subscribeToBmeResponcesTopic();
        }
    }

    private getLoggedInUserId() {
        this.userDto = this.appDataService.userDto;
        return this.userDto == null ? null : this.userDto.id;
    }

    private isUserLoggedIn() {
        this.userDto = this.appDataService.userDto;
        return this.userDto == null ? false : true;
    }

    subscribeToOwnMarketTradeTopic() {
        this.userDto = this.appDataService.userDto;
        if (this.isUserLoggedIn() === false) {
            return;
        }

        const ws = new SockJS(webSocketUrl + '?wst=' + btoa(UserService.getJwt()));
        this.stompClient = Stomp.over(ws);

        this.stompClient.connect({}, (frame) => {

            setTimeout(() => {
                const subscription = this.stompClient.subscribe('/topic/market/matching/' + this.getLoggedInUserId(), (message) => {
                    this.msgService.publishMessage('MarketMatching', message.body);
                    this.msgService.publishMessage('RefreshNavBar', message.body);

                });
                this.subscriptionIds.push(subscription.id);
            }, 6000);
        });
    }

    subscribeToMarketOrderTopic() {
        console.log('1001.****');
        this.userDto = this.appDataService.userDto;
        if (this.isUserLoggedIn() === false) {
            return;
        }
        console.log('1002.****');
        const ws = new SockJS(webSocketUrl + '?wst=' + btoa(UserService.getJwt()));
        this.stompClient = Stomp.over(ws);
        console.log('1003.****');
        this.stompClient.connect({}, (frame) => {
            console.log('1004.****');
            setTimeout(() => {
                const subscription = this.stompClient.subscribe('/topic/market/order', (message) => {
                    console.log('1005.**** subscribeToMarketOrderTopic()');
                    this.msgService.publishMessage('MarketOrder', message.body);
                    this.msgService.publishMessage('RefreshNavBar', message.body);
                });
                this.subscriptionIds.push(subscription.id);
            }, 6000);
        });
    }

    subscribeToOwnNotificationTopic() {
        this.userDto = this.appDataService.userDto;
        if (this.isUserLoggedIn() === false) {
            return;
        }
        const ws = new SockJS(webSocketUrl + '?wst=' + btoa(UserService.getJwt()));
        this.stompClient = Stomp.over(ws);

        this.stompClient.connect({}, (frame) => {
            setTimeout(() => {
                const subscription = this.stompClient.subscribe('/topic/notifications/'+ this.getLoggedInUserId(), (message) => {
                    this.msgService.publishMessage('RefreshNavBar', message.body);
                });
                this.subscriptionIds.push(subscription.id);
            }, 6000);
        });
    }


    subscribeToBmeResponcesTopic() {
        this.userDto = this.appDataService.userDto;
        if (this.isUserLoggedIn() === false) {
            return;
        }
        const ws = new SockJS(webSocketUrl + '?wst=' + btoa(UserService.getJwt()));
        this.stompClient = Stomp.over(ws);

        this.stompClient.connect({}, (frame) => {

            setTimeout(() => {
                const subscription = this.stompClient.subscribe('/topic/market/bme-commands', (message) => {
                    this.msgService.publishMessage('bme-commands', message.body);
                });
                this.subscriptionIds.push(subscription.id);
            }, 6000);
        });
    }


    unsubscribeUserFromTopics() {
        console.log('100. ************************ unsubscribeUserFromTopics************************');
        if (this.stompClient != null || this.stompClient != undefined) {
            console.log('101. ************************ unsubscribeUserFromTopics************************');
            for (const subscriptionId of this.subscriptionIds) {
                this.stompClient.unsubscribe(subscriptionId);
            }
            this.subscriptionIds.length = 0;
            this.stompClient.disconnect();
            this.stompClient = null;
        }
    }
}
