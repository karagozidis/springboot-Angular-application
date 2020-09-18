import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  Inject,
  AfterViewInit,
  Renderer2,
  HostListener,
  Input,
  ChangeDetectorRef,
  OnDestroy
} from '@angular/core';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { FormControl, FormGroup } from '@angular/forms';
import { Observable, interval, Subscription } from 'rxjs';
import {
  MatAutocomplete,
  MatChipInputEvent,
  MatAutocompleteSelectedEvent,
  MAT_DIALOG_DATA,
  MatSort,
  MatPaginator,
  MatTableDataSource,
  MAT_AUTOCOMPLETE_SCROLL_STRATEGY,
  MatTable,
  MatMenuTrigger,
  MatDatepickerInputEvent,
  MatDialogRef,
  MatDialog,
  MAT_DATE_LOCALE,
  DateAdapter,
  MAT_DATE_FORMATS
} from '@angular/material';
import { startWith, map } from 'rxjs/operators';
import { OrderDTO } from 'app/shared/dto/order-dto';
import { MAT_SELECT_SCROLL_STRATEGY } from '@angular/material';
import { Overlay, BlockScrollStrategy } from '@angular/cdk/overlay';
import { Platform } from '@angular/cdk/platform';
import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';
import { ProductDTO } from 'app/shared/dto/product-dto';
import { MyOrdersService } from 'app/services/myOrders.service';
import { ActiveProductsService } from 'app/services/activeProducts.service';
import * as TableStructure from '../components/market-table/table-structure/table-structure';
import { MyTradesService } from 'app/services/myTrades.service';
import { OrderStateService } from 'app/services/orderState.service';
import { BasketDataSourceChangedService } from 'app/services/basketDataSourceChanged.service';
import { MarketConfirmationDialogComponent } from '../components/market-confirmation-dialog/market-confirmation-dialog.component';
import { DataSourceChangedService } from 'app/services/dataSourceChanged.service';
import { BasketDTO } from 'app/shared/dto/basket-dto';
import { BasketData } from '../components/market-dialog/market-dialog.component';
import { BasketService } from 'app/services/basket.service';
import { OrdersByProductService } from 'app/services/ordersByProduct.service';
import { AppConstants, UpdateSignals } from 'app/shared/config/app-constants';
import * as moment from 'moment'
import { MyOrdersTableData, MyTradesTableData, AllTradesTableData } from '../components/market-table/table-structure/table-structure';
import { MomentDateAdapter, MAT_MOMENT_DATE_ADAPTER_OPTIONS, MAT_MOMENT_DATE_FORMATS } from '@angular/material-moment-adapter';
import { InternalMessageService } from 'app/shared/utils/internal-message.service';
import { AppDataService } from 'app/services/app-data.service';
import { WeatherService } from 'app/services/weather.service';
import { DisplayDropDownOptionService } from 'app/services/displayCurrentDropdownOption.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';
import { saveAs } from 'file-saver';
import { NotificationToastService } from 'app/shared/utils/notification-toast-service';


export function scrollFactory(overlay: Overlay) {
  return () => overlay.scrollStrategies.block();
}

export const MY_FORMATS = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'LL',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

export interface MyOrdersData {
  products: string;
  orderStatus: string;
  orderDirection: string;
  orderType: string;
  quantity: string;
  price: string;
  bidTime: string;
}

enum BottomLeftTables {
      MY_ORDERS = 'MY_ORDERS_TABLE',
      MY_TRADES = 'MY_TRADES_TABLE',
      BASKET = 'BASKET_TABLE',
      ALL_TRADES = 'ALL_TRADES_TABLE'
}

@Component({
  selector: 'market-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss'],
  animations: [
    trigger('detailExpand', [
      state(
        'collapsed',
        style({ height: '0px', minHeight: '0', visibility: 'hidden' })
      ),
      state('expanded', style({ height: '*', visibility: 'visible' })),
      transition(
        'expanded <=> collapsed',
        animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')
      )
    ])
  ],
  providers: [
    {
      provide: MAT_AUTOCOMPLETE_SCROLL_STRATEGY,
      useFactory: scrollFactory,
      deps: [Overlay, Platform]
    },
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},

      {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ]
})
export class OrdersComponent implements OnInit, OnDestroy {
  productName: string[] = [];
  productObj: object;
  date;
  myTime;
  showDate = false;
  //dataSource = new MatTableDataSource<Transaction>(DATA);
  fromDate = ''
  toDate = ''

  datasourceProductsTable = new MatTableDataSource([]);
  datasourceOrdersByProductTable = new MatTableDataSource([]);
  datasourceMyOrdersTable = new MatTableDataSource([]);
  datasourceMyTradesTable = new MatTableDataSource([]);
  datasourceAllTrades = new MatTableDataSource([]);
  basketDataSource = new MatTableDataSource([])
  datasourceWeatherTable = new MatTableDataSource([]);
  dataSourceMyOrdersTableDisplayingData = new MatTableDataSource([]);
  dataSourceMyTradesTableDisplayingData = new MatTableDataSource([]);
  dataSourceAllTradesDisplayingData = new MatTableDataSource([]);
  

