import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Observable, throwError} from 'rxjs';
import {AppConstants} from 'app/shared/config/app-constants';
import {LoginInfoDto} from 'app/shared/dto/login-info-dto';
import {MatDialog} from '@angular/material';
import {UserDto} from 'app/shared/dto/user-dto';
import {CrudService} from './crud.service';
import { MyOrdersDTO } from 'app/shared/dto/myOrders-dto';
import 'rxjs/add/operator/map';
import { ProductDTO } from 'app/shared/dto/product-dto';
import { ProductBookDTO } from 'app/shared/dto/product-book-dto';
import { OrderDTO } from 'app/shared/dto/order-dto';
import { catchError } from 'rxjs/operators';
import { BasketDTO } from 'app/shared/dto/basket-dto';
/**
* A service providing functionality for the user of the application, including authentication,
* authorisation and session management.
*/
@Injectable({
    providedIn: 'root'
})
export class BasketService extends CrudService<ProductBookDTO> {

    private serviceUrl = AppConstants.CRM_CENTRAL_URL + '/modules/market/products/active/';
    private url = AppConstants.CRM_CENTRAL_URL + '/modules/market/baskets/';
    private removeUrl = AppConstants.CRM_CENTRAL_URL + '/modules/market/baskets?basket-id=';
    private getUrl = AppConstants.CRM_CENTRAL_URL + '/modules/market/baskets/by-id'
    
    httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': `application/json`
        })
    };

    constructor(http: HttpClient, private localHttp: HttpClient, private jwtService: JwtHelperService) {
        super(http, 'users');
    }

    createBasket(basket: BasketDTO)  {
        return this.http.post<any>(this.url, JSON.stringify(basket), this.httpOptions).pipe(catchError(this.errorHandler));
    }
    removeBasket(id:number): Observable<any>{
        return this.http.delete<any>(this.removeUrl+id)
    }
    getOrdersByBasketId(id: number): Observable<any>{
        let params = new HttpParams()
        params = params.set('basket-id', id.toString())
        return this.http.get<any>(this.getUrl, {params: params});
    }
    updateBasket(basketDTO: BasketDTO, basketId: number){
        return this.http.put<any>(this.url+basketId, basketDTO);
    }
    deactivateBasket(basketId: string){
        return this.http.post<any>(this.removeUrl  + basketId, null)
    }
    errorHandler(error: HttpErrorResponse) {
        return throwError(error);
    }



}
