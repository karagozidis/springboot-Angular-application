import {EventEmitter, Injectable} from '@angular/core';
import {UserDto} from 'app/shared/dto/user-dto';
import {AppConstants, AppType, UpdateSignals, UserAccess, MarketCountryType} from 'app/shared/config/app-constants';
import {UserService} from './user.service';
import {LoginInfoDto} from 'app/shared/dto/login-info-dto';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import { Auxiliary } from 'app/shared/utils/auxiliary';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AppDataService {
  notificationMode = false;
  userDto: UserDto = null
  crmLocalActiveState = false
  appType = AppType.CRM
  userAccess: UserAccess = null
  responsiveButtonState = false;
  orderAndTradesTableSelection = AppConstants.Market.Tables.OrdersAndTrades.State.ORDERS
  defaultMarket: String = MarketCountryType.DEFAULT_MARKET_CYPRUS;
  sideBarVisible = true;
  // ordersAndTradesTableState =


  updateEmmiter = new EventEmitter();

  constructor(private userService: UserService, private http: HttpClient, private router: Router) {
    // this.intervalId = setInterval(() => this.updateAll(), AppConstants.Settings.UPDATE_INTERVAL)
  }

  // setSideBarHidden(isHidden: boolean){
  //     this.isSidebarHidden = isHidden
  //     this.toggleEvent.emit('sidebar toggled');
  // }

  setUserDto(userDto: UserDto) {
    this.userDto = userDto
    this.onUserDtoUpdated(this.userDto)
    this.setMarketCountry(this.userDto.defaultMarket)
    console.log(this.userDto)
    this.updateEmmiter.emit(UpdateSignals.USER_DTO)
    console.log(UpdateSignals.USER_DTO)
  }

  updateMarketTables() {
    this.updateEmmiter.emit(UpdateSignals.UPDATE_MARKET_TABLES)
    console.log(UpdateSignals.UPDATE_MARKET_TABLES)
  }

  updateUserDto() {
    this.userService.getCurrentUser().subscribe(userDto => {
      this.userDto = userDto
      this.onUserDtoUpdated(this.userDto)
      this.setMarketCountry(this.userDto.defaultMarket)
      this.updateEmmiter.emit(UpdateSignals.USER_DTO)
      console.log(UpdateSignals.USER_DTO)
    })
  }

  setAppType(appType: AppType) {
    this.appType = appType
    this.updateEmmiter.emit(UpdateSignals.APP_TYPE)
     if(this.appType === AppType.CRM){
        this.setSideBarVisible(true)
    }
   
    console.log(UpdateSignals.APP_TYPE)
  }

  setMarketCountry(defaultMarket: String){
    this.defaultMarket = defaultMarket
  }
  setSideBarVisible(visible: boolean) {
    this.sideBarVisible = visible;
    this.updateEmmiter.emit(UpdateSignals.SIDEBAR_VISIBILITY_STATE)
    console.log(UpdateSignals.SIDEBAR_VISIBILITY_STATE)
  }

  setOrdersAndTradesTableSelection(selection: string) {
    this.orderAndTradesTableSelection = selection
    this.updateEmmiter.emit(UpdateSignals.ORDERS_AND_TRADES_TABLE_STATE)
  }

  updateCrmLocalActiveState() {
    console.log(AppConstants.SessionInfo.username)
    this.checkIfLocalIsOn().subscribe(
      response =>{
        console.log(response)
        if(response === 'OK'){
          console.log(AppConstants.SessionInfo.username)
          console.log(AppConstants.SessionInfo.password)
          if(AppConstants.SessionInfo.username!=='' &&  AppConstants.SessionInfo.password!=='' && AppConstants.SessionInfo.username!==null &&  AppConstants.SessionInfo.password!==null
          && AppConstants.SessionInfo.username!==undefined 
          &&  AppConstants.SessionInfo.password!==undefined ){
            const loginInfo = new LoginInfoDto();
            loginInfo.username = AppConstants.SessionInfo.username
            loginInfo.password = AppConstants.SessionInfo.password
            loginInfo.capthca = '';
            loginInfo.tfaCode = '';
            loginInfo.tfaValid = false;

            console.log(loginInfo.username)
            console.log(loginInfo.password)
            this.userService.loginToToLocal(loginInfo).subscribe(
              data => {
                this.crmLocalActiveState = true
                this.updateEmmiter.emit(UpdateSignals.CRM_LOCAL_ACTIVE_STATE)
              },
              onError => {
                this.crmLocalActiveState = false
                this.updateEmmiter.emit(UpdateSignals.CRM_LOCAL_ACTIVE_STATE)
              }
            )
          }
          else{
            this.router.navigate(['login']);
          }
        }
      },
      onError => {
        this.crmLocalActiveState = false
        this.updateEmmiter.emit(UpdateSignals.CRM_LOCAL_ACTIVE_STATE)
      }

    )
    
    console.log(this.crmLocalActiveState)
    console.log(UpdateSignals.CRM_LOCAL_ACTIVE_STATE)
  }

  checkIfLocalIsOn(): Observable<any> {
    return this.http.get(AppConstants.CRM_LOCAL_ALIVE_RESPONSE, {responseType: 'text'});
  }

  setCrmLocalActiveState(crmLocalActiveState: boolean) {
    this.crmLocalActiveState = crmLocalActiveState
    this.updateEmmiter.emit(UpdateSignals.CRM_LOCAL_ACTIVE_STATE)
    console.log(UpdateSignals.CRM_LOCAL_ACTIVE_STATE)
  }

  setResponsiveButtonState(responsiveButtonState: boolean) {
    this.responsiveButtonState = responsiveButtonState
    this.updateEmmiter.emit(UpdateSignals.RESPONSIVE_BUTTON_STATE)
    console.log(UpdateSignals.RESPONSIVE_BUTTON_STATE)
  }

  private onUserDtoUpdated(userDto: UserDto) {
    this.userAccess = this.updateUserAccess(userDto)
  }

  private updateUserAccess(userDto: UserDto) {
    let userAccess = null
    const userRolesCrm = userDto.rolesCrm.split(',');
    // const userModulesCrm = userDto.modulesCrm.split(',');
    const userRolesMarket = userDto.rolesMarket.split(',');
    // const userModulesMarket = userDto.modulesMarket.split(',');
    if (userRolesMarket.length > 0 && (userRolesCrm.length === 1 && userRolesCrm[0] === '')) {
      userAccess = UserAccess.MARKET
      // tslint:disable-next-line:max-line-length
    } else if (userRolesCrm.length > 0 && (userRolesMarket.length === 1 && userRolesMarket[0] === '')) {
      userAccess = UserAccess.CRM;
    }
    // tslint:disable-next-line:max-line-length
    else if (userRolesMarket.length > 0 && (userRolesCrm.length > 0 && (userRolesCrm[0] !== '' || userRolesCrm[0] !== undefined || userRolesCrm[0] !== null))) {
      userAccess = UserAccess.CRM_AND_MARKET;
    }

    return userAccess
  }
}
