import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Destination, NavigationService} from "../core/services/navigation.service";
import {RegisterErrorConfig} from "../registration/registration.component";
import {AuthorizationService} from "../core/services/authorization.service";
import {Authorization} from "../../data/models/authorization";
import {ErrorCodes} from "../../data/models/error-codes";
import {StorageHelper} from "../core/helpers/storage.helper";

@Component({
  selector: 'poly-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  @ViewChild("passwordInputElement")
  passwordInputElement!: ElementRef;

  @ViewChild("passwordAgainInputElement")
  passwordAgainInputElement!: ElementRef;

  registerErrorConfig: RegisterErrorConfig = {
    emailError: null,
    nicknameError: null,
    nameError: null,
    surnameError: null,
    passwordError: null,
    passwordAgainError: null
  }

  constructor(private navigationService: NavigationService, private authorizationService: AuthorizationService) { }

  ngOnInit(): void {
  }

  toFeed(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.FEED);
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
    if (this.passwordInputElement.nativeElement.value.length > 72) {
      this.registerErrorConfig.passwordError = ErrorCodes.PASSWORD_BIGGER;
      legal = false;
    }
    if (this.passwordInputElement.nativeElement.value.length < 8) {
      this.registerErrorConfig.passwordError = ErrorCodes.PASSWORD_LESS;
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

  confirmPassword(e: Event): void {
    if (this.checkPasswordsIdentical() && this.checkEnterValidation()) {
      e.preventDefault();
      const passwordToken: string | undefined = StorageHelper.getCookie("password-token");
      console.log(passwordToken);
      if (passwordToken != undefined) {
        const data: Authorization.SavePassword = {
          token: passwordToken,
          newPassword: this.passwordInputElement.nativeElement.value
        }
        this.authorizationService.savePassword(data).subscribe(() => {
          StorageHelper.deleteCookie("password-token");
          this.navigationService.navigateTo(Destination.LOGIN);
        });
      }
    }
  }

}
