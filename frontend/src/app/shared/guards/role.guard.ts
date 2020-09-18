import { Injectable } from '@angular/core';
import {
  Router,
  CanActivate,
  ActivatedRouteSnapshot
} from '@angular/router';
// import { AuthService } from './auth.service';
// import decode from 'jwt-decode';
import {AppConstants, UpdateSignals} from 'app/shared/config/app-constants'
import { AppDataService } from 'app/services/app-data.service';
import { UserDto } from '../dto/user-dto';
import { UserService } from 'app/services/user.service';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
  })
export class RoleGuard implements CanActivate {
    userDto = null

    constructor(public router: Router,private appDataService:AppDataService, private userService: UserService) {
        this.appDataService.updateEmmiter.subscribe(event => {
            if (event == UpdateSignals.USER_DTO) {
                this.onUpdateSignal()
            }
        })
        this.userDto = this.appDataService.userDto
    }

    onUpdateSignal() {
        this.userDto = this.appDataService.userDto
    }

    canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
        this.userDto = this.appDataService.userDto
        if (this.userDto == null) {
            return this.userService.getCurrentUser().map(userDto => {
                return this.checkRoles(route,userDto)
            }).catch(() => {
                this.router.navigate(['/login']);
                return Observable.of(false);
            });
           
            /* return Observable.of(true) */
           
        }
        else{
           return Observable.of(this.checkRoles(route, this.userDto))
        }
        /* if (this.userDto == null) {
            this.userService.getCurrentUser().subscribe(userDto => {
                this.checkRoles(route,userDto)
            })
           
        }
        else{
           return Observable.of(this.checkRoles(route, this.userDto))
        } */

    }

    checkRoles(route: ActivatedRouteSnapshot, userDto: UserDto){
        const menuRolesCrm  = route.data.rolesCrm;
        const menuModuleCrm = route.data.moduleCrm;
        const menuRolesMarket  = route.data.rolesMarket;
        const menuModuleMarket = route.data.moduleMarket;

        const userRolesCrm = userDto.rolesCrm.split(',');
        const userModulesCrm = userDto.modulesCrm.split(',');
        const userRolesMarket = userDto.rolesMarket.split(',');
        const userModulesMarket = userDto.modulesMarket.split(',');

       

        /*
        *
        * Check if current Menu Module is assigned to the User Modules.
        * If it is then return true
        *
        */
        if (menuModuleCrm !== undefined && menuModuleCrm !== null) {
            for (const userModuleCrm of userModulesCrm) {
                if ( userModuleCrm.match(menuModuleCrm) ) {
                    return true;
                }
            }
        }

        /*
        *
        * Check on current menu, if, at least one Menu Role is assigned to the User Roles.
        * If it is then return true
        * Otherwise check the modules
        *
        */
       /*  if (menuRolesMarket !== undefined && menuRolesMarket !== null) {
            for (const userRoleMarket  of userRolesMarket ) {
                if ( menuRolesMarket.indexOf(userRoleMarket) !== -1 ) {
                    return true;
                }
            }
        } */

        /*
        *
        * Check if current Menu Module is assigned to the User Modules.
        * If it is then return true
        *
        */
        if (menuModuleMarket !== undefined && menuModuleMarket !== null) {
            for (const userModuleMarket of userModulesMarket ) {
                if ( userModuleMarket.match(menuModuleMarket ) ) {
                    return true;
                }
            }
        }

        /*
        *
        * if No matched Role - Module assignements were found
        * User has no writes on this path & return value is false
        *
        */
        this.router.navigateByUrl('login')
        return false;

    }
}
