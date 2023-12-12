import {ComponentFixture, fakeAsync, flush, inject, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {HeaderComponent} from "../shared/layout/header/header.component";
import {SharedModule} from "../shared/shared.module";
import {ArticlesService} from "../core/services/articles.service";
import {Article} from "../../data/models/article";
import {ProfileComponent} from "./profile.component";
import {ActivatedRoute, ActivatedRouteSnapshot, Router} from "@angular/router";
import {of} from "rxjs";
import {UsersService} from "../core/services/users.service";
import {ProfileSortState} from "../../data/models/profile-sort-state";
import User = Article.User;

describe('Profile Test', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;

  beforeEach(() => {
    let snapshot = new ActivatedRouteSnapshot();
    snapshot.fragment = ProfileSortState.PUBLISHED;
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SharedModule],
      declarations: [ProfileComponent, HeaderComponent],
      providers: [ArticlesService, UsersService,
        {
          provide: ActivatedRoute,
          useValue: {
            url: of("test"),
            snapshot: snapshot
          },
        },]
    });
    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance; // SortBarComponent test instance
  });
  beforeEach(fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        fixture.detectChanges();
        backend.expectOne({}).flush(new User("test"));
        backend.expectOne({}).flush({contents: [Article.Item.createTemporary()]});
        flush();
        fixture.detectChanges();
        expect(component.articles.length).toBe(1);
      })));

  it('Init test', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('Scroll test', fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        const lengthBefore = component.articles.length;
        component.onScroll();
        backend.expectOne({}).flush({contents: [Article.Item.createTemporary()]});
        fixture.detectChanges();
        expect(component.articles.length).toBe(lengthBefore + 1);
      })));

  it('onPublishedTabClick test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    fixture.detectChanges();
    component.onTabClick(ProfileSortState.PUBLISHED);
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(`profile#published`);
  }));

  it('onFavouriteTabClick test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    fixture.detectChanges();
    component.onTabClick(ProfileSortState.FAVOURITES);
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(`profile#favourites`);
  }));
});

describe('Profile Test', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;

  beforeEach(() => {
    let snapshot = new ActivatedRouteSnapshot();
    snapshot.fragment = ProfileSortState.FAVOURITES;
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SharedModule],
      declarations: [ProfileComponent, HeaderComponent],
      providers: [ArticlesService, UsersService,
        {
          provide: ActivatedRoute,
          useValue: {
            url: of("test"),
            snapshot: snapshot
          },
        },]
    });
    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance; // SortBarComponent test instance
  });
  beforeEach(fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        fixture.detectChanges();
        backend.expectOne({}).flush({});
        backend.expectOne({}).flush({contents: [Article.Item.createTemporary()]});
        flush();
        fixture.detectChanges();
        expect(component.articles.length).toBe(1);
      })));

  it('Init favourite test', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('Scroll favourite test', fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        const lengthBefore = component.articles.length;
        component.onScroll();
        backend.expectOne({}).flush({contents: [Article.Item.createTemporary()]});
        fixture.detectChanges();
        expect(component.articles.length).toBe(lengthBefore + 1);
      })));
});
