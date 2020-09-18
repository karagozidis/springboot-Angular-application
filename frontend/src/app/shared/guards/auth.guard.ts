import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, CanActivate, Router} from '@angular/router';
import {Observable } from 'rxjs';
import {JwtHelperService} from '@auth0/angular-jwt';
import {AppConstants} from 'app/shared/config/app-constants'

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router, private jwtService: JwtHelperService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
      const jwtString = localStorage.getItem(AppConstants.StorageLabels.JWT_STORAGE_NAME);
      if (jwtString) {
          return true;
      } else {
          localStorage.removeItem(AppConstants.StorageLabels.JWT_STORAGE_NAME);
          const redirect = window.location.href;
          this.router.navigate(['login', {redirect: redirect}]);
          return false;
      }
  }
}
