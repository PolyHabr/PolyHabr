import {ComponentFixture, TestBed} from "@angular/core/testing";
import {SortBarComponent} from "./sort-bar.component";

describe('Sort bar Test', () => {
  let component: SortBarComponent;
  let fixture: ComponentFixture<SortBarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ SortBarComponent ]
    });
    fixture = TestBed.createComponent(SortBarComponent);
    component = fixture.componentInstance; // SortBarComponent test instance
  });
  it('Init test', () => {
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('h1').innerText).toBe("Все публикации");
  });
  it('Test view Sort', () => {
    component.selectSort(SortBarComponent.VIEW_SORT);
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelectorAll('.item.link')[1].classList.contains("selected")).toBeTrue();
  });
  it('Test select Sort', () => {
    component.selectSort(SortBarComponent.VIEW_SORT);
    fixture.detectChanges();
    component.confirmSelect();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.options').classList.contains("active")).toBeFalse();
  });
  it('Test open Sort', () => {
    component.selectSort(SortBarComponent.VIEW_SORT);
    fixture.detectChanges();
    component.confirmSelect();
    fixture.detectChanges();
    component.selectSort(SortBarComponent.VIEW_SORT);
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.options').classList.contains("active")).toBeTrue();
  });

  it('Test date Sort', () => {
    component.selectSort(SortBarComponent.DATE_SORT);
    fixture.detectChanges();
    component.confirmSelect();
    fixture.detectChanges();
    expect(component.getSelectedSort()).toBe(SortBarComponent.DATE_SORT)
  });
});
