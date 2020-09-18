import { OrderDTO } from "./order-dto";
import { UserDto } from "./user-dto";

export class ProductDTO {
    public id: string;
    public name: string;
    public createdOn: string;
    public deliveryTimeStart: string;
    public deliveryTimeEnd: string;
    public closesAt: string;
    public period: string;
    public productStatus: string;
    public user: UserDto
    public latestSellOrder: OrderDTO
    public latestBuyOrder: OrderDTO
    public marketCode: String;
    public hasOrders: boolean;
    public totalOrders: number;

  }
