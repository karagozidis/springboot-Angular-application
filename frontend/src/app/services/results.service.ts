import { Injectable } from '@angular/core';
import { UserGroupDTO } from 'app/shared/dto/user-group-dto';
import { MessageInfoDTO } from 'app/shared/dto/message-info-dto';
import { CrudService } from './crud.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs';
import { RequestOptions, ResponseContentType } from '@angular/http';
import { HyppoIoMetaDataDTO } from 'app/shared/dto/HyppoIoMetaDataDTO';
import { AppConstants } from 'app/shared/config/app-constants';

@Injectable()
export class ResultService extends CrudService<any> {

  private serviceUrl = AppConstants.CRM_LOCAL_URL + '/modules/hyppo-io/';


  constructor(http: HttpClient, private localHttp: HttpClient, private jwtService: JwtHelperService) {
    super(http, 'users');
  }


   getResults(executionName: string): Observable<any> {
    return this.http.get<any>(this.serviceUrl + executionName + '/results');
    }

}
