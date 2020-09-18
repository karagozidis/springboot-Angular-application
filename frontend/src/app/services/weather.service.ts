import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders, HttpResponse, HttpParams} from '@angular/common/http';
import {AppConstants} from 'app/shared/config/app-constants';
import { JwtHelperService } from '@auth0/angular-jwt';
import { WeatherDTO } from 'app/shared/dto/weather-dto';
import { OrderStateService } from './orderState.service';


@Injectable({
  providedIn: 'root'
})
export class WeatherService {
  endpoint = AppConstants.CRM_CENTRAL_URL + '/modules/weather/today'

  constructor(public http: HttpClient,
              private jwtService: JwtHelperService,
              private orderStateService: OrderStateService) { }


  upload(fileToUpload: File, marketCode: string): Observable<boolean> {
    const httpOptions = {
      headers: new HttpHeaders(
          {
            'Content-Type': 'multipart/form-data'
          }
      )
    };
    const serviceUrl = AppConstants.CRM_CENTRAL_URL + '/modules/weather' +
    '?market-code=' + marketCode
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
    return this.http
      .post(serviceUrl, formData, httpOptions)
      .map(() => { return true; });
    }

  getOnPeriod(datetimeFrom: string, dateTimeTo: string, marketCode: string): Observable<any[]> {
    return this.http.get<any[]>( AppConstants.CRM_CENTRAL_URL + '/modules/weather/period' +
    '?time-from=' + datetimeFrom +
    '&time-to=' + dateTimeTo +
    '&market-code=' + marketCode
     );
    }

    getToday(): Observable<any[]> {
      return this.http.get<any[]>( AppConstants.CRM_CENTRAL_URL + '/modules/weather/today');
      }

    deleteById(id: number): Observable<{}> {
      const url = AppConstants.CRM_CENTRAL_URL + '/modules/weather/by-id?id=' + id ;
      return this.http.delete(url);
    }


    deleteOnPeriod(datetimeFrom: string, dateTimeTo: string, marketCode: string): Observable<{}> {
      const url = AppConstants.CRM_CENTRAL_URL + '/modules/weather/period' +
            '?time-from=' + datetimeFrom +
            '&time-to=' + dateTimeTo +
            '&market-code=' + marketCode;

      return this.http.delete(url);
    }

    getWeather(): Observable<WeatherDTO[]> { 
      let params = new HttpParams()
      params = params.set('market-code', this.orderStateService.country)
      return this.http.get<any>(this.endpoint , {params: params})
    }

}
