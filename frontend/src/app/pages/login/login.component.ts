import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AppConstants, UpdateSignals, UserAccess} from '../../shared/config/app-constants'
import {UserService} from '../../services/user.service'
import {LoginInfoDto} from '../../shared/dto/login-info-dto'
import {SideBarToggleService} from 'app/services/sidebar-toggle.service';
import {NotificationToastService} from 'app/shared/utils/notification-toast-service';
import {InternalMessageService} from 'app/shared/utils/internal-message.service';
import {AppDataService} from 'app/services/app-data.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  username = '';
  password = '';
  isChecked = false;
  showLoadingButton = false;

  constructor(private router: Router, private userService: UserService,
              private sidebarToggleService: SideBarToggleService,
              private notificationToastService: NotificationToastService,
              private msgService: InternalMessageService,
              private appDataService: AppDataService) {
  }

  ngOnInit() {
    this.showLoadingButton = false;
    if (localStorage.getItem(AppConstants.StorageLabels.REMEMBER_ME) !== null) {
      if (localStorage.getItem(AppConstants.StorageLabels.REMEMBER_ME) === 'yes') {
        this.username = localStorage.getItem(AppConstants.StorageLabels.USERNAME);
        this.password = localStorage.getItem(AppConstants.StorageLabels.PASSWORD);
        this.isChecked = true;
      } else {
        this.username = '';
        this.password = '';
        this.isChecked = false;
      }
    }
  }

  authenticateUser() {
    if (this.username === '') {
      this.notificationToastService.showNotification('Please fill in your username', 'error', 'top',
          'center', 'warning');
    }

    if (this.password === '') {
      this.notificationToastService.showNotification('Please fill in your password', 'error', 'top',
          'center', 'warning');
    }

    if (this.username === '' || this.password === '') {
      return;
    }

    /* if (this.isChecked === true) {
      localStorage.setItem(AppConstants.StorageLabels.USERNAME, this.username);
      localStorage.setItem(AppConstants.StorageLabels.PASSWORD, this.password);
      localStorage.setItem(AppConstants.StorageLabels.REMEMBER_ME, 'yes');
    } else {
      localStorage.setItem(AppConstants.StorageLabels.REMEMBER_ME, 'no')
    } */

    const loadingButtonHandler = setTimeout(() => {
      this.showLoadingButton = true
    }, 300);
    const loginInfo = new LoginInfoDto();
    loginInfo.username = this.username;
    loginInfo.password = this.password;
    loginInfo.capthca = '';
    loginInfo.tfaCode = '';     
    loginInfo.tfaValid = false;

    this.userService.login(loginInfo).subscribe(
        authData => {
          if (this.isChecked === true) {
            localStorage.setItem(AppConstants.StorageLabels.USERNAME, this.username);
            localStorage.setItem(AppConstants.StorageLabels.PASSWORD, this.password);
            localStorage.setItem(AppConstants.StorageLabels.REMEMBER_ME, 'yes');
          } else {
            localStorage.setItem(AppConstants.StorageLabels.REMEMBER_ME, 'no')
            localStorage.setItem(AppConstants.StorageLabels.USERNAME, '');
            localStorage.setItem(AppConstants.StorageLabels.PASSWORD, '');
          }
          AppConstants.SessionInfo.username = this.username
          AppConstants.SessionInfo.password = this.password
          const jwt = authData['jwt'];
          if (jwt !== '') {
            localStorage.setItem(AppConstants.StorageLabels.JWT_STORAGE_NAME, authData['jwt'])
            this.userService.getCurrentUser().subscribe(userDto => {
              this.appDataService.setUserDto(userDto);

              // if (userDto.landingPage === 'market') {
              //   this.appDataService.updateEmmiter.emit(UpdateSignals.HIDE_SIDEMENU);
              // }

              //this.router.navigateByUrl('/' + userDto.landingPage);

                console.log(this.appDataService.userAccess)
               if (this.appDataService.userAccess === UserAccess.CRM || this.appDataService.userAccess === UserAccess.CRM_AND_MARKET) {
                console.log(this.appDataService.userAccess) 
                this.router.navigateByUrl('/data');
               } else if (this.appDataService.userAccess === UserAccess.MARKET) {
                 this.router.navigateByUrl('/market');
               } 

            })
          } else {
            this.password = '';

            this.notificationToastService.showNotification(
                'Wrong username or password. Please try again.', 'error', 'top', 'center',
                'danger');
            clearTimeout(loadingButtonHandler);
            this.showLoadingButton = false
          }
        }, onError => {
          this.notificationToastService.showNotification(
              'An error occured while logging in. Please try again later.', 'error', 'top',
              'center', 'danger');
          clearTimeout(loadingButtonHandler);
          this.showLoadingButton = false
        });
  }



  onEnterMethod(){
    this.authenticateUser();
  }
}
