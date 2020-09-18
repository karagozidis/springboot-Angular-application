import {
  Component,
  OnInit,
  Renderer2,
  Inject,
  ViewChild,
  ElementRef,

  Input,
  AfterViewInit,
  EventEmitter,
  Output,
  ChangeDetectorRef,
  ViewContainerRef,
  TemplateRef,
  SimpleChanges
} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatSort,
  MatPaginator,
  MatAutocomplete,
  MatMenuTrigger,
  MatTable,
  MatDialog,
  MatTableDataSource,
  MatDialogRef
} from '@angular/material';
import { SideBarToggleService } from 'app/services/sidebar-toggle.service';
import * as TableStructure from './table-structure/table-structure';
import { MarketDialogComponent } from '../market-dialog/market-dialog.component';
import { ProductBookDTO } from 'app/shared/dto/product-book-dto';
import { OrderDTO } from 'app/shared/dto/order-dto';
import { OrderMetadataDTO } from 'app/shared/dto/orderMetadataDTO';
import { OrderStateService } from 'app/services/orderState.service';
import { BasketDataSourceChangedService } from 'app/services/basketDataSourceChanged.service';
import { MyOrdersService } from 'app/services/myOrders.service';
import { ProductDTO } from 'app/shared/dto/product-dto';
import { DataSourceChangedService } from 'app/services/dataSourceChanged.service';
import { OrdersByProductService } from 'app/services/ordersByProduct.service';
import { OverlayRef, Overlay } from '@angular/cdk/overlay';
import { TemplatePortal } from '@angular/cdk/portal';
import { Subscription, fromEvent , Observable } from 'rxjs';
import { take, filter } from 'rxjs/operators';
import { MarketConfirmationDialogComponent } from '../market-confirmation-dialog/market-confirmation-dialog.component';
import { OrdersComponent } from '../../orders/orders.component';
import { AppConstants, UpdateSignals } from 'app/shared/config/app-constants';
import { connectableObservableDescriptor } from 'rxjs/internal/observable/ConnectableObservable';
import { ProductTransactionsComponent } from './../../../market-tools/product-transactions/product-transactions.component';
import { DisplayDropDownOptionService } from 'app/services/displayCurrentDropdownOption.service';
import { BasketService } from 'app/services/basket.service';
import { AppDataService } from 'app/services/app-data.service';
import { NotificationToastService } from 'app/shared/utils/notification-toast-service';
import { Auxiliary } from 'app/shared/utils/auxiliary';
import { UserDto } from 'app/shared/dto/user-dto';
@Component({
  selector: 'app-market-table',
  templateUrl: './market-table.component.html',
  styleUrls: ['./market-table.component.scss']
})
export class MarketTableComponent implements OnInit {
  pressed = false;
  clicked = false;
  currentResizeIndex: number;
  startX: number;
  startWidth: number;
  isResizingRight: boolean;
  orderModalX: any;
  orderModalY: any;
  selectedRowIndex = -1;
  resizableMousemove: () => void;
  resizableMouseup: () => void;
  enableFlex = false;
  aquamarine = true;
  redColor = false;
  firstChoice = true;
  secondChoice = false;
  thirdChoice = false;
  forthChoice = false;
  rowToDisplay = '';
  selectedMarketChoice = 'buy';
  selectedMarketTypeChoice = 'limit';
  selectedBasketTypeChoice = 'none';
  enabledStatus = false;
  hideBox = 0;
  modalIndex = -10;
  icebergNotSelected = 0;
  volume_constrained = 0;
  cumulative_volume_constrained = 0;
  exclusive = 0;
  linked_group = 0;
  icebergIndex = -5;
  volumeIndex = -5;
  cumulativeIndex = -5;
  isPopupOpened = true;
  dataFromRow: any
  productObject = new ProductDTO()
  selectedOrderDto = new OrderDTO();
  updatedBasketDataSource = new MatTableDataSource([]);
  selectedBasketId= 0;
  UILabels = {
      OrderStatus : AppConstants.Market.UILabels.OrderStatus
  }

