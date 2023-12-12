import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Destination, NavigationService} from "../core/services/navigation.service";
import {RegisterErrorConfig} from "../registration/registration.component";
import {ErrorCodes} from "../../data/models/error-codes";
import {InputFieldsType} from "../../data/models/input-field-types";
import {RegexType} from "../../data/models/regex-types";
import {AuthorizationService} from "../core/services/authorization.service";

@Component({
  selector: 'poly-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {

  @ViewChild("emailInputElement")
  emailInputElement!: ElementRef;

  readonly RegexType = RegexType;
  readonly InputFieldsType = InputFieldsType;

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
            this.registerErrorConfig.emailError = ErrorCodes.INCORRECT_EMAIL;
            break;
        }
      } else {
        switch (errorField) {
          case InputFieldsType.EMAIL:
            this.registerErrorConfig.emailError = null;
            break;
        }
      }
    } else {
      switch (errorField) {
        case InputFieldsType.EMAIL:
          this.registerErrorConfig.emailError = null;
          break;
      }
    }
  }

  sendEmail(e: Event): void {
    if (this.registerErrorConfig.emailError == null) {
      e.preventDefault();
      this.authorizationService.forgotPassword(this.emailInputElement.nativeElement.value).subscribe(result => {
        console.log(result);
      });
    }
  }

  toFeed(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.FEED);
  }

  toRegistration(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.REGISTER);
  }

  toLogin(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.LOGIN);
  }

}
