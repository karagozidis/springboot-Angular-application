import { Component, OnInit } from '@angular/core';
import { UserDto, Country } from 'app/shared/dto/user-dto';
import { UserService } from 'app/services/user.service';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { NotificationToastService } from '../../shared/utils/notification-toast-service';
import { MAT_AUTOCOMPLETE_SCROLL_STRATEGY, MAT_SELECT_SCROLL_STRATEGY,
  MatAutocomplete, MatChipInputEvent, MatAutocompleteSelectedEvent } from '@angular/material';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  userDto: UserDto;

  public password: string = '';
  public passwordRepeat: string = '';
  public oldPassword: string = '';

  public countries: any;

  constructor(private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private notificationToastService: NotificationToastService ) { }

  ngOnInit() {
    this.userService.getCurrentUser().subscribe(data => {
      this.userDto = data;
    });

    this.refreshCountries();

  }

  saveUser() {

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

       return;
    }


    this.userService.update(this.userDto).subscribe();


    this.notificationToastService.showNotification(
      '<h4>Data updated</h4> User data updated sucessfully',
      'info',
      'top',
      'center',
      'success');

  }

  refreshCountries() {
    this.userService.getAllCountries().subscribe(data => {
      this.countries = data;
    })
  }


  editPassword() {

    if ( this.password === '' || this.passwordRepeat === '' ||  this.oldPassword === ''  ) {

        this.notificationToastService.showNotification(
          '<h4>Error</h4> Empty fields.',
          'info',
          'top',
          'center',
          'danger');
          return;

    }


    if ( this.password !== this.passwordRepeat ) {

      this.notificationToastService.showNotification(
        '<h4>Error</h4>You typed different passwords on the fields.',
        'info',
        'top',
        'center',
        'danger');
        return;

    }

    this.userService.updatePassword(this.oldPassword, this.password ).subscribe(data => {

      if (data === true) {

        this.notificationToastService.showNotification(
          '<h4>Sucess</h4>Password updated',
          'info',
          'top',
          'center',
          'success');

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
