import {ComponentFixture, fakeAsync, inject, TestBed} from "@angular/core/testing";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Router} from "@angular/router";
import {TypeComponent} from "./type.component";
import {DisciplineTypesService} from "../core/services/discipline_types.service";
import {Destination} from "../core/services/navigation.service";
import {Article} from "../../data/models/article";

describe('Type Test', () => {
  let component: TypeComponent;
  let fixture: ComponentFixture<TypeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TypeComponent],
      providers: [DisciplineTypesService]
    });
    fixture = TestBed.createComponent(TypeComponent);
    component = fixture.componentInstance; // SortBarComponent test instance
  });
  beforeEach(fakeAsync(
    inject([DisciplineTypesService, HttpTestingController],
      (articlesService: DisciplineTypesService, backend: HttpTestingController) => {
        fixture.detectChanges();
        backend.expectOne({}).flush({contents: [new Article.Type("test")]});
        fixture.detectChanges();
        expect(component.types.length).toBe(1);
      })));
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

  it('SelectDiscipline test', fakeAsync(
    inject([DisciplineTypesService, HttpTestingController, Router],
      (articlesService: DisciplineTypesService, backend: HttpTestingController, router: Router) => {
        component.selectedType = new Article.Type("test");
        spyOn(router, 'navigateByUrl').and.stub();
        fixture.detectChanges();
        component.chooseDisciplines(new Event(""));
        backend.expectOne({}).flush({});
        fixture.detectChanges();
        expect(router.navigateByUrl).toHaveBeenCalled();
        expect(router.navigateByUrl).toHaveBeenCalledWith(Destination.FEED.toPath());
      })));

  it('SelectDiscipline error test', fakeAsync(
    inject([DisciplineTypesService, HttpTestingController, Router],
      (articlesService: DisciplineTypesService, backend: HttpTestingController, router: Router) => {
        spyOn(router, 'navigateByUrl').and.stub();
        fixture.detectChanges();
        component.chooseDisciplines(new Event(""));
        expect(component.errorText).toBe(TypeComponent.ErrorText);
      })));
});
