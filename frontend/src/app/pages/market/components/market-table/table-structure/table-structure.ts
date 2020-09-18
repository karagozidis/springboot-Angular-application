import { OrderDialogData } from "../../market-dialog/market-dialog.component";
import { AppConstants } from "app/shared/config/app-constants";
import { OrderDTO } from "app/shared/dto/order-dto";
import * as moment from 'moment'
import { ProductDTO } from "app/shared/dto/product-dto";
import { Auxiliary } from "app/shared/utils/auxiliary";
import { TradeDTO } from "app/shared/dto/trade-dto";
import { BaseInfoTradeDTO } from "app/shared/dto/base-info-trade-dto";
import { WeatherDTO } from "app/shared/dto/weather-dto";
import { AppDataService } from "app/services/app-data.service";
import { OrderMetadataDTO } from "app/shared/dto/orderMetadataDTO";
import { UserDto } from "app/shared/dto/user-dto";


export class ProductsTableData {
    nameProductsTable: string;
    deliveryTimeStartProductsTable: string;
    deliveryTimeEndProductsTable: string;
    closesAtProductsTable: string;
    periodProductsTable: string;
    dateProductsTable: string;
    buyQuantityProductsTable: string;
    buyPriceProductsTable: string;
    sellQuantityProductsTable: string;
    sellPriceProductsTable: string;

    static createFromProductDTO (productDTO: ProductDTO){
        const productData = new ProductsTableData();
        productData.nameProductsTable = productDTO.name;

        productData.closesAtProductsTable = productDTO.closesAt;
        productData.deliveryTimeStartProductsTable = productDTO.deliveryTimeStart
        // let today = new Date();
        // let createdOnDate = new Date(productDTO.deliveryTimeStart);
        //
        // if(createdOnDate.getDay() === today.getDay()){
        //     productData['dateProductsTable'] = 'Today'
        // }
        // else{
        //     productData['dateProductsTable'] = 'Tomorrow'
        // }
        productData['dateProductsTable'] = Auxiliary.getTodayTomorrowLabel(productDTO.deliveryTimeStart)
        //var timezone = (-1 * new Date(currentTableDataObject.closesAt ).getTimezoneOffset()) /60;
        // tslint:disable-next-line:prefer-const

        const closesAtDate = new Date( productData.closesAtProductsTable);

        // tslint:disable-next-line:prefer-const
        const closesAtDateHours = closesAtDate.getHours();
        // tslint:disable-next-line:prefer-const
        let deliveryTimeStartMinutes;
        let closesAtDateMinutes;
        productData.closesAtProductsTable = moment(new Date(productDTO.closesAt)).format('HH:mm')
        // if(closesAtDate.getMinutes() < 10) {
        //   deliveryTimeStartMinutes = '0' + closesAtDate.getMinutes().toString()
        //   closesAtDateMinutes = '0' + closesAtDate.getMinutes().toString()
        // }
        // else{

        //   deliveryTimeStartMinutes = closesAtDate.getMinutes().toString()
        //   closesAtDateMinutes = closesAtDate.getMinutes().toString()
        // }
        // productData.closesAt = closesAtDateHours.toString() +':' + closesAtDateMinutes.toString();

        productData.deliveryTimeStartProductsTable = moment(new Date(productDTO.closesAt)).add(1,'h').format('HH:mm')

        if(productDTO['period'] === 'MINUTES60'){
            productData.periodProductsTable = '1 Hour';
        }
        else {
            productData.periodProductsTable = '15 minutes';
        }

        if (productDTO['latestBuyOrder'] === null) {
            productData.buyQuantityProductsTable = '\xa0';
            productData.buyPriceProductsTable = '\xa0';
        } else {
            productData.buyQuantityProductsTable =
            productDTO['latestBuyOrder']['current_quantity'];
            productData.buyPriceProductsTable = productDTO['latestBuyOrder']['current_price'];
        }

        if (productDTO['latestSellOrder'] === null) {
            productData.sellQuantityProductsTable = '\xa0';
            productData.sellPriceProductsTable = '\xa0';
        } else {
            productData.sellQuantityProductsTable =
            productDTO['latestSellOrder']['current_quantity'];
            productData.sellPriceProductsTable =
            productDTO['latestSellOrder']['current_price'];
        }

        return productData;
    }
}

export class OrdersByProduct {
    buyQuantityOrdersByProduct: string;
    buyPriceOrdersByProduct: string;
    sellQuantityOrdersByProduct: string;
    sellPriceOrdersByProduct: string;

