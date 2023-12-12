import {ComponentFixture, fakeAsync, inject, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {RegistrationComponent} from "./registration.component";
import {Router} from "@angular/router";
import {Destination} from "../core/services/navigation.service";
import {ErrorCodes} from "../../data/models/error-codes";
import {RegexType} from "../../data/models/regex-types";
import {InputFieldsType} from "../../data/models/input-field-types";
import {AuthorizationService} from "../core/services/authorization.service";
import {environment} from "../../environments/environment";

describe("Registration tests",() => {
  let component: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RegistrationComponent]
    }).compileComponents();
  })
  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Init registration component test', () => {
    expect(component).toBeTruthy();
  });

  it('To feed page navigation test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    component.toFeed(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.FEED.toPath());
  }));

  it('To login page navigation test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    component.toLogin(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.LOGIN.toPath());
  }));

  it('To forgot password page navigation test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    component.toForgotPassword(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.FORGOT_PASSWORD.toPath());
  }));

  it("Identical passwords", () => {
    component.passwordInputElement.nativeElement.value = "admincool";
    component.passwordAgainInputElement.nativeElement.value = "admincool";
    component.checkPasswordsIdentical();
    expect(component.registerErrorConfig.passwordError).toBe(null);
  });

  it("Non identical passwords", () => {
    component.passwordInputElement.nativeElement.value = "admincool";
    component.passwordAgainInputElement.nativeElement.value = "admincool1";
    component.checkPasswordsIdentical();
    expect(component.registerErrorConfig.passwordError).toBe(ErrorCodes.PASSWORDS_MISMATCH);
    expect(component.registerErrorConfig.passwordAgainError).toBe(ErrorCodes.PASSWORDS_MISMATCH);
  });

  it("Email bigger 256 signs", () => {
    let email = "";
    for (let i = 0; i <= 256; i++) {
      email += "a";
    }
    component.emailInputElement.nativeElement.value = email;
    component.checkEnterValidation();
    expect(component.registerErrorConfig.emailError).toBe(ErrorCodes.EMAIL_BIGGER);
  });

  it("Name bigger 70 signs", () => {
    let name = "";
    for (let i = 0; i <= 70; i++) {
      name += "a";
    }
    component.nameInputElement.nativeElement.value = name;
    component.checkEnterValidation();
    expect(component.registerErrorConfig.nameError).toBe(ErrorCodes.NAME_BIGGER);
  });

  it("Surname bigger 70 signs", () => {
    let surname = "";
    for (let i = 0; i <= 70; i++) {
      surname += "a";
    }
    component.surnameInputElement.nativeElement.value = surname;
    component.checkEnterValidation();
    expect(component.registerErrorConfig.surnameError).toBe(ErrorCodes.SURNAME_BIGGER);
  });

  it("Nickname bigger 70 signs", () => {
    let nickname = "";
    for (let i = 0; i <= 70; i++) {
      nickname += "a";
    }
    component.nicknameInputElement.nativeElement.value = nickname;
    component.checkEnterValidation();
    expect(component.registerErrorConfig.nicknameError).toBe(ErrorCodes.NICKNAME_BIGGER);
  });

  it("Nickname less 4 signs", () => {
    component.nicknameInputElement.nativeElement.value = "a";
    component.checkEnterValidation();
    expect(component.registerErrorConfig.nicknameError).toBe(ErrorCodes.NICKNAME_LESS);
  });

  it("Password bigger 72 signs", () => {
    let password = "";
    for (let i = 0; i <= 72; i++) {
      password += "a";
    }
    component.passwordInputElement.nativeElement.value = password;
    component.checkEnterValidation();
    expect(component.registerErrorConfig.passwordError).toBe(ErrorCodes.PASSWORD_BIGGER);
  });

  it("Password less 4 signs", () => {
    component.passwordInputElement.nativeElement.value = "a";
    component.checkEnterValidation();
    expect(component.registerErrorConfig.passwordError).toBe(ErrorCodes.PASSWORD_LESS);
  });

  it("Email equals 0", () => {
    component.emailInputElement.nativeElement.value = "";
    component.checkEnterValidation();
    expect(component.registerErrorConfig.emailError).toBe(ErrorCodes.NON_FILLED_FIELD);
  });

  it("Nickname equals 0", () => {
    component.nicknameInputElement.nativeElement.value = "";
    component.checkEnterValidation();
    expect(component.registerErrorConfig.nicknameError).toBe(ErrorCodes.NON_FILLED_FIELD);
  });

  it("Name equals 0", () => {
    component.nameInputElement.nativeElement.value = "";
    component.checkEnterValidation();
    expect(component.registerErrorConfig.nameError).toBe(ErrorCodes.NON_FILLED_FIELD);
  });

  it("Surname equals 0", () => {
    component.surnameInputElement.nativeElement.value = "";
    component.checkEnterValidation();
    expect(component.registerErrorConfig.surnameError).toBe(ErrorCodes.NON_FILLED_FIELD);
  });

  it("Password equals 0", () => {
    component.passwordInputElement.nativeElement.value = "";
    component.checkEnterValidation();
    expect(component.registerErrorConfig.passwordError).toBe(ErrorCodes.NON_FILLED_FIELD);
  });

  it("Repeated password equals 0", () => {
    component.passwordAgainInputElement.nativeElement.value = "";
    component.checkEnterValidation();
    expect(component.registerErrorConfig.passwordAgainError).toBe(ErrorCodes.NON_FILLED_FIELD);
  });

  it('Check nickname field validation test: regex fail', () => {
    const input = component.nicknameInputElement;
    input.nativeElement.value = "admin./1";
    component.checkFieldValidation(RegexType.LOGIN, input, InputFieldsType.NICKNAME);
    expect(component.registerErrorConfig.nicknameError).toBe(ErrorCodes.NICKNAME_LOGIN);
  });

  it('Check nickname field validation test: regex correct', () => {
    const input = component.nicknameInputElement;
    input.nativeElement.value = "admin";
    component.checkFieldValidation(RegexType.LOGIN, input.nativeElement, InputFieldsType.NICKNAME);
    expect(component.registerErrorConfig.nicknameError).toBe(null);
  });

  it('Check nickname field validation test: field empty', () => {
    const input = component.nicknameInputElement;
    input.nativeElement.value = "";
    component.checkFieldValidation(RegexType.LOGIN, input.nativeElement, InputFieldsType.NICKNAME);
    expect(component.registerErrorConfig.nicknameError).toBe(null);
  });

  it('Check email field validation test: regex fail', () => {
    const input = component.emailInputElement;
    input.nativeElement.value = "admin./1";
    component.checkFieldValidation(RegexType.EMAIL, input, InputFieldsType.EMAIL);
    expect(component.registerErrorConfig.emailError).toBe(ErrorCodes.INCORRECT_EMAIL);
  });

  it('Check email field validation test: regex correct', () => {
    const input = component.emailInputElement;
    input.nativeElement.value = "admin@gmail.com";
    component.checkFieldValidation(RegexType.EMAIL, input.nativeElement, InputFieldsType.EMAIL);
    expect(component.registerErrorConfig.emailError).toBe(null);
  });

  it('Check email field validation test: field empty', () => {
    const input = component.emailInputElement;
    input.nativeElement.value = "";
    component.checkFieldValidation(RegexType.EMAIL, input.nativeElement, InputFieldsType.EMAIL);
    expect(component.registerErrorConfig.emailError).toBe(null);
  });

  it('Check name field validation test: regex fail', () => {
    const input = component.nameInputElement;
    input.nativeElement.value = "admin./1";
    component.checkFieldValidation(RegexType.NAME, input, InputFieldsType.NAME);
    expect(component.registerErrorConfig.nameError).toBe(ErrorCodes.INCORRECT_NAME);
  });

  it('Check name field validation test: regex correct', () => {
    const input = component.nameInputElement;
    input.nativeElement.value = "";
    component.checkFieldValidation(RegexType.NAME, input.nativeElement, InputFieldsType.NAME);
    expect(component.registerErrorConfig.nameError).toBe(null);
  });

  it('Check name field validation test: field empty', () => {
    const input = component.nameInputElement;
    input.nativeElement.value = "admin";
    component.checkFieldValidation(RegexType.NAME, input.nativeElement, InputFieldsType.NAME);
    expect(component.registerErrorConfig.nameError).toBe(null);
  });

  it('Check surname field validation test: regex fail', () => {
    const input = component.surnameInputElement;
    input.nativeElement.value = "admin./1";
    component.checkFieldValidation(RegexType.SURNAME, input, InputFieldsType.SURNAME);
    expect(component.registerErrorConfig.surnameError).toBe(ErrorCodes.INCORRECT_SURNAME);
  });

  it('Check name field validation test: regex correct', () => {
    const input = component.surnameInputElement;
    input.nativeElement.value = "admin";
    component.checkFieldValidation(RegexType.SURNAME, input.nativeElement, InputFieldsType.SURNAME);
    expect(component.registerErrorConfig.surnameError).toBe(null);
  });

  it('Check name field validation test: field empty', () => {
    const input = component.surnameInputElement;
    input.nativeElement.value = "";
    component.checkFieldValidation(RegexType.SURNAME, input.nativeElement, InputFieldsType.SURNAME);
    expect(component.registerErrorConfig.surnameError).toBe(null);
  });

  it("Login isn't free", fakeAsync(
    inject([AuthorizationService, HttpTestingController],
      (authorizationService: AuthorizationService, backend: HttpTestingController) => {
        component.nicknameInputElement.nativeElement.value = "admin";
        component.checkFieldValidation(RegexType.LOGIN, component.nicknameInputElement.nativeElement, InputFieldsType.NICKNAME);
        backend.expectOne({url: `${environment.api_url}/api/auth/checkFreeLogin?login=admin&`}).flush({}, {status: 400, statusText: "Bad request"});
        expect(component.registerErrorConfig.nicknameError).toBe(ErrorCodes.TAKEN_NICKNAME);
      })
  ));

  it("Email isn't free", fakeAsync(
    inject([AuthorizationService, HttpTestingController],
      (authorizationService: AuthorizationService, backend: HttpTestingController) => {
        component.emailInputElement.nativeElement.value = "admin@mail.ru";
        component.checkFieldValidation(RegexType.EMAIL, component.emailInputElement.nativeElement, InputFieldsType.EMAIL);
        backend.expectOne({url: `${environment.api_url}/api/auth/checkFreeEmail?email=admin@mail.ru&`}).flush({}, {status: 400, statusText: "Bad request"});
        expect(component.registerErrorConfig.emailError).toBe(ErrorCodes.TAKEN_EMAIL);
      })
  ));

  it("Register new user", fakeAsync(
    inject([AuthorizationService, HttpTestingController, Router],
      (authorizationService: AuthorizationService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();

        component.nicknameInputElement.nativeElement.value = "admin1";
        component.nameInputElement.nativeElement.value = "Admin";
        component.surnameInputElement.nativeElement.value = "admin";
        component.emailInputElement.nativeElement.value = "admin1@gmail.com";
        component.passwordInputElement.nativeElement.value = "admin1admin";
        component.passwordAgainInputElement.nativeElement.value = "admin1admin";

        component.onEnterButtonClicked(new Event(""));
        backend.expectOne({}).flush({});

        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.EMAIL_CONFIRM.toPath());
      })
  ));
});
