import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpHeaders } from "@angular/common/http";
import { Router } from "@angular/router";
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import { AppConstants } from "../config/app-constants";

@Injectable()
export class AuthenticationHeaderInterceptor implements HttpInterceptor {
    constructor(private router: Router) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const jwtToken = localStorage.getItem(AppConstants.StorageLabels.JWT_STORAGE_NAME)
        if (jwtToken === '' || jwtToken === undefined || jwtToken == null) {
            return next.handle(request)
        }

        let updatedRequest
        if (request.headers.has('Content-Type') && request.headers.get('Content-Type') === 'multipart/form-data' ) {
            updatedRequest = request.clone({
                headers: new HttpHeaders({
                    'Authorization': 'Bearer ' + localStorage.getItem(AppConstants.StorageLabels.JWT_STORAGE_NAME)
                })
            });
        } else {
            updatedRequest = request.clone({
                headers: new HttpHeaders({
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem(AppConstants.StorageLabels.JWT_STORAGE_NAME)
                })
            });
        }
        return next.handle(updatedRequest);
    }

}
