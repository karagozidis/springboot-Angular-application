import {AppConstants} from 'app/shared/config/app-constants'
import * as moment from 'moment'

export class Auxiliary {
    constructor() {}

    /**
    * Checks if the current user is logged in or not.
    * @returns {boolean}
    */
    
    public static isLoggedIn(): boolean {
        return localStorage.getItem(AppConstants.StorageLabels.JWT_STORAGE_NAME) != null &&
        localStorage.getItem(AppConstants.StorageLabels.JWT_STORAGE_NAME) !== undefined;
    }

    public static getTodayTomorrowLabel(timestamp: string) {
        let today = new Date();
        let createdOnDate = new Date(timestamp)

        if(createdOnDate.getDay() === today.getDay()){
            return 'Today'
        }
        else{
            return 'Tomorrow'
        }
    }

    public static getDateLabel(timestamp: string){
        // const date = new Date(orderData)
        var now = new Date().getTime()
        var date = (now - new Date(timestamp).getTime())
        let temporaryDate;
        let dateResponse;
        if(date < 60 * 60 * 1000){
            temporaryDate = Math.floor(date / (60 * 1000))
            if(temporaryDate < 1){
                dateResponse = 'a few seconds ago'
            }
            else if(temporaryDate  > 1.5) {
                dateResponse = temporaryDate + ' minutes ago'
            }
            else{
                dateResponse = temporaryDate  + ' minute ago';
            }
        }
        else if(date >= 60 * 60 * 1000 && date < 60 * 60 * 1000 * 24){
            temporaryDate = Math.floor(date /  3600000)
            if(temporaryDate > 1.5){
                dateResponse = temporaryDate  + ' hours ago'
            }
            else{
                dateResponse = temporaryDate  + ' hour ago'
            }
        }
        else if(date >=  60 * 60 * 1000 * 24 && date <  60 * 60 * 1000 * 24 * 7){
            temporaryDate = Math.floor(date / 86400000)
            if(temporaryDate > 1.5) {
                dateResponse = temporaryDate  + ' days ago'

            }
            else{
                dateResponse = temporaryDate  + ' day ago'
            }
        }
        else if(date >=  60*60 *1000* 24*7 && date <  60*60 *1000 *24 * 30){
            temporaryDate  = Math.floor(date / 604800000)
            if(temporaryDate > 1.5){
                dateResponse = temporaryDate  + ' weeks ago'
            }
            else{
                dateResponse = temporaryDate  + ' week ago'
            }
        }
        else if(date >=  60*60 *1000 *24 * 30){
           dateResponse = moment(new Date(timestamp)).format('HH:mm  DD/MM/YYYY')
        }
        return dateResponse;
    }

    static getOrderTypeLabel(orderData: string) {
        switch(orderData) {
            case AppConstants.Market.BackendEnums.OrderType.LIMIT:
            return AppConstants.Market.UILabels.OrderType.LIMIT;
            case AppConstants.Market.BackendEnums.OrderType.ICEBERG:
            return AppConstants.Market.UILabels.OrderType.ICEBERG;
            case AppConstants.Market.BackendEnums.OrderType.IOC:
            return AppConstants.Market.UILabels.OrderType.IOC;
            case AppConstants.Market.BackendEnums.OrderType.FOK:
            return AppConstants.Market.UILabels.OrderType.FOK;
            case AppConstants.Market.BackendEnums.OrderType.AON:
            return AppConstants.Market.UILabels.OrderType.AON;
            case AppConstants.Market.BackendEnums.OrderType.BASKET:
            return AppConstants.Market.UILabels.OrderType.BASKET;
            default:
            return '';
        }
    }

    static getBasketTypeLabel(orderData: string) {

        switch(orderData){
            case AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE:
            return AppConstants.Market.UILabels.BasketType.EXCLUSIVE;
            case AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED:
            return AppConstants.Market.UILabels.BasketType.VOLUME_CONSTRAINED
            case AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED:
            return AppConstants.Market.UILabels.BasketType.CUMULATIVE_VOLUME_CONSTRAINED
            case AppConstants.Market.BackendEnums.BasketType.LINKED:
            return AppConstants.Market.UILabels.BasketType.LINKED
            case AppConstants.Market.BackendEnums.BasketType.NONE:
            return AppConstants.Market.UILabels.BasketType.NONE
            default:
            return ''

        }
    }
    static getOrderDirectionLabel(orderData: string) {
        switch(orderData){
            case AppConstants.Market.BackendEnums.OrderDirection.BUY:
            return AppConstants.Market.UILabels.OrderDirection.BUY;
            case AppConstants.Market.BackendEnums.OrderDirection.SELL:
            return AppConstants.Market.UILabels.OrderDirection.SELL;
            default:
            return '';
        }
    }
    static getStatusLabel(orderData: string) {
        switch(orderData){
            case AppConstants.Market.BackendEnums.OrderStatus.ACTIVE:
            return AppConstants.Market.UILabels.OrderStatus.ACTIVE;
            case AppConstants.Market.BackendEnums.OrderStatus.DEACTIVATED:
            return AppConstants.Market.UILabels.OrderStatus.DEACTIVATED;
            case AppConstants.Market.BackendEnums.OrderStatus.REMOVED:
            return AppConstants.Market.UILabels.OrderStatus.REMOVED;
            case AppConstants.Market.BackendEnums.OrderStatus.EXPIRED:
            return AppConstants.Market.UILabels.OrderStatus.EXPIRED;
            case AppConstants.Market.BackendEnums.OrderStatus.MATCHED:
            return AppConstants.Market.UILabels.OrderStatus.MATCHED;
            case AppConstants.Market.BackendEnums.OrderStatus.PARTLY_MATCHED:
            return AppConstants.Market.UILabels.OrderStatus.PARTLY_MATCHED;

            default:
            return '';
        }
    }

    static getOrderStatusLabel(orderData: string){
        switch(orderData){
            case AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE:
            return AppConstants.Market.UILabels.BasketType.EXCLUSIVE;
            case AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED:
            return AppConstants.Market.UILabels.BasketType.VOLUME_CONSTRAINED
            case AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED:
            return AppConstants.Market.UILabels.BasketType.CUMULATIVE_VOLUME_CONSTRAINED
            case AppConstants.Market.BackendEnums.BasketType.LINKED:
            return AppConstants.Market.UILabels.BasketType.LINKED
            case AppConstants.Market.BackendEnums.BasketType.NONE:
            return AppConstants.Market.UILabels.BasketType.NONE
            default:
            return ''
        }
    }
    static logInformation(information:any){ 
        if(AppConstants.MODE === AppConstants.ModeTypes.DEV){
           console.log(information)
        }
        else{
          return
        }
      }
}