    static createFromOrderDTO(orderByProductDTO:OrderDTO): OrdersByProduct {
        const orderByProductData = new OrdersByProduct();
        if(orderByProductDTO['orderDirection'] === AppConstants.Market.BackendEnums.OrderDirection.BUY){
            orderByProductData.buyQuantityOrdersByProduct = orderByProductDTO['current_quantity']
            orderByProductData.buyPriceOrdersByProduct = orderByProductDTO['current_price']
            orderByProductData.sellQuantityOrdersByProduct  = AppConstants.Market.UILabels.WHITESPACE_CHARACTER;
            orderByProductData.sellPriceOrdersByProduct  = AppConstants.Market.UILabels.WHITESPACE_CHARACTER;
        }
        else{
            orderByProductData.sellQuantityOrdersByProduct = orderByProductDTO['current_quantity']
            orderByProductData.sellPriceOrdersByProduct = orderByProductDTO['current_price']
            orderByProductData.buyQuantityOrdersByProduct = AppConstants.Market.UILabels.WHITESPACE_CHARACTER;
            orderByProductData.buyPriceOrdersByProduct = AppConstants.Market.UILabels.WHITESPACE_CHARACTER;
        }

        return orderByProductData;
    }
}

export class MyOrdersTableData {
    orderIdMyOrdersTable: string;
    productNameMyOrdersTable: string;
    orderStatusMyOrdersTable: string;
    orderDirectionMyOrdersTable: string;
    quantityMyOrdersTable: string;
    priceMyOrdersTable: string;
    initialQuantityMyOrdersTable: string;
    initialPriceMyOrdersTable: string;
    deliveryTimeStartMyOrdersTable: string;
    periodMyOrdersTable: string;
    dateMyOrdersTable: string;
    orderTypeMyOrdersTable: string;
    basketTypeMyOrdersTable: string;
    fullDateMyOrdersTable: string;
    fullOrderTypeMyOrdersTable: string;
    fullBasketTypeMyOrdersTable: string;
    icebergMaximumQuantityParameter: string;
    icebergPriceChangeParameter: string;
    excluvsiveMaxMatchedOrdersParameter: string;
    volumeConstrainedTotalQuantityParameter: string;
    cumulativeVolumeConstrainedMaximumCapacityParameter: string;
    cumulativeVolumeConstrainedInitialChargeParameter: string;
    cumulativeVolumeConstrainedRatedPowerParameter: string;
    orderDtoMyOrdersTable: OrderDTO
    