  basketDataSourceToDisplay;
  dataSourceToDisplay = new MatTableDataSource([]);
  overlayRef: OverlayRef | null;
  @ViewChild(MatTable, { read: ElementRef, static: false })
  private matTableRef: ElementRef;
  @ViewChild(MatMenuTrigger, null)
  @Input()
  myTable;
  @ViewChild('rowModal', { read: ElementRef, static: false })
  private rowModalRef: ElementRef;
  @Input() columns: any[] = [];

  // @Input() displayedColumns: any[] = [];
  displayedColumns: any[] = [];
  @Input() dataSource = new MatTableDataSource([]);
  @Input() minWidth;
  myTitle = 'Input1';
  @ViewChild('ordersMenu', null) ordersMenu: TemplateRef<any>;
  @ViewChild('rowIndex', null) rowIndex: TemplateRef<any>;
  displayedData = ''
  headerDisplayedData = ''
  userRole = null

  mouseOverIndex = -1;
  sub : Subscription
  ordersAndTradesState = AppConstants.Market.Tables.OrdersAndTrades.State
  updateEmmiterSubscriptionId: Subscription


  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private renderer: Renderer2,
    private sidebarToggleService: SideBarToggleService,
    private basketDataSourceChanged: BasketDataSourceChangedService,
    private dataSourceChanged: DataSourceChangedService,
    private displayDropDownOption: DisplayDropDownOptionService,
    private element: ElementRef,
    private dialog: MatDialog,
    private dialogRef: MatDialogRef<OrdersComponent>,
    public ordersByProductService: OrdersByProductService,
    public myOrdersService: MyOrdersService,
    public orderStateService: OrderStateService,
    private changeDetectorRefs: ChangeDetectorRef,
    public overlay: Overlay,
    public viewContainerRef: ViewContainerRef,
    public basketService: BasketService,
    private appDataService: AppDataService,
    private notificationToastService : NotificationToastService,

  ) {
    this.updateEmmiterSubscriptionId =
                this.appDataService.updateEmmiter.subscribe(event => {
                if (event === UpdateSignals.USER_DTO || event === UpdateSignals.APP_TYPE ) {
                    this.onUpdateSignal()
                }
                })
  }

  onUpdateSignal(){
      if(this.appDataService.userDto == null){
        return
      }
      this.userRole = this.appDataService.userDto.rolesMarket

  }

  ngOnInit() {
    
    this.onUpdateSignal()
    this.setDisplayedColumns();
  }

  setDisplayedColumns() {
    /* this.columns.forEach((column, index) => {
      column.index = index;
      this.displayedColumns[index] = column.field;
    }); */
    if(this.userRole === 'admin'){
      console.log(this.columns)
      console.log(this.dataSource)
      this.columns.forEach((column, index) => {
        column.index = index;
        this.displayedColumns[index] = column.field;
      });
    }
    else{
      this.columns.forEach((column, index) => {
        column.index = index;
        if(column.field === 'buyerOrderTypeAllTradesTable' || column.field === 'sellerOrderTypeAllTradesTable' ){
          return;
        }
        this.displayedColumns[index] = column.field;
      });

    }
    console.log(this.displayedColumns)
    


  }

  removeRowHighlight() {
       this.selectedBasketId = 0
  }

  setSelectedBasket(orderIndex: number,tableId: number){
      if (tableId != 3 || this.getOrdersAndTradesCurrentTable() !== this.ordersAndTradesState.ORDERS) {
          return
      }

      const selectedOrder = this.orderStateService.getMyOrdersArray()[orderIndex];
      if(selectedOrder.basket != null && selectedOrder.basket.id != null){
          this.selectedBasketId = selectedOrder.basket.id
      }
      else{
          this.selectedBasketId = 0
      }
  }

 showMenu({ x, y }: MouseEvent, row, k){
   console.log(this.appDataService.userDto)
   if(this.appDataService.userDto.market.includes(this.orderStateService.country)===false){
     return;
   }
   this.close();
  const positionStrategy = this.overlay.position()
  .onCloudAppbleConnectedTo({ x, y })
  .withPositions([
    {
      originX: 'end',
      originY: 'bottom',
      overlayX: 'end',
      overlayY: 'top',
    }
  ]);

this.overlayRef = this.overlay.create({
  positionStrategy,
  scrollStrategy: this.overlay.scrollStrategies.close()
});

this.overlayRef.attach(new TemplatePortal(this.ordersMenu, this.viewContainerRef, {
  $implicit: k
}));



this.sub = fromEvent<MouseEvent>(document, 'click')
      .pipe(
        filter(event => {
          const clickTarget = event.target as HTMLElement;
          return !!this.overlayRef && !this.overlayRef.overlayElement.contains(clickTarget);
        }),
        take(1)
      ).subscribe(() => this.close())

 }
 close(){
  // tslint:disable-next-line:no-unused-expression
  this.sub && this.sub.unsubscribe();
  if (this.overlayRef) {
    this.overlayRef.dispose();
    this.overlayRef = null;
  }
 }

 getToolTipData(row , columnName:string, index: number){
     if(columnName === 'Date'  && this.columns[0]['table'] !== 4){
         switch (this.orderStateService.selectedArrayToView) {
             case "my_orders":
                this.displayedData = row.fullDateMyOrdersTable
                break;
             case "my_trades":
                this.displayedData = row.fullDateMyTradesTable
                break;
             case "all_trades":
                this.displayedData = row.fullDateAllTradesTable
                break;
             case "basket_orders":
                this.displayedData = ""
                break;
         }
     }
     else if(columnName === 'Order'){
       if(this.orderStateService.selectedArrayToView === 'my_orders' || this.orderStateService.selectedArrayToView === 'basket_orders'){
         if(row.fullOrderTypeMyOrdersTable === AppConstants.Market.UILabels.OrderType.AON ){
             this.displayedData = AppConstants.Market.UILabels.OrderType.AON_FULL_WORD
         }
         // tslint:disable-next-line:max-line-length
         else if(row.fullOrderTypeMyOrdersTable === AppConstants.Market.UILabels.OrderType.FOK ){
             this.displayedData = AppConstants.Market.UILabels.OrderType.FOK_FULL_WORD
         }
         // tslint:disable-next-line:max-line-length
         else if(row.fullOrderTypeMyOrdersTable === AppConstants.Market.UILabels.OrderType.IOC ){
             this.displayedData = AppConstants.Market.UILabels.OrderType.IOC_FULL_WORD
         }
         else if(row.fullOrderTypeMyOrdersTable === AppConstants.Market.UILabels.OrderType.ICEBERG ){
          this.displayedData = 'Iceberg Parameters: \n'+
           ' Total Quantity:'+ +row.icebergMaximumQuantityParameter  +'\n'+  ' Price Change: '+row.icebergPriceChangeParameter
        }
        else{
             this.displayedData = row.orderTypeMyOrdersTable
         }
        }
        else if(this.orderStateService.selectedArrayToView === 'my_trades'){
          if(row.orderTypeMyTradesTable === AppConstants.Market.UILabels.OrderType.AON ){
            this.displayedData = AppConstants.Market.UILabels.OrderType.AON_FULL_WORD
          }
          else if(row.orderTypeMyTradesTable === AppConstants.Market.UILabels.OrderType.FOK ){
            this.displayedData = AppConstants.Market.UILabels.OrderType.FOK_FULL_WORD
          }
          else if(row.orderTypeMyTradesTable === AppConstants.Market.UILabels.OrderType.IOC ){
            this.displayedData = AppConstants.Market.UILabels.OrderType.IOC_FULL_WORD
          }
          else{
            this.displayedData = row.orderTypeMyTradesTable
          }
        }
     }
     else if(columnName === 'Basket'){
       if(this.orderStateService.selectedArrayToView === 'my_orders' || this.orderStateService.selectedArrayToView === 'basket_orders'){
         // tslint:disable-next-line:max-line-length
         if(row.orderTypeMyOrdersTable=== AppConstants.Market.UILabels.OrderType.BASKET && row.basketTypeMyOrdersTable === AppConstants.Market.UILabels.BasketType.VOLUME_CONSTRAINED){
             this.displayedData = AppConstants.Market.UILabels.BasketType.VOLUME_CONSTRAINED_FULL_WORD 
             if(this.orderStateService.selectedArrayToView !== 'basket_orders'){ 
                this.displayedData +=  ' Parameter: \n'+'Total Quantity: '+row.volumeConstrainedTotalQuantityParameter
            }
            
         }
         // tslint:disable-next-line:max-line-length
         else if(row.orderTypeMyOrdersTable=== AppConstants.Market.UILabels.OrderType.BASKET && row.basketTypeMyOrdersTable === AppConstants.Market.UILabels.BasketType.CUMULATIVE_VOLUME_CONSTRAINED){
             this.displayedData = AppConstants.Market.UILabels.BasketType.CUMULATIVE_VOLUME_CONSTRAINED_FULL_WORD
            if(this.orderStateService.selectedArrayToView !== 'basket_orders'){
                this.displayedData += ' Parameters: \n'+' Maximum Capacity: '+row.cumulativeVolumeConstrainedMaximumCapacityParameter +'\n'+
                ' Initial Charge: '+row.cumulativeVolumeConstrainedInitialChargeParameter  +'\n' + ' Rated Power: '+row.cumulativeVolumeConstrainedRatedPowerParameter
            }
         }
         else if(row.orderTypeMyOrdersTable=== AppConstants.Market.UILabels.OrderType.BASKET && row.basketTypeMyOrdersTable === AppConstants.Market.UILabels.BasketType.EXCLUSIVE){
          this.displayedData = AppConstants.Market.UILabels.BasketType.EXCLUSIVE 
          if(this.orderStateService.selectedArrayToView !== 'basket_orders'){ 
             this.displayedData +=' Parameter: \n'+'Max Matched Orders: '+row.excluvsiveMaxMatchedOrdersParameter
          }
      }

         else{
             this.displayedData = row.fullBasketTypeMyOrdersTable
         }
      }
      else if(this.orderStateService.selectedArrayToView === 'my_trades'){
          // tslint:disable-next-line:max-line-length
          if(row.orderTypeMyTradesTable=== AppConstants.Market.UILabels.OrderType.BASKET && row.basketTypeMyTradesTable === AppConstants.Market.UILabels.BasketType.VOLUME_CONSTRAINED){
              this.displayedData = AppConstants.Market.UILabels.BasketType.VOLUME_CONSTRAINED_FULL_WORD
          }
          // tslint:disable-next-line:max-line-length
          else if(row.orderTypeMyTradesTable=== AppConstants.Market.UILabels.OrderType.BASKET && row.basketTypeMyTradesTable === AppConstants.Market.UILabels.BasketType.CUMULATIVE_VOLUME_CONSTRAINED){
              this.displayedData = AppConstants.Market.UILabels.BasketType.CUMULATIVE_VOLUME_CONSTRAINED_FULL_WORD
          }

          else{
              this.displayedData = row.basketTypeMyTradesTable
          }
      }
      else {
          this.displayedData = ''
      }
     }
     else if(columnName === 'Qty' && this.columns[0]['table'] === 3) {
         switch(this.orderStateService.selectedArrayToView){
           case 'my_orders':
              this.displayedData = "Initial Qty:" + row.initialQuantityMyOrdersTable + "MW"
              break;
          default:
              this.displayedData = ''
         }

     }
     else if(columnName === 'Price' && this.columns[0]['table'] === 3){
      switch(this.orderStateService.selectedArrayToView){
        case 'my_orders':
           this.displayedData = "Initial Price:" + row.initialPriceMyOrdersTable + "€/MWh"
           break;
        default:
           this.displayedData = ''
      }
    }
    else if(columnName === 'Buyer' && this.columns[0]['table'] === 3){
      if(this.orderStateService.selectedArrayToView === 'all_trades' && this.userRole === 'admin'){
        this.displayedData = 'Order Type: ' + row.buyerOrderTypeAllTradesTable;
      }
      else{
        this.displayedData = ''
      }
    }
    else if(columnName === 'Seller' && this.columns[0]['table'] === 3){
      if(this.orderStateService.selectedArrayToView === 'all_trades' && this.userRole === 'admin'){
        this.displayedData = 'Order Type: ' +  row.sellerOrderTypeAllTradesTable;
      }
      else{
        this.displayedData = ''
      }
    }
    else if(columnName === 'Date'||columnName === 'Time'||columnName === 'Temperature'||columnName === 'Sun Radiation'||
     columnName === 'Wind Speed'||columnName === 'Sky Info' && (this.columns[0]['table'] === 4)){
        if( row.typeWeatherTable==='Actual'){
          this.displayedData = 'Actual'
        }
        else{ 
          this.displayedData = 'Prediction'
        }

    }
     else {
         this.displayedData = ''
     }

 }
 getHeaderTooltipData(columnName:string){
    if(columnName==='Buy Price' || columnName==='Sell Price' || columnName==='Price' ){
      this.headerDisplayedData = '€/MWh'
    }
    else if(columnName ==='Buy Qty' || columnName==='Sell Qty' || columnName === 'Qty'){
      this.headerDisplayedData = 'MW' 
    }
    else{
      this.headerDisplayedData = ''
    }
 }

  public selectedProductName:string;


 isHighLightedProduct(productName: string, tableNumber: number) {
   if (this.selectedProductName === productName && tableNumber === 1 ) {
     
     return true;
   } else {
    
     return false;
   }
 }
 isFirstWeatherRow( tableId:number, index: number){
    if(index===0 && tableId ===4){
        return true;
    }
    else{
      return false;
    }
 }

 isOddProduct(productName: string, tableNumber: number, odd: boolean) {
  if (this.selectedProductName === productName && tableNumber === 1 ) {
    return false;
  } else if (!odd) {
    return false;
  } else {
    return true;
  }
}

