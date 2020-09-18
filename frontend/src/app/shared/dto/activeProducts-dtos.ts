import { ProductDTO } from "./product-dto";

  export class ActiveProductsDTO {
    constructor(
            public name: string,
            public createdOn: string,
            public deliveryTimeStart: string,
            public deliveryTimeEnd: string,
            public closesAt: string,
            public period: string,
            public productStatus: string,
            public orderDirection: string,
            public orderType: string,
            public quantity: string,
            public price: string,
            public bidTime: string

    ) {}
  }
