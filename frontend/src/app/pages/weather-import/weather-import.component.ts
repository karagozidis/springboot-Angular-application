import {Component, ElementRef, OnInit, ViewChild, EventEmitter} from '@angular/core';
import {DatePipe} from '@angular/common'
import {NotificationToastService} from 'app/shared/utils/notification-toast-service';
import {WeatherService} from 'app/services/weather.service';
import {MatPaginator, MatSort, MatTableDataSource, MatDialog} from '@angular/material';
import { MarketCountryType, UpdateSignals, AppType } from 'app/shared/config/app-constants';
import { Subscription } from 'rxjs';
import { AppDataService } from 'app/services/app-data.service';
import { MarketConfirmationDialogComponent } from '../market/components/market-confirmation-dialog/market-confirmation-dialog.component';
import { OrderStateService } from 'app/services/orderState.service';

@Component({
  selector: 'app-weather-import',
  templateUrl: './weather-import.component.html',
  styleUrls: ['./weather-import.component.scss']
})
export class WeatherImportComponent implements OnInit {

  public productDateFrom: Date;
  public productDateTo: Date;
  public productsViewMarketCode: string = MarketCountryType.DEFAULT_MARKET_CYPRUS;
  updateEmmiterSubscriptionId: Subscription
  updateEmmiter = new EventEmitter();

  @ViewChild(MatSort, null) sort: MatSort;
  @ViewChild(MatPaginator, null) paginator: MatPaginator;
  @ViewChild('fileSelector', {static: false})
  fileSelector: ElementRef;
  dataSource;
  displayedColumns = ['actions',
    'datetime', 'sun_radiation', 'wind_speed', 'temperature', 'sky_code','type'];

  constructor(private notificationToastService: NotificationToastService,
              private weatherService: WeatherService,
              public datePipe: DatePipe,
              private dialog: MatDialog,
              public appDataService: AppDataService,
              public orderStateService: OrderStateService) {
                this.updateEmmiterSubscriptionId =
                this.appDataService.updateEmmiter.subscribe(event => {
                if (event === UpdateSignals.USER_DTO || event === UpdateSignals.CRM_LOCAL_ACTIVE_STATE ||
                    event === UpdateSignals.APP_TYPE) {
                    this.onUpdateSignal()
                }
                })
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

    window.open('/assets/pdf/ImportWeatherCsvFiles.pdf', '_blank',
        'toolbar=no, location=no, directories=no, ' +
        'status=no, menubar=no, scrollbars=no, ' +
        'resizable=no, copyhistory=no,' +
        ' height=' + height + ',' +
        ' width=' + width + ',' +
        ' left=' + window.screenLeft + ',' +
        ' top=' + window.screenTop + ',');
  }

  ngOnInit() {
    this.appDataService.setAppType(AppType.MARKET)
    this.productDateFrom = new Date();
    this.productDateTo = new Date();
    this.onUpdateSignal();
    this.refresh();

    document.getElementById('periodFilters').click();
  }

  onUpdateSignal(){
    if(this.appDataService.userDto === null){
      return;
    }
    else{
        this.appDataService.defaultMarket = this.appDataService.userDto.defaultMarket
        if(this.appDataService.defaultMarket=== MarketCountryType.DEFAULT_MARKET_BULGARIA){
          this.productsViewMarketCode =  MarketCountryType.DEFAULT_MARKET_BULGARIA
        }
        else{
          this.productsViewMarketCode =  MarketCountryType.DEFAULT_MARKET_CYPRUS
        } 
    }
  }

  onFromSelect(event) {
    this.productDateFrom = event;
    if (this.productDateFrom > this.productDateTo) {
      this.productDateTo = this.productDateFrom;
    }
  }

  onToSelect(event) {
    this.productDateTo = event;
  }


  uploadFile(files: FileList) {
    this.weatherService.upload(files.item(0), this.productsViewMarketCode).subscribe(data => {

          this.notificationToastService.showNotification(
              'File uploaded',
              'thumb_up_alt',
              'top',
              'center',
              'success');

          this.refresh();
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

  deleteSelected() {
    const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
      data:{
        userCommand:'deleteAllWeatherData'
      }
    })
    dialogRef.afterClosed().subscribe(result => {

      if(this.orderStateService.getResponse()==='no'){
        return
      }
      const from = this.datePipe.transform(this.productDateFrom, 'yyyy-MM-dd') + 'T00:00:00Z';
      const to = this.datePipe.transform(this.productDateTo, 'yyyy-MM-dd') + 'T23:59:00Z';
  
      this.weatherService.deleteOnPeriod(from, to, this.productsViewMarketCode).subscribe(() => {
        this.refresh();
      });
    });
    
  }

  delete(id: any) {
    this.weatherService.deleteById(id).subscribe(() => {
      this.refresh();
    });
  }

  refresh() {
    const from = this.datePipe.transform(this.productDateFrom, 'yyyy-MM-dd') + 'T00:00:00Z';
    const to = this.datePipe.transform(this.productDateTo, 'yyyy-MM-dd') + 'T23:59:00Z';

    this.weatherService.getOnPeriod(from, to, this.productsViewMarketCode).subscribe(data => {
      this.dataSource = new MatTableDataSource(data);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
    });
  }

}
