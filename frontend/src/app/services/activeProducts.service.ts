import { MarketObject } from './../shared/dto/market-object';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Observable, throwError} from 'rxjs';
import {AppConstants} from 'app/shared/config/app-constants';
import {CrudService} from './crud.service';
import 'rxjs/add/operator/map';
import { ProductBookDTO } from 'app/shared/dto/product-book-dto';
import { OrderDTO } from 'app/shared/dto/order-dto';
import { catchError } from 'rxjs/operators';
import { UserService } from './user.service';
import { OrderStateService } from './orderState.service';
/**
 * A service providing functionality for the user of the application, including authentication,
 * authorisation and session management.
 */
@Injectable({
  providedIn: 'root'
})
export class ActiveProductsService extends CrudService<ProductBookDTO> {
  // private endpoint = '/modules/market'

  private serviceUrl = AppConstants.CRM_CENTRAL_URL + '/modules/market/products/active/?market-code=';

  private postUrl = AppConstants.CRM_CENTRAL_URL + '/modules/market/orders/';
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + UserService.getJwt()
      })
    };

  // tslint:disable-next-line:max-line-length
  constructor(http: HttpClient, private localHttp: HttpClient, private jwtService: JwtHelperService, private orderStateService: OrderStateService) {
    super(http, 'users');
  }

   getActiveProducts(): Observable<any> {
   
    return this.http.get<any>(this.serviceUrl + this.orderStateService.country);
    }

    getTransactions( id: string ): Observable<MarketObject[]> {
      return this.http.get<MarketObject[]>(
        AppConstants.CRM_CENTRAL_URL + '/modules/market/products/market-transactions' + '?product-id=' + id );
    }

    getProduct( id: string ): Observable<any> {
      return this.http.get<any>(
        AppConstants.CRM_CENTRAL_URL + '/modules/market/products/by-id' + '?id=' + id );
    }

    getUserTransactions( id: string ): Observable<MarketObject[]> {
      return this.http.get<MarketObject[]>(
        AppConstants.CRM_CENTRAL_URL + '/modules/market/products/market-transactions/user-relevant' + '?product-id=' + id );
    }

    getUsersProducts(showProductDateFrom: string, showProductDateTo: string, productsMarketCode: string): Observable<any> {
      return this.http.get<any>(
        AppConstants.CRM_CENTRAL_URL + '/modules/market/products/user-relevant/' +
        '?delivery-time-start-from=' + showProductDateFrom +
        '&delivery-time-start-to=' + showProductDateTo +
        '&market-code=' + productsMarketCode
        );
      }

    getProducts(showProductDateFrom: string, showProductDateTo: string, productsMarketCode: string): Observable<any> {

      return this.http.get<any>(
        AppConstants.CRM_CENTRAL_URL + '/modules/market/products/' +
        '?delivery-time-start-from=' + showProductDateFrom +
        '&delivery-time-start-to=' + showProductDateTo +
        '&market-code=' + productsMarketCode
        );
      }

    placeOrder(order: OrderDTO)  {
      return this.http.post<any>( this.postUrl, JSON.stringify(order), this.httpOptions).pipe(catchError(this.errorHandler));
    }

    errorHandler(error: HttpErrorResponse) {
      return throwError(error);
    }

    generateDay(atDate: string, period: string, marketCode: string ) {

      const httpOptions = {
        headers: new HttpHeaders(
            { 'Content-Type': 'application/json',
              'Authorization': 'Bearer ' + UserService.getJwt()}
          )
      };

      return this.http
      .post(
        AppConstants.CRM_CENTRAL_URL + '/modules/market/products/generate-day' +
        '?date=' + atDate +
        '&delivery-period=' + period +
        '&market-code=' + marketCode,
       httpOptions);
    }

    getGraph(productId: any) {
        return this.http.get<MarketObject[]>(
            AppConstants.CRM_CENTRAL_URL + '/modules/market/products/graph' + '?product-id=' + productId );
    }
}
