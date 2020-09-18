
export class MessageInfoDTO {
    constructor(
    public uuid: string,
    public id: string,
    public name: string,
    public tag: string,
    public timeSent: string,
    public userGroup: string,
    public sender: string,
    public metadata: string,
    public createdOn: string
    ) {}
  }