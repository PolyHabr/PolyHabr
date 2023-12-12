import {ComponentFixture, fakeAsync, inject, TestBed, tick} from "@angular/core/testing";
import {Router} from "@angular/router";
import {Destination} from "../../../core/services/navigation.service";
import {MenuComponent, MenuState} from "./menu.component";
import {StorageHelper} from "../../../core/helpers/storage.helper";
import {DataHelper} from "../../../core/helpers/data.helper";
import {Article} from "../../../../data/models/article";
import User = Article.User;

describe('Menu Test', () => {
  let component: MenuComponent;
  let fixture: ComponentFixture<MenuComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MenuComponent]
    });
    fixture = TestBed.createComponent(MenuComponent);
    component = fixture.componentInstance;
  });
  it('Init test', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('Open feed test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    fixture.detectChanges();
    component.toFeed();
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.FEED.toPath());
  }));

  it('Open registration test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    fixture.detectChanges();
    component.toRegistration(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.REGISTER.toPath());
  }));

  it('Open login test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    fixture.detectChanges();
    component.toLogin(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.LOGIN.toPath());
  }));

  it('Open menu test', fakeAsync(() => {
    fixture.detectChanges();
    component.showMenu();
    expect(component.state).toBe(MenuState.SHOWING);
    tick(100);
    expect(component.state).toBe(MenuState.SHOWN);
  }));

  it('Hide menu test', fakeAsync(() => {
    fixture.detectChanges();
    component.showMenu();
    tick(100);
    expect(component.state).toBe(MenuState.SHOWN);
    component.hideMenu();
    fixture.detectChanges();
    expect(component.state).toBe(MenuState.HIDDEN);
  }));

  it('Logout test', fakeAsync(inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    DataHelper.user = new User("");
    StorageHelper.setCookie("accessToken", "test");
    expect(StorageHelper.getCookie("accessToken")).toBe("test");
    fixture.detectChanges();
    component.logout(new Event(""));
    fixture.detectChanges();
    expect(StorageHelper.getCookie("accessToken")).toBeUndefined();
    expect(DataHelper.user).toBeUndefined();
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.FEED.toPath());
  })));
});
