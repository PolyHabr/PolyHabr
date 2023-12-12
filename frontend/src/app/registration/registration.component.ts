import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Destination, NavigationService} from "../core/services/navigation.service";
import {ErrorCodes} from "../../data/models/error-codes";
import {RegexType} from 'src/data/models/regex-types';
import {InputFieldsType} from 'src/data/models/input-field-types';
import {AuthorizationService} from "../core/services/authorization.service";
import {Authorization} from "../../data/models/authorization";
import {StorageHelper} from "../core/helpers/storage.helper";

@Component({
  selector: 'poly-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  @ViewChild("emailInputElement")
  emailInputElement!: ElementRef;

  @ViewChild("nameInputElement")
  nameInputElement!: ElementRef;

  @ViewChild("surnameInputElement")
  surnameInputElement!: ElementRef;

  @ViewChild("nicknameInputElement")
  nicknameInputElement!: ElementRef;

  @ViewChild("passwordInputElement")
  passwordInputElement!: ElementRef;

  @ViewChild("passwordAgainInputElement")
  passwordAgainInputElement!: ElementRef;

  readonly RegexType = RegexType;
  readonly InputFieldsType = InputFieldsType;
  nameInputValue: string | null = null;
  surnameInputValue: string | null = null;
  nicknameInputValue: string | null = null;
  emailInputValue: string | null = null;

  registerErrorConfig: RegisterErrorConfig = {
    emailError: null,
    nicknameError: null,
    nameError: null,
    surnameError: null,
    passwordError: null,
    passwordAgainError: null
  }

  constructor(private navigationService: NavigationService, private authorizationService: AuthorizationService) {
  }

  ngOnInit(): void {
  }

  toFeed(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.FEED);
  }

  toLogin(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.LOGIN);
  }

  toForgotPassword(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.FORGOT_PASSWORD);
  }

  onEnterButtonClicked(e: Event): void {
    if (this.checkEnterValidation() && this.checkPasswordsIdentical()) {
      e.preventDefault();
      const data: Authorization.SignUp = {
        username: this.nicknameInputElement.nativeElement.value,
        firstName: this.nameInputElement.nativeElement.value,
        lastName: this.surnameInputElement.nativeElement.value,
        email: this.emailInputElement.nativeElement.value,
        password: this.passwordInputElement.nativeElement.value
      };
      this.authorizationService.signUp(data).subscribe(() => {
        StorageHelper.setCookie("email", data.email);
        this.navigationService.navigateTo(Destination.EMAIL_CONFIRM);
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
          case InputFieldsType.EMAIL:
            this.registerErrorConfig.emailError = ErrorCodes.INCORRECT_EMAIL;
            break;
          case InputFieldsType.NICKNAME:
            this.registerErrorConfig.nicknameError = ErrorCodes.NICKNAME_LOGIN;
            break;
          case InputFieldsType.NAME:
            this.registerErrorConfig.nameError = ErrorCodes.INCORRECT_NAME;
            break;
          case InputFieldsType.SURNAME:
            this.registerErrorConfig.surnameError = ErrorCodes.INCORRECT_SURNAME;
            break;
        }
      } else {
        switch (errorField) {
          case InputFieldsType.EMAIL:
            this.registerErrorConfig.emailError = null;
            break;
          case InputFieldsType.NICKNAME:
            this.registerErrorConfig.nicknameError = null;
            break;
          case InputFieldsType.NAME:
            this.registerErrorConfig.nameError = null;
            break;
          case InputFieldsType.SURNAME:
            this.registerErrorConfig.surnameError = null;
            break;
        }
      }
    } else {
      switch (errorField) {
        case InputFieldsType.EMAIL:
          this.registerErrorConfig.emailError = null;
          break;
        case InputFieldsType.NICKNAME:
          this.registerErrorConfig.nicknameError = null;
          break;
        case InputFieldsType.NAME:
          this.registerErrorConfig.nameError = null;
          break;
        case InputFieldsType.SURNAME:
          this.registerErrorConfig.surnameError = null;
          break;
      }
    }
    this.authorizationService.checkFreeLogin((result) => {
      if (this.nicknameInputElement.nativeElement.value.length >= 4 && result == 400) {
        this.registerErrorConfig.nicknameError = ErrorCodes.TAKEN_NICKNAME;
      }
    }, this.nicknameInputElement.nativeElement.value).subscribe();
    this.authorizationService.checkFreeEmail((result) => {
      if (this.emailInputElement.nativeElement.value.length >= 4 && result == 400) {
        this.registerErrorConfig.emailError = ErrorCodes.TAKEN_EMAIL;
      }
    }, this.emailInputElement.nativeElement.value).subscribe();
  }

  checkPasswordsIdentical(): boolean {
    if (this.passwordInputElement.nativeElement.value != this.passwordAgainInputElement.nativeElement.value) {
      this.registerErrorConfig.passwordError = ErrorCodes.PASSWORDS_MISMATCH;
      this.registerErrorConfig.passwordAgainError = ErrorCodes.PASSWORDS_MISMATCH;
      return false;
    } else {
      return true;
    }
  }

  checkEnterValidation(): boolean {
    let legal = true;
    if (this.emailInputElement.nativeElement.value.length > 256) {
      this.registerErrorConfig.emailError = ErrorCodes.EMAIL_BIGGER;
      legal = false;
    }
    if (this.nameInputElement.nativeElement.value.length > 70) {
      this.registerErrorConfig.nameError = ErrorCodes.NAME_BIGGER;
      legal = false;
    }
    if (this.surnameInputElement.nativeElement.value.length > 70) {
      this.registerErrorConfig.surnameError = ErrorCodes.SURNAME_BIGGER;
      legal = false;
    }
    if (this.nicknameInputElement.nativeElement.value.length > 20) {
      this.registerErrorConfig.nicknameError = ErrorCodes.NICKNAME_BIGGER;
      legal = false;
    }
    if (this.nicknameInputElement.nativeElement.value.length < 4) {
      this.registerErrorConfig.nicknameError = ErrorCodes.NICKNAME_LESS;
      legal = false;
    }
    if (this.passwordInputElement.nativeElement.value.length > 72) {
      this.registerErrorConfig.passwordError = ErrorCodes.PASSWORD_BIGGER;
      legal = false;
    }
    if (this.passwordInputElement.nativeElement.value.length < 8) {
      this.registerErrorConfig.passwordError = ErrorCodes.PASSWORD_LESS;
      legal = false;
    }
    if (this.emailInputElement.nativeElement.value.length == 0) {
      this.registerErrorConfig.emailError = ErrorCodes.NON_FILLED_FIELD;
      legal = false;
    }
    if (this.nicknameInputElement.nativeElement.value.length == 0) {
      this.registerErrorConfig.nicknameError = ErrorCodes.NON_FILLED_FIELD;
      legal = false;
    }
    if (this.nameInputElement.nativeElement.value.length == 0) {
      this.registerErrorConfig.nameError = ErrorCodes.NON_FILLED_FIELD;
      legal = false;
    }
    if (this.surnameInputElement.nativeElement.value.length == 0) {
      this.registerErrorConfig.surnameError = ErrorCodes.NON_FILLED_FIELD;
      legal = false;
    }
    if (this.passwordInputElement.nativeElement.value.length == 0) {
      this.registerErrorConfig.passwordError = ErrorCodes.NON_FILLED_FIELD;
      legal = false;
    }
    if (this.passwordAgainInputElement.nativeElement.value.length == 0) {
      this.registerErrorConfig.passwordAgainError = ErrorCodes.NON_FILLED_FIELD;
      legal = false;
    }
    return legal;
  }

}

export interface RegisterErrorConfig {
  emailError: ErrorCodes | null,
  nameError: ErrorCodes | null,
  surnameError: ErrorCodes | null,
  nicknameError: ErrorCodes | null,
  passwordError: ErrorCodes | null,
  passwordAgainError: ErrorCodes | null
}
