import {ComponentFixture, fakeAsync, inject, TestBed, tick} from "@angular/core/testing";
import {ArticleComponent} from "./article.component";
import {Article} from "../../data/models/article";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {SharedModule} from "../shared/shared.module";
import {ArticlesService} from "../core/services/articles.service";
import {ActivatedRoute, Params, RouterModule} from "@angular/router";
import {DataHelper} from "../core/helpers/data.helper";
import User = Article.User;
import {of} from "rxjs";

describe('Article Test', () => {
  let component: ArticleComponent;
  let fixture: ComponentFixture<ArticleComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SharedModule],
      declarations: [ArticleComponent],
      providers: [ArticlesService,
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({"article": 1}),
          },
        },
      ]
    });
    fixture = TestBed.createComponent(ArticleComponent);
    component = fixture.componentInstance;
  });

  beforeEach(fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        const article = Article.Item.createTemporary();
        fixture.detectChanges();
        backend.expectOne({}).flush(article);
        backend.expectOne({}).flush({contents:[]});
        backend.expectOne({}).flush({contents:[]});
        fixture.detectChanges();
        expect(component.article).toBe(article);
      })));

  it('init test', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('GetComments test', fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        const comment = new Article.Comment("test");
        component.getComments();
        backend.expectOne({}).flush({contents: [comment]});
        fixture.detectChanges();
        expect(component.comments.length).toBe(1);
        expect(component.comments[0]).toBe(comment);
      })));

  it('SendComment test', fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        const comment = new Article.Comment("test");
        let elem = document.createElement("textarea");
        elem.value = "test";
        component.sendComment(elem);
        backend.expectOne({}).flush({});
        fixture.detectChanges();
        backend.expectOne({}).flush({contents: [comment]});
        expect(component.comments.length).toBe(1);
        expect(component.comments[0]).toBe(comment);
      })));

  it('isOwnedArticle test', () => {
    DataHelper.user = new User("test");
    expect(component.isOwnedArticle(component.article.user.id)).toBeTrue();
  });

  it('isOwnedArticle false test', () => {
    DataHelper.user = undefined;
    expect(component.isOwnedArticle(component.article.user.id)).toBeFalse();
  });
});
