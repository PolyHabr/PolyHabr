import {ComponentFixture, fakeAsync, inject, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ForgotPasswordComponent} from "./forgot-password.component";
import {RegexType} from "../../data/models/regex-types";
import {InputFieldsType} from "../../data/models/input-field-types";
import {ErrorCodes} from "../../data/models/error-codes";
import {Router} from "@angular/router";
import {Destination} from "../core/services/navigation.service";
import {AuthorizationService} from "../core/services/authorization.service";

describe("Forgot password tests",() => {
  let component: ForgotPasswordComponent;
  let fixture: ComponentFixture<ForgotPasswordComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ForgotPasswordComponent]
    }).compileComponents();
  });
  beforeEach(() => {
    fixture = TestBed.createComponent(ForgotPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Forgot password component test', () => {
    expect(component).toBeTruthy();
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

  it('To Registration page navigation test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    component.toRegistration(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.REGISTER.toPath());
  }));

  it("Send mail to email", fakeAsync(
    inject([AuthorizationService, HttpTestingController],
      (authorizationService: AuthorizationService, backend: HttpTestingController) => {
        component.emailInputElement.nativeElement.value = "admin@gmail.com";
        component.sendEmail(new Event(""));
        backend.expectOne({}).flush({}, {});
      })
  ));
});
