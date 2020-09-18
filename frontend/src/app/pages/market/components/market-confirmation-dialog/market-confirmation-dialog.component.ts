import { Component, OnInit, Inject } from '@angular/core';
import { MarketDialogComponent } from '../market-dialog/market-dialog.component';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { MarketTableComponent } from '../market-table/market-table.component';
import { OrderStateService } from 'app/services/orderState.service';
import { MyOrdersService } from 'app/services/myOrders.service';
import { AppDataService } from 'app/services/app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';


@Component({
  selector: 'app-market-confirmation-dialog',
  templateUrl: './market-confirmation-dialog.component.html',
  styleUrls: ['./market-confirmation-dialog.component.scss']
})
export class MarketConfirmationDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<MarketDialogComponent, MarketTableComponent>,
    public orderStateService: OrderStateService,
    public myOrdersService: MyOrdersService,
    public appDataService: AppDataService,
    @Inject(MAT_DIALOG_DATA) public modalDialogData: any) {
      dialogRef.disableClose = true
     }

  ngOnInit() {

  }

  onNoClick() {
    this.orderStateService.setResponse('no')
    this.dialogRef.close();
  }

  removeOrderFromBasket(){
   this.orderStateService.removeOrderFromBasketArray(this.modalDialogData.basketItemIndex) ;
   this.orderStateService.setResponse('yes')
   this.dialogRef.close();
  }

  removeOrderFromList(){
    this.orderStateService.setResponse('yes')
    this.dialogRef.close();
  }

  clearAll(){
    this.orderStateService.resetBasketArray();
    this.orderStateService.setResponse('yes')
    this.dialogRef.close();

  }

  postBasket(){
    this.orderStateService.setResponse('yes')
    this.dialogRef.close();
  }

  updateBasket(){
    this.orderStateService.setResponse('yes')
    this.dialogRef.close();
  }

  modifyBasket(){
    this.orderStateService.resetBasketArray();
    this.orderStateService.setResponse('yes')
    this.orderStateService.addToBasket(this.modalDialogData.updatedDTO)
    this.dialogRef.close();
  }

  deactivateBasket(){
    this.orderStateService.setResponse('yes')
    this.dialogRef.close();
  }

  updateRow(){
    this.orderStateService.setResponse('yes')
    this.dialogRef.close();
  }

  deleteFileById(){
    this.orderStateService.setResponse('yes')
    this.dialogRef.close();
  }

  deleteAllMarketData(){
    this.orderStateService.setResponse('yes')
    this.dialogRef.close();
  }
  deleteAllWeatherData(){
    this.orderStateService.setResponse('yes')
    this.dialogRef.close();
  }
}
