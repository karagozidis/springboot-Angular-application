export const AppConstants = {
  //Prod
  // MODE:"prod" ,
  // CRM_CENTRAL_URL: 'https://onCloudApp.eurodyn.com/api',
  // CRM_LOCAL_URL: 'https://localhost:30000/api',

  // //Dev
  MODE : 'dev' ,
  CRM_CENTRAL_URL: 'http://localhost:20000/api',
  // CRM_CENTRAL_URL: 'http://kv-dev.eurodyn.com:20000/api',
  CRM_LOCAL_URL: 'http://localhost:30000/api',


  //Common Urls
  CRM_LOCAL_ALIVE_RESPONSE: 'http://localhost:30000/api/modules/hyppo-io/alive',

  // The claims available in JWT.
  jwt: {
    claims: {
      USERNAME: 'sub'
    }
  },
  ModeTypes:{
    PROD:'prod',
    DEV:'dev'
  },

  StorageLabels: {
    USERNAME: 'username',
    PASSWORD: 'password',
    REMEMBER_ME: 'remember_me',
    // User: 'USER',
    JWT_STORAGE_NAME: 'jwt_token',
    SCHEDULED_SERVICES: '[]',
    INITIAL_ENERGY:'eint',
    FINAL_ENERGY: 'eter',
    MINIMUM_ENERGY:'emax',
    MAXIMUM_ENERGY: 'emin',
    MAXIMUM_POWER_DISCHARGE:'pdismax',
    MINIMUM_POWER_DISCHARGE:'pdismin',
    MAXIMUM_POWER_CHARGE:'pchmax',
    MINIMUM_POWER_CHARGE:'pchmin',
    CHARGE_EFFICIENCY: 'etach',
    DISCHARGE_EFFICIENCY: 'etadis'

  },
  SessionInfo:{
    username:'',
    password: '',
  },

  Market: {
    Settings: {
      UPDATE_INTERVAL: 1 * 60 * 1000
    },
    BackendEnums: {
      MarketScenarios:{
        ONE_HOUR_PRODUCT:'MINUTES60_PRODUCTS',
        FIFTEEN_MINUTES_PRODUCT: 'MINUTES15_PRODUCTS'
      },
      Product: {
        ONE_HOUR_PERIOD: 'MINUTES60',
        FIFTEEN_MINUTES_PERIOD: 'MINUTES15'

      },
      BasketType: {
        EXCLUSIVE: 'EXCLUSIVE',
        VOLUME_CONSTRAINED: 'VOLUME_CONSTRAINED',
        CUMULATIVE_VOLUME_CONSTRAINED: 'CUMULATIVE_VOLUME_CONSTRAINED',
        LINKED: 'LINKED',
        NONE: 'NONE',
      },
      Parameters:{
        IcebergParameters:{
          MAXIMUM_ICEBERG_QUANTITY: 'Maximum Iceberg Quantity(MW)',
          PRICE_CHANGE: 'Price Change(€/MWh)'
        },
        ExclusiveParameters:{
          MAX_MATCHED_ORDERS: 'Max Matched Orders'
        },
        VolumeConstrainedParameters:{
          TOTAL_QUANTITY:'Total Quantity(MW)'
        },
        CumulativeVolumeConstrainedParameters:{
          MAXIMUM_CAPACITY:'Maximum Capacity',
          INITIAL_CHARGE:'Initial Charge',
          RATED_POWER: 'Rated Power'
        }

      },

      OrderDirection: {
        BUY: 'BUY',
        SELL: 'SELL'
      },

      OrderType: {
        LIMIT: 'LIMIT',
        ICEBERG: 'ICEBERG',
        IOC: 'IMMEDIATE_OR_CANCEL',
        FOK: 'FILL_OR_KILL',
        AON: 'ALL_OR_NONE',
        BASKET: 'BASKET',
      },

      OrderStatus: {
        ACTIVE: 'ACTIVE',
        DEACTIVATED: 'DEACTIVATED',
        REMOVED: 'REMOVED',
        EXPIRED: 'EXPIRED',
        MATCHED: 'MATCHED',
        PARTLY_MATCHED: 'PARTLY_MATCHED'
      }
    },

    UILabels: {
      Product: {
        ONE_HOUR_PERIOD: '1 Hour',
        FIFTEEN_MINUTES_PERIOD: '15 minutes'

            },
            ProcessMode:{
                ADD_TO_BASKET: 'addToBasket',
                UPDATE_BASKET_FROM_ORDERS_ARRAY:'updateBasketFromOrdersArray',
            },
            BasketType: {
                EXCLUSIVE: 'Exclusive',
                VOLUME_CONSTRAINED: 'VC',
                VOLUME_CONSTRAINED_FULL_WORD: 'Volume Constrained',
                CUMULATIVE_VOLUME_CONSTRAINED: 'CVC',
                CUMULATIVE_VOLUME_CONSTRAINED_FULL_WORD: 'Cumulative Volume Constrained',
                LINKED: 'Linked Group',
                NONE: 'None',
            },

      OrderDirection: {
        BUY: 'Buy',
        SELL: 'Sell'
      },

      OrderType: {
        LIMIT: 'Limit',
        ICEBERG: 'Iceberg',
        IOC: 'IoC',
        IOC_FULL_WORD: 'Immediate Or Cancel',
        FOK: 'FoK',
        FOK_FULL_WORD: 'Fill Or Kill',
        AON: 'AON',
        AON_FULL_WORD: 'AON',
        BASKET: 'Basket',
      },

      OrderStatus: {
        ACTIVE: 'Active',
        DEACTIVATED: 'Deactivated',
        REMOVED: 'Removed',
        EXPIRED: 'Expired',
        MATCHED: 'Matched',
        PARTLY_MATCHED: 'Partly Matched',
        QUEUED: 'Queued'
      },

      WHITESPACE_CHARACTER: '\xa0',
    },

    Tables: {
      OrdersAndTrades: {
        State: {
          ORDERS: 'my_orders',
          BASKET: 'basket_orders',
          TRADES: 'my_trades',
          ALL_TRADES: 'all_trades'
        },
        MyOrdersHeaders: ['orderIdMyOrdersTable',  'productNameMyOrdersTable', 'orderStatusMyOrdersTable',
        'orderDirectionMyOrdersTable','priceMyOrdersTable','quantityMyOrdersTable','deliveryTimeStartMyOrdersTable',
        'periodMyOrdersTable', 'fullDateMyOrdersTable',
        'orderTypeMyOrdersTable', 'basketTypeMyOrdersTable'],

        MyTradesHeaders: ['tradeIdMyTradesTable','orderIdMyTradesTable','productMyTradesTable','orderDirectionMyTradesTable','priceMyTradesTable','quantityMyTradesTable',
      'buyerMyTradesTable','sellerMyTradesTable','fullDateMyTradesTable','orderTypeMyTradesTable','basketTypeMyTradesTable'],

        AllTradesHeaders: ['productAllTradesTable','quantityAllTradesTable','priceAllTradesTable','buyerAllTradesTable','sellerAllTradesTable','fullDateAllTradesTable','buyerOrderTypeAllTradesTable','sellerOrderTypeAllTradesTable'],

        WeatherHeaders: ['dateWeatherTable','timeWeatherTable','temperatureWeatherTable','sunRadiationWeatherTable','windSpeedWeatherTable','skyInfoWeatherTable'],
        Threshold: 500

      },

    },

    Descriptions: {
      OrderType: {
        LIMIT: 'Has no additional behavior.',
        ICEBERG: 'The full size of an iceberg order is not visible in the order book. Instead, it will be displayed as an order with a smaller size, called the peak size. If after matching, the shown size would reach zero, it is reset to the peak size (as long as the real size has not reached zero as well) and a price delta is added to the limit price.',
        IOC: 'When added, these orders are matched with existing orders, but they are immediately deactivated afterwards, even if their quantity has not reached zero. They are never visible in the order book.',
        FOK: 'These orders can be matched against multiple orders at the same time, but if their quantity does not reach zero after matching, no trades are generated. I.e. they can only be fully matched at once or not at all. Orders of this type are never visible in the order book, i.e. if one cannot be fully matched when  it is entered, it is deleted.',
        AON: 'Similarly to fill or kill orders, these can only be fully matched at once or not at all, but all or none orders can only be matched against one order. In other words, they will only be matched successfully with a single order with an equal or greater size. There are no restrictions on the type of orders that they can be matched with. Orders of this type are added to the order book.'

      },
      BasketType: {
        EXCLUSIVE: 'Several of these orders can be placed in a basket. These act like an all or none order, but only a limited number of them can be matched. This limit is the property of the basket, and after it is reached, the remaining orders in the basket are deactivated.',
        VOLUME_CONSTRAINED: 'Several of these orders can be placed in a basket. The total matchable quantity across all orders in a basket can be limited.',
        CUMULATIVE_VOLUME_CONSTRAINED: 'Several of these orders can be placed in a basket. The basket as a whole is designed to model a battery. For each order, the battery’s charge level at the delivery period is calculated, and the order’s quantity is constrained so that the charge level will always stay between zero and the maximum capacity. This is recalculated after every trade involving the basket.',
        LINKED: 'Several of these orders can be placed in a basket. All of them must be matched at the same time or no trades are generated.'

      },
      Parameters:{
        ICEBERG_MAXIMUM_QUANTITY:'The maximum quantity of the iceberg.',
        ICEBERG_PRICE_CHANGE:'The price change per iceberg quantity chunk.',
        EXCLUSIVE_MAX_MATCHED_ORDERS:'The maximum number of orders that can be matched before the basket is removed.',
        VOLUME_CONSTRAINED_TOTAL_QUANTITY:'The maximum quantity across all orders that can be matched before the basket is removed.',
        CUMULATIVE_VOLUME_CONSTRAINED_MAXIMUM_QUANTITY:'The maximum capacity of the basket.',
        CUMULATIVE_VOLUME_CONSTRAINED_INITIAL_CHARGE:'The initial charge of the basket.',
        CUMULATIVE_VOLUME_CONSTRAINED_RATED_POWER:'The rated power of the basket.',
      }
    }

  },

  ActiveSubstation:{
     Headers:{
       CLIENT_INTERFACE: "2",
     },
     Commands:{
       CLIENT_ENQUIRY: true,
       SERVER_ACKNOWLEDGE: true,
     },
     BackendEnums:{
       STATUS:{
         EXECUTED:'EXECUTED',
         NOT_EXECUTED:'NOT_EXECUTED',
         ON_EXECUTION: 'ON_EXECUTION',
         OFFER_SENT: 'OFFER_SENT'
       },
       LOCATION:{
         CRM_LOCAL:'CRMLOCAL',
         CRM_CENTRAL:'ONCRMCENTRAL',
         SYNC: 'SYNC'
       }
     },
     UILabels:{
      STATUS:{
        EXECUTED:'Executed',
        NOT_EXECUTED:'Not Executed',
        ON_EXECUTION:'On Execution',
        OFFER_SENT: 'Offer Sent'
      },
      LOCATION:{
        CRM_LOCAL:'Crm Local',
        CRM_CENTRAL:'On Crm Central',
        SYNC: 'Sync'
      }
     }
  }
}

