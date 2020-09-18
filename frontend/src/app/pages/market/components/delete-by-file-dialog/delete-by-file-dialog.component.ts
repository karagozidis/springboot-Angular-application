import { Component, OnInit, EventEmitter, Inject } from '@angular/core';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material';
import { MarketDataImporterComponent } from 'app/pages/MarketDataImporter/MarketDataImporter.component';
import { OrderStateService } from 'app/services/orderState.service';
import { MarketConfirmationDialogComponent } from '../market-confirmation-dialog/market-confirmation-dialog.component';
import { NotificationToastService } from 'app/shared/utils/notification-toast-service';
import { OrderUploadService } from 'app/services/OrderUpload.service';
import { UpdateSignals } from 'app/shared/config/app-constants';

@Component({
  selector: 'app-delete-by-file-dialog',
  templateUrl: './delete-by-file-dialog.component.html',
  styleUrls: ['./delete-by-file-dialog.component.scss']
})
export class DeleteByFileDialogComponent implements OnInit {
  selectedFileId: string;
  isPopupOpened = true;
  updateEmmiter = new EventEmitter();
  fileIdsArray = [];
  fileIdFound = false;

  constructor(private dialogRef: MatDialogRef<MarketDataImporterComponent>,
    public orderStateService: OrderStateService,
    private notificationToastService: NotificationToastService,
    private dialog: MatDialog,
    private orderUploadService: OrderUploadService,
    @Inject(MAT_DIALOG_DATA) public modalDialogData: any) {
      
     }
  
  onNoClick(): void {
    this.dialogRef.close();
  } 


  ngOnInit() {

    for(let i=0;i<this.modalDialogData.listWithFileIds.data.length;i++){
      this.fileIdsArray.push(this.modalDialogData.listWithFileIds.data[i])
    }
  }
  onDelete(){
    
    
    for(let k=0;k<this.fileIdsArray.length;k++){
      console.log(this.fileIdsArray[0]['messageInfoId'])
      console.log(this.selectedFileId)
      if(this.fileIdsArray[k]['messageInfoId'].toString() === this.selectedFileId){
         this.fileIdFound = true;
         break;
      }
      console.log(this.fileIdFound)
    }
    console.log(this.fileIdFound)
    if(this.fileIdFound === false){
      this.notificationToastService.showNotification('The id you submitted does not exist. Please try again.',
          'error',
          'top',
          'center',
          'danger');
        return;
    }
    if(this.selectedFileId === undefined || this.selectedFileId==null){
      this.notificationToastService.showNotification('You have not given any input. Try again.',
          'error',
          'top',
          'center',
          'danger');
        return;
    }
    if(+this.selectedFileId % 1 !== 0){
      this.notificationToastService.showNotification('Your input should be a whole number, not a decimal.',
          'error',
          'top',
          'center',
          'danger');
        return;
    }
    const dialogRef = this.dialog.open(MarketConfirmationDialogComponent, {
      data: {
        selectedFileIdToSend: this.selectedFileId,
        userCommand: 'deleteFileById'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (this.orderStateService.getResponse() === 'no') {
        return;
      }
      else{
        this.orderUploadService.deleteByFileId(this.selectedFileId).subscribe(
          response => {
            console.log(response);
            this.updateEmmiter.emit(UpdateSignals.DELETE_MARKET_DATA_BY_FILE_ID)
            this.dialogRef.close(); 
          },
          onError => {
              this.notificationToastService.showNotification(
                'An error occured. Please try again.', 'error', 'top',
                'center', 'danger');
           
          })
          console.log('You successfully deleted file with id' +this.selectedFileId)
          console.log('The dialog was closed');
          this.isPopupOpened = false;
          this.dialogRef.close();
      }
    });
    this.fileIdFound = false;
  }

}