    static createFromOrderDTO (orderDTO: OrderDTO){
        
        const orderData = new MyOrdersTableData();
        let orderMetadataObject= new OrderMetadataDTO();
        orderData.orderIdMyOrdersTable = orderDTO.id
        orderData.orderTypeMyOrdersTable = Auxiliary.getOrderTypeLabel(orderDTO.orderType)
        orderData.quantityMyOrdersTable = orderDTO.current_quantity
        orderData.priceMyOrdersTable = orderDTO.current_price
        orderData.initialQuantityMyOrdersTable = orderDTO.quantity
        orderData.initialPriceMyOrdersTable =  orderDTO.price
        orderData.orderStatusMyOrdersTable = Auxiliary.getStatusLabel(orderDTO.orderStatus)
        orderData.orderDirectionMyOrdersTable = Auxiliary.getOrderDirectionLabel(orderDTO.orderDirection)

        if(orderDTO.hasOwnProperty('basket') === false){
            orderData.orderIdMyOrdersTable = '-'
            orderData.productNameMyOrdersTable = orderDTO['productName']
            orderData.basketTypeMyOrdersTable = Auxiliary.getBasketTypeLabel(orderDTO['basket']['basketType'])
            // orderData.dateMyOrdersTable = moment(new Date(orderDTO['deliveryTimeStart'])).format('HH:mm, DD/MM/YYYY')
            // orderData.dateMyOrdersTable = orderDTO.date
            // orderData.deliveryTimeStartMyOrdersTable = orderDTO['deliveryTimeStart']
            orderData.deliveryTimeStartMyOrdersTable = moment(new Date(orderDTO['deliveryTimeStart'])).format('HH:mm  DD/MM/YYYY')
            orderData.dateMyOrdersTable = 'now'

            // const deliveryStartDate = new Date(orderData.deliveryTimeStartMyOrdersTable)
            //  let deliveryStartDateMinutes = deliveryStartDate.getMinutes().toString();
            //    if(deliveryStartDate.getMinutes() < 10){
            //       deliveryStartDateMinutes = '0' + deliveryStartDate.getMinutes().toString()
            //     }
            //     else{
            //      deliveryStartDateMinutes =  deliveryStartDate.getMinutes().toString()
            //     }
            //  orderData.deliveryTimeStartMyOrdersTable = deliveryStartDate.getHours().toString() + ':' + deliveryStartDateMinutes;
            orderData.periodMyOrdersTable = orderDTO['period']
            // if(orderDTO['period'] === 'MINUTES60'){
            //   orderData.periodMyOrdersTable = '1 Hour';
            // }
            //  else {
            //     orderData.periodMyOrdersTable = '15 minutes';
            //  }

        }else{
            orderData.productNameMyOrdersTable = orderDTO['product']['name'];

            orderData.dateMyOrdersTable = Auxiliary.getDateLabel(orderDTO['createdOn'])
            orderData.deliveryTimeStartMyOrdersTable = moment(new Date(orderDTO['product']['deliveryTimeStart'])).format('HH:mm  DD/MM/YYYY')
            if(orderDTO['product']['period'] === 'MINUTES60'){
                orderData.periodMyOrdersTable = '1 Hour';
            }
            else {
                orderData.periodMyOrdersTable = '15 minutes';
            }

            if (orderDTO['basket']===null){
                orderData.basketTypeMyOrdersTable = AppConstants.Market.UILabels.BasketType.NONE
                orderData.fullBasketTypeMyOrdersTable = AppConstants.Market.UILabels.BasketType.NONE

                if(orderData.orderTypeMyOrdersTable === AppConstants.Market.UILabels.OrderType.ICEBERG ){
                    orderMetadataObject = JSON.parse(orderDTO['metadata'])
                    orderData.icebergMaximumQuantityParameter = orderMetadataObject['icebergQuantity']
                    orderData.icebergPriceChangeParameter = orderMetadataObject['icebergPriceDelta']
                }
            }
            else {
                orderData.basketTypeMyOrdersTable = Auxiliary.getBasketTypeLabel(orderDTO['basket']['basketType'])
                orderMetadataObject = JSON.parse(orderDTO['basket']['metadata'])
                if(orderData.basketTypeMyOrdersTable === AppConstants.Market.UILabels.BasketType.EXCLUSIVE){
                    orderData.excluvsiveMaxMatchedOrdersParameter = orderMetadataObject['maxMatchedOrders']
                }
                else if(orderData.basketTypeMyOrdersTable === AppConstants.Market.UILabels.BasketType.VOLUME_CONSTRAINED){
                    orderData.volumeConstrainedTotalQuantityParameter = orderMetadataObject['totalQuantity']
                }
                else if(orderData.basketTypeMyOrdersTable === AppConstants.Market.UILabels.BasketType.CUMULATIVE_VOLUME_CONSTRAINED){
                    orderData.cumulativeVolumeConstrainedMaximumCapacityParameter = orderMetadataObject['maxCapacity']
                    orderData.cumulativeVolumeConstrainedInitialChargeParameter = orderMetadataObject['initialCharge']
                    orderData.cumulativeVolumeConstrainedRatedPowerParameter= orderMetadataObject['ratedPower']
                }
                // orderData.basketTypeMyOrdersTable = orderDTO['basket']['basketType']
            }
            orderData.fullDateMyOrdersTable = moment(new Date(orderDTO['createdOn'])).format('HH:mm  DD/MM/YYYY')
            orderData.fullOrderTypeMyOrdersTable = Auxiliary.getOrderTypeLabel(orderDTO.orderType)
        }
        orderData.orderDtoMyOrdersTable = orderDTO
      
       

        return orderData;
    }

