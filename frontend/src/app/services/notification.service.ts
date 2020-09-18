import {UserService} from 'app/services/user.service';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {CrudService} from '../services/crud.service';
import {NotificationDto} from 'app/shared/dto/notification-dto';
import {AppConstants} from 'app/shared/config/app-constants';

@Injectable({
  providedIn: 'root'
})
export class NotificationService extends CrudService<NotificationDto> {

  private serviceUrl = AppConstants.CRM_CENTRAL_URL + '/notifications/';

  constructor(
      public http: HttpClient) {
    super(http, 'notifications');
  }

  getAll(): Observable<any> {
    return this.http.get(this.serviceUrl + 'received-by/current/all?status=all');
  }

  getNotification(id): Observable<any> {
    return this.http.get(this.serviceUrl + 'notification-id/' + id);
  }

  getPending(): Observable<any> {
    return this.http.get(this.serviceUrl + 'received-by/current/all?status=pending');
  }

  getPage(page: any, size: any, sort: any, direction: any): Observable<any> {
    return this.http.get(`${this.serviceUrl}received-by/current?sort=${sort}&direction=${direction}&size=${size}&page=${page}&status=all`);
  }

  public editStatus(id: number, status: string): Observable<any> {
    const url = this.serviceUrl + 'status?notification-id=' + id + '&status=' + status;
    const httpOptions = {
      headers: new HttpHeaders(
          {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + UserService.getJwt()
          }
      )
    };
    return this.http.put(url, httpOptions);
  }

  public editAllStatuses(status: string): Observable<any> {
    const url = this.serviceUrl + 'status/all?' + '&status=' + status;
    const httpOptions = {
      headers: new HttpHeaders(
          {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + UserService.getJwt()
          }
      )
    };
    return this.http.put(url, httpOptions);
  }

}