  //datasourceMyOrdersTable = new MatTableDataSource<Transaction3>(DATA3);
  selected = 'my_orders';
  filterForm = new FormGroup({
    fromDate: new FormControl(),
    toDate: new FormControl()
  });
  filterForm_ = new FormGroup({
    fromDate_: new FormControl(),
    toDate_: new FormControl()
  });
  epochFromTime = 1;
  epochToTime = new Date().getTime() + 24 * 60 * 60 * 1000;
  /* displayFromDate = new Date();
  displayToDate = new Date(new Date().getTime() + 48 * 60 * 60 * 1000) */
  displayFromDate = new Date(this.orderStateService.from)
  displayToDate = new Date(this.orderStateService.to)
  filteredValues = {
    Products: '',
    Status: '',
    Dir: '',
    Type: '',
    Qty: '',
    Price: '',
    Time: ''
  };
  myOrdersData: any = null;
  selectedDate: string;
  selectedMarketChoice: string;
  separatorKeysCodes: number[] = [ENTER, COMMA];
  productCtrl = new FormControl();
  filteredProducts: Observable<string[]>;
  productSelected: string = null;
  ordersList: string[] = [];
  pressed = false;
  clicked = false;
  globalFilter = '';
  renderedData = [];
  currentResizeIndex: number;
  startX: number;
  startWidth: number;
  isResizingRight: boolean;
  displayMaxNumberOfOrdersAndTrades: boolean
  scenario1TestArray = []
  orderTypes = AppConstants.Market.BackendEnums.OrderType;
  resizableMousemove: () => void;
  resizableMouseup: () => void;
  @ViewChild(MatSort, null) sort: MatSort;
  @ViewChild(MatPaginator, null) paginator: MatPaginator;
  intervalId

  @ViewChild('topTablesDiv', null) topTablesDivElement: ElementRef;

  @ViewChild('topLeftTable', null) topLeftTableElement: ElementRef;
  @ViewChild('topLeftHeader', null) topLeftHeaderElement: ElementRef;

  @ViewChild('topRightTable', null) topRightTableElement: ElementRef;
  @ViewChild('topRightHeader', null) topRightHeaderElement: ElementRef;

  @ViewChild('bottomTablesDiv', null) bottomTablesDivElement: ElementRef;

  @ViewChild('bottomLeftTable', null) bottomLeftTableElement: ElementRef;
  @ViewChild('bottomLeftHeader', null) bottomLeftHeaderElement: ElementRef;

  @ViewChild('bottomRightTable', null) bottomRightTableElement: ElementRef;
  @ViewChild('bottomRightHeader', null) bottomRightHeaderElement: ElementRef;

  viewRendered: Boolean = false;
  selectedBottomLeftTable = BottomLeftTables.MY_ORDERS
  BottomLeftTables = BottomLeftTables
  BasketMode = AppConstants.Market.UILabels.ProcessMode

  updateWrapTablesToAvailableHeight(){
    if (window.innerWidth < 767) {
      this.wrapTablesToAvailableHeight = false;

    } else {
      this.wrapTablesToAvailableHeight = true;
    }
    this.changeDetectorRefs.detectChanges();
  }

  ngAfterViewInit() {
    this.viewRendered = true;
    this.updateWrapTablesToAvailableHeight();
  }

  @ViewChild('productInput', { static: false }) productInput: ElementRef<
    HTMLInputElement
  >;
  @ViewChild('auto', { static: false }) matAutocomplete: MatAutocomplete;
  @ViewChild(MatMenuTrigger, null)
  selectedRowIndex = -1;
  completedOrder: any = {};

  contextMenu: MatMenuTrigger;
  productsTableColumns = TableStructure.productsTableColumns
  ordersByProductsColumns = TableStructure.ordersByProductTableColumns
  myOrdersTableColumns = TableStructure.myOrdersTableColumns
  myTradesTableColumns = TableStructure.myTradesTableColumns
  allTradesTableColumns = TableStructure.allTradesTableColumns
  weatherTableColumns = TableStructure.weatherTableColumns
  updatedBasketDataSource = this.orderStateService.getDataSourceToUpdate()

  columns2: any[] = [
    { field: 'Buy_Qty', width: 50, table: 2, name: 'Buy Qty' },
    { field: 'Buy_Price', width: 50, table: 2, name: 'Buy Price' },
    { field: 'Sell_Qty', width: 50, table: 2, name: 'Sell Qty' },
    { field: 'Sell_Price', width: 50, table: 2, name: 'Sell Price' }
  ];

  columns4: any[] = [
    { field: 'Date', width: 15, table: 4, name: 'Date' },
    { field: 'Time', width: 15, table: 4, name: 'Time' },
    { field: 'Sun_Radiation', width: 15, table: 4, name: 'Sun Radiation' },
    { field: 'Wind_Speed', width: 15, table: 4, name: 'Wind Speed' },
    { field: 'Temperature', width: 15, table: 4, name: 'Temperature' },
    { field: 'Sky_Code', width: 15, table: 4, name: 'Sky Code' },

  ];

  // tslint:disable-next-line:prefer-const


  topLeftVisibilityStatus: Boolean;
  topRightVisibilityStatus: Boolean;
  bottomLeftVisibilityStatus: Boolean;
  bottomRightVisibilityStatus: Boolean;
  wrapTablesToAvailableHeight: Boolean;

  fromDatePlaceHolder = 'From Date'
  toDatePlaceHolder = 'To Date'

  basketChangedEventHandleId: Subscription
  dataSourceChangedEventHandleId: Subscription
  updateEmmiterSubscriptionId: Subscription

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,