    static createFromTempBasketDTO(tempBasketDTO : OrderDTO, processMode: String){
        const orderData = new MyOrdersTableData();
        orderData.orderIdMyOrdersTable = '-'
        orderData.orderStatusMyOrdersTable = 'Queued'
        orderData.orderDirectionMyOrdersTable = Auxiliary.getOrderDirectionLabel(tempBasketDTO['orderDirection'])
        orderData.priceMyOrdersTable = tempBasketDTO['price']
        orderData.quantityMyOrdersTable = tempBasketDTO['quantity']
        orderData.orderTypeMyOrdersTable = Auxiliary.getOrderTypeLabel(tempBasketDTO['orderType'])
        orderData.dateMyOrdersTable = 'now'

        orderData.productNameMyOrdersTable = tempBasketDTO['product']['name']
        orderData.deliveryTimeStartMyOrdersTable = moment(new Date(tempBasketDTO['product']['deliveryTimeStart'])).format('HH:mm  DD/MM/YYYY')
        if( tempBasketDTO['product']['period'] === AppConstants.Market.BackendEnums.Product.ONE_HOUR_PERIOD){
            orderData.periodMyOrdersTable = AppConstants.Market.UILabels.Product.ONE_HOUR_PERIOD
        }
        else{
            orderData.periodMyOrdersTable = AppConstants.Market.UILabels.Product.FIFTEEN_MINUTES_PERIOD
        }

        orderData.basketTypeMyOrdersTable = Auxiliary.getBasketTypeLabel(tempBasketDTO['basket']['basketType'])

        // else{
        //     orderData.productNameMyOrdersTable = tempBasketDTO['productName']
        //     orderData.deliveryTimeStartMyOrdersTable = moment(new Date(tempBasketDTO['deliveryTimeStart'])).format('HH:mm, DD/MM/YYYY')
        //     orderData.periodMyOrdersTable = tempBasketDTO['period']
        //     orderData.basketTypeMyOrdersTable = Auxiliary.getBasketTypeLabel(tempBasketDTO['basketType'])
        // }
        

        return orderData;
    }


}

export class MyTradesTableData {
    tradeIdMyTradesTable: string;
    orderIdMyTradesTable: string;
    productMyTradesTable: string;
    priceMyTradesTable: string;
    quantityMyTradesTable: string;
    orderDirectionMyTradesTable: string;
    buyerMyTradesTable: string;
    sellerMyTradesTable: string;
    dateMyTradesTable: string;
    fullDateMyTradesTable: string;
    orderTypeMyTradesTable: string;
    basketTypeMyTradesTable: string;
    tradesDtoMyTradesTable : TradeDTO;
    

    static createFromTradesDTO (myTradesDTO: TradeDTO, userDto: UserDto) {
        const myTradesData = new MyTradesTableData();
        myTradesData.orderIdMyTradesTable = myTradesDTO['singleOrder1'].id
        myTradesData.orderDirectionMyTradesTable = Auxiliary.getOrderDirectionLabel(myTradesDTO['singleOrder1'].orderDirection)
        myTradesData.orderTypeMyTradesTable = Auxiliary.getOrderTypeLabel(myTradesDTO['singleOrder1'].orderType)
        myTradesData.tradeIdMyTradesTable = myTradesDTO.id
        myTradesData.productMyTradesTable = myTradesDTO['singleOrder1']['product']['name']
        // myTradesData.orderIdMyTradesTable = myTradesDTO['singleOrder1'].id
        myTradesData.priceMyTradesTable = myTradesDTO.price
        myTradesData.quantityMyTradesTable = myTradesDTO.quantity;

        if(userDto.rolesMarket === 'admin'){ 
            myTradesData.buyerMyTradesTable = myTradesDTO['buyer']['username'];
            myTradesData.sellerMyTradesTable = myTradesDTO['seller']['username'];   
        }
        else{
            if(myTradesData.orderDirectionMyTradesTable === AppConstants.Market.UILabels.OrderDirection.BUY){
                myTradesData.buyerMyTradesTable = myTradesDTO['buyer']['username'];
                myTradesData.sellerMyTradesTable = myTradesDTO['seller']['country']['name'];
             }
             else{
                myTradesData.buyerMyTradesTable = myTradesDTO['buyer']['country']['name'];
                myTradesData.sellerMyTradesTable = myTradesDTO['seller']['username']
             }
        }

       
        
        myTradesData.dateMyTradesTable = myTradesDTO.createdOn
        myTradesData.dateMyTradesTable = Auxiliary.getDateLabel(myTradesDTO['createdOn'])
        myTradesData.fullDateMyTradesTable = moment(new Date(myTradesDTO['createdOn'])).format('HH:mm  DD/MM/YYYY')

        if (myTradesDTO['singleOrder1']['basket'] === null) {
          myTradesData.basketTypeMyTradesTable = AppConstants.Market.UILabels.BasketType.NONE
       } else {
          myTradesData.basketTypeMyTradesTable = Auxiliary.getBasketTypeLabel(myTradesDTO['singleOrder1']['basket']['basketType'])
        }
        myTradesData.tradesDtoMyTradesTable = myTradesDTO
       

        return myTradesData;
    }
}

