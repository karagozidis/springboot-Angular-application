import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ImportOrderDTO} from 'app/shared/dto/import-order-dto';
import {AppConstants} from 'app/shared/config/app-constants';

@Injectable({
  providedIn: 'root'
})
export class OrderUploadService {

  constructor(public http: HttpClient) {
  }

  getAll(marketCode: string): Observable<ImportOrderDTO[]> {
    return this.http.get<ImportOrderDTO[]>(AppConstants.CRM_CENTRAL_URL + '/modules/market/import/orders?market-code=' + marketCode);
  }


  getPage(marketCode: string, page: any, size: any, sort: any, direction: any): Observable<any> {
    return this.http.get<any>(`${AppConstants.CRM_CENTRAL_URL}/modules/market/import/orders/page?market-code=${marketCode}&sort=${sort}&direction=${direction}&size=${size}&page=${page}`);
  }

  deleteAll(marketCode: string): Observable<{}> {
    return this.http.delete(AppConstants.CRM_CENTRAL_URL + '/modules/market/import/orders?market-code=' + marketCode);
  }

  deleteGroup(id: string): Observable<{}> {
    return this.http.delete(AppConstants.CRM_CENTRAL_URL + '/modules/market/import/orders/group-by-id?id=' + id);
  }

  deleteByFileId(id:string): Observable<any>{
    return this.http.delete(AppConstants.CRM_CENTRAL_URL +'/modules/market/import/orders/by-message-info-id?message-info-id='+id)
  }


  upload(fileToUpload: File, marketCode: string): Observable<boolean> {

    const httpOptions = {
      headers: new HttpHeaders(
          {
            'Content-Type': 'multipart/form-data'
          }
      )
    };

    const serviceUrl = AppConstants.CRM_CENTRAL_URL + '/modules/market/import/orders?market-code=' + marketCode
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
    return this.http
    .post(serviceUrl, formData, httpOptions)
    .map(() => {
      return true;
    });
  }

}

