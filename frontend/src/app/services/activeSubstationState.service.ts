import { HttpClient } from "@angular/common/http";
import { Injectable, OnInit } from "@angular/core";
import { JwtHelperService } from "@auth0/angular-jwt";
import { MatTableDataSource } from "@angular/material";
import 'rxjs/add/operator/map';
import { ProductDTO } from "app/shared/dto/product-dto";
import { OrderDTO } from "app/shared/dto/order-dto";
import * as TableStructure from 'app/pages/market/components/market-table/table-structure/table-structure';
import {  BasketParameters } from 'app/pages/market/components/market-dialog/market-dialog.component';
import { AppConstants, MarketCountryType } from "app/shared/config/app-constants";
import * as moment from 'moment'
import { BasketDTO } from "app/shared/dto/basket-dto";
import { Observable } from "rxjs";
import { AppDataService } from "./app-data.service";
import { Auxiliary } from "app/shared/utils/auxiliary";
/**
 * A service providing functionality for the user of the application, including authentication,
 * authorisation and session management.
 */
@Injectable({
  providedIn: "root"
})
export class ActiveSubstationStateService   {
    historyResult = '';
    constructor(
        private http: HttpClient,
        private localHttp: HttpClient,
        private appDataService: AppDataService,
        private jwtService: JwtHelperService
    ) {
        

       
    }

    

    
}



