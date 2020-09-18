import { UserDto } from "./user-dto";

export class BaseInfoTradeDTO {
    public id: string
    public createdOn: string
    public productName: string
    public price: string
    public quantity: string
    public metadata: string
    public buyer: UserDto
    public seller: UserDto
}