export enum HyppoIOExecutionStatus {
  EXECUTED = 'Executed',
  RUNNING = 'Running',
  FAILED = 'Failed'
}

export enum AppType {
  CRM = 'CRM',
  MARKET = 'Market'
}
export enum MarketCountryType{
  DEFAULT_MARKET_CYPRUS = 'CY',
  DEFAULT_MARKET_BULGARIA = 'BG',
  COUNTRY_CYPRUS = 'Cyprus',
  COUNTRY_BULGARIA = 'Bulgaria'

}

export enum UpdateSignals {
    USER_DTO = "user_dto_updated",
    CRM_LOCAL_ACTIVE_STATE = "crm_local_active_state_updated",
    APP_TYPE = "app_type_updated",
    RESPONSIVE_BUTTON_STATE = "responsive_button_updated",
    NOTIFICATION_FOUND = "notification_found",
    ORDERS_AND_TRADES_TABLE_STATE = "orders_and_trades_selection_updated",
    UPDATE_MARKET_TABLES = "update_market_tables",
    SHOW_LOADING_INDICATION = "show_loading_indication",
    HIDE_LOADING_INDICATION = "hide_loading_indication",
    /* SHOW_HIDE_SIDEMENU = "Show/Hide Menu",
    SHOW_SIDEMENU = "Show Menu",
    HIDE_SIDEMENU = "Hide Menu", */
    SIDEBAR_VISIBILITY_STATE = 'sidebar_visibility_updated',
    DELETE_MARKET_DATA_BY_FILE_ID = 'delete_market_data_by_file_id'
}

export enum UserAccess {
  CRM = 'crm',
  MARKET = 'market',
  CRM_AND_MARKET = 'market&crmUser',
}
