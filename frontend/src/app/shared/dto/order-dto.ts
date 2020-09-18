import { UserDto } from './user-dto';
import { ProductDTO } from "./product-dto";
import { OrderMetadataDTO } from "./orderMetadataDTO";
import { OrderDialogData } from "app/pages/market/components/market-dialog/market-dialog.component";
import { AppConstants } from "../config/app-constants";
import { BasketDTO } from './basket-dto';

export class OrderDTO {
  public id: string;
  public product: ProductDTO =  new ProductDTO()
  public bidTime: string
  public user: UserDto
  public price: string
  public quantity: string
  public current_price: string
  public current_quantity: string
  public orderStatus: string
  public orderDirection: string
  public orderType: string
  public metadata: string
  public basket: BasketDTO 
  // public basketType: string
  // public date: string
  // public productName: string
  // public deliveryTimeStart: string
  // public period: string

  static createFromOrderDialogData(productId:string, orderDialogData: OrderDialogData) : OrderDTO {
    const productDTO = new ProductDTO();
    const orderDTO = new OrderDTO();
    ;
    productDTO.id = productId;
    orderDTO.product =  productDTO
    orderDTO['product']['name'] = orderDialogData.productName;
    // orderDTO['product'][''] = orderDialogData.date
    orderDTO['product']['deliveryTimeStart'] = orderDialogData.deliveryTimeStart
    orderDTO['product']['period'] = orderDialogData.period
    if(orderDialogData.period === AppConstants.Market.UILabels.Product.ONE_HOUR_PERIOD){
      orderDTO['product']['period'] =AppConstants.Market.BackendEnums.Product.ONE_HOUR_PERIOD
    }
    else if(orderDialogData.period === AppConstants.Market.UILabels.Product.FIFTEEN_MINUTES_PERIOD){
      orderDTO['product']['period'] =AppConstants.Market.BackendEnums.Product.FIFTEEN_MINUTES_PERIOD
    }
    
    orderDTO.quantity = orderDialogData.quantity
    // orderDTO.current_quantity = orderDTO.quantity
    orderDTO.price = orderDialogData.price
    // orderDTO.current_price = orderDTO.price
    orderDTO.orderStatus = orderDialogData.orderStatus
    orderDTO.orderDirection = orderDialogData.orderDirection
    orderDTO.orderType = orderDialogData.orderType
    
    
    if(orderDTO.orderType === AppConstants.Market.BackendEnums.OrderType.BASKET){
         orderDTO.basket = new BasketDTO()
         orderDTO['basket']['basketType']= orderDialogData.basketType
         if(orderDialogData.basketType === AppConstants.Market.BackendEnums.BasketType.VOLUME_CONSTRAINED ) {
            let orderParameters = new Object();
            orderParameters['totalQuantity'] = orderDialogData.metadata.param1;
            orderDTO.metadata = JSON.stringify(orderParameters);
            orderDTO['basket']['metadata'] = orderDTO.metadata
         }
         else if(orderDialogData.basketType === AppConstants.Market.BackendEnums.BasketType.EXCLUSIVE) {
          let orderParameters = new Object();
          orderParameters['maxMatchedOrders'] = orderDialogData.metadata.param1;
          orderDTO.metadata = JSON.stringify(orderParameters);
          orderDTO['basket']['metadata'] = orderDTO.metadata
        } 
         else if(orderDialogData.basketType === AppConstants.Market.BackendEnums.BasketType.CUMULATIVE_VOLUME_CONSTRAINED){
            let orderParameters = new Object();
            orderParameters['maxCapacity'] = orderDialogData.metadata.param1;
            orderParameters['initialCharge'] = orderDialogData.metadata.param2;
            orderParameters['ratedPower'] = orderDialogData.metadata.param3;
            orderDTO.metadata = JSON.stringify(orderParameters);
            orderDTO['basket']['metadata'] = orderDTO.metadata
         }
         else{
          orderDTO.metadata = '{}'
        }
    }

    else if (orderDialogData.orderType === AppConstants.Market.BackendEnums.OrderType.ICEBERG) {
      let orderParameters = new Object();
      orderParameters[ 'icebergQuantity'] = orderDialogData.metadata.param1;
      orderParameters['icebergPriceDelta'] = orderDialogData.metadata.param2;
      orderDTO.metadata = JSON.stringify(orderParameters);
    }
    else{
      orderDTO.metadata = '{}'
    }


    return orderDTO;
  }
}
