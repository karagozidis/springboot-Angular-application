import { OrderDTO } from "./order-dto";

export class ProductBookDTO {
    constructor(
    public name: string,
    public deliveryTimeStart: string,
    public deliveryTimeEnd: string,
    public latestBuyOrder: OrderDTO,
    public latestSellOrder: OrderDTO,
    public productStatus: string
    ) {}
  }