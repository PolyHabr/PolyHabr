import {ComponentFixture, fakeAsync, inject, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {HeaderComponent} from "../shared/layout/header/header.component";
import {SharedModule} from "../shared/shared.module";
import {ArticlesService} from "../core/services/articles.service";
import {Article} from "../../data/models/article";
import {SearchComponent} from "./search.component";

describe('Search Test', () => {
  let component: SearchComponent;
  let fixture: ComponentFixture<SearchComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SharedModule],
      declarations: [SearchComponent, HeaderComponent],
      providers: [ArticlesService]
    });
    fixture = TestBed.createComponent(SearchComponent);
    component = fixture.componentInstance; // SortBarComponent test instance
  });
  it('Init test', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('Scroll test', fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        fixture.detectChanges();
        component.filter = "test";
        const lengthBefore = component.articles.length;
        component.onScroll();
        backend.expectOne({}).flush({contents: [Article.Item.createTemporary()]});
        fixture.detectChanges();
        expect(component.articles.length).toBe(lengthBefore + 1);
      })));

  it('Search test', fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        fixture.detectChanges();
        component.filter = "test";
        const lengthBefore = component.articles.length;
        component.onSearch();
        backend.expectOne({}).flush({contents: [Article.Item.createTemporary()]});
        fixture.detectChanges();
        expect(component.articles.length).toBe(lengthBefore + 1);
      })));

  it('Search empty result test', fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        fixture.detectChanges();
        component.filter = "test";
        component.onSearch();
        backend.expectOne({}).flush({contents: []});
        fixture.detectChanges();
        expect(component.articles.length).toBe(0);
      })));
});
