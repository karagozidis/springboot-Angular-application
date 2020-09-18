import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError, retry } from 'rxjs/operators';
import { throwError, Observable, BehaviorSubject } from 'rxjs';
import { AppConstants } from 'app/shared/config/app-constants';
import { AppDataService } from './app-data.service';
import { Auxiliary } from 'app/shared/utils/auxiliary';

@Injectable({
  providedIn: 'root'
})
export class PartnerService {
  _partnersList: Partner[] = [];
  partnersList: any;
  empsList: Observable<any>;
  dataChange: BehaviorSubject<Partner[]> = new BehaviorSubject<Partner[]>([]);
  private serviceUrl = AppConstants.CRM_CENTRAL_URL + '/user-groups/userGroups/all';
  constructor(private http: HttpClient,private appDataService: AppDataService) { }

  getPartners(): Observable<Partner[]> {
    return this.http.get<Partner[]>(this.serviceUrl);
    }

    getPartnerById(partnerId: number): Observable<Partner[]> {
      return this.http.get<Partner[]>(this.serviceUrl + '/'  + partnerId)
      .pipe(
      retry(1),
      catchError(this.errorHandler)
      );
      }

  submitPartner(partner: Partner) {
      return this.http.post<any>(this.serviceUrl, partner)
        .pipe(catchError(this.errorHandler));

    }
    errorHandler(error: HttpErrorResponse) {
      return throwError(error);
    }

    editPartner(partner: Partner, index: number): Observable<Partner> {  
      this.empsList = this.http.get<Partner[]>(this.serviceUrl);
      const body = JSON.stringify(partner);
      this.empsList[index] = partner;
      const httpOptions = {
        headers: new HttpHeaders({'Content-Type': 'application/json'})
      }; 
      const resp = this.http.put<any>(`${this.serviceUrl}/${index}`, partner);
      this.empsList = resp;
      return this.http.put<any>(`${this.serviceUrl}/${index}`, partner)
        .pipe(catchError(this.errorHandler));   


    }

    deletePartner(allPartners: Partner, partner: Partner, index: number): Observable<Partner> {
      this.partnersList = allPartners;
      this.http.delete<any>(`${this.serviceUrl}/${index}`).pipe(catchError(this.errorHandler));
      return this.http.delete<any>(`${this.serviceUrl}/${index}`).pipe(catchError(this.errorHandler));
    }

}

class Partner {
    // public check: boolean ;
    public name: string;
    // public id: string;
    public phone: string;
    // public topic: string;
    // public timePreference: string;
    // public subscribe: boolean
}
