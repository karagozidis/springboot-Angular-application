import {Injectable} from '@angular/core';
import {MessageInfoDTO} from 'app/shared/dto/message-info-dto';
import {CrudService} from './crud.service';
import {JwtHelperService} from '@auth0/angular-jwt';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {HyppoIoMetaDataDTO} from 'app/shared/dto/HyppoIoMetaDataDTO';
import {UserService} from 'app/services/user.service';
import {AppConstants} from 'app/shared/config/app-constants';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable()
export class HistoryService extends CrudService<MessageInfoDTO> {
  selectedData: MessageInfoDTO;
  private serviceUrl = AppConstants.CRM_LOCAL_URL + '/modules/hyppo-io/history';
  private downloadUrl = ''
  private deleteUrlCentral = AppConstants.CRM_CENTRAL_URL + '/message-infos/by-unique-id';
  private deleteUrlLocal = AppConstants.CRM_LOCAL_URL + '/modules/hyppo-io?name='


  constructor(http: HttpClient, private localHttp: HttpClient, private jwtService: JwtHelperService) {
    super(http, 'users');
  }


  getHistory(): Observable<HyppoIoMetaDataDTO[]> {
    return this.http.get<HyppoIoMetaDataDTO[]>(this.serviceUrl);
  }


  downloadHyppoIoData(uuid: string): Observable<HttpResponse<Blob>> {
    let headers = new HttpHeaders();

    headers = headers.append('Accept', 'application/zip');
    headers = headers.append('Authorization', 'Bearer ' + UserService.getJwt());

    return this.http.get('http://localhost:30000/api/message-infos/download?unique-id=' + uuid, {
      headers: headers,
      observe: 'response',
      responseType: 'blob'
    });
  }

  downloadZip(name: string): Observable<HttpResponse<Blob>> {
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'application/zip');

    return this.http.get(AppConstants.CRM_LOCAL_URL + '/modules/hyppo-io/' + name + '/zip', {
      headers: headers,
      observe: 'response',
      responseType: 'blob'
    });
  }

  deleteResultInCentral(uuid: string): Observable<{}> {
    const urlCentral = `${this.deleteUrlCentral}/${uuid}`;

    return this.http.delete(urlCentral, httpOptions);
  }

  deleteResultInLocal(uuid: string): Observable<{}> {
    const urlLocal = this.deleteUrlLocal + uuid;
    return this.http.delete(urlLocal);
  }


  copyLocally(name: string): Observable<any> {

    const url = AppConstants.CRM_LOCAL_URL + '/modules/hyppo-io/copy-locally?name=' + name;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + UserService.getJwt()
      })
    };

    return this.http.get(url, httpOptions);

  }


}