export class AllTradesTableData {
    productAllTradesTable: string;
    priceAllTradesTable: string;
    quantityAllTradesTable: string;
    buyerAllTradesTable: string;
    sellerAllTradesTable: string;
    dateAllTradesTable: string;
    fullDateAllTradesTable: string;
    buyerOrderTypeAllTradesTable: string;
    sellerOrderTypeAllTradesTable: string;
    allTradesDtoAllTradesTable: BaseInfoTradeDTO

    static createFromBaseInfoTradeDTO (baseInfoTradeDTO: BaseInfoTradeDTO, userDto: UserDto) {
        console.log(userDto.rolesMarket)
        const allTradesData = new AllTradesTableData();
        allTradesData.productAllTradesTable = baseInfoTradeDTO.productName
        // allTradesData.orderIdAllTradesTable = allTradesDTO['singleOrder1'].id
        allTradesData.priceAllTradesTable = baseInfoTradeDTO.price
        allTradesData.quantityAllTradesTable = baseInfoTradeDTO.quantity
        allTradesData.dateAllTradesTable = Auxiliary.getDateLabel(baseInfoTradeDTO['createdOn'])
        allTradesData.fullDateAllTradesTable = moment(new Date(baseInfoTradeDTO['createdOn'])).format('HH:mm  DD/MM/YYYY')
        if(userDto.rolesMarket === 'admin'){
            if(baseInfoTradeDTO['singleOrder1']['orderDirection'] === AppConstants.Market.BackendEnums.OrderDirection.BUY){
                allTradesData.buyerOrderTypeAllTradesTable = Auxiliary.getOrderTypeLabel(baseInfoTradeDTO['singleOrder1']['orderType'])
                allTradesData.sellerOrderTypeAllTradesTable = Auxiliary.getOrderTypeLabel(baseInfoTradeDTO['singleOrder2']['orderType'])
            }
            else{
                allTradesData.buyerOrderTypeAllTradesTable = Auxiliary.getOrderTypeLabel(baseInfoTradeDTO['singleOrder2']['orderType'])
                allTradesData.sellerOrderTypeAllTradesTable = Auxiliary.getOrderTypeLabel(baseInfoTradeDTO['singleOrder1']['orderType'])
            }
            allTradesData.buyerAllTradesTable = baseInfoTradeDTO.buyer.username
            allTradesData.sellerAllTradesTable = baseInfoTradeDTO.seller.username
            
           
        }
        else{
            allTradesData.buyerAllTradesTable = baseInfoTradeDTO.buyer.country.name
            allTradesData.sellerAllTradesTable = baseInfoTradeDTO.seller.country.name
        }
        /* allTradesData.buyerOrderTypeAllTradesTable = 'Limit'
        allTradesData.sellerOrderTypeAllTradesTable = 'Iceberg'; */

        allTradesData.allTradesDtoAllTradesTable = baseInfoTradeDTO

        return allTradesData;
    }
}

export class WeatherTableData {
    temperatureWeatherTable: string
    sunRadiationWeatherTable: string
    windSpeedWeatherTable: string
    skyInfoWeatherTable: string
    dateWeatherTable: string
    timeWeatherTable: string
    typeWeatherTable: string
    weatherDtoWeatherTable: WeatherDTO

    static createFromWeatherDTO (weatherDto: WeatherDTO) {
        const weatherData = new WeatherTableData();
        weatherData.temperatureWeatherTable = weatherDto.temperature
        weatherData.sunRadiationWeatherTable = weatherDto.radiation
        weatherData.windSpeedWeatherTable = weatherDto.windSpeed
        weatherData.skyInfoWeatherTable = weatherDto.skyCode

        weatherData.dateWeatherTable = Auxiliary.getTodayTomorrowLabel(weatherDto.weatherDateTime)
        weatherData.timeWeatherTable = moment(new Date(weatherDto.weatherDateTime)).format('HH:mm')
        weatherData.typeWeatherTable = weatherDto.type
        weatherData.weatherDtoWeatherTable = weatherDto;

        return weatherData;
    }
}

