import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Observable} from 'rxjs';
import {AppConstants} from 'app/shared/config/app-constants';
import {LoginInfoDto} from 'app/shared/dto/login-info-dto';
import {UserDto} from 'app/shared/dto/user-dto';
import {CrudService} from '../services/crud.service';


/**
 * A service providing functionality for the user of the application, including authentication,
 * authorisation and session management.
 */
@Injectable({
  providedIn: 'root'
})
export class UserService extends CrudService<UserDto> {

  private serviceUrl = AppConstants.CRM_CENTRAL_URL + '/users';
  private verifiedUsersUrl = AppConstants.CRM_CENTRAL_URL + '/users/crm-verified'
  private localServiceUrl = AppConstants.CRM_LOCAL_URL + '/users';
  private sendCSVUrl = '';

  // crmAndMarketWithNoLocal = false;

  constructor(http: HttpClient,
              private localHttp: HttpClient,
              private jwtService: JwtHelperService) {
    super(http, 'users');
  }


  public static getJwt(): string {
    return localStorage.getItem(AppConstants.StorageLabels.JWT_STORAGE_NAME);
  }


  handshake(): Observable<any> {
    const url = `${this.localServiceUrl}/handshake`;
    const httpOptions = {
      headers: new HttpHeaders(
          {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + UserService.getJwt()
          }
      )
    };
    return this.http.post(url, httpOptions);
  }

  getUser(id: string): Observable<UserDto> {
    return this.http.get<UserDto>(this.serviceUrl + '/' + id);
  }

  save(userDto: UserDto) {
    return this.http
    .post<UserDto>(this.serviceUrl, userDto)
  }

  updateUser(userDto: UserDto) {
    return this.http
    .put<UserDto>(this.serviceUrl + '/user-details', userDto)
  }

  update(userDto: UserDto) {
    return this.http
    .put<UserDto>(this.serviceUrl + '/details', userDto)
  }

  updatePassword(oldPassword: string, password: string): Observable<boolean> {
    return this.http
    .put<boolean>(this.serviceUrl + '/password' +
        '?old=' + oldPassword +
        '&new=' + password,
        null);
  }

  updateUserPassword(id: string, password: string): Observable<boolean> {
    return this.http
    .put<boolean>(this.serviceUrl + '/user-password' +
        '?id=' + id +
        '&password=' + password,
        null);
  }

  getAllUsers(): Observable<UserDto[]> { 
    return this.http.get<UserDto[]>(this.serviceUrl);
  }

  getAllCountries(): Observable<any[]> {
    return this.http.get<any[]>(AppConstants.CRM_CENTRAL_URL + '/countries');
  }


  getVerifiedUsers(): Observable<UserDto[]> {
    return this.http.get<UserDto[]>(this.verifiedUsersUrl);
  }

  getAllUsersPageable(page: any, size: any, sort: any, direction: any): Observable<UserDto[]> {
    return this.http.get<any>(`${this.serviceUrl}/pages?sort=${sort}&direction=${direction}&size=${size}&page=${page}`);
  }

  login(loginInfoDTO: LoginInfoDto): Observable<string> {
    return this.localHttp.post<string>(
        AppConstants.CRM_CENTRAL_URL + '/users/auth',
        JSON.stringify(loginInfoDTO),
        {headers: {'Content-Type': 'application/json'}});
  }

  loginToToLocal(loginInfoDTO: LoginInfoDto): Observable<any> {
    const url = `${this.localServiceUrl}/auth`;
    const httpOptions = {
      headers: new HttpHeaders(
          {'Content-Type': 'application/json'}
      )
    };
    return this.http.post(url, JSON.stringify(loginInfoDTO), httpOptions);
  }


  getCurrentUser(): Observable<UserDto> {

    const bearer = localStorage.getItem(AppConstants.StorageLabels.JWT_STORAGE_NAME)

    return this.http.get<UserDto>(this.serviceUrl + '/current', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': bearer
      }
    });

  }

  // Return a claim from JWT.
  getJWTClaim(claim: string): string {
    let claimValue: string;

    if (UserService.getJwt()) {
      claimValue = this.jwtService.decodeToken(UserService.getJwt())[claim];
    }

    return claimValue;
  }

  // Logs out the user terminating its session.
  // TODO observable type
  logout(): Observable<any> {

    return this.localHttp.get(AppConstants.CRM_CENTRAL_URL + `/${this.endpoint}/logout`);
  }

  uploadFile(file) {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);

    this.http.post(this.sendCSVUrl, formData);
  }

  deleteById(id: number): Observable<{}> {
    const url = AppConstants.CRM_CENTRAL_URL + '/modules/weather/by-id?id=' + id;
    return this.http.delete(url);
  }


}
