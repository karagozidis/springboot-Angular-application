import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {MessageInfoDTO} from 'app/shared/dto/message-info-dto';
import {AppConstants} from 'app/shared/config/app-constants';
import { ActiveSubstationForecastUploadDTO } from 'app/shared/dto/activeSubstationForecastUpload-dto';

@Injectable({
  providedIn: 'root'
})
export class FileUploadRegistryService {
  private serviceUrl = AppConstants.CRM_CENTRAL_URL + '/modules/file-uploader';
  private forecastFileUrl = AppConstants.CRM_CENTRAL_URL +'/modules/active-substation-forecasts';

  constructor(public http: HttpClient) {
  }

  getAll(): Observable<MessageInfoDTO[]> {
    return this.http.get<MessageInfoDTO[]>(this.serviceUrl);
  }


  uploadFileToCrmCentral(fileToUpload: File, userGroup:number): Observable<boolean> {

    const httpOptions = {
      headers: new HttpHeaders(
          {
            'content-type': 'multipart/form-data'
          }
      )
    };

    const serviceUrl = AppConstants.CRM_CENTRAL_URL + '/modules/file-uploader/files?user-group='+userGroup
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
    return this.http
        .post(serviceUrl, formData, httpOptions)
        .map(() => {
          return true;
        });
  }

  uploadFileToCrmLocal(fileToUpload: File, userGroup:number): Observable<boolean> {

    const httpOptions = {
      headers: new HttpHeaders(
          {
            'content-type': 'multipart/form-data'
          }
      )
    };

    const serviceUrl = AppConstants.CRM_LOCAL_URL + '/modules/file-uploader/files?user-group='+userGroup
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
    return this.http
        .post(serviceUrl, formData, httpOptions)
        .map(() => {
          return true;
        });
  }
  getForecastFile(): Observable<MessageInfoDTO[]>{
      return this.http.get<MessageInfoDTO[]>(this.forecastFileUrl);
  }

  
  uploadForecastFileToCrmLocal(fileToUpload: File, activeSubstationForecastFileDTO: ActiveSubstationForecastUploadDTO): Observable<boolean> {

    const httpOptions = {
      headers: new HttpHeaders(
          {
            'content-type': 'multipart/form-data'
          }
      )
    };

    const serviceUrl = AppConstants.CRM_LOCAL_URL + '/modules/active-substation-forecasts/files?user-group='+ activeSubstationForecastFileDTO.usergroup+
     '&tag='+ activeSubstationForecastFileDTO.tag + '&date-from='+activeSubstationForecastFileDTO.forecastDate + 
     '&date-to='+ activeSubstationForecastFileDTO.dateTo + 
     '&resolution=' + activeSubstationForecastFileDTO.res +  
     '&type='+ activeSubstationForecastFileDTO.type + 
     '&description='+ activeSubstationForecastFileDTO.description

    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
    return this.http
        .post(serviceUrl, formData, httpOptions)
        .map(() => {
          return true;
        });
  }



  uploadOrder(fileToUpload: File): Observable<boolean> {
    const serviceUrl = AppConstants.CRM_CENTRAL_URL + '/modules/market/import/orders'
    const formData: FormData = new FormData();

    const httpOptions = {
      headers: new HttpHeaders(
          {
            'content-type': 'multipart/form-data'
          }
      )
    };

    formData.append('file', fileToUpload, fileToUpload.name);
    return this.http
        .post(serviceUrl, formData, httpOptions)
        .map(() => {
          return true;
        });
  }

  delete(id: number): Observable<{}> {
    const url = AppConstants.CRM_CENTRAL_URL + '/modules/file-uploader?id=' + id;
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.delete(url, httpOptions);
  }
  deleteForecastData(id: number): Observable<{}> {
    const url = AppConstants.CRM_CENTRAL_URL + '/modules/active-substation-forecasts?unique-id=' + id;
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.delete(url, httpOptions);
  }


  downloadFromCrmCentral(id: string, prefix: string): Observable<HttpResponse<Blob>> {
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'application/' + prefix);

    return this.http.get(AppConstants.CRM_CENTRAL_URL + '/modules/file-uploader/download?id=' + id, {
      headers: headers,
      observe: 'response',
      responseType: 'blob'
    });
  }

  downloadFromCrmLocal(uniqueId: string, prefix: string): Observable<HttpResponse<Blob>> {
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'application/' + prefix);

    return this.http.get(AppConstants.CRM_LOCAL_URL + '/modules/file-uploader/download?unique-id=' + uniqueId, {
      headers: headers,
      observe: 'response',
      responseType: 'blob'
    });
  }


  downloadForecastDataFromCrmLocal(uniqueId: string, prefix: string): Observable<HttpResponse<Blob>> {
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'application/' + prefix);

    return this.http.get(AppConstants.CRM_LOCAL_URL + '/modules/active-substation-forecasts/download?unique-id=' + uniqueId, {
      headers: headers,
      observe: 'response',
      responseType: 'blob'
    });
  }
  
    getHistoricalMarketData(): Observable<MessageInfoDTO[]> {
        return this.http.get<MessageInfoDTO[]>(this.serviceUrl+'/by-tags?tags=historical_market_data,encrypted_historical_market_data');
    }


    uploadHistoricalDataToCrmCentral(fileToUpload: File, userGroup:number) {
        const httpOptions = {
            headers: new HttpHeaders(
                {
                    'content-type': 'multipart/form-data'
                }
            )
        };

        const serviceUrl = AppConstants.CRM_CENTRAL_URL + '/modules/file-uploader/files-by-tag?tag=historical_market_data&user-group='+userGroup
        const formData: FormData = new FormData();
        formData.append('file', fileToUpload, fileToUpload.name);
        return this.http
            .post(serviceUrl, formData, httpOptions)
            .map(() => {
                return true;
            });
    }

    uploadHistoricalDataFileToCrmLocal(fileToUpload: File, userGroup:number): Observable<boolean> {

        const httpOptions = {
            headers: new HttpHeaders(
                {
                    'content-type': 'multipart/form-data'
                }
            )
        };

        const serviceUrl = AppConstants.CRM_LOCAL_URL + '/modules/file-uploader/files-by-tag?tag=encrypted_historical_market_data&user-group='+userGroup
        const formData: FormData = new FormData();
        formData.append('file', fileToUpload, fileToUpload.name);
        return this.http
            .post(serviceUrl, formData, httpOptions)
            .map(() => {
                return true;
            });
    }



}
