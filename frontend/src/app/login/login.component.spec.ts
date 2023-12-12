import {ComponentFixture, fakeAsync, inject, TestBed} from "@angular/core/testing";
import {LoginComponent} from "./login.component";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Router} from "@angular/router";
import {Destination} from "../core/services/navigation.service";
import {By} from "@angular/platform-browser";
import {RegexType} from "../../data/models/regex-types";
import {InputFieldsType} from "../../data/models/input-field-types";
import {ErrorCodes} from "../../data/models/error-codes";
import {AuthorizationService} from "../core/services/authorization.service";

describe("Login tests",() => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LoginComponent]
    }).compileComponents();
  })
  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  it('Init login component test', () => {
    expect(component).toBeTruthy();
  });

  it('To feed page navigation test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    component.toFeed(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.FEED.toPath());
  }));

  it('To registration page navigation test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    component.toRegistration(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.REGISTER.toPath());
  }));

  it('To forgot password page navigation test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    component.toForgotPassword(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.FORGOT_PASSWORD.toPath());
  }));

  it('Check fields validation test: regex fail', () => {
    const input = fixture.debugElement.query(By.css(".background .layout .content .nickname input"));
    input.nativeElement.value = "admin./1";
    component.checkFieldValidation(RegexType.LOGIN, input, InputFieldsType.NICKNAME);
    expect(component.registerErrorConfig.nicknameError).toBe(ErrorCodes.NICKNAME_LOGIN);
  });

  it('Check fields validation test: regex correct', () => {
    const input = fixture.debugElement.query(By.css(".background .layout .content .nickname input"));
    input.nativeElement.value = "admin";
    component.checkFieldValidation(RegexType.LOGIN, input.nativeElement, InputFieldsType.NICKNAME);
    expect(component.registerErrorConfig.nicknameError).toBe(null);
  });

  it('Login fail test', fakeAsync(
    inject([AuthorizationService, HttpTestingController],
      (authorizationService: AuthorizationService, backend: HttpTestingController) => {
        component.onEnterButtonClicked(new Event(""));
        const requestWrapper = backend.expectOne({});
        requestWrapper.flush({}, {status: 400, statusText: 'Bad Request'});
        fixture.detectChanges();
        expect(component.registerErrorConfig.passwordError).toBe(ErrorCodes.LOGIN_ERROR);
      })
  ));

  it('Login correct', fakeAsync(
    inject([AuthorizationService, HttpTestingController],
      (authorizationService: AuthorizationService, backend: HttpTestingController) => {
        component.onEnterButtonClicked(new Event(""));
        const requestWrapper = backend.expectOne({});
        requestWrapper.flush({});
        expect(component.registerErrorConfig.passwordError).toBe(null);
      })
  ));

  it('Login first time', fakeAsync(
    inject([AuthorizationService, HttpTestingController, Router],
      (authorizationService: AuthorizationService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();

        component.onEnterButtonClicked(new Event(""));
        backend.expectOne({}).flush({isFirst: true});
        backend.expectOne({}).flush({});

        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.TYPES.toPath());
      })
  ));

  it('Login not first time', fakeAsync(
    inject([AuthorizationService, HttpTestingController, Router],
      (authorizationService: AuthorizationService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();

        component.onEnterButtonClicked(new Event(""));
        backend.expectOne({}).flush({});
        backend.expectOne({}).flush({});

        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.FEED.toPath());
      })
  ));
});
