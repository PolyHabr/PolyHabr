import {ComponentFixture, fakeAsync, inject, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {ActivatedRoute, Router} from "@angular/router";
import {DisciplineTypesService} from "../core/services/discipline_types.service";
import {Article} from "../../data/models/article";
import {UploadComponent} from "./upload.component";
import {ArticleTypesService} from "../core/services/article_types.service";
import {ArticlesService} from "../core/services/articles.service";
import {ReactiveFormsModule} from "@angular/forms";
import {of} from "rxjs";
import {SharedModule} from "../shared/shared.module";

describe('Upload with file Test', () => {
  let component: UploadComponent;
  let fixture: ComponentFixture<UploadComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SharedModule, ReactiveFormsModule],
      declarations: [UploadComponent],
      providers: [ArticleTypesService, ArticlesService, DisciplineTypesService, ArticlesService,
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({"id": 1}),
          },
        },]
    });
    fixture = TestBed.createComponent(UploadComponent);
    component = fixture.componentInstance; // SortBarComponent test instance
  });
  beforeEach(fakeAsync(
    inject([DisciplineTypesService, HttpTestingController],
      (articlesService: DisciplineTypesService, backend: HttpTestingController) => {
        const type = new Article.Type("Test");
        fixture.detectChanges();
        backend.expectOne({}).flush({contents: [type]});
        backend.expectOne({}).flush({contents: [type]});
        backend.expectOne({}).flush(Article.Item.createTemporary());
        fixture.detectChanges();
        expect(component.selectedType).toBe(type);
        expect(component.selectedDiscipline).toBe(type);
      })));
  it('Init test', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('Open article test', inject([Router], (router: Router) => {
    spyOn(router, 'navigateByUrl').and.stub();
    fixture.detectChanges();
    component.toArticle("0");
    expect(router.navigateByUrl).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith("article/0");
  }));

  it('Select file test', () => {
    let file = new File([""], "");
    Object.defineProperty(
      file, 'size', {value: 14 * 1024 * 1024, writable: false});
    fixture.detectChanges();
    component.onFileSelected([file]);
    fixture.detectChanges();
    expect(component.file).toBe(file);
  });
  it('Select file error test', () => {
    let file = new File([""], "");
    Object.defineProperty(
      file, 'size', {value: 15 * 1024 * 1024, writable: false});
    fixture.detectChanges();
    component.onFileSelected([file]);
    fixture.detectChanges();
    expect(component.hasError).toBeTrue();
  });

  it('Select preview test', () => {
    let file = new File([""], "");
    Object.defineProperty(
      file, 'size', {value: 14 * 1024 * 1024, writable: false});
    fixture.detectChanges();
    component.onPreviewSelected([file]);
    fixture.detectChanges();
    expect(component.preview).toBe(file);
  });
  it('Select preview error test', () => {
    let file = new File([""], "");
    Object.defineProperty(
      file, 'size', {value: 15 * 1024 * 1024, writable: false});
    fixture.detectChanges();
    component.onPreviewSelected([file]);
    fixture.detectChanges();
    expect(component.hasPreviewError).toBeTrue();
  });

  it('Add test', fakeAsync(
    inject([DisciplineTypesService, HttpTestingController, Router],
      (articlesService: DisciplineTypesService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();
        fixture.detectChanges();
        component.add(new Event(""));
        fixture.detectChanges();
        backend.expectOne({}).flush({id: 0});
        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith("article/1");
      })));

  it('Add with preview test', fakeAsync(
    inject([DisciplineTypesService, HttpTestingController, Router],
      (articlesService: DisciplineTypesService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();
        fixture.detectChanges();
        component.preview = new File([""], "test");
        component.add(new Event(""));
        fixture.detectChanges();
        backend.expectOne({}).flush({id: 0});
        backend.expectOne({}).flush({id: 0});
        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith("article/1");
      })));

  it('Add with file test', fakeAsync(
    inject([DisciplineTypesService, HttpTestingController, Router],
      (articlesService: DisciplineTypesService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();
        fixture.detectChanges();
        component.file = new File([""], "test");
        component.add(new Event(""));
        fixture.detectChanges();
        backend.expectOne({}).flush({id: 0});
        backend.expectOne({}).flush({id: 0});
        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith("article/1");
      })));

  it('Add with file and preview test', fakeAsync(
    inject([DisciplineTypesService, HttpTestingController, Router],
      (articlesService: DisciplineTypesService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();
        fixture.detectChanges();
        component.file = new File([""], "test");
        component.preview = new File([""], "test");
        component.add(new Event(""));
        fixture.detectChanges();
        backend.expectOne({}).flush({id: 0});
        backend.expectOne({}).flush({id: 0});
        backend.expectOne({}).flush({id: 0});
        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith("article/1");
      })));
});

describe('Upload Test', () => {
  let component: UploadComponent;
  let fixture: ComponentFixture<UploadComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReactiveFormsModule],
      declarations: [UploadComponent],
      providers: [ArticleTypesService, ArticlesService, DisciplineTypesService, ArticlesService,
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({})
          },
        },]
    });
    fixture = TestBed.createComponent(UploadComponent);
    component = fixture.componentInstance; // SortBarComponent test instance
  });
  beforeEach(fakeAsync(
    inject([DisciplineTypesService, HttpTestingController],
      (articlesService: DisciplineTypesService, backend: HttpTestingController) => {
        const type = new Article.Type("test");
        fixture.detectChanges();
        backend.expectOne({}).flush({contents: [type]});
        backend.expectOne({}).flush({contents: [type]});
        fixture.detectChanges();
        expect(component.selectedType).toBe(type);
        expect(component.selectedDiscipline).toBe(type);
      })));
  it('Init test', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('Add test', fakeAsync(
    inject([DisciplineTypesService, HttpTestingController, Router],
      (articlesService: DisciplineTypesService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();
        fixture.detectChanges();
        component.add(new Event(""));
        fixture.detectChanges();
        backend.expectOne({}).flush({id: 0});
        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith("article/0");
      })));

  it('Add with preview test', fakeAsync(
    inject([DisciplineTypesService, HttpTestingController, Router],
      (articlesService: DisciplineTypesService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();
        fixture.detectChanges();
        component.preview = new File([""], "test");
        component.add(new Event(""));
        fixture.detectChanges();
        backend.expectOne({}).flush({id: 0});
        backend.expectOne({}).flush({id: 0});
        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith("article/0");
      })));

  it('Add with file test', fakeAsync(
    inject([DisciplineTypesService, HttpTestingController, Router],
      (articlesService: DisciplineTypesService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();
        fixture.detectChanges();
        component.file = new File([""], "test");
        component.add(new Event(""));
        fixture.detectChanges();
        backend.expectOne({}).flush({id: 0});
        backend.expectOne({}).flush({id: 0});
        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith("article/0");
      })));

  it('Add with file and preview test', fakeAsync(
    inject([DisciplineTypesService, HttpTestingController, Router],
      (articlesService: DisciplineTypesService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();
        fixture.detectChanges();
        component.file = new File([""], "test");
        component.preview = new File([""], "test");
        component.add(new Event(""));
        fixture.detectChanges();
        backend.expectOne({}).flush({id: 0});
        backend.expectOne({}).flush({id: 0});
        backend.expectOne({}).flush({id: 0});
        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith("article/0");
      })));
});
