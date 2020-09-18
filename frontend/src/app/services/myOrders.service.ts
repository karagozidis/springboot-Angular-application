import {HttpClient, HttpParams, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Observable} from 'rxjs';
import {AppConstants} from 'app/shared/config/app-constants';
import {LoginInfoDto} from 'app/shared/dto/login-info-dto';
import {MatDialog} from '@angular/material';
import {UserDto} from 'app/shared/dto/user-dto';
import {CrudService} from '../services/crud.service';
import {MyOrdersDTO} from 'app/shared/dto/myOrders-dto';
import 'rxjs/add/operator/map';
import {ProductDTO} from 'app/shared/dto/product-dto';
import {OrderDTO} from 'app/shared/dto/order-dto';
import {catchError} from 'rxjs/operators';
import {UpdateSingleOrderData} from 'app/pages/market/components/market-dialog/market-dialog.component';
import {OrderStateService} from './orderState.service';
import {UserService} from './user.service';
import {RandomOrderGenerationSettingsDTO} from '../shared/dto/random-order-generation-settings-dto';
import { AppDataService } from './app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';

/**
 * A service providing functionality for the user of the application, including authentication,
 * authorisation and session management.
 */
@Injectable({
  providedIn: 'root'
})
export class MyOrdersService extends CrudService<MyOrdersDTO> {
  bidTimeFrom = ''
  bidTimTo = ''
  // tslint:disable-next-line:max-line-length
  private myOrdersUrl =
      // tslint:disable-next-line:max-line-length
      AppConstants.CRM_CENTRAL_URL + '/users/current/market/orders?orderType=&bidTimeFrom=2020-01-10T16:21:45.600Z&bidTimeTo=2040-11-28T16:21:45.600Z&market-code=';
  private myOrdersUrl_ = AppConstants.CRM_CENTRAL_URL + '/users/current/market/orders';
  // tslint:disable-next-line:max-line-length
  private putUrl = AppConstants.CRM_CENTRAL_URL + '/modules/market/orders'
  private deleteUrl = AppConstants.CRM_CENTRAL_URL + '/modules/market/orders'

  constructor(
      http: HttpClient,
      private localHttp: HttpClient,
      private appDataService: AppDataService,
      private jwtService: JwtHelperService,
      private orderStateService: OrderStateService
  ) {
    super(http, 'users');
  }

  getmyOrders(): Observable<any> {
    let params = new HttpParams()
    params = params.set('orderType', '')
    params = params.set('bidTimeFrom', this.orderStateService.from)
    params = params.set('bidTimeTo', this.orderStateService.to)
    params = params.set('market-code', this.orderStateService.country)
    console.log(this.http.get<any>(this.myOrdersUrl_, {params: params}))
    return this.http.get<any>(this.myOrdersUrl_, {params: params});
  }

  updateOrder(orderToUpdate: UpdateSingleOrderData, index: number) {
    return this.http.put<any>(`${this.putUrl}/${index}`, orderToUpdate)
  }

  removeOrderFromList(index: string) {
    return this.http.delete<any>(this.deleteUrl + '?order-id=' + index)
  }

  generateRandom(randomOrderGenerationSettingsDTO: RandomOrderGenerationSettingsDTO) {
    const httpOptions = {
      headers: new HttpHeaders(
          {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + UserService.getJwt()
          }
      )
    };

    return this.http
    .post(
        AppConstants.CRM_CENTRAL_URL + '/modules/market/orders/generate-random',
        randomOrderGenerationSettingsDTO, httpOptions);

  }


}
