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
import { AppDataService } from './app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';

@Injectable()
export class GraphService extends CrudService<any> {

  private serviceUrl = AppConstants.CRM_LOCAL_URL + '/modules/hyppo-io/';
  urlParameters1 = 'graphs?graph-variables=';
  urlParameters2 = '&filename=';
  private csvUrl = AppConstants.CRM_LOCAL_URL + '/modules/hyppo-io/'


  constructor(http: HttpClient, private localHttp: HttpClient, private jwtService: JwtHelperService,
    private appDataService: AppDataService) {
    super(http, 'users');
  }

  getSpecificRunCSVs(executionName: string) {
     return this.http.get<any>(this.csvUrl + executionName + '/csvs' + '/names');
  }

   
  getGraph(executionName: string, csvFileName: string, parametersList: string): Observable<any> {
    return this.http.get<any>(this.serviceUrl  + executionName +'/' +this.urlParameters1 + parametersList + this.urlParameters2 + csvFileName);
    }

}
