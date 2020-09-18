import { UserDto } from './user-dto';
import { ProductDTO } from "./product-dto";

export class MyOrdersDTO {
    constructor(
    public id: number,
    public product: ProductDTO['name'],
    public status: string,
    public orderDirection: string,
    public orderType: string,
    public quantity: string,
    public price: string,
    public current_quantity: string,
    public current_price: string,
    public bidTime: string,
    public user: UserDto,
    public metadata: string,
    public removedOn: Date
    ) {}
  }
