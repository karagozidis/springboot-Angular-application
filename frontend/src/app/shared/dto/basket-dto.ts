import { OrderStateService } from "app/services/orderState.service";
import { AppConstants } from "../config/app-constants";

export class BasketDTO {
  public id: number
  public basketType: string
  public metadata: string
  public orders: any[]

  static createFromDialogData(orderStateService: OrderStateService){
    // const basketData= new BasketData();
    const basketDTO = new BasketDTO();
    const metadata = new Object()
    

    basketDTO.basketType = orderStateService.getBasketTableArray()[0]['basket']['basketType']

    if(basketDTO.basketType === AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED){
        metadata['totalQuantity'] =  orderStateService.getBasketParameters()['totalQuantity']
    }
    else if(basketDTO.basketType === AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE){
      metadata['maxMatchedOrders'] =  orderStateService.getBasketParameters()['maxMatchedOrders']
    } 
    else if(basketDTO.basketType === AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED){
        metadata['maxCapacity'] =  orderStateService.getBasketParameters()['maxCapacity']
        metadata['initialCharge'] =  orderStateService.getBasketParameters()['initialCharge']
        metadata['ratedPower'] =  orderStateService.getBasketParameters()['ratedPower']
    }

    basketDTO.metadata = JSON.stringify(metadata)
    basketDTO.orders = orderStateService.getBasketTableArray()

    for(let i = 0; i < basketDTO.orders.length; i++) {
        basketDTO.orders[i].orderStatus = null;
    }
    return basketDTO;
  }
}
