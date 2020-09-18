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

  /**
   * A service providing functionality for the user of the application, including authentication,
   * authorisation and session management.
   */
  @Injectable({
    providedIn: "root"
  })
  export class GeHyppoService extends CrudService<GeHyppoParametersDTO> {
    userGroupsFromServer: string;
    private serviceUrl = AppConstants.CRM_LOCAL_URL + '/modules/hyppo-io/run';

    userGroupList: Observable<any>;
    constructor(
      http: HttpClient,
      private localHttp: HttpClient,
      private jwtService: JwtHelperService
    ) {
      super(http, "users");
    }

    setDefaultUserGroup(defaultUserGroup: string){
      this.userGroupsFromServer = defaultUserGroup;
    }

    getDefaultUserGroup(){
      return this.userGroupsFromServer;
    }


    postGeHyppoParameters(geHyppoParams: GeHyppoParametersDTO) {
      return this.http
        .post<any>(this.serviceUrl, geHyppoParams)
        .pipe(catchError(this.errorHandler));
    }
    errorHandler(error: HttpErrorResponse) {
      return throwError(error);
    }

  }
