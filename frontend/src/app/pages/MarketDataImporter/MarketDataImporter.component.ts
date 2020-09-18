import {Component, ElementRef, OnInit, ViewChild, EventEmitter} from '@angular/core';
import {OrderUploadService} from 'app/services/OrderUpload.service';
import {MatPaginator, MatSort, MatTableDataSource, MatDialog} from '@angular/material';
import * as moment from 'moment'
import {NotificationToastService} from 'app/shared/utils/notification-toast-service';
import { MarketCountryType, UpdateSignals, AppType } from 'app/shared/config/app-constants';
import { Subscription } from 'rxjs';
import { AppDataService } from 'app/services/app-data.service';
import { DeleteByFileDialogComponent } from '../market/components/delete-by-file-dialog/delete-by-file-dialog.component';
import { MarketConfirmationDialogComponent } from '../market/components/market-confirmation-dialog/market-confirmation-dialog.component';
import { OrderStateService } from 'app/services/orderState.service';

@Component({
  selector: 'app-MarketDataImporter',
  templateUrl: './MarketDataImporter.component.html',
  styleUrls: ['./MarketDataImporter.component.scss']
})
export class MarketDataImporterComponent implements OnInit {


  dataSource;
  totalElements: any ;
  displayedColumns = [
    'actions','file_id','order_ID', 'action', 'product_name', 'orderType', 'quantity',
    'price', 'orderDirection', 'timeStamp', 'basket_ID', 'basket_Type', 'status', 'order_id'];

  public productsViewMarketCode: string = MarketCountryType.DEFAULT_MARKET_CYPRUS;
  updateEmmiterSubscriptionId: Subscription
  updateEmmiter = new EventEmitter();
  userRolesMarket = []
  noElementsExist = true;
  displayedData = ''

  formatDate(timestamp: string) {
    return moment(new Date(timestamp)).format('HH:mm  DD/MM/YYYY')
  }

  @ViewChild(MatSort, null) sort: MatSort;
  @ViewChild(MatPaginator, null) paginator: MatPaginator;
  @ViewChild('fileSelector', {static: false})
  fileSelector: ElementRef;

  constructor(private orderUploadService: OrderUploadService,
              private notificationToastService: NotificationToastService,
              private dialog: MatDialog,
              public appDataService: AppDataService,
              public orderStateService: OrderStateService) {
                this.updateEmmiterSubscriptionId =
                this.appDataService.updateEmmiter.subscribe(event => {
                if (event === UpdateSignals.USER_DTO || event === UpdateSignals.CRM_LOCAL_ACTIVE_STATE ||
                    event === UpdateSignals.APP_TYPE || event === UpdateSignals.DELETE_MARKET_DATA_BY_FILE_ID) {
                    this.onUpdateSignal()
                }
                })
  }

  ngOnInit() {
    this.appDataService.setAppType(AppType.MARKET)
    this.onUpdateSignal()
    this.paginator.pageSize = 100;
    this.refresh();
  }
  onUpdateSignal(){
    
    if(this.appDataService.userDto === null){
      return;
    }
    else{
        this.refresh()
        this.userRolesMarket = this.appDataService.userDto.rolesMarket.split(',');
        this.appDataService.defaultMarket = this.appDataService.userDto.defaultMarket
        if(this.appDataService.defaultMarket=== MarketCountryType.DEFAULT_MARKET_BULGARIA){
          this.productsViewMarketCode =  MarketCountryType.DEFAULT_MARKET_BULGARIA
        }
        else{
          this.productsViewMarketCode =  MarketCountryType.DEFAULT_MARKET_CYPRUS
        } 
    }
  }

  refresh() {

    let sort = '';
    let direction = 'desc';
    if (this.sort.active !== undefined) {
      this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
      sort = this.sort.active;
      direction = this.sort.direction;
    }
    let pageSize = 5;
    if (this.paginator.pageSize !== undefined) {
      pageSize = this.paginator.pageSize;
    }

    this.orderUploadService.getPage(this.productsViewMarketCode,this.paginator.pageIndex, pageSize, sort,
        direction).subscribe(data => {
      this.dataSource = new MatTableDataSource(data.content);
      this.dataSource.sort = this.sort;
      this.totalElements = data.totalElements;
    });
  }

  deleteAll() {
    const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
      data:{
        userCommand:'deleteAllMarketData'
      }
    })
    dialogRef.afterClosed().subscribe(result => {
      if(this.orderStateService.getResponse()==='no'){
        return
      }

      this.orderUploadService.deleteAll(this.productsViewMarketCode).subscribe(responce => {
        this.refresh();
        this.notificationToastService.showNotification(
          'Data deleted',
          'delete',
          'top',
          'center',
          'danger');
      });
    });
  }

  uploadFile(files: FileList) {


    this.orderUploadService.upload(files.item(0), this.productsViewMarketCode).subscribe(data => {
          this.refresh();

          this.notificationToastService.showNotification(
              'File uploaded',
              'thumb_up_alt',
              'top',
              'center',
              'success');

        },
        responce => {
          if (responce.status === 500) {

            this.notificationToastService.showNotification(
                responce.error.message,
                'error',
                'top',
                'center',
                'danger');

          }
        }
    );

    this.fileSelector.nativeElement.value = '';

  }
  getToolTipData(row){
    this.displayedData = row.messageInfoName  
    
    
  }

  deleteGroup(row) {
    this.orderUploadService.deleteGroup(row.id).subscribe(response => {
      this.refresh();
    })
  }
  deleteByFileId(){
    if(this.totalElements === 0){
      this.notificationToastService.showNotification('There are no uploaded data, please try again.',
      'error',
      'top',
      'center',
      'danger');
    return;
    }
    const dialogRef = this.dialog.open(DeleteByFileDialogComponent, {
      data:{
        listWithFileIds: this.dataSource
      }
    })
    dialogRef.afterClosed().subscribe(result => {
     this.refresh();
    });
  }


  showHelpPdf() {

    let height = window.innerHeight;
    let width = window.innerWidth;

    if (height >= 500) {
      height = 500;
    }
    if (width >= 500) {
      width = 500;
    }

    window.open('/assets/pdf/ImportOrdersCsvFiles.pdf', '_blank',
        'toolbar=no, location=no, directories=no, ' +
        'status=no, menubar=no, scrollbars=no, ' +
        'resizable=no, copyhistory=no,' +
        ' height=' + height + ',' +
        ' width=' + width + ',' +
        ' left=' + window.screenLeft + ',' +
        ' top=' + window.screenTop + ',');
  }

}
