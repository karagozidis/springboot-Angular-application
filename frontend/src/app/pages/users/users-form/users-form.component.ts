import {UserService} from 'app/services/user.service';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Country, UserDto} from 'app/shared/dto/user-dto';
import {NotificationToastService} from '../../../shared/utils/notification-toast-service';
import { UserGroupService } from 'app/services/user-group.service';


declare interface ModuleOption {
    code: string;
    name: string;
    selected: Boolean;
}

export const crmModuleOptions: ModuleOption[] = [
    {
        code: 'dashboard',
        name: 'Dashboard',
        selected: false,
    },
    {
        code: 'data',
        name: 'Data',
        selected: true,
    },
    {
        code: 'hyppoio',
        name: 'HyppoIo',
        selected: false,
    },
    {
        code: 'upload-file',
        name: 'Upload Files',
        selected: false,
    },
    {
        code: 'user-groups',
        name: 'User Groups',
        selected: true,
    },
    {
        code: 'users',
        name: 'Users',
        selected: false,
    },
    {
        code: 'contacts',
        name: 'Contacts',
        selected: true,
    },
    {
        code: 'notifications',
        name: 'Notifications',
        selected: true,
    },
    {
        code: 'historical-market-data',
        name: 'Historical Market Data',
        selected: false,
    },
    {
        code: 'active-substations-management',
        name: 'Active Substations Management',
        selected: false,
    },
    {
        code: 'system-operator',
        name: 'System Operator',
        selected: false,
    },
    {
        code: 'substation-operator',
        name: 'Substation Operator',
        selected: false,
    },
    {
        code: 'import-forecasted-data',
        name: 'Import Forecasted Data',
        selected: false,
    },
    {
        code: 'substation-optimization',
        name: 'Substation Optimization',
        selected: false,
    },
];

export const marketModuleOptions: ModuleOption[] = [
    {
        code: 'market',
        name: 'Market',
        selected: true,
    },
    {
        code: 'market-stats',
        name: 'Market Stats',
        selected: true,
    },
    {
        code: 'market-imports',
        name: 'Market Imports',
        selected: false,
    },
    {
        code: 'weather-imports',
        name: 'Weather Imports',
        selected: false,
    },
    {
        code: 'market-tools',
        name: 'Market Tools',
        selected: false,
    },
    {
        code: 'market-scenarios',
        name: 'Market Scenarios',
        selected: false,
    },
    {
        code: 'bme-commands-inspector',
        name: 'Trading exe Inspector',
        selected: false,
    },
    {
        code: 'admin-market-stats',
        name: 'Admin Market Stats',
        selected: false,
    },
    {
        code: 'market-notifications',
        name: 'Market Notifications',
        selected: true,
    }
];

@Component({
    selector: 'app-users-form',
    templateUrl: './users-form.component.html',
    styleUrls: ['./users-form.component.scss']
})
export class UsersFormComponent implements OnInit {

    public mode: string;
    userDto: UserDto;

    public password = '';
    public passwordRepeat = '';

    public countries: any;
    public userGroups: any;

    public hyppoIoCrmModule: Boolean;
    public uploadFileCrmModule: Boolean;
    public activeSubstationsCrmModule: Boolean;
    public marketModule: Boolean;
    public marketStats: Boolean;
    public marketImports: Boolean;
    public weatherImports: Boolean;

    marketModuleOptions: ModuleOption[];
    crmModuleOptions: ModuleOption[];

    constructor(private activatedRoute: ActivatedRoute,
                private userService: UserService,
                private router: Router,
                private notificationToastService: NotificationToastService,
                private userGroupService: UserGroupService
                ) {
    }

