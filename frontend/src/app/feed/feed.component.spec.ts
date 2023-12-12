import {ComponentFixture, fakeAsync, inject, TestBed} from "@angular/core/testing";
import {FeedComponent} from "./feed.component";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {SortBarComponent} from "../shared/components/sort-bar/sort-bar.component";
import {HeaderComponent} from "../shared/layout/header/header.component";
import {SharedModule} from "../shared/shared.module";
import {ArticlesService} from "../core/services/articles.service";
import {Article} from "../../data/models/article";

describe('Feed Test', () => {
  let component: FeedComponent;
  let fixture: ComponentFixture<FeedComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SharedModule],
      declarations: [FeedComponent, HeaderComponent],
      providers: [ArticlesService]
    });
    fixture = TestBed.createComponent(FeedComponent);
    component = fixture.componentInstance; // SortBarComponent test instance
  });
  beforeEach(fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        fixture.detectChanges();
        backend.expectOne({}).flush({contents: [Article.Item.createTemporary()]});
        fixture.detectChanges();
        expect(component.articles.length).toBe(1);
      })));
  it('Init test', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('Select view sort test', fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        component.selectSort({type: SortBarComponent.VIEW_SORT, data: "1w"});
        backend.expectOne({}).flush({contents: []});
        fixture.detectChanges();
        expect(component.selectedSort).toBe(SortBarComponent.VIEW_SORT);
        expect(component.selectedOption).toBe("1w");
        expect(component.articles.length).toBe(0);
      })));

  it('Select rating sort test', fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        component.selectSort({type: SortBarComponent.RATING_SORT, data: "1w"});
        backend.expectOne({}).flush({contents: []});
        fixture.detectChanges();
        expect(component.selectedSort).toBe(SortBarComponent.RATING_SORT);
        expect(component.selectedOption).toBe("1w");
        expect(component.articles.length).toBe(0);
      })));

  it('Scroll test', fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        const lengthBefore = component.articles.length;
        component.onScroll();
        backend.expectOne({}).flush({contents: [Article.Item.createTemporary()]});
        fixture.detectChanges();
        expect(component.articles.length).toBe(lengthBefore + 1);
      })));
});