export const productsTableColumns: any[] = [
    { field: 'nameProductsTable', width: 20, table: 1, name: 'Product' },
    { field: 'closesAtProductsTable', width: 20, table: 1, name: 'Closes At' },
    { field: 'deliveryTimeStartProductsTable', width: 30,table: 1,name: 'Delivery Start' },
    { field: 'periodProductsTable', width: 20, table: 1, name: 'Delivery Period' },
    { field: 'dateProductsTable', width: 20, table: 1, name: 'Date' },
    { field: 'buyQuantityProductsTable', width: 20, table: 1, name: 'Buy Qty' },
    { field: 'buyPriceProductsTable', width: 20, table: 1, name: 'Buy Price' },
    { field: 'sellPriceProductsTable', width: 20, table: 1, name: 'Sell Price' },
    { field: 'sellQuantityProductsTable', width: 20, table: 1, name: 'Sell Qty' }
];

export const ordersByProductTableColumns: any[] = [
    { field: 'buyQuantityOrdersByProduct', width: 20, table: 2, name: 'Buy Qty' },
    { field: 'buyPriceOrdersByProduct', width: 20, table: 2, name: 'Buy Price' },
    { field: 'sellPriceOrdersByProduct', width: 20, table: 2, name: 'Sell Price' },
    { field: 'sellQuantityOrdersByProduct', width: 30,table: 2,name: 'Sell Qty'}
];


export const myOrdersTableColumns: any[] = [
    { field: 'orderIdMyOrdersTable', width: 5, table: 3, name: 'Order Id' },
    { field: 'productNameMyOrdersTable', width: 5, table: 3, name: 'Product' },
    { field: 'orderStatusMyOrdersTable', width: 2, table: 3, name: 'Status' },
    { field: 'orderDirectionMyOrdersTable', width: 2, table: 3, name: 'Dir' },
    { field: 'priceMyOrdersTable', width: 2, table: 3, name: 'Price' },
    { field: 'quantityMyOrdersTable', width: 2, table: 3, name: 'Qty' },
    { field: 'deliveryTimeStartMyOrdersTable', width: 2, table: 3, name: 'Delivery Start' },
    { field: 'periodMyOrdersTable', width: 2, table: 3, name: 'Delivery Period' },
    { field: 'dateMyOrdersTable', width: 2, table: 3, name: 'Date' },
    { field: 'orderTypeMyOrdersTable', width: 2, table: 3, name: 'Order' },
    { field: 'basketTypeMyOrdersTable', width: 10, table: 3, name: 'Basket' }
];

export const myTradesTableColumns: any[] = [
    { field: 'tradeIdMyTradesTable', width: 10, table: 3, name: 'Trade Id' },
    { field: 'orderIdMyTradesTable', width: 10, table: 3, name: 'Order Id' },
    { field: 'productMyTradesTable', width: 5, table: 3, name: 'Product' },
    { field: 'orderDirectionMyTradesTable', width: 10, table: 3, name: 'Dir' },
    { field: 'priceMyTradesTable', width: 10, table: 3, name: 'Price' },
    { field: 'quantityMyTradesTable', width: 10, table: 3, name: 'Qty' },
    { field: 'buyerMyTradesTable', width: 10, table: 3, name: 'Buyer' },
    { field: 'sellerMyTradesTable', width: 10, table: 3, name: 'Seller' },
    { field: 'dateMyTradesTable', width: 2, table: 3, name: 'Date' },
    { field: 'orderTypeMyTradesTable', width: 20, table: 3, name: 'Order' },
    { field: 'basketTypeMyTradesTable', width: 20, table: 3, name: 'Basket' }
];

export const allTradesTableColumns: any[] = [
    { field: 'productAllTradesTable', width: 5, table: 3, name: 'Product' },
    { field: 'quantityAllTradesTable', width: 10, table: 3, name: 'Qty' },
    { field: 'priceAllTradesTable', width: 10, table: 3, name: 'Price' },
    { field: 'buyerAllTradesTable', width: 10, table: 3, name: 'Buyer' },  
    { field: 'sellerAllTradesTable', width: 10, table: 3, name: 'Seller' },
    { field: 'dateAllTradesTable', width: 2, table: 3, name: 'Date' }
];

export const weatherTableColumns: any[] = [
    { field: 'dateWeatherTable', width: 10, table: 4, name: 'Date' },
    { field: 'timeWeatherTable', width: 10, table: 4, name: 'Time' },
    { field: 'temperatureWeatherTable', width: 5, table: 4, name: 'Temperature' },
    { field: 'sunRadiationWeatherTable', width: 10, table: 4, name: 'Sun Radiation' },
    { field: 'windSpeedWeatherTable', width: 10, table: 4, name: 'Wind Speed' },
    { field: 'skyInfoWeatherTable', width: 2, table: 4, name: 'Sky Info' }
];
