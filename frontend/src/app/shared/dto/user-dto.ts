import { RoleDTO } from './role-dto';
import { ModuleDTO } from './module-dto';

export class UserDto {
    id: string;
    username: string;
    email: string;
    password: string;
    status: string;
    createdOn: Date;
    salt: string;
    users: Members[];
    country: Country;
    rolesCrm: String;
    modulesCrm: String;
    rolesMarket: String;
    modulesMarket: String;
    defaultMarket: String;
    market: String;
    landingPage: String;
    activeSubstationUserGroupId: number;
}

export interface Members {
    userGroup: string;
    users: String[];
}

export class Country {
    id: string;
    name: string;
}
