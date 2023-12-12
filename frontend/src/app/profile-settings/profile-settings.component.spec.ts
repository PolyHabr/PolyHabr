import {ComponentFixture, fakeAsync, inject, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ProfileSettingsComponent} from "./profile-settings.component";
import {RegexType} from "../../data/models/regex-types";
import {InputFieldsType} from "../../data/models/input-field-types";
import {ErrorCodes} from "../../data/models/error-codes";
import {Router} from "@angular/router";
import {Destination} from "../core/services/navigation.service";
import {UsersService} from "../core/services/users.service";

describe("Profile settings component tests",() => {
  let component: ProfileSettingsComponent;
  let fixture: ComponentFixture<ProfileSettingsComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProfileSettingsComponent]
    }).compileComponents();
  });
  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Profile settings component test', () => {
    expect(component).toBeTruthy();
  });

  it('Check email field validation test: regex fail', () => {
    const input = component.emailInputElement;
    input.nativeElement.value = "admin./1";
    component.checkFieldValidation(RegexType.EMAIL, input, InputFieldsType.EMAIL);
    expect(component.profileSettingsErrorConfig.emailError).toBe(ErrorCodes.INCORRECT_EMAIL);
  });

  it('Check email field validation test: regex correct', () => {
    const input = component.emailInputElement;
    input.nativeElement.value = "admin@gmail.com";
    component.checkFieldValidation(RegexType.EMAIL, input.nativeElement, InputFieldsType.EMAIL);
    expect(component.profileSettingsErrorConfig.emailError).toBe(null);
  });

  it('Check email field validation test: field empty', () => {
    const input = component.emailInputElement;
    input.nativeElement.value = "";
    component.checkFieldValidation(RegexType.EMAIL, input.nativeElement, InputFieldsType.EMAIL);
    expect(component.profileSettingsErrorConfig.emailError).toBe(null);
  });

  it('Check name field validation test: regex fail', () => {
    const input = component.nameInputElement;
    input.nativeElement.value = "admin./1";
    component.checkFieldValidation(RegexType.NAME, input, InputFieldsType.NAME);
    expect(component.profileSettingsErrorConfig.nameError).toBe(ErrorCodes.INCORRECT_NAME);
  });

  it('Check name field validation test: regex correct', () => {
    const input = component.nameInputElement;
    input.nativeElement.value = "";
    component.checkFieldValidation(RegexType.NAME, input.nativeElement, InputFieldsType.NAME);
    expect(component.profileSettingsErrorConfig.nameError).toBe(null);
  });

  it('Check name field validation test: field empty', () => {
    const input = component.nameInputElement;
    input.nativeElement.value = "admin";
    component.checkFieldValidation(RegexType.NAME, input.nativeElement, InputFieldsType.NAME);
    expect(component.profileSettingsErrorConfig.nameError).toBe(null);
  });

  it('Check surname field validation test: regex fail', () => {
    const input = component.surnameInputElement;
    input.nativeElement.value = "admin./1";
    component.checkFieldValidation(RegexType.SURNAME, input, InputFieldsType.SURNAME);
    expect(component.profileSettingsErrorConfig.surnameError).toBe(ErrorCodes.INCORRECT_SURNAME);
  });

  it('Check name field validation test: regex correct', () => {
    const input = component.surnameInputElement;
    input.nativeElement.value = "admin";
    component.checkFieldValidation(RegexType.SURNAME, input.nativeElement, InputFieldsType.SURNAME);
    expect(component.profileSettingsErrorConfig.surnameError).toBe(null);
  });

  it('Check name field validation test: field empty', () => {
    const input = component.surnameInputElement;
    input.nativeElement.value = "";
    component.checkFieldValidation(RegexType.SURNAME, input.nativeElement, InputFieldsType.SURNAME);
    expect(component.profileSettingsErrorConfig.surnameError).toBe(null);
  });

  it('To types page navigation test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    component.toTypes(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.TYPES.toPath());
  }));

  it('To profile page navigation test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    component.toProfile();
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.PROFILE.toPath());
  }));

  it("Identical passwords", () => {
    component.passwordInputElement.nativeElement.value = "admincool";
    component.passwordAgainInputElement.nativeElement.value = "admincool";
    component.checkPasswordsIdentical();
    expect(component.profileSettingsErrorConfig.passwordError).toBe(null);
  });

  it("Non identical passwords", () => {
    component.passwordInputElement.nativeElement.value = "admincool";
    component.passwordAgainInputElement.nativeElement.value = "admincool1";
    component.checkPasswordsIdentical();
    expect(component.profileSettingsErrorConfig.passwordError).toBe(ErrorCodes.PASSWORDS_MISMATCH);
    expect(component.profileSettingsErrorConfig.passwordAgainError).toBe(ErrorCodes.PASSWORDS_MISMATCH);
  });

  it("Email bigger 256 signs", () => {
    let email = "";
    for (let i = 0; i <= 256; i++) {
      email += "a";
    }
    component.emailInputElement.nativeElement.value = email;
    component.checkEnterValidation();
    expect(component.profileSettingsErrorConfig.emailError).toBe(ErrorCodes.EMAIL_BIGGER);
  });

  it("Name bigger 72 signs", () => {
    let name = "";
    for (let i = 0; i <= 73; i++) {
      name += "a";
    }
    component.nameInputElement.nativeElement.value = name;
    component.checkEnterValidation();
    expect(component.profileSettingsErrorConfig.nameError).toBe(ErrorCodes.NAME_BIGGER);
  });

  it("Surname bigger 72 signs", () => {
    let surname = "";
    for (let i = 0; i <= 72; i++) {
      surname += "a";
    }
    component.surnameInputElement.nativeElement.value = surname;
    component.checkEnterValidation();
    expect(component.profileSettingsErrorConfig.surnameError).toBe(ErrorCodes.SURNAME_BIGGER);
  });

  it("Password bigger 72 signs", () => {
    let password = "";
    for (let i = 0; i <= 73; i++) {
      password += "a";
    }
    component.passwordOldInputElement.nativeElement.value = password;
    component.passwordAgainInputElement.nativeElement.value = password;
    component.checkEnterValidation();
    expect(component.profileSettingsErrorConfig.passwordOldError).toBe(ErrorCodes.PASSWORD_BIGGER);
    expect(component.profileSettingsErrorConfig.passwordAgainError).toBe(ErrorCodes.PASSWORD_BIGGER);
  });

  it("Password less 4 signs", () => {
    component.passwordInputElement.nativeElement.value = "a";
    component.passwordOldInputElement.nativeElement.value = "a";
    component.passwordAgainInputElement.nativeElement.value = "a";
    component.checkEnterValidation();
    expect(component.profileSettingsErrorConfig.passwordError).toBe(ErrorCodes.PASSWORD_LESS);
    expect(component.profileSettingsErrorConfig.passwordOldError).toBe(ErrorCodes.PASSWORD_LESS);
    expect(component.profileSettingsErrorConfig.passwordAgainError).toBe(ErrorCodes.PASSWORD_LESS);
  });

  it("Email equals 0", () => {
    component.emailInputElement.nativeElement.value = "";
    component.checkEnterValidation();
    expect(component.profileSettingsErrorConfig.emailError).toBe(ErrorCodes.NON_FILLED_FIELD);
  });

  it("Surname equals 0", () => {
    component.surnameInputElement.nativeElement.value = "";
    component.checkEnterValidation();
    expect(component.profileSettingsErrorConfig.surnameError).toBe(ErrorCodes.NON_FILLED_FIELD);
  });

  it("Register new user", fakeAsync(
    inject([UsersService, HttpTestingController, Router],
      (usersService: UsersService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();

        component.nameInputElement.nativeElement.value = "Admin";
        component.surnameInputElement.nativeElement.value = "admin";
        component.emailInputElement.nativeElement.value = "admin1@gmail.com";
        component.passwordInputElement.nativeElement.value = "admin1admin";
        component.passwordOldInputElement.nativeElement.value = "admin1admin";
        component.passwordAgainInputElement.nativeElement.value = "admin1admin";

        component.onClickConfirmChanges(new Event(""));
        backend.expectOne({}).flush({});
        backend.expectOne({}).flush({});

        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.PROFILE.toPath());
      })
  ));
});
