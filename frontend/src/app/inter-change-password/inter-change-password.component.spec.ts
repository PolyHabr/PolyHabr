import {ComponentFixture, fakeAsync, inject, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {InterChangePasswordComponent} from "./inter-change-password.component";
import {DisciplineTypesService} from "../core/services/discipline_types.service";
import {AuthorizationService} from "../core/services/authorization.service";
import {Router} from "@angular/router";
import {Destination} from "../core/services/navigation.service";

describe("Inter change password tests", () => {
  let component: InterChangePasswordComponent;
  let fixture: ComponentFixture<InterChangePasswordComponent>;
  beforeEach(async () => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [InterChangePasswordComponent]
    });
    fixture = TestBed.createComponent(InterChangePasswordComponent);
    component = fixture.componentInstance; // SortBarComponent test instance
  });
  beforeEach(fakeAsync(
    inject([DisciplineTypesService, HttpTestingController, Router],
      (authorizationService: AuthorizationService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();
        fixture.detectChanges();
        backend.expectOne({}).flush({});
        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.CHANGE_PASSWORD.toPath());
      })));

  it('Inter change password component test', () => {
    expect(component).toBeTruthy();
  });
});
