import {Component} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Router} from '@angular/router';
import {AppConstants} from 'app/shared/config/app-constants'
import {CrmLocalWebSocketsService} from './services/crm-local-web-sockets.service'
import {AppDataService} from './services/app-data.service';
import {CrmCentralWebSocketsService} from './services/crm-central-web-sockets.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {

    constructor(
        private router: Router,
        private jwtService: JwtHelperService,
        private crmLocalWebSocketsService: CrmLocalWebSocketsService,
        private appDataService: AppDataService,
        private crmCentralWebSocketsService: CrmCentralWebSocketsService
    ) {
        // Check if an expired JWT exists and remove it.
        const jwtString = localStorage.getItem(AppConstants.StorageLabels.JWT_STORAGE_NAME);
        if (jwtString) {
            try {
                if (this.jwtService.isTokenExpired(jwtString)) {
                    // this.log.data('Removing expired JWT.');
                    localStorage.removeItem(AppConstants.StorageLabels.JWT_STORAGE_NAME);
                }
            } catch (err) {
                // this.log.data('Could not decode JWT, removing it.');
                localStorage.removeItem(AppConstants.StorageLabels.JWT_STORAGE_NAME);
            }
        } else {
            // this.log.data('Did not find a JWT.');
        }

        if (jwtString != null && jwtString != undefined && jwtString != "") {
            this.appDataService.updateUserDto()
        }
    }

    isLoggedIn(): boolean {
        return localStorage.getItem(AppConstants.StorageLabels.JWT_STORAGE_NAME) != null &&
            localStorage.getItem(AppConstants.StorageLabels.JWT_STORAGE_NAME) !== undefined;
    }

    ngOnInit() {
        if (!this.isLoggedIn()) {
            this.router.navigate(['login']);
        } else {
            // Check for token expiration.
            const checkInterval: any = setInterval(() => {
                let tokenExpired = false;
                const jwtString = localStorage.getItem(AppConstants.StorageLabels.JWT_STORAGE_NAME);
                //if local storage getItems (username, password) are null or undefined or ''
               
                
                if (jwtString) {
                    try {
                        tokenExpired = this.jwtService.isTokenExpired(jwtString);
                    } catch (err) {
                    }
                }
            }, 1000);
                if(localStorage.getItem(AppConstants.StorageLabels.USERNAME) !==null || localStorage.getItem(AppConstants.StorageLabels.USERNAME) !== undefined
                || localStorage.getItem(AppConstants.StorageLabels.USERNAME) !==''){
                    AppConstants.SessionInfo.username = localStorage.getItem(AppConstants.StorageLabels.USERNAME)
                    console.log(AppConstants.SessionInfo.username)
                }
                if(localStorage.getItem(AppConstants.StorageLabels.PASSWORD) !==null || localStorage.getItem(AppConstants.StorageLabels.PASSWORD) !== undefined
                || localStorage.getItem(AppConstants.StorageLabels.PASSWORD) !==''){

                    AppConstants.SessionInfo.password = localStorage.getItem(AppConstants.StorageLabels.PASSWORD)
                    console.log(AppConstants.SessionInfo.password)
                }
        }
        this.crmLocalWebSocketsService.initializeConnections();
    }
}
