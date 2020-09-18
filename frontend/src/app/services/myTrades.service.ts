import { HttpClient, HttpParams } from "@angular/common/http";
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
import { TradeDTO } from "app/shared/dto/trade-dto";
import { OrderStateService } from "./orderState.service";
/**
* A service providing functionality for the user of the application, including authentication,
* authorisation and session management.
*/
@Injectable({
    providedIn: "root"
})
export class MyTradesService {
    private endpoint = AppConstants.CRM_CENTRAL_URL + '/modules/market/trades'

    constructor(
        private http: HttpClient,
        private jwtService: JwtHelperService,
        private orderStateService: OrderStateService
    ) {}

    getMyTrades(): Observable<TradeDTO[]> {
        let params = new HttpParams()
        params = params.set('createdFrom', this.orderStateService.from)
        params = params.set('createdTo', this.orderStateService.to)
        params = params.set('market-code', this.orderStateService.country)
        return this.http.get<any>(this.endpoint, {params: params});
    }

    getAllTrades(): Observable<TradeDTO[]> {
        let params = new HttpParams()
        params = params.set('createdFrom', this.orderStateService.from)
        params = params.set('createdTo', this.orderStateService.to)
        params = params.set('market-code', this.orderStateService.country)
        return this.http.get<any>(this.endpoint + '/all', {params: params});
    }
}
