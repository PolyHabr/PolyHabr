import {ComponentFixture, inject, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {EmailConfirmComponent} from "./email-confirm.component";
import {Router} from "@angular/router";
import {Destination} from "../core/services/navigation.service";

describe("Email Confirm tests",() => {
  let component: EmailConfirmComponent;
  let fixture: ComponentFixture<EmailConfirmComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EmailConfirmComponent]
    }).compileComponents();
  })
  beforeEach(() => {
    fixture = TestBed.createComponent(EmailConfirmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Init email confirm component test', () => {
    expect(component).toBeTruthy();
  });

  it('To feed page navigation test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    component.toFeed(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.FEED.toPath());
  }));
});
