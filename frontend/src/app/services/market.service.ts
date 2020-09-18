import { Injectable } from '@angular/core';
import {CrudService} from './crud.service';
import {Observable, throwError} from 'rxjs';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {AppConstants} from 'app/shared/config/app-constants';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class MarketService extends CrudService<any> {

   constructor(http: HttpClient,
     private localHttp: HttpClient) {
    super(http, 'users');
  }

  get( ): Observable<boolean> {
    return this.http.get<boolean>(
      AppConstants.CRM_CENTRAL_URL + '/modules/market/settings/log-bme-commands-to-websockets' );
  }


  put(connectionState) {

    const httpOptions = {
      headers: new HttpHeaders(
          { 'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + UserService.getJwt()}
        )
    };
    return this.http
    .put(AppConstants.CRM_CENTRAL_URL +  '/modules/market/settings/log-bme-commands-to-websockets?connection-state='+connectionState , httpOptions );

  }

}
