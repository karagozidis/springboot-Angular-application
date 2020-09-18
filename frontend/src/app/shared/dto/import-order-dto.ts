export class ImportOrderDTO {
    constructor(
        public id: string,
        public createdOn: string,
        public order_ID: string,
        public action: string,
        public product_name: string,
        public orderType: string,
        public quantity: string,
        public price: string,
        public orderDirection: string,
        public timeStamp: string,
        public order_meta: string,
        public basket_ID: string,
        public basket_Type: string,
        public basket_meta: string,
        public userId: string,
        public status: string,
        public messageInfoId: string
        ) {}
}
