import { UserDto } from "./user-dto";

export class UserGroupDTO {
    constructor(
    public name: string,
    public description: string,
    public users: Array<UserDto>
    ) {}
  }