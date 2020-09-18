import {TradeDTO} from './trade-dto'
import {MyOrdersDTO} from './myOrders-dto'

export class MarketObject {
    constructor(
        public type: string,
        public createdOn: Date,
        public tradeDTO: TradeDTO,
        public singleOrderDTO: MyOrdersDTO
        ) {}
}
