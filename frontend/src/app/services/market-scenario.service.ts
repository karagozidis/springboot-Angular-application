import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {AppConstants} from '../shared/config/app-constants';
import {CrudService} from './crud.service';
import {MarketScenarioDTO} from '../shared/dto/market-scenario-dto';


@Injectable({
  providedIn: 'root'
})
export class MarketScenarioService extends CrudService<MarketScenarioDTO> {

  constructor(http: HttpClient) {
    super(http, 'market-scenario');
  }


  getAllPageable(page: any, size: any, sort: any, direction: any): Observable<any> {
    return this.http.get<any>(`${AppConstants.CRM_CENTRAL_URL}/market-scenario/pages?sort=${sort}&direction=${direction}&size=${size}&page=${page}`);
  }

}
