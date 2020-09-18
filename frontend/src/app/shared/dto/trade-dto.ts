import { OrderDTO } from "./order-dto";
import { BaseInfoTradeDTO } from "./base-info-trade-dto";

export class TradeDTO extends BaseInfoTradeDTO {
    public singleOrder1: OrderDTO
    public singleOrder2: OrderDTO
    public order1_price: number
    public order1_quantity: number
    public order2_price: number
    public order2_quantity: number
  }
