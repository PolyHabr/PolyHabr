import {ComponentFixture, inject, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {VerifyComponent} from "./verify.component";
import {Router} from "@angular/router";
import {Destination} from "../core/services/navigation.service";

describe("Verify component tests",() => {
  let component: VerifyComponent;
  let fixture: ComponentFixture<VerifyComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [VerifyComponent]
    }).compileComponents();
  });
  beforeEach(() => {
    fixture = TestBed.createComponent(VerifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Verify component test', () => {
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
});
