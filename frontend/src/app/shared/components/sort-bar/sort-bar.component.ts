import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Sort} from "../../../../data/models/sort-type";
import {SortBarState} from "../../../../data/models/sort-bar-state";

@Component({
  selector: 'poly-sort-bar',
  templateUrl: './sort-bar.component.html',
  styleUrls: ['./sort-bar.component.scss']
})
export class SortBarComponent {

  readonly SortBarState = SortBarState;

  @Input()
  state: SortBarState = SortBarState.SORT;

  @Output()
  onSearch: EventEmitter<string> = new EventEmitter<string>();

  @Output()
  onOptionSelected: EventEmitter<{ type: Sort.Type, data: String | null }> = new EventEmitter<{ type: Sort.Type, data: String | null }>();

  static readonly DATE_SORT = new Sort.Type("По дате выпуска");
  static readonly VIEW_SORT = new Sort.Type("По просмотрам", true);
  static readonly RATING_SORT = new Sort.Type("По рейтингу", true);

  readonly sortTypes: Sort.Type[] = [
    SortBarComponent.DATE_SORT,
    SortBarComponent.VIEW_SORT,
    SortBarComponent.RATING_SORT
  ];

  readonly sortOptions: Sort.Option[] = [
    new Sort.Option("За неделю", "1w"),
    new Sort.Option("За месяц", "1m"),
    new Sort.Option("За год", "1y"),
    new Sort.Option("За всё время", null),
  ];

  private selectedSort: Sort.Type = this.sortTypes[0];
  private selectedOption: Sort.Option | undefined = undefined;

  constructor() {
  }

  selectSort(sortType: Sort.Type): void {
    if (this.selectedSort != sortType) {
      this.selectedSort.clearOption();
      this.selectedOption = undefined;
      this.selectedSort = sortType;
      if (sortType.hasOptions) {
        this.selectOption(this.sortOptions[0]);
        this.selectedSort.selectOption(this.sortOptions[0]);
      }
    } else {
      if (sortType.hasOptions) {
        this.selectedOption = this.selectedSort.selectedOption;
      }
    }
    if (this.selectedSort == SortBarComponent.DATE_SORT) {
      this.confirmSelect();
    }
  }

  isSelected(sortType: Sort.Type): boolean {
    return this.selectedSort == sortType;
  }

  getSelectedOption(): Sort.Option | undefined {
    return this.selectedOption;
  }

  getSelectedSort(): Sort.Type {
    return this.selectedSort
  }

  isOptionSelected(option: Sort.Option): boolean {
    return this.selectedOption == option;
  }

  selectOption(option: Sort.Option): void {
    this.selectedOption = option;
  }

  confirmSelect(): void {
    this.selectedSort.selectOption(this.selectedOption);
    this.onOptionSelected.emit({
      type: this.selectedSort,
      data: this.selectedOption?.data ? this.selectedOption?.data : ""
    });
    this.selectedOption = undefined;
  }
}