    ngOnInit() {
        this.userDto = new UserDto();
        this.userDto.country = new Country();
        this.userDto.country.id = '0';
        this.userDto.rolesCrm = '';
        this.userDto.rolesMarket = '';
        this.userDto.modulesCrm = '';
        this.userDto.modulesMarket = '';

        this.hyppoIoCrmModule = false;
        this.uploadFileCrmModule = false;
        this.activeSubstationsCrmModule = false;
        this.marketModule = false;
        this.marketStats = false;
        this.marketImports = false;
        this.weatherImports = false;

        this.password = '';
        this.passwordRepeat = '';
        this.marketModuleOptions = JSON.parse(JSON.stringify(marketModuleOptions));
        this.crmModuleOptions = JSON.parse(JSON.stringify(crmModuleOptions));

        let id = '0';
        if (this.activatedRoute.snapshot.paramMap.has('id')) {
            id = this.activatedRoute.snapshot.paramMap.get('id');
        }
  
        if (id === '0') {
            this.mode = 'new-record';
            this.crmModuleOptions.forEach(element => element.selected=false)
            this.marketModuleOptions.forEach(element => element.selected=false)
        } else {
            this.mode = 'edit-record';
        }

        if (this.mode === 'edit-record') {
            this.userService.getUser(id).subscribe(data => {
                this.userDto = data;
                console.log(this.userDto)
                
                this.modulesCrmToBooleans();
                this.modulesMarketToBooleans();
                if (this.userDto.country === null) {
                    this.userDto.country = new Country();
                    this.userDto.country.id = '0';
                }
                if(this.userDto.rolesCrm===''){

                    this.crmModuleOptions.forEach(element => element.selected=false)
                    
                }
                 if(this.userDto.rolesMarket === ''){
                    this.marketModuleOptions.forEach(element => element.selected=false)
                }

            });
        }
        

    
        
        
       
        /* if(this.mode==='new-record'){
            this.crmModuleOptions.map(element =>{
                 element.selected()
            })
        } */


        this.refreshCountries();
        this.refreshUserGroups();
    }



    modulesCrmToBooleans() {
        const splitted = this.userDto.modulesCrm.split(',');
        splitted.forEach((module) => {

            const foundModules = this.crmModuleOptions.find(x => x.code === module);
            if (foundModules !== undefined) {
                foundModules.selected = true;
            }

            // if (module === 'hyppoio') {
            //     this.hyppoIoCrmModule = true;
            // } else if (module === 'upload-file') {
            //     this.uploadFileCrmModule = true;
            // }
        });

    }


    modulesMarketToBooleans() {
        const splitted = this.userDto.modulesMarket.split(',');
        splitted.forEach((module) => {

            const foundModules = this.marketModuleOptions.find(x => x.code === module);
            if (foundModules !== undefined) {
                foundModules.selected = true;
            }

            // if (module === 'market') {
            //     this.marketModule = true;
            // } else if (module === 'market-stats') {
            //     this.marketStats = true;
            // } else if (module === 'market-imports') {
            //     this.marketImports = true;
            // } else if (module === 'weather-imports') {
            //     this.weatherImports = true;
            // }
        });
    }
    onMarketRoleChange(event){
        this.marketModuleOptions = JSON.parse(JSON.stringify(marketModuleOptions));
        if(event.value === ''){
            this.marketModuleOptions.forEach(element => element.selected=false)
        }
        

    }
    onCrmRoleChange(event){
        this.crmModuleOptions = JSON.parse(JSON.stringify(crmModuleOptions));
        if(event.value === ''){
            this.crmModuleOptions.forEach(element => element.selected=false)
        }
    }

