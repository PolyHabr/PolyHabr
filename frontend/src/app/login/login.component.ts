import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Destination, NavigationService} from "../core/services/navigation.service";
import {ErrorCodes} from "../../data/models/error-codes";
import {AuthorizationService} from "../core/services/authorization.service";
import {RegisterErrorConfig} from "../registration/registration.component";
import {RegexType} from "../../data/models/regex-types";
import {InputFieldsType} from "../../data/models/input-field-types";
import {Authorization} from "../../data/models/authorization";
import {StorageHelper} from "../core/helpers/storage.helper";
import {DataHelper} from "../core/helpers/data.helper";
import {UsersService} from "../core/services/users.service";

@Component({
  selector: 'poly-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  @ViewChild("nicknameInputElement")
  nicknameInputElement!: ElementRef;

  @ViewChild("passwordInputElement")
  passwordInputElement!: ElementRef;
  readonly RegexType = RegexType;
  readonly InputFieldsType = InputFieldsType;
  nicknameInputValue: string | null = null;

  registerErrorConfig: RegisterErrorConfig = {
    emailError: null,
    nicknameError: null,
    nameError: null,
    surnameError: null,
    passwordError: null,
    passwordAgainError: null
  }

  constructor(private navigationService: NavigationService, private authorizationService: AuthorizationService,
              private usersService: UsersService) {
  }

  ngOnInit(): void {
  }

  toFeed(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.FEED);
  }

  toRegistration(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.REGISTER);
  }

  toForgotPassword(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.FORGOT_PASSWORD);
  }

  onEnterButtonClicked(e: Event): void {
    if (this.registerErrorConfig.nicknameError == null) {
      e.preventDefault();
      const data: Authorization.SignIn = {
        username: this.nicknameInputElement.nativeElement.value,
        password: this.passwordInputElement.nativeElement.value
      }
      this.authorizationService.signIn(() => {
        this.registerErrorConfig.passwordError = ErrorCodes.LOGIN_ERROR;
      }, data).subscribe(loginResult => {
        StorageHelper.setCookie("accessToken", loginResult.accessToken);
        this.usersService.getMe().subscribe(result => {
          DataHelper.user = result;
          if (loginResult.isFirst) {
          this.navigationService.navigateTo(Destination.TYPES);
          } else {
            this.navigationService.navigateTo(Destination.FEED);
          }
        });
      });
    }
  }

  checkFieldValidation(regexType: RegexType, element: any, errorField: InputFieldsType): void {
    let parseString: string;
    if (element instanceof HTMLInputElement) {
      parseString = String(element.value);
    } else {
      parseString = String(element);
    }
    const regex = new RegExp(regexType, 'g');
    const result = parseString.match(regex);
    if (parseString.length > 0) {
      if (result == null) {
        switch (errorField) {
          case InputFieldsType.NICKNAME:
            this.registerErrorConfig.nicknameError = ErrorCodes.NICKNAME_LOGIN;
            break;
        }
      } else {
        switch (errorField) {
          case InputFieldsType.NICKNAME:
            this.registerErrorConfig.nicknameError = null;
            break;
        }
      }
    }
  }
}
