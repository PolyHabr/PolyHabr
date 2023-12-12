import {Component, ElementRef, ViewChild} from '@angular/core';
import {InputFieldsType} from 'src/data/models/input-field-types';
import {RegexType} from 'src/data/models/regex-types';
import {ErrorCodes} from "../../data/models/error-codes";
import {Destination, NavigationService} from "../core/services/navigation.service";
import {DataHelper} from "../core/helpers/data.helper";
import {UsersService} from "../core/services/users.service";
import {Data} from "../core/types/Data";

@Component({
  selector: 'poly-profile-settings',
  templateUrl: './profile-settings.component.html',
  styleUrls: ['./profile-settings.component.scss']
})
export class ProfileSettingsComponent {

  @ViewChild("emailInputElement")
  emailInputElement!: ElementRef;

  @ViewChild("nameInputElement")
  nameInputElement!: ElementRef;

  @ViewChild("surnameInputElement")
  surnameInputElement!: ElementRef;

  @ViewChild("passwordOldInputElement")
  passwordOldInputElement!: ElementRef;

  @ViewChild("passwordInputElement")
  passwordInputElement!: ElementRef;

  @ViewChild("passwordAgainInputElement")
  passwordAgainInputElement!: ElementRef;

  readonly RegexType = RegexType;
  readonly InputFieldsType = InputFieldsType;
  readonly DataHelper = DataHelper;
  nameInputValue: string | null = null;
  surnameInputValue: string | null = null;
  emailInputValue: string | null = null;
  oldPasswordInputValue: string | null = null;
  passwordInputValue: string | null = null;
  submitPasswordInputValue: string | null = null;

  profileSettingsErrorConfig: ProfileSettingsErrorConfig = {
    emailError: null,
    nameError: null,
    surnameError: null,
    passwordOldError: null,
    passwordError: null,
    passwordAgainError: null
  }

  constructor(private navigationService: NavigationService, private usersService: UsersService) {
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
    if (result == null) {
      if (parseString.length > 0) {
        switch (errorField) {
          case InputFieldsType.EMAIL:
            this.profileSettingsErrorConfig.emailError = ErrorCodes.INCORRECT_EMAIL;
            break;
          case InputFieldsType.NAME:
            this.profileSettingsErrorConfig.nameError = ErrorCodes.INCORRECT_NAME;
            break;
          case InputFieldsType.SURNAME:
            this.profileSettingsErrorConfig.surnameError = ErrorCodes.INCORRECT_SURNAME;
            break;
        }
      } else {
        switch (errorField) {
          case InputFieldsType.EMAIL:
            this.profileSettingsErrorConfig.emailError = null;
            break;
          case InputFieldsType.NAME:
            this.profileSettingsErrorConfig.nameError = null;
            break;
          case InputFieldsType.SURNAME:
            this.profileSettingsErrorConfig.surnameError = null;
            break;
        }
      }
    } else {
      switch (errorField) {
        case InputFieldsType.EMAIL:
          this.profileSettingsErrorConfig.emailError = null;
          break;
        case InputFieldsType.NAME:
          this.profileSettingsErrorConfig.nameError = null;
          break;
        case InputFieldsType.SURNAME:
          this.profileSettingsErrorConfig.surnameError = null;
          break;
      }
    }
  }

  onClickConfirmChanges(e: Event): void {
    if (this.checkEnterValidation() && this.checkPasswordsIdentical()) {
      e.preventDefault();
      let body = {
        name: this.nameInputElement.nativeElement.value,
        surname: this.surnameInputElement.nativeElement.value,
        email: this.emailInputElement.nativeElement.value,
      } as Data;
      if (this.passwordInputElement.nativeElement.value.length > 0) {
        body["newPassword"] = this.passwordInputElement.nativeElement.value;
        body["password"] = this.passwordOldInputElement.nativeElement.value;
      }
      this.usersService.update(body).subscribe(() => {
        this.usersService.getMe().subscribe(result => DataHelper.user = result);
        this.toProfile();
      });
    }
  }

  toTypes(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.TYPES);
  }

  toProfile(): void {
    this.navigationService.navigateTo(Destination.PROFILE);
  }

  checkPasswordsIdentical(): boolean {
    if (this.passwordInputElement.nativeElement.value != this.passwordAgainInputElement.nativeElement.value) {
      this.profileSettingsErrorConfig.passwordError = ErrorCodes.PASSWORDS_MISMATCH;
      this.profileSettingsErrorConfig.passwordAgainError = ErrorCodes.PASSWORDS_MISMATCH;
      return false;
    } else {
      return true;
    }
  }

  checkEnterValidation(): boolean {
    let legal = true;
    if (this.emailInputElement.nativeElement.value.length > 256) {
      this.profileSettingsErrorConfig.emailError = ErrorCodes.EMAIL_BIGGER;
      legal = false;
    }
    if (this.nameInputElement.nativeElement.value.length > 72) {
      this.profileSettingsErrorConfig.nameError = ErrorCodes.NAME_BIGGER;
      legal = false;
    }
    if (this.surnameInputElement.nativeElement.value.length > 72) {
      this.profileSettingsErrorConfig.surnameError = ErrorCodes.SURNAME_BIGGER;
      legal = false;
    }
    if (this.passwordOldInputElement.nativeElement.value.length > 0 && this.passwordOldInputElement.nativeElement.value.length > 72) {
      this.profileSettingsErrorConfig.passwordOldError = ErrorCodes.PASSWORD_BIGGER;
      legal = false;
    }
    if (this.passwordAgainInputElement.nativeElement.value.length > 0 && this.passwordAgainInputElement.nativeElement.value.length > 72) {
      this.profileSettingsErrorConfig.passwordAgainError = ErrorCodes.PASSWORD_BIGGER;
      legal = false;
    }
    if (this.passwordInputElement.nativeElement.value.length > 0 && this.passwordInputElement.nativeElement.value.length < 8) {
      this.profileSettingsErrorConfig.passwordError = ErrorCodes.PASSWORD_LESS;
      legal = false;
    }
    if (this.passwordOldInputElement.nativeElement.value.length > 0 && this.passwordOldInputElement.nativeElement.value.length < 8) {
      this.profileSettingsErrorConfig.passwordOldError = ErrorCodes.PASSWORD_LESS;
      legal = false;
    }
    if (this.passwordAgainInputElement.nativeElement.value.length > 0 && this.passwordAgainInputElement.nativeElement.value.length < 8) {
      this.profileSettingsErrorConfig.passwordAgainError = ErrorCodes.PASSWORD_LESS;
      legal = false;
    }
    if (this.emailInputElement.nativeElement.value.length == 0) {
      this.profileSettingsErrorConfig.emailError = ErrorCodes.NON_FILLED_FIELD;
      legal = false;
    }
    if (this.nameInputElement.nativeElement.value.length == 0) {
      this.profileSettingsErrorConfig.nameError = ErrorCodes.NON_FILLED_FIELD;
      legal = false;
    }
    if (this.surnameInputElement.nativeElement.value.length == 0) {
      this.profileSettingsErrorConfig.surnameError = ErrorCodes.NON_FILLED_FIELD;
      legal = false;
    }
    return legal;
  }

}

export interface ProfileSettingsErrorConfig {
  emailError: ErrorCodes | null,
  nameError: ErrorCodes | null,
  surnameError: ErrorCodes | null,
  passwordOldError: ErrorCodes | null,
  passwordError: ErrorCodes | null,
  passwordAgainError: ErrorCodes | null
}
