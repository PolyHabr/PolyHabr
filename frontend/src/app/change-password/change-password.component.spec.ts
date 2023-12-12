import {ComponentFixture, fakeAsync, inject, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ChangePasswordComponent} from "./change-password.component";
import {Router} from "@angular/router";
import {Destination} from "../core/services/navigation.service";
import {ErrorCodes} from "../../data/models/error-codes";
import {AuthorizationService} from "../core/services/authorization.service";
import {StorageHelper} from "../core/helpers/storage.helper";

describe("Change password component tests",() => {
  let component: ChangePasswordComponent;
  let fixture: ComponentFixture<ChangePasswordComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ChangePasswordComponent]
    }).compileComponents();
  });
  beforeEach(() => {
    fixture = TestBed.createComponent(ChangePasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Change password component test', () => {
    expect(component).toBeTruthy();
  });

  it('To feed page navigation test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    component.toFeed(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.FEED.toPath());
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

  it("Password equals 0", () => {
    component.passwordInputElement.nativeElement.value = "";
    component.passwordAgainInputElement.nativeElement.value = "";
    component.checkEnterValidation();
    expect(component.registerErrorConfig.passwordError).toBe(ErrorCodes.NON_FILLED_FIELD);
    expect(component.registerErrorConfig.passwordAgainError).toBe(ErrorCodes.NON_FILLED_FIELD);
  });

  it("Confirm password", fakeAsync(
    inject([AuthorizationService, HttpTestingController, Router],
      (authorizationService: AuthorizationService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();
        StorageHelper.setCookie("password-token", "a");

        component.passwordInputElement.nativeElement.value = "admin1admin";
        component.passwordAgainInputElement.nativeElement.value = "admin1admin";

        component.confirmPassword(new Event(""));
        backend.expectOne({}).flush({});

        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.LOGIN.toPath());
      })
  ));
});