    booleansToModulesMarket() {

        let moduleListEmpty = true;
        this.userDto.modulesMarket = '';
        console.log(this.marketModuleOptions)
        this.marketModuleOptions.forEach((moduleOption) => {
            
            if (moduleOption.selected) {
                if (moduleListEmpty === false) {
                    this.userDto.modulesMarket += ',';
                }
                this.userDto.modulesMarket += moduleOption.code;
                moduleListEmpty = false;
            }
        });

        // if (this.marketModule === true) {
        //     this.userDto.modulesMarket = 'market';
        //     moduleListEmpty = false;
        // }
        //
        // if (this.marketStats === true) {
        //     if (moduleListEmpty === false) {
        //         this.userDto.modulesMarket += ',';
        //     }
        //     this.userDto.modulesMarket += 'market-stats'
        // }
        //
        // if (this.marketImports === true) {
        //     if (moduleListEmpty === false) {
        //         this.userDto.modulesMarket += ',';
        //     }
        //     this.userDto.modulesMarket += 'market-imports'
        // }
        // if (this.weatherImports === true) {
        //     if (moduleListEmpty === false) {
        //         this.userDto.modulesMarket += ',';
        //     }
        //     this.userDto.modulesMarket += 'weather-imports'
        // }

    }

    booleansToModulesCrm() {
        let moduleListEmpty = true;
        this.userDto.modulesCrm = '';
        this.crmModuleOptions.forEach((moduleOption) => {

            if (moduleOption.selected === true) {
                console.log(moduleOption)
                if (moduleListEmpty === false) {
                    this.userDto.modulesCrm += ',';
                }
                this.userDto.modulesCrm += moduleOption.code;
                moduleListEmpty = false;
            }
        });
        console.log(this.userDto.modulesCrm)
    }


    saveUser() {

        this.booleansToModulesCrm();
        this.booleansToModulesMarket();

        if (this.mode === 'new-record') {
            this.createNewUser();    
        } else {
            this.updateUser();
        }
    }

    createNewUser() {

        if (this.userDto.username === '' ||
            this.userDto.email === '' ||
            this.userDto.password === '' ||
            this.userDto.country.id === '' ||
            this.userDto.country.id === '0' ||
            this.userDto.username === undefined ||
            this.userDto.email === undefined ||
            this.userDto.password === undefined ||
            this.userDto.country.id === undefined
        ) {

            this.notificationToastService.showNotification(
                '<h4>UserName, Email, Country & Password fields must not be empty</h4>',
                'info',
                'top',
                'center',
                'danger');

        }

        this.userDto.status = 'enabled';

        this.userService.save(this.userDto).subscribe(data => {
            this.router.navigate(['/users']);
        });
    }

    refreshCountries() {
        this.userService.getAllCountries().subscribe(data => {
            this.countries = data;
        })
    }

    refreshUserGroups() {
        this.userGroupService.getAllUserGroupsAnyUser().subscribe(data => {
            this.userGroups = data;
        });
        
    }


    


    updateUser() {

        if (this.userDto.username === '' ||
            this.userDto.email === '' ||
            this.userDto.username === undefined ||
            this.userDto.email === undefined
        ) {
            this.notificationToastService.showNotification(
                '<h4>UserName & Email fields must not be empty</h4>',
                'info',
                'top',
                'center',
                'danger');
        }

        this.userService.updateUser(this.userDto).subscribe(data => {
            this.router.navigate(['/users']);
        });

    }



    editPassword() {

        if (this.password === '' || this.passwordRepeat === '') {

            this.notificationToastService.showNotification(
                '<h4>Error</h4> Empty fields.',
                'info',
                'top',
                'center',
                'danger');
            return;

        }


        if (this.password !== this.passwordRepeat) {

            this.notificationToastService.showNotification(
                '<h4>Error</h4>You typed different passwords on the fields.',
                'info',
                'top',
                'center',
                'danger');
            return;

        }

        this.userService.updateUserPassword(this.userDto.id, this.password).subscribe(data => {

            if (data === true) {
                this.notificationToastService.showNotification(
                    '<h4>Sucess</h4>Password updated',
                    'info',
                    'top',
                    'center',
                    'success');
                this.router.navigate(['/users']);
            } else {
                this.notificationToastService.showNotification(
                    '<h4>Error</h4>Could not update password.',
                    'info',
                    'top',
                    'center',
                    'danger');
            }
        });

    }

}
