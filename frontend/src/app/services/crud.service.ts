import {AppConstants} from '../shared/config/app-constants';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
// import {QFormsService, QPageableReply} from '@eurodyn/forms';
import {FormGroup} from '@angular/forms';

/**
 * A convenience CRUD service to be extended by concrete services to provide default CRUD methods.
 */
export class CrudService<T> {
  constructor(public http: HttpClient, protected endpoint: string) {
  }

  save(object: T) {
    return this.http.post(`${AppConstants.CRM_CENTRAL_URL}/${this.endpoint}`, object);
  }

  get(id: any): Observable<T> {
    return this.http.get<T>(`${AppConstants.CRM_CENTRAL_URL}/${this.endpoint}/${id}`);
  }

  getFirst(): Observable<T> {
    return this.http.get<T>(`${AppConstants.CRM_CENTRAL_URL}/${this.endpoint}`);
  }

  delete(id: any): Observable<any> {
    return this.http.delete(`${AppConstants.CRM_CENTRAL_URL}/${this.endpoint}/${id}`);
  }

  deleteAll(): Observable<any> {
    return this.http.delete(`${AppConstants.CRM_CENTRAL_URL}/${this.endpoint}`);
  }
}
