export class NotificationDto {
    id: string;
    name: string;
    description: string;
    timeSent: Date;
    viewed: Boolean;
    status: string;
    type: string;
    senderId: string;
    receiverId: string;
    createdOn: Date;
}
