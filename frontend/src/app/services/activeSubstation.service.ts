import {
    HttpClient,
    HttpErrorResponse,
    HttpHeaders
} from "@angular/common/http";
import { Injectable } from "@angular/core";
import { JwtHelperService } from "@auth0/angular-jwt";
import { Observable, throwError } from "rxjs";
import { AppConstants } from "app/shared/config/app-constants";
import { LoginInfoDto } from "app/shared/dto/login-info-dto";
import { MatDialog } from "@angular/material";
import { UserDto} from "app/shared/dto/user-dto";
import { CrudService } from "../services/crud.service";
import { catchError } from "rxjs/operators";
import { UserGroupDTO } from "app/shared/dto/user-group-dto";
import { GeHyppoParametersDTO } from "app/shared/dto/ge-hyppo-parameters-dto";
import { ActiveSubstationInputDTO } from "app/shared/dto/activeSubstationInput-dto";
import { ActiveSubstationInputFlexibilityServiceScheduleDTO } from "app/shared/dto/activeSubstationInputFlexibilityServiceSchedule-dto";
import { ActiveSubstationOfferDTO } from "app/shared/dto/activeSubstationOffer-dto";

  @Injectable({
    providedIn: "root"
  })
  export class ActiveSubstationService extends CrudService<ActiveSubstationInputDTO> {
    userGroupsFromServer: string;
    private serviceUrl = AppConstants.CRM_LOCAL_URL + '/modules/active-substation';
    private latestDatesUrl = AppConstants.CRM_CENTRAL_URL + '/modules/active-substation-forecasts/dates';
    private lastestScheduledServices =AppConstants.CRM_LOCAL_URL + '/modules/active-substation/cached-onCloudAppbility-service-schedule?user-group-id='
                                                                
    offerGenerated = false
    acceptOffer = false; 
    rejectOffer = false;
    sendOffer = false;
    userGroupList: Observable<any>;
    constructor(
      http: HttpClient,
      private localHttp: HttpClient,
      private jwtService: JwtHelperService
    ) { 
      super(http, "users");
    }

    

    postInputFile(activeSubstationInputDTO: ActiveSubstationInputDTO) {
      return this.http
        .post<any>(this.serviceUrl, activeSubstationInputDTO)
        .pipe(catchError(this.errorHandler));
    }
    errorHandler(error: HttpErrorResponse) {
      return throwError(error);
    }

    getLatestDates(): Observable<any>{
      return this.http.get<any>(this.latestDatesUrl);
    }

    getOutputInfo(uuid:string): Observable<any>{
      // return this.http.get<any>('http://localhost:3000/output')
      console.log(uuid)
      return this.http.get<any>(this.serviceUrl + '/output-by-id?id='+uuid);
    }

    getLatestScheduleServicesArray(userGroup): Observable<any>{
      return this.http.get<any>(this.lastestScheduledServices+userGroup)
    }
    
    postLatestScheduleServicesArray(activeSubstationInputFlexibilityServices: ActiveSubstationInputFlexibilityServiceScheduleDTO, userGroup): Observable<any>{
      return this.http
        .post<any>(this.serviceUrl +'/cache-onCloudAppbility-service-schedule?user-group-id='+ userGroup, activeSubstationInputFlexibilityServices)
        .pipe(catchError(this.errorHandler));
    }

    postOffer(uuid: string, activeSubstationOffer: ActiveSubstationOfferDTO):Observable<any>{
     // http://localhost:30000/api/modules/active-substation-offer?id=04b7ebe7-b355-4b6f-8820-1bb499db57ca
     return this.http
     .post<any>(AppConstants.CRM_LOCAL_URL +'/modules/active-substation-offer?id='+uuid,activeSubstationOffer)
     .pipe(catchError(this.errorHandler));
    }

  }
