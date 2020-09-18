import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable, throwError } from 'rxjs';
import { AppConstants } from 'app/shared/config/app-constants';
import { LoginInfoDto } from 'app/shared/dto/login-info-dto';
import { MatDialog } from '@angular/material';
import { UserDto } from 'app/shared/dto/user-dto';
import { CrudService } from '../services/crud.service';
import { catchError } from 'rxjs/operators';
import { UserGroupDTO } from 'app/shared/dto/user-group-dto';
import { AppDataService } from './app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

/**
 * A service providing functionality for the user of the application, including authentication,
 * authorisation and session management.
 */
@Injectable({
  providedIn: 'root'
})
export class UserGroupService extends CrudService<UserDto> {

  private serviceUrl = AppConstants.CRM_CENTRAL_URL + '/user-groups';
  private postUrl = AppConstants.CRM_CENTRAL_URL + '/user-groups';
  private putUrl = AppConstants.CRM_CENTRAL_URL + '/user-groups';
  private deleteUrl = AppConstants.CRM_CENTRAL_URL + '/user-groups';
  userGroupList: Observable<any>;
  idOfDTOToUpdate: number;

  constructor(
    http: HttpClient,
    private localHttp: HttpClient,
    private appDataService: AppDataService,
    private jwtService: JwtHelperService
  ) {
    super(http, 'users' );
  }

  editUserGroup(
    userGroup: UserGroupDTO,
    index: number
  ): Observable<UserGroupDTO> {
    const body = JSON.stringify(userGroup);
    return this.http
      .put<any>(`${this.putUrl}/${index}`, userGroup)
      .pipe(catchError(this.errorHandler));
  }

  createUserGroups(userGroup: UserGroupDTO) {
    return this.http
      .post<any>(this.postUrl, userGroup)
      .pipe(catchError(this.errorHandler));
  }

  errorHandler(error: HttpErrorResponse) {
    return throwError(error);
  }

  getAllUserGroups(): Observable<any> {
    return this.http.get(this.serviceUrl+'/users/current/');
  }

  getAllUserGroupsAnyUser(): Observable<any> {
    return this.http.get(this.serviceUrl);
  }


  getAllUserGroupsPage(page: any, size: any, sort: any, direction: any): Observable<any> {
    return this.http.get( `${this.serviceUrl}/users/current/pages?sort=${sort}&direction=${direction}&size=${size}&page=${page}`);
  }


  deleteUserGroup(id: number): Observable<{}> {
    const url = `${this.deleteUrl}/${id}`;
    return this.http.delete(url, httpOptions);
  }


}
