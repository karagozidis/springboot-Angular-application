import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Observable} from 'rxjs';
import {AppConstants} from 'app/shared/config/app-constants';
import {CrudService} from '../services/crud.service';
import {MessageInfoDTO} from 'app/shared/dto/message-info-dto';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

/**
 * A service providing functionality for the user of the application, including authentication,
 * authorisation and session management.
 */
@Injectable({
  providedIn: 'root'
})
export class MessagesService extends CrudService<MessageInfoDTO> {
  private serviceUrl = AppConstants.CRM_CENTRAL_URL + '/message-infos/all';
  private servicePage = AppConstants.CRM_CENTRAL_URL + '/message-infos/page';
  private deleteUrl = AppConstants.CRM_CENTRAL_URL + '/message-infos'

  constructor(http: HttpClient, private localHttp: HttpClient, private jwtService: JwtHelperService) {
    super(http, 'users');
  }

  getAllMessages(): Observable<MessageInfoDTO[]> {
    return this.http.get<MessageInfoDTO[]>(this.serviceUrl);
  }


  getPage(name: string, tag: string, from: string, to: string, page: any, size: any, sort: any, direction: any): Observable<any> {
    return this.http.get<any>(
        `${this.servicePage}?sort=${sort}&direction=${direction}&size=${size}&page=${page}&name=${name}&tag=${tag}&from=${from}&to=${to}`);
  }

  deleteMessage(id: number): Observable<{}> {
    const url = `${this.deleteUrl}/${id}`;
    return this.http.delete(url, httpOptions);
  }


}
