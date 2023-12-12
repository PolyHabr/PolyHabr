import {ComponentFixture, fakeAsync, inject, TestBed} from "@angular/core/testing";
import {CardComponent} from "./card.component";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Article} from "../../../../data/models/article";
import {environment} from "../../../../environments/environment";
import {Router} from "@angular/router";
import {ArticlesService} from "../../../core/services/articles.service";

describe('Card Test', () => {
  let component: CardComponent;
  let fixture: ComponentFixture<CardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CardComponent],
      providers: [ArticlesService]
    });
    fixture = TestBed.createComponent(CardComponent);
    component = fixture.componentInstance; // SortBarComponent test instance
    component.article = Article.Item.createTemporary();
  });
  it('Init test', () => {
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('h2').innerText).toBe(component.article.title);
  });
  it('Get file url test', () => {
    fixture.detectChanges();
    expect(component.getFileUrl()).toBe(`${environment.api_url}/files/${component.article.fileId}/download`);
  });
  it('Get image url test', () => {
    fixture.detectChanges();
    expect(component.getImageUrl()).toBe(`${environment.api_url}/files/${component.article.previewImgId}/download`);
  });
  it('Get preview url test', () => {
    fixture.detectChanges();
    expect(component.getPreviewUrl()).toBe(`http://docs.google.com/viewer?url=${component.getFileUrl()}&embedded=true`);
  });
  it('Get text test', () => {
    fixture.detectChanges();
    expect(component.getText()).toBe(component.article.text.split('\n').join('<br/>'));
  });
  it('Open file test', () => {
    fixture.detectChanges();
    spyOn(window, 'open');
    component.toFile(new Event(""));
    expect(window.open).toHaveBeenCalled();
    expect(window.open).toHaveBeenCalledWith(component.getFileUrl());
  });

  it('Open article test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    fixture.detectChanges();
    component.toArticle(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(`article/${component.article.id.toString()}`);
  }));

  it('Open upload test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    fixture.detectChanges();
    component.toUpload(new Event(""));
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith(`upload/${component.article.id.toString()}`);
  }));

  it('LikeDislike test', fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        const likesBefore = component.article.likes;
        component.likeDislike();
        backend.expectOne({}).flush({});
        fixture.detectChanges();
        expect(component.article.likes).toBe(likesBefore + 1);

        component.likeDislike();
        backend.expectOne({}).flush({}, {status: 400, statusText: 'Bad Request'});
        backend.expectOne({}).flush({});
        fixture.detectChanges();
        expect(component.article.likes).toBe(likesBefore);
      })));

  it('AddRemoveFav test', fakeAsync(
    inject([ArticlesService, HttpTestingController],
      (articlesService: ArticlesService, backend: HttpTestingController) => {
        const isSaveToFavourite = component.article.isSaveToFavourite;
        component.addRemoveFav();
        backend.expectOne({}).flush({});
        fixture.detectChanges();
        expect(component.article.isSaveToFavourite).not.toBe(isSaveToFavourite);

        component.addRemoveFav();
        backend.expectOne({}).flush({});
        fixture.detectChanges();
        expect(component.article.isSaveToFavourite).toBe(isSaveToFavourite);
      })));
});
