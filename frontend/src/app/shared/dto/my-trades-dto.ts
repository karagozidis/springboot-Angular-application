import { ProductDTO } from "./product-dto";
import { OrderDTO } from "./order-dto";

export class TradeDTO {
    constructor(
    public id: string,
    public createdOn: string,
    public singleOrder1: OrderDTO,
    public singleOrder2: OrderDTO,
    public price: string,
    public quantity: string,
    public metadata: string,
    public order1_price: number,
    public order1_quantity: number,
    public order2_price: number,
    public order2_quantity: number
    ) {}
  }
