import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Observable} from 'rxjs';
import {AppConstants} from 'app/shared/config/app-constants';
import {LoginInfoDto} from 'app/shared/dto/login-info-dto';
import {MatDialog} from '@angular/material';
import {UserDto} from 'app/shared/dto/user-dto';
import {CrudService} from '../services/crud.service';
import { UserService } from 'app/services/user.service';

/**
 * A service providing functionality for the user of the application, including authentication,
 * authorisation and session management.
 */
@Injectable({
  providedIn: 'root'
})
export class ContactService extends CrudService<UserDto> {
  usersArrayOfObjects = [];
  private getUrl = AppConstants.CRM_CENTRAL_URL + '/users/current/contacts?status=all';
  private getPageUrl = AppConstants.CRM_CENTRAL_URL + '/users/current/contacts/page?status=all';
  private postUrl = AppConstants.CRM_CENTRAL_URL + '/contacts/'
  private deleteUrl = AppConstants.CRM_CENTRAL_URL + '/contacts'

  constructor(http: HttpClient, private localHttp: HttpClient, private jwtService: JwtHelperService) {
    super(http, 'users');
  }


   getAllContacts(): Observable<UserDto[]> {
    return this.http.get<UserDto[]>(this.getUrl);
    }

    getAllContactsPage(page: any, size: any, sort: any, direction: any): Observable<any> {
    return this.http.get<any>(`${this.getPageUrl}&sort=${sort}&direction=${direction}&size=${size}&page=${page}`);
  }

    postContacts(contactsArray: Array<String>){
      return this.http.post<any>(AppConstants.CRM_CENTRAL_URL +'/contacts/', contactsArray)
    }
    deleteContact(contactId: number){
      return this.http.delete<any>(`${this.deleteUrl}/${contactId}`)
    }

    setContactsArrayFromServer(contactsFromServer: any){
      this.usersArrayOfObjects = contactsFromServer;
    }
    getContactsArrayFromServer() {
     return  this.usersArrayOfObjects;
    }

    setInvitationApproved(row){

      const httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + UserService.getJwt()
            })
      };

      const url = AppConstants.CRM_CENTRAL_URL + '/contacts/' + row.id + '?status=accepted' ;
      return this.http.put<any>(url , null, httpOptions);
    }

    getApprovedContacts(){
      const httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + UserService.getJwt()
            })
      };
      const url = AppConstants.CRM_CENTRAL_URL +'/users/current/contact-users?status=accepted'
      return this.http.get<any>(url, httpOptions)
    }

    setInvitationRejected(row){
      const httpOptions = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + UserService.getJwt()
            })
      };

      const url = AppConstants.CRM_CENTRAL_URL + '/contacts/' + row.id + '?status=rejected' ;
      return this.http.put<any>(url , null, httpOptions);
    }



}
