import {ComponentFixture, fakeAsync, inject, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Article} from "../../../../data/models/article";
import {environment} from "../../../../environments/environment";
import {Router} from "@angular/router";
import {ArticlesService} from "../../../core/services/articles.service";
import {HeaderComponent} from "./header.component";
import {Destination} from "../../../core/services/navigation.service";

describe('Header Test', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderComponent]
    });
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
  });
  it('Init test', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('Open feed test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    fixture.detectChanges();
    component.toFeed(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.FEED.toPath());
  }));

  it('Open profile test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    fixture.detectChanges();
    component.toProfile(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.PROFILE.toPath());
  }));

  it('Open search test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    fixture.detectChanges();
    component.toSearch(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.SEARCH.toPath());
  }));

  it('Open upload test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    fixture.detectChanges();
    component.toUpload(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.UPLOAD.toPath());
  }));
});