    private dialogRef: MatDialogRef<MarketConfirmationDialogComponent>,
    private dialog: MatDialog,
    private renderer: Renderer2,
    // tslint:disable-next-line:max-line-length
    public myOrdersService: MyOrdersService,
    public myTradesService: MyTradesService,
    public activeProductsService: ActiveProductsService,
    public ordersByProductService: OrdersByProductService,
    public orderStateService: OrderStateService,
    public basketService: BasketService,
    public basketDataSourceChangedService: BasketDataSourceChangedService,
    private basketDataSourceChanged: BasketDataSourceChangedService,
    public dataSourceChangedService: DataSourceChangedService,
    private changeDetectorRefs: ChangeDetectorRef,
    private messageServiceService: InternalMessageService,
    private appDataService: AppDataService,
    private weatherService: WeatherService,
    private notificationToastService: NotificationToastService
  ) {
    // this.filteredProducts = this.productCtrl.valueChanges.pipe(
    //   startWith(null),
    //   map((product: string | null) =>
    //     product ? this._filter(product) : this.allProducts.slice()
    //   )
    // );
    this.getCurrentOrderTypes();
    this.intervalId = setInterval(() => this.refreshAll(), AppConstants.Market.Settings.UPDATE_INTERVAL)
  }


  ngOnInit() {
    this.displayMaxNumberOfOrdersAndTrades = true;
    this.orderStateService.selectedArrayToView = this.selected;
    this.basketChangedEventHandleId = this.basketDataSourceChangedService.basketChangedEvent.subscribe(i =>  this.basketEventExample());
    this.dataSourceChangedEventHandleId = this.dataSourceChangedService.dataSourceChangedEvent.subscribe(i =>  this.refreshAll());

    this.updateEmmiterSubscriptionId =
    this.appDataService.updateEmmiter.subscribe(event => {
        if (event === UpdateSignals.ORDERS_AND_TRADES_TABLE_STATE || event === UpdateSignals.UPDATE_MARKET_TABLES) {
            this.onUpdateSignal()
        }
    })

    // this.updateTemporaryBasketArray()
    // this.dataSourceChangedService.dataSourceChangedEvent.subscribe(i =>  this.addOrderEventExample());

    setTimeout( () => {
        this.updateActiveProductsArray();
        this.updateMyOrdersArray();
        this.updateWeatherTable();
    }, 700);


    // this.updateTemporaryBasketArray()
    this.changeDetectorRefs.detectChanges();

    this.subscribeToMessageService();
    this.subscribeToTablesVisibilityMessageServices();

    this.topLeftVisibilityStatus = true;
    this.topRightVisibilityStatus = true;
    this.bottomLeftVisibilityStatus = true;
    this.bottomRightVisibilityStatus = true;
  }

  onUpdateSignal() {
      this.switchOrdersAndTableArraySelection(this.appDataService.orderAndTradesTableSelection)
      this.refreshAll()
  }

  subscribeToTablesVisibilityMessageServices() {
    this.messageServiceService
        .accessMessage('topLeftVisibilityUpdate')
        .subscribe(msg => {
          this.hideProductsTable();
        });

        this.messageServiceService
        .accessMessage('topRightVisibilityUpdate')
        .subscribe(msg => {
          this.hideOrdersByProductTable();
        });

        this.messageServiceService
        .accessMessage('bottomLeftVisibilityUpdate')
        .subscribe(msg => {
          this.hideOrdersAndTradesTable();
        });

        this.messageServiceService
        .accessMessage('bottomRightVisibilityUpdate')
        .subscribe(msg => {
          this.hideWeatherTable();
        });
    }

    tableExistsAtFullScreen() {
      let countInvisibleCorners = 0;
      if( this.topLeftVisibilityStatus == false ) countInvisibleCorners ++;
      if( this.topRightVisibilityStatus == false )  countInvisibleCorners ++;
      if( this.bottomLeftVisibilityStatus == false ) countInvisibleCorners ++;
      if( this.bottomRightVisibilityStatus == false ) countInvisibleCorners ++;

      if( countInvisibleCorners >=3 ) return true;
      else return false;
    }

    switchProductsTableFullScreen() {
        if (this.topLeftVisibilityStatus = true && this.tableExistsAtFullScreen()) {
            this.topLeftVisibilityStatus = true
            this.topRightVisibilityStatus = true
            this.bottomLeftVisibilityStatus = true
            this.bottomRightVisibilityStatus = true
        } else {
            this.topLeftVisibilityStatus = true
            this.topRightVisibilityStatus = false
            this.bottomLeftVisibilityStatus = false
            this.bottomRightVisibilityStatus = false

        }

        this.changeDetectorRefs.detectChanges();
    }

    switchOrdersByProductTableFullScreen() {
        if (this.topLeftVisibilityStatus = true && this.tableExistsAtFullScreen()) {
            this.topLeftVisibilityStatus = true
            this.topRightVisibilityStatus = true
            this.bottomLeftVisibilityStatus = true
            this.bottomRightVisibilityStatus = true
        } else {
            this.topLeftVisibilityStatus = false
            this.topRightVisibilityStatus = true
            this.bottomLeftVisibilityStatus = false
            this.bottomRightVisibilityStatus = false
        }

        this.changeDetectorRefs.detectChanges();
    }

    switchOrdersAndTradesTableFullScreen() {
        if (this.topLeftVisibilityStatus = true && this.tableExistsAtFullScreen()) {
            this.topLeftVisibilityStatus = true
            this.topRightVisibilityStatus = true
            this.bottomLeftVisibilityStatus = true
            this.bottomRightVisibilityStatus = true
        } else {
            this.topLeftVisibilityStatus = false
            this.topRightVisibilityStatus = false
            this.bottomLeftVisibilityStatus = true
            this.bottomRightVisibilityStatus = false
        }

        this.changeDetectorRefs.detectChanges();
    }

    switchWeatherTableFullScreen() {
        if (this.topLeftVisibilityStatus = true && this.tableExistsAtFullScreen()) {
            this.topLeftVisibilityStatus = true
            this.topRightVisibilityStatus = true
            this.bottomLeftVisibilityStatus = true
            this.bottomRightVisibilityStatus = true
        } else {
            this.topLeftVisibilityStatus = false
            this.topRightVisibilityStatus = false
            this.bottomLeftVisibilityStatus = false
            this.bottomRightVisibilityStatus = true
        }

        this.changeDetectorRefs.detectChanges();
    }

    hideProductsTable() {
        if(this.topLeftVisibilityStatus == true && !this.tableExistsAtFullScreen()) this.topLeftVisibilityStatus = false;
        else this.topLeftVisibilityStatus = true;
        this.changeDetectorRefs.detectChanges();
    }

    hideOrdersByProductTable() {
        if(this.topRightVisibilityStatus == true && !this.tableExistsAtFullScreen()) this.topRightVisibilityStatus = false;
        else this.topRightVisibilityStatus = true;
        this.changeDetectorRefs.detectChanges();
    }

    hideOrdersAndTradesTable() {
        if(this.bottomLeftVisibilityStatus == true && !this.tableExistsAtFullScreen()) this.bottomLeftVisibilityStatus = false;
        else this.bottomLeftVisibilityStatus = true;
        this.changeDetectorRefs.detectChanges();
    }

    hideWeatherTable() {
        if(this.bottomRightVisibilityStatus == true && !this.tableExistsAtFullScreen()) this.bottomRightVisibilityStatus = false;
        else this.bottomRightVisibilityStatus = true;
        this.changeDetectorRefs.detectChanges();
    }

    getTopLeftVisibility(){
       if( this.topLeftVisibilityStatus == true &&  this.topRightVisibilityStatus == true ) return 'side';
       else if( this.topLeftVisibilityStatus == true &&  this.topRightVisibilityStatus == false ) return 'wide';
       else return 'hidden';
    }

    getTopRightVisibility(){
       if( this.topRightVisibilityStatus == true &&  this.topLeftVisibilityStatus == true ) return 'side';
       else if( this.topRightVisibilityStatus == true &&  this.topLeftVisibilityStatus == false ) return 'wide';
       else return 'hidden';
    }

    getBottomLeftVisibility(){
       if( this.bottomLeftVisibilityStatus == true &&  this.bottomRightVisibilityStatus == true ) return 'side';
       else if( this.bottomLeftVisibilityStatus == true &&  this.bottomRightVisibilityStatus == false ) return 'wide';
       else return 'hidden';
    }


    getBottomRightVisibility(){
       if( this.bottomRightVisibilityStatus == true &&  this.bottomLeftVisibilityStatus == true ) return 'side';
       else if( this.bottomRightVisibilityStatus == true &&  this.bottomLeftVisibilityStatus == false ) return 'wide';
       else return 'hidden';
    }

    getTopVisibility(){

       if(this.wrapTablesToAvailableHeight === false) return 'unwrapped';

       if( (this.topLeftVisibilityStatus == true || this.topRightVisibilityStatus == true) &&
           (this.bottomLeftVisibilityStatus == true || this.bottomRightVisibilityStatus == true)  ) return 'side';
       else if( (this.topLeftVisibilityStatus == true || this.topRightVisibilityStatus == true) &&
           (this.bottomLeftVisibilityStatus == false && this.bottomRightVisibilityStatus == false)  ) return 'wide';

       else return 'hidden';
    }

    getBottomVisibility(){

       if(this.wrapTablesToAvailableHeight === false) return 'unwrapped';

       if(  (this.bottomRightVisibilityStatus == true || this.bottomLeftVisibilityStatus == true) &&
            (this.topLeftVisibilityStatus == true || this.topRightVisibilityStatus == true) ) return 'side';
       else if(  (this.bottomRightVisibilityStatus == true || this.bottomLeftVisibilityStatus == true) &&
            (this.topLeftVisibilityStatus == false && this.topRightVisibilityStatus == false) ) return 'wide';
       else return 'hidden';
    }

    basketEventExample() {
        this.updateTemporaryBasketArray()
    }

    switchOrdersAndTableArraySelection(selection: string){
      
        switch (selection) {
            case AppConstants.Market.Tables.OrdersAndTrades.State.ORDERS:
                this.updateOrdersAndTradesTable(BottomLeftTables.MY_ORDERS)
                break;
            case AppConstants.Market.Tables.OrdersAndTrades.State.TRADES:
                this.updateOrdersAndTradesTable(BottomLeftTables.MY_TRADES)
                break;
            case AppConstants.Market.Tables.OrdersAndTrades.State.ALL_TRADES:
                this.updateOrdersAndTradesTable(BottomLeftTables.ALL_TRADES)
                break;
            case AppConstants.Market.Tables.OrdersAndTrades.State.BASKET:
                this.updateOrdersAndTradesTable(BottomLeftTables.BASKET)
                break;
        }
    }

  refreshAll(){
    this.updateActiveProductsArray();
    this.updateOrdersByProductArray();
    this.updateOrdersAndTradesTable(this.selectedBottomLeftTable);
    this.updateWeatherTable()
  }

  onDropdownChange(){
    this.displayMaxNumberOfOrdersAndTrades = true
    this.updateOrdersAndTradesTable(this.selectedBottomLeftTable);
  }

  updateBasket(){
    let basketId = this.orderStateService.basketIdFromBasketDTOInOrdersArray
    console.log(basketId)
    const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
      data: {
          userCommand: 'updateBasket'
  }});

  dialogRef.afterClosed().subscribe(result => {
       if (this.orderStateService.getResponse() === 'no') {
           return;
      }
      let basketDTO = BasketDTO.createFromDialogData(this.orderStateService)
      console.log(basketDTO)
      this.basketService.updateBasket(basketDTO, basketId).subscribe(
          response => {
              this.appDataService.setOrdersAndTradesTableSelection(AppConstants.Market.Tables.OrdersAndTrades.State.ORDERS)
       });
       this.orderStateService.resetBasketArray();
       this.orderStateService.processMode = AppConstants.Market.UILabels.ProcessMode.ADD_TO_BASKET
       this.basketDataSourceChanged.basketChangedEvent.emit('basket changed');
  });
  }

  updateDatePickersPlaceholders() {
      if (this.fromDate === null || this.fromDate === '') {
          this.fromDatePlaceHolder = 'From Date'
      } else {
          this.fromDatePlaceHolder = null
      }

      if (this.toDate === null || this.toDate === '') {
          this.toDatePlaceHolder = 'To Date'
      } else {
          this.toDatePlaceHolder = null
      }
  }

  updateOrdersByProductArray(){
    let ordersByProductList: TableStructure.OrdersByProduct[] = [];
    if(this.orderStateService.productId == null){
      ordersByProductList = [];
      this.datasourceOrdersByProductTable = new MatTableDataSource(ordersByProductList)
    }
    else{

    this.ordersByProductService.getOrdersByProduct(this.orderStateService.productId).subscribe(ordersByProductData =>{
       for(let i = 0; i  < ordersByProductData.length ; i++) {
         let currentOrdersByProductDataObject = new TableStructure.OrdersByProduct();
         currentOrdersByProductDataObject = TableStructure.OrdersByProduct.createFromOrderDTO(ordersByProductData[i])
          ordersByProductList.push(currentOrdersByProductDataObject);
       }
       console.log(ordersByProductList)
       this.datasourceOrdersByProductTable = new MatTableDataSource(ordersByProductList)

       console.log(this.datasourceOrdersByProductTable)
      })
    }


  }

  updateMyOrdersArray(){
    this.orderStateService.selectedArrayToView =  AppConstants.Market.Tables.OrdersAndTrades.State.ORDERS
    const myOrderTableDataList: TableStructure.MyOrdersTableData[] = [];
    //My Orders Call
    this.myOrdersService.getmyOrders().subscribe(ordersDTO => {
      ordersDTO = ordersDTO.reverse();
      console.log(ordersDTO)
      this.myOrdersData = ordersDTO;
      this.orderStateService.setMyOrdersArray( this.myOrdersData);
      for (let i = 0; i < ordersDTO.length; i++) {
        let currentMyOrdersTableDataObject = new TableStructure.MyOrdersTableData();
        currentMyOrdersTableDataObject  = MyOrdersTableData.createFromOrderDTO(ordersDTO[i])
        myOrderTableDataList.push(currentMyOrdersTableDataObject);
        }
        this.datasourceMyOrdersTable = new MatTableDataSource(myOrderTableDataList);
        console.log(this.datasourceMyOrdersTable)
        if(this.datasourceMyOrdersTable.data.length > AppConstants.Market.Tables.OrdersAndTrades.Threshold){
          this.dataSourceMyOrdersTableDisplayingData.data = this.datasourceMyOrdersTable.data.slice(0,AppConstants.Market.Tables.OrdersAndTrades.Threshold)
          if(this.displayMaxNumberOfOrdersAndTrades === true){
            this.notificationToastService.showNotification('Too many Orders. Displaying first '+AppConstants.Market.Tables.OrdersAndTrades.Threshold + ' out of '+this.datasourceMyOrdersTable.data.length+ ' entries. For' 
            +' the full list, please download the CSV File.', 'error', 'bottom',
            'center', 'info')
          }
        }
        else{
          this.dataSourceMyOrdersTableDisplayingData.data = this.datasourceMyOrdersTable.data
        }
        console.log(this.dataSourceMyOrdersTableDisplayingData.data.length)
      
      
        this.changeDetectorRefs.detectChanges()
        this.displayMaxNumberOfOrdersAndTrades = false;
      });

      // this.dataSource.sort = this.sort;
      // this.dataSource.paginator = this.paginator;
      // this.datasourceMyOrdersTable.filterPredicate = this.customFilterPredicate();

  }

  downloadTableAsCSV(){
        switch(this.orderStateService.selectedArrayToView){
          case AppConstants.Market.Tables.OrdersAndTrades.State.ORDERS:
              this.datasourceMyOrdersTable.connect().subscribe(data=>{
              this.renderedData=data;
              const replacer = (key, value) => value === null ? '' : value; // specify how you want to handle null values here
              const myOrdersHeaders = JSON.parse(JSON.stringify(AppConstants.Market.Tables.OrdersAndTrades.MyOrdersHeaders));
              let csv = this.renderedData.map(row =>  myOrdersHeaders.map(fieldName => JSON.stringify(row[fieldName], replacer)).join(',').replace(/\"/g, ""));
              csv.unshift(myOrdersHeaders.join(','));
              let csvArray = csv.join('\r\n');
              const finalCSVArray = this.replaceCSVTitles(csvArray)
              const blob = new Blob([finalCSVArray], { type: 'text/csv;charset=utf-8;' });
              saveAs(blob, 'myOrders.csv');
             })
             break;
          case AppConstants.Market.Tables.OrdersAndTrades.State.TRADES:
            this.datasourceMyTradesTable.connect().subscribe(data=>{
              this.renderedData=data;
              const replacer = (key, value) => value === null ? '' : value; // specify how you want to handle null values here
              const myTradesHeaders = JSON.parse(JSON.stringify(AppConstants.Market.Tables.OrdersAndTrades.MyTradesHeaders))
              let csv = this.renderedData.map(row =>  myTradesHeaders.map(fieldName => JSON.stringify(row[fieldName], replacer)).join(',').replace(/\"/g, ""));
              csv.unshift(myTradesHeaders.join(','));
              let csvArray = csv.join('\r\n');
              const finalCSVArray = this.replaceCSVTitles(csvArray)
              const blob = new Blob([finalCSVArray], { type: 'text/csv;charset=utf-8;' });
              saveAs(blob, 'myTrades.csv');   
            })
            break;
            case AppConstants.Market.Tables.OrdersAndTrades.State.ALL_TRADES:
              console.log(this.datasourceAllTrades)
            this.datasourceAllTrades.connect().subscribe(data=>{
              console.log(data);
              this.renderedData=data;
              const replacer = (key, value) => value === null ? '' : value; // specify how you want to handle null values here
              const allTradesHeaders = JSON.parse(JSON.stringify(AppConstants.Market.Tables.OrdersAndTrades.AllTradesHeaders))
              if(this.appDataService.userDto.rolesMarket !== 'admin'){
                const buyerOrderTypeIndex  =  allTradesHeaders.indexOf('buyerOrderTypeAllTradesTable', 0);
                const sellerOrderTypeIndex  =  allTradesHeaders.indexOf('sellerOrderTypeAllTradesTable', 0);
                console.log(buyerOrderTypeIndex)
                console.log(sellerOrderTypeIndex)
                allTradesHeaders.splice(buyerOrderTypeIndex)
                allTradesHeaders.splice(sellerOrderTypeIndex)
              }
              let csv = this.renderedData.map(row =>  allTradesHeaders.map(fieldName => JSON.stringify(row[fieldName], replacer)).join(',').replace(/\"/g, ""));
              csv.unshift(allTradesHeaders.join(','));
              let csvArray = csv.join('\r\n');
              const finalCSVArray = this.replaceCSVTitles(csvArray)
              const blob = new Blob([finalCSVArray], { type: 'text/csv;charset=utf-8;' });
              saveAs(blob, 'allTrades.csv'); 
            })
            break;
        }
        
       
     
  }
  replaceCSVTitles(csv){
    
    csv= csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyOrdersHeaders[0], 'Order Id')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyOrdersHeaders[1],'Product')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyOrdersHeaders[2],'Status')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyOrdersHeaders[3],'Dir')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyOrdersHeaders[4],'Price')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyOrdersHeaders[5],'Qty')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyOrdersHeaders[6],'Delivery Start')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyOrdersHeaders[7],'Delivery Period')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyOrdersHeaders[8],'Date')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyOrdersHeaders[9],'Order')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyOrdersHeaders[10],'Basket')
    csv= csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyTradesHeaders[0], 'Trade Id')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyTradesHeaders[1],'Order Id')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyTradesHeaders[2],'Product')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyTradesHeaders[3],'Dir')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyTradesHeaders[4],'Price')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyTradesHeaders[5],'Qty')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyTradesHeaders[6],'Buyer')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyTradesHeaders[7],'Seller')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyTradesHeaders[8],'Date')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyTradesHeaders[9],'Order')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.MyTradesHeaders[10],'Basket')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.AllTradesHeaders[0],'Product')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.AllTradesHeaders[1],'Qty')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.AllTradesHeaders[2],'Price')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.AllTradesHeaders[3],'Buyer')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.AllTradesHeaders[4],'Seller')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.AllTradesHeaders[5],'Date')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.AllTradesHeaders[6],'Buyer Order Type')
    csv=csv.replace(AppConstants.Market.Tables.OrdersAndTrades.AllTradesHeaders[7],'Seller Order Type')
    
    
    return csv;
  }

  subscribeToMessageService() {

          this.messageServiceService
              .accessMessage('MarketOrder')
              .subscribe(msg => {
                  this.updateActiveProductsArray();
              });

        this.messageServiceService
              .accessMessage('MarketMatching')
              .subscribe(msg => {
                  this.refreshAll();
              });

  }

  updateActiveProductsArray() {
      const  tableDataList: TableStructure.ProductsTableData[] = [];
      //Active Products Call
      this.activeProductsService.getActiveProducts().subscribe(activeProductsDTO => {
          console.log(activeProductsDTO)
          this.orderStateService.setActiveProductsArray(activeProductsDTO);
          for (let i = 0; i < activeProductsDTO.length; i++) {
              // tslint:disable-next-line:prefer-const
              let currentTableDataObject = new TableStructure.ProductsTableData();
              currentTableDataObject = TableStructure.ProductsTableData.createFromProductDTO(activeProductsDTO[i])
              tableDataList.push(currentTableDataObject);
          }
          this.datasourceProductsTable = new MatTableDataSource(tableDataList)
          this.changeDetectorRefs.detectChanges()
      });
  }

  clearAllBasketOrders() {
      const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
          data: {
              userCommand: 'clearAll'
          }});

          dialogRef.afterClosed().subscribe(result => {
              if (this.orderStateService.getResponse() === 'no') {
                  return;
              }
              this.orderStateService.resetBasketArray()
              this.orderStateService.processMode = AppConstants.Market.UILabels.ProcessMode.ADD_TO_BASKET
              this.basketDataSourceChangedService.basketChangedEvent.emit('basket changed');
              dialogRef.close();
          });
      }

  ngOnDestroy(): void {

      this.messageServiceService.unsubscribeTopic('MarketOrder');
      this.messageServiceService.unsubscribeTopic('MarketMatching');

      clearInterval(this.intervalId);
      this.dataSourceChangedEventHandleId.unsubscribe()
      this.basketChangedEventHandleId.unsubscribe()
      this.updateEmmiterSubscriptionId.unsubscribe()
  }

  postBasket(){
      const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
          data: {
              userCommand: 'postBasket'
      }});

      dialogRef.afterClosed().subscribe(result => {
          if (this.orderStateService.getResponse() === 'no') {
             return;
          }
          let basketDTO = BasketDTO.createFromDialogData(this.orderStateService)
          console.log(basketDTO)
          this.basketService.createBasket(basketDTO).subscribe(
              response => {
                  this.appDataService.setOrdersAndTradesTableSelection(AppConstants.Market.Tables.OrdersAndTrades.State.ORDERS)
           });
           this.orderStateService.resetBasketArray();
           this.basketDataSourceChanged.basketChangedEvent.emit('basket changed');
      });
  }

  updateOrdersAndTradesTable(selectedTable) {
      if(selectedTable === BottomLeftTables.ALL_TRADES) {
          console.log('--1--')
          this.orderStateService.selectedArrayToView =  AppConstants.Market.Tables.OrdersAndTrades.State.ALL_TRADES
          this.selectedBottomLeftTable = BottomLeftTables.ALL_TRADES
          this.updateAllTradesArray()
      } else if(selectedTable === BottomLeftTables.MY_ORDERS) {
          console.log('--2--')
          this.orderStateService.selectedArrayToView = AppConstants.Market.Tables.OrdersAndTrades.State.ORDERS
          this.selectedBottomLeftTable = BottomLeftTables.MY_ORDERS
          this.updateMyOrdersArray();
      } else if(selectedTable === BottomLeftTables.MY_TRADES) {
          console.log('--3--')
          this.orderStateService.selectedArrayToView = AppConstants.Market.Tables.OrdersAndTrades.State.TRADES
          this.selectedBottomLeftTable = BottomLeftTables.MY_TRADES
          this.updateMyTradesArray()
      } else if(selectedTable === BottomLeftTables.BASKET) {
          console.log('--4--')
          this.orderStateService.selectedArrayToView = AppConstants.Market.Tables.OrdersAndTrades.State.BASKET
          this.selectedBottomLeftTable = BottomLeftTables.BASKET
          this.updateTemporaryBasketArray();
      }
      
  }

  updateMyTradesArray(){
    this.orderStateService.selectedArrayToView =  AppConstants.Market.Tables.OrdersAndTrades.State.TRADES
    const optionTradesTableDataList: TableStructure.MyTradesTableData[] = [];
    this.myTradesService.getMyTrades().subscribe(tradesDTO => {
      tradesDTO = tradesDTO.reverse()
      console.log(tradesDTO)
      // tslint:disable-next-line:prefer-const
      for (let i = 0; i < tradesDTO.length; i++) {
        // tslint:disable-next-line:prefer-const
        let optionMyTradesTableDataObject = MyTradesTableData.createFromTradesDTO(tradesDTO[i],this.appDataService.userDto)
        optionTradesTableDataList.push(optionMyTradesTableDataObject);
      }

      this.datasourceMyTradesTable = new MatTableDataSource(optionTradesTableDataList);
      if(this.datasourceMyTradesTable.data.length > AppConstants.Market.Tables.OrdersAndTrades.Threshold){
        this.dataSourceMyTradesTableDisplayingData.data = this.datasourceMyTradesTable.data.slice(0, AppConstants.Market.Tables.OrdersAndTrades.Threshold)
        if(this.displayMaxNumberOfOrdersAndTrades === true){
          this.notificationToastService.showNotification('Too many Trades. Displaying first '+AppConstants.Market.Tables.OrdersAndTrades.Threshold + ' out of '+this.datasourceMyTradesTable.data.length+ ' entries. For' 
            +' the full list, please download the CSV File.', 'error', 'bottom',
            'center', 'info')
        }  
        
      }
      else{
        this.dataSourceMyTradesTableDisplayingData.data = this.datasourceMyTradesTable.data
      }
      this.changeDetectorRefs.detectChanges()
      this.displayMaxNumberOfOrdersAndTrades = false;
    });
  }

  updateAllTradesArray(){
      this.orderStateService.selectedArrayToView =  AppConstants.Market.Tables.OrdersAndTrades.State.ALL_TRADES
      const optionTradesTableDataList: TableStructure.AllTradesTableData[] = [];
      this.myTradesService.getAllTrades().subscribe(baseInfoTradeDTOs => {
          baseInfoTradeDTOs = baseInfoTradeDTOs.reverse()
          console.log(baseInfoTradeDTOs)

          for (let i = 0; i < baseInfoTradeDTOs.length; i++) {
              // tslint:disable-next-line:prefer-const
              let optionMyTradesTableDataObject = AllTradesTableData.createFromBaseInfoTradeDTO(baseInfoTradeDTOs[i], this.appDataService.userDto)
              optionTradesTableDataList.push(optionMyTradesTableDataObject);
          }

          console.log(optionTradesTableDataList)
          this.datasourceAllTrades = new MatTableDataSource(optionTradesTableDataList);
          if(this.datasourceAllTrades.data.length > AppConstants.Market.Tables.OrdersAndTrades.Threshold){
            this.dataSourceAllTradesDisplayingData.data = this.datasourceAllTrades.data.slice(0,AppConstants.Market.Tables.OrdersAndTrades.Threshold)
            if(this.displayMaxNumberOfOrdersAndTrades === true){
              this.notificationToastService.showNotification('Too many Trades. Displaying first '+ AppConstants.Market.Tables.OrdersAndTrades.Threshold + ' out of '+this.datasourceAllTrades.data.length+ ' entries. For' 
              +' the full list, please download the CSV File.', 'error', 'bottom',
              'center', 'info')
            }
          }
          else{
            this.dataSourceAllTradesDisplayingData.data = this.datasourceAllTrades.data
          }
          console.log(this.datasourceAllTrades)
          this.changeDetectorRefs.detectChanges()
          this.displayMaxNumberOfOrdersAndTrades = false;
      });
  }

  updateTemporaryBasketArray(){
      this.orderStateService.selectedArrayToView =  AppConstants.Market.Tables.OrdersAndTrades.State.BASKET
      console.log(this.orderStateService.getBasketTableArray())
      const basketOrdersList: TableStructure.MyOrdersTableData[] = [];
      for(let i = 0; i < this.orderStateService.getBasketTableArray().length; i++) {
          // tslint:disable-next-line:max-line-length
          const currentMyBasketTableDataObject =  MyOrdersTableData.createFromTempBasketDTO (this.orderStateService.getBasketTableArray()[i], this.orderStateService.processMode);
          // currentMyBasketTableDataObject.orderStatusMyOrdersTable = 'Queued';
          // currentMyBasketTableDataObject.priceMyOrdersTable = currentMyBasketTableDataObject.initialPriceMyOrdersTable
          // currentMyBasketTableDataObject.quantityMyOrdersTable = currentMyBasketTableDataObject.initialQuantityMyOrdersTable

          basketOrdersList.push(currentMyBasketTableDataObject)

          // }
          console.log('-----------' +basketOrdersList)
      }
      this.basketDataSource = new MatTableDataSource(basketOrdersList);
  }

  updateWeatherTable(){
      const weatherTableData: TableStructure.WeatherTableData[] = [];

      this.weatherService.getWeather().subscribe(weatherDTOs => {
          console.log(weatherDTOs)

          for (let i = 0; i < weatherDTOs.length; i++) {
              let weatherTableDataObject = TableStructure.WeatherTableData.createFromWeatherDTO(weatherDTOs[i])
              weatherTableData.push(weatherTableDataObject);
          }

          console.log(weatherTableData)
          this.datasourceWeatherTable = new MatTableDataSource(weatherTableData);
          this.changeDetectorRefs.detectChanges()
      });
  }

  mapOrderDataToMyOrdersTableData(){}

  applyFilter(filter) {
    this.globalFilter = filter;
    this.datasourceMyOrdersTable.filter = JSON.stringify(this.filteredValues);
  }

  // ngAfterViewInit() {
  //   this.setTableResize(this.matTableRef.nativeElement.clientWidth);

  // }

  getProductSelection(productOption) {
    console.log(productOption.option.value);
    this.productSelected = productOption.option.value;
    console.log(this.productSelected);
  }

  customFilterPredicate() {
    console.log(this.filteredValues);
    const myFilterPredicate = (
      myOrderData: MyOrdersData,
      filter: string
    ): boolean => {
      var globalMatch = !this.globalFilter;
      if (this.globalFilter) {
        // search all text fields
        // globalMatch = myOrderData.name.toString().trim().toLowerCase().indexOf(this.globalFilter.toLowerCase()) !== -1;
      }
      if (!globalMatch) {
        return;
      }
      // tslint:disable-next-line:prefer-const
      let searchString = JSON.parse(filter);
      return (
        new Date(myOrderData.bidTime).valueOf() >= this.epochFromTime &&
        new Date(myOrderData.bidTime).valueOf() <= this.epochToTime
      );
    };
    return myFilterPredicate;
  }

  addFromEvent(type: string, event: MatDatepickerInputEvent<Date>) {
    this.displayMaxNumberOfOrdersAndTrades = true;
    console.log(new Date(event.value))
    this.displayFromDate = new Date(event.value)
    if(this.displayFromDate >= this.displayToDate){
      this.displayToDate = this.displayFromDate
    }
    console.log(this.displayFromDate)
    console.log(this.displayToDate)
    // this.fromDate = moment(new Date(event.value)).format('HH:mm, DD/MM/YYYY').toString()
    this.fromDate = this.displayFromDate.toISOString()
    this.toDate =  moment(this.displayToDate.setHours(23, 59, 59, 999)).toISOString()
    this.searchByDate(this.fromDate, this.toDate)

    const selectedEpochFromTime = new Date(event.value).valueOf();
    this.epochFromTime = selectedEpochFromTime;
    // this.applyDateFilter();
    this.updateDatePickersPlaceholders()
  }

  addToEvent(type: string, event: MatDatepickerInputEvent<Date>) {
    this.displayMaxNumberOfOrdersAndTrades  =true
    this.fromDate =  moment(this.displayFromDate.setHours(0, 0, 0, 999)).toISOString()
   this.displayToDate = new Date(event.value)
   this.toDate = moment(new Date(event.value).setHours(23, 59, 59, 999)).toISOString()
   console.log(this.fromDate)
   this.searchByDate(this.fromDate, this.toDate)
    const selectedEpochToTime = new Date(event.value).valueOf();
    this.epochToTime =
      selectedEpochToTime + 23 * 60 * 60 * 1000 + 59 * 60 * 1000;
    this.updateDatePickersPlaceholders()
    // this.applyDateFilter();

  }
  searchByDate(from: string, to: string){
    console.log(from)
    console.log(to)
    this.orderStateService.from = from
    this.orderStateService.to = to
    console.log(this.orderStateService.selectedArrayToView)
    if(this.orderStateService.selectedArrayToView === 'my_orders'){
    this.updateMyOrdersArray();
    }
  else if(this.orderStateService.selectedArrayToView === 'my_trades'){
    this.updateMyTradesArray();
  }
  else if(this.orderStateService.selectedArrayToView === 'all_trades'){
    this.updateAllTradesArray();
  }
  }

  // applyDateFilter() {
  //   this.datasourceMyOrdersTable.filter = '' + Math.random();
  //   // this.filteredValues['quantity'] = Math.random().toString()
  //   // this.datasourceMyOrdersTable.filter = JSON.stringify(this.filteredValues);
  // }
  onSubmit() {}

  selectionChanged(item) {
    console.log(item.value);
    console.log('Selected value: ' + item.value);

    this.selectedDate = item.value;

    if (this.selectedDate === 'today') {
      this.date = new Date();
      this.showDate = true;
    }
    if (this.selectedDate === 'tommorow') {
      this.date = new Date(new Date().setDate(new Date().getDate() + 1));
      this.showDate = true;
    }
  }

  marketChoiceChanged(marketPreference) {
      this.selectedMarketChoice = marketPreference.value;
      console.log(this.selectedMarketChoice);
  }
  getCurrentOrderTypes() {
    this.orderStateService.getCurrentOrderTypeStatus().subscribe(response => {
      this.scenario1TestArray = response.scenario.split(",");
    },
      onError => {
        this.notificationToastService.showNotification(
          'There is no available market scenario. Please provide some order Type(s) first.', 'error', 'top',
          'center', 'danger')
      }
    )
  }
}