isEvenProduct(productName: string, tableNumber: number, odd: boolean) {
  if (this.selectedProductName === productName && tableNumber === 1 ) {
    return false;
  } else if (odd) {
    return false;
  } else {
    return true;
  }
}

 updateOrdersByProductsTable(e: MouseEvent, row, finished: boolean, k) {
     let tableId = this.columns[0]['table'];
     if (tableId === 1) {
         const ordersByProductId = this.orderStateService.getActiveProductsArray()[k]['id'];
         this.selectedProductName = this.orderStateService.getActiveProductsArray()[k]['name'];
         this.orderStateService.productId = ordersByProductId;
         this.orderStateService.enableOrdersByProductArray = true;
         //this.onRowClicked(e,row,true,k)
         this.dataSourceChanged.dataSourceChangedEvent.emit('dataSource changed');
         
     }
 }

 isOrderInsideBasket(k: number) {
    if(this.getOrdersAndTradesCurrentTable()=== AppConstants.Market.Tables.OrdersAndTrades.State.BASKET ||this.getOrdersAndTradesCurrentTable()=== AppConstants.Market.Tables.OrdersAndTrades.State.TRADES||this.getOrdersAndTradesCurrentTable()=== AppConstants.Market.Tables.OrdersAndTrades.State.ALL_TRADES){
      return false;
    }
    else{
      const selectedOrderDto = this.orderStateService.getMyOrdersArray()[k];

    if(selectedOrderDto.orderType === AppConstants.Market.BackendEnums.OrderType.BASKET){
        return true
    }
    else{
        return false;
    }
}
}
isOrderRemovedOrExpired(k: number) {
  if(this.getOrdersAndTradesCurrentTable()=== AppConstants.Market.Tables.OrdersAndTrades.State.BASKET ||this.getOrdersAndTradesCurrentTable()=== AppConstants.Market.Tables.OrdersAndTrades.State.TRADES||this.getOrdersAndTradesCurrentTable()=== AppConstants.Market.Tables.OrdersAndTrades.State.ALL_TRADES){
    return false;
  }
  else{
    const selectedOrderDto = this.orderStateService.getMyOrdersArray()[k];
    if(selectedOrderDto.orderStatus === AppConstants.Market.BackendEnums.OrderStatus.REMOVED || selectedOrderDto.orderStatus === AppConstants.Market.BackendEnums.OrderStatus.EXPIRED){
        return true
    }
    else{
        return false;
    }
}
}
isOrderActive(k: number) {
  if(this.getOrdersAndTradesCurrentTable() === AppConstants.Market.Tables.OrdersAndTrades.State.BASKET || this.getOrdersAndTradesCurrentTable() === AppConstants.Market.Tables.OrdersAndTrades.State.TRADES ||this.getOrdersAndTradesCurrentTable() === AppConstants.Market.Tables.OrdersAndTrades.State.ALL_TRADES){
    return true;
  }
  const selectedOrderDto = this.orderStateService.getMyOrdersArray()[k]
 if(selectedOrderDto.orderStatus ==  AppConstants.Market.BackendEnums.OrderStatus.ACTIVE || selectedOrderDto.orderStatus === AppConstants.Market.BackendEnums.OrderStatus.PARTLY_MATCHED){
    return true
 }
 else{
    return false;
 }
}

 onRowClicked(e: MouseEvent, row, finished: boolean, k) {
     this.close();
     let tableId: number;
     tableId = this.columns[0]['table'];
     console.log(tableId);
     let basket_Ids: number
     
     if(this.appDataService.userDto.market.includes(this.orderStateService.country)===false 
     || (this.getOrdersAndTradesCurrentTable() === AppConstants.Market.Tables.OrdersAndTrades.State.TRADES && tableId===3) 
     || (this.getOrdersAndTradesCurrentTable() === AppConstants.Market.Tables.OrdersAndTrades.State.ALL_TRADES && tableId===3)){
      return;
     }
     if (tableId!== 1 && tableId!== 3) {
         return;
     }

     this.myTitle = row['name'];
     this.selectedRowIndex = row;
     this.selectedOrderDto = this.orderStateService.getActiveProductsArray()[k];

     if (tableId === 1) {
         const dialogRef = this.dialog.open(MarketDialogComponent, {
             data: {
                 tableIdToPass: tableId,
                 orderToPlace: this.selectedOrderDto,
                 productId: this.orderStateService.getActiveProductsArray()[k]['id'],
                 rowIndex: k,
                 title: this.selectedOrderDto['name'],
                 deliveryPeriod: this.selectedOrderDto['period'],
                 date: this.selectedOrderDto['createdOn'],
                 deliveryTimeStart: this.selectedOrderDto['deliveryTimeStart'],
                 compAction: 'placeOrder',

                 orderReceived: OrderDTO
             }
         });

         dialogRef.afterClosed().subscribe(result => {
             this.orderStateService.enableOrdersByProductArray = false;
             if (result !== null) {
                 if(this.orderStateService.orderMode ==='addToBasket'){
                     this.basketDataSourceChanged.basketChangedEvent.emit('basket changed');
                 } else if(this.orderStateService.orderMode === 'orderPlaced'){
                     this.dataSourceChanged.dataSourceChangedEvent.emit('dataSource changed');
            }
            this.isPopupOpened = false;
          }

          this.isPopupOpened = false;
        });
      } else if (tableId === 3) {
        if(this.orderStateService.selectedArrayToView === 'my_orders' && this.orderStateService.getMyOrdersArray()[k]['orderType']=== AppConstants.Market.BackendEnums.OrderType.BASKET){
              let basketOrdersArray = [];
              const orderDtoToUpdate = this.orderStateService.getMyOrdersArray()[k]
              console.log(orderDtoToUpdate)
              if(orderDtoToUpdate.orderStatus!==AppConstants.Market.BackendEnums.OrderStatus.ACTIVE){
                return
             }
              const basketId = orderDtoToUpdate['basket']['id']
              this.orderStateService.basketIdFromBasketDTOInOrdersArray = basketId
              this.basketService.getOrdersByBasketId(basketId).subscribe(
                response => {
                    basketOrdersArray = response
                    console.log(basketOrdersArray['orders'])
                    this.orderStateService.resetBasketArray();
                    this.orderStateService.setBasketParameters(null)
                    for(let i=0; i<basketOrdersArray['orders'].length ; i++){
                        this.orderStateService.addToBasket(basketOrdersArray['orders'][i])
                    }
                        this.orderStateService.setLastOrderDto(basketOrdersArray['orders'][0]);
                        this.orderStateService.basketType = basketOrdersArray['basketType']
                        this.orderStateService.processMode = AppConstants.Market.UILabels.ProcessMode.UPDATE_BASKET_FROM_ORDERS_ARRAY;
                        if(basketOrdersArray['metadata'] === '{}'){
                          this.orderStateService.setBasketParameters(null)
                        }
                        else{
                          this.orderStateService.setBasketParameters(JSON.parse(basketOrdersArray['metadata']));
                        }
                        //this.displayDropDownOption.displayDropDownOptionEvent.emit('display temporary basket array.')
                        this.appDataService.setOrdersAndTradesTableSelection(AppConstants.Market.Tables.OrdersAndTrades.State.BASKET)
                });
        }
        else{
        if (this.orderStateService.selectedArrayToView === 'my_orders') {
            this.selectedOrderDto = this.orderStateService.getMyOrdersArray()[k];
            console.log(this.selectedOrderDto)
            if(this.selectedOrderDto.orderStatus!==AppConstants.Market.BackendEnums.OrderStatus.ACTIVE){
               return
            }
            this.orderStateService.orderMode ='getSingleOrder'
            if( this.selectedOrderDto['basket'] !== null){
                basket_Ids = this.selectedOrderDto['basket']['id']
            }   
        }
        else {
            this.selectedOrderDto = this.orderStateService.getBasketTableArray()[k];
            this.orderStateService.orderMode = 'getBasketOrder'
        }


        const dialogRef = this.dialog.open(MarketDialogComponent, {
          data: {
            tableIdToPass: tableId,
            basketIds: basket_Ids ,
            currentRow: row,
            rowIndex: k,
            orderToUpdate: this.selectedOrderDto,
            arrayViewInDialog: this.orderStateService.selectedArrayToView,
            compAction: 'updateOrder',
            placeOrder: ProductBookDTO,
          }
        });

        dialogRef.afterClosed().subscribe(result => {
          this.orderStateService.enableOrdersByProductArray = false;
          if (this.orderStateService.orderMode === 'getBasketOrder') {
              this.basketDataSourceChanged.basketChangedEvent.emit('basket changed');
              this.basketDataSourceToDisplay = new MatTableDataSource(
                  this.orderStateService.getBasketTableArray()
              );

          }
          else{
            this.dataSourceChanged.dataSourceChangedEvent.emit('dataSource changed');
          }
          
          this.isPopupOpened = false;
        });
      }
    }
  }


  getOrdersAndTradesCurrentTable() {
      return this.orderStateService.selectedArrayToView
  }

  onNoClick() {
    this.hideBox = 0;
    this.modalIndex = -10;
    this.selectedMarketTypeChoice = 'limit';
    this.selectedBasketTypeChoice = 'none';
    this.icebergNotSelected = 0;
    this.volume_constrained = 0;
    this.cumulative_volume_constrained = 0;
    this.exclusive = 0;
    this.linked_group = 0;
    this.icebergIndex = -5;
    this.volumeIndex = -5;
    this.cumulativeIndex = -5;
  }

  ordersBelongToSelectedBasket(orderIndex: number){
   const selectedOrder = this.orderStateService.getMyOrdersArray()[orderIndex];

   if(selectedOrder != null && selectedOrder.basket != null && selectedOrder.basket.id != null){
      return selectedOrder.basket.id == this.selectedBasketId
   }
   else{
     return false;
   }
  }

  @ViewChild('apt', { static: false} ) public apt: ProductTransactionsComponent;
  @ViewChild('myModal',{ static: false} ) myModal:ElementRef;

  showProductTimeline(k) {
  this.close();
  let elementRef:any;
  elementRef =  $(this.myModal.nativeElement);
  elementRef.modal('show');
  this.dataFromRow = this.orderStateService.getActiveProductsArray()[k];
  this.apt.refreshTransactionsForProduct(this.dataFromRow.id);
  }

  showOrderTimeline(k) {
    let elementRef:any;
    elementRef =  $(this.myModal.nativeElement);
    elementRef.modal('show');
    this.dataFromRow = this.orderStateService.getMyOrdersArray()[k];
    this.apt.refreshTransactionsForProduct(this.dataFromRow.product.id);
    this.apt.orderIdsAllFilter = this.dataFromRow.id.toString();
  }

  removeOrder(e: MouseEvent, row, finished: boolean, k){
   this.close()
   console.log(k)
   console.log(row)
    let tableId: number;
    tableId = this.columns[0]['table'];
    console.log(tableId);
    let basket_Ids : number
    this.myTitle = row['name'];
    this.selectedRowIndex = row;
    if(this.orderStateService.selectedArrayToView === 'my_orders' && this.orderStateService.getMyOrdersArray()[k]['orderType']=== AppConstants.Market.BackendEnums.OrderType.BASKET){
      let basketOrdersArray = [];
      const orderDtoToUpdate = this.orderStateService.getMyOrdersArray()[k]
      const basketId = orderDtoToUpdate['basket']['id']
      this.orderStateService.basketIdFromBasketDTOInOrdersArray = basketId
      this.basketService.getOrdersByBasketId(basketId).subscribe(
        response => {
           const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
            data: {
              userCommand: 'deactivateBasketFromList'
            }
          });

          dialogRef.afterClosed().subscribe(result => {
            if (this.orderStateService.getResponse() === 'no') {
              return;
            }
            this.basketService.removeBasket(basketId).subscribe(removedData =>{
              console.log(removedData)
              this.myOrdersService.getmyOrders().subscribe(ordersData => {
                console.log(ordersData);
              });
              this.appDataService.updateMarketTables()
              this.isPopupOpened = false;

          })
            this.isPopupOpened = false;
          });
        });
    }
    else{
        if(this.orderStateService.selectedArrayToView === 'my_orders'){
          this.selectedOrderDto = this.orderStateService.getMyOrdersArray()[k];
          this.orderStateService.orderMode ='getSingleOrder'
        }
        else{
            console.log('basket here')
            this.selectedOrderDto = this.orderStateService.getBasketTableArray()[k];
            this.orderStateService.orderMode = 'getBasketOrder'
        }

        console.log(this.selectedOrderDto)

        console.log(row.id)
        const idToUpdate = this.selectedOrderDto['id']

        if(this.orderStateService.orderMode === 'getBasketOrder'){
          const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
            data: {
              basketItemIndex: k,
              userCommand: 'clearSpecificRow'
            }
          });

          dialogRef.afterClosed().subscribe(result => {
            if (this.orderStateService.getResponse() === 'no') {
              return;
            }

          if (result !== null) {
              this.basketDataSourceChanged.basketChangedEvent.emit('basket changed');
              this.updatedBasketDataSource = new MatTableDataSource(
                this.orderStateService.getBasketTableArray()
              );
              this.orderStateService.setDataSourceToUpdate(
                this.updatedBasketDataSource
              );

              console.log(this.updatedBasketDataSource);
              this.isPopupOpened = false;
          }

          this.isPopupOpened = false;
        // this.dialogRef.close();
          });
    }
    else{
        const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
          data: {
            orderItemIndex: k,
            userCommand: 'clearSpecificOrderFromList'
          }
        });

        dialogRef.afterClosed().subscribe(result => {
          if (this.orderStateService.getResponse() === 'no') {
            return;
          }
          this.myOrdersService.removeOrderFromList(idToUpdate).subscribe(updatedData =>{
            console.log(updatedData)
            this.myOrdersService.getmyOrders().subscribe(ordersData => {
              console.log(ordersData);
            });
            this.appDataService.updateMarketTables()
            this.isPopupOpened = false;

        })
          this.isPopupOpened = false;
        });
  }
 }
}
}
