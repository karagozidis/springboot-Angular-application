import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { JwtHelperService } from "@auth0/angular-jwt";
import { Observable } from "rxjs";
import { AppConstants } from "app/shared/config/app-constants";
import { LoginInfoDto } from "app/shared/dto/login-info-dto";
import { MatDialog } from "@angular/material";
import { UserDto } from "app/shared/dto/user-dto";
import { CrudService } from "../services/crud.service";
import { MyOrdersDTO } from "app/shared/dto/myOrders-dto";
import "rxjs/add/operator/map";
import { ProductDTO } from "app/shared/dto/product-dto";
/**
* A service providing functionality for the user of the application, including authentication,
* authorisation and session management.
*/
@Injectable({
    providedIn: "root"
})
export class OrdersByProductService extends CrudService<MyOrdersDTO> {
    // tslint:disable-next-line:max-line-length
    private ordersByProductUrl = AppConstants.CRM_CENTRAL_URL + '/modules/market/products/orders?product-id=';
    // tslint:disable-next-line:max-line-length

    constructor(http: HttpClient, private localHttp: HttpClient, private jwtService: JwtHelperService) {
        super(http, "users");
    }

    getOrdersByProduct(productId: number): Observable<any> {
        return this.http.get<any>(this.ordersByProductUrl  + productId) ;
    }
}
