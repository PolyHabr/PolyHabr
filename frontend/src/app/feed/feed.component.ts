import {Component, ElementRef, HostListener, ViewChild} from '@angular/core';
import {Article} from "../../data/models/article";
import {SortBarState} from "../../data/models/sort-bar-state";
import {ArticlesService} from "../core/services/articles.service";
import {CardComponent} from "../shared/components/card/card.component";
import {Sort} from "../../data/models/sort-type";
import {SortBarComponent} from "../shared/components/sort-bar/sort-bar.component";

@Component({
  selector: 'poly-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent {

  readonly SortBarState = SortBarState;

  @ViewChild("container")
  container!: ElementRef;

  @ViewChild('cardComponent') set cardComponent(cardComponent: CardComponent) {
    if (cardComponent && cardComponent.root && this.scrollDelta < 0) {
      this.scrollDelta = cardComponent.root.nativeElement.clientHeight;
    }
  }

  articles: Article.Item[] = [];

  private _isItemsLoading: boolean = false;
  private queryCount = 0;
  private count = 5;
  private offset = -1;
  private scrollDelta = -1;
  private lastVerticalOffset = -1;

  private _selectedSort: Sort.Type = SortBarComponent.DATE_SORT;
  private _selectedOption: String | null = null;

  constructor(private articlesService: ArticlesService) {
    this.getArticles();
  }

  get selectedSort(): Sort.Type {
    return this._selectedSort;
  }

  get selectedOption(): String | null {
    return this._selectedOption;
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(): void {
    const verticalOffset = document.documentElement.scrollTop
      || document.body.scrollTop || 0;
    if (window.scrollY + this.container.nativeElement.offsetTop > this.container.nativeElement.clientHeight - this.scrollDelta * 3
      && this.lastVerticalOffset < verticalOffset) {
      this.getArticles(true);
    }
    this.lastVerticalOffset = verticalOffset;
  }

  selectSort(data: { type: Sort.Type, data: String | null }): void {
    this._selectedSort = data.type;
    this._selectedOption = data.data;
    this.getArticles(false);
  }

  getArticles(isScroll: boolean = false): void {
    if ((!this._isItemsLoading && isScroll) || !isScroll) {
      let tmpQuery = ++this.queryCount;
      this._isItemsLoading = true;
      if (isScroll) {
        this.offset++;
      } else {
        this.offset = 0;
      }
      this.articlesService.getArticles(this.offset, this.count,
        this._selectedSort == SortBarComponent.VIEW_SORT,
        this._selectedSort == SortBarComponent.RATING_SORT,
        this._selectedOption)
        .subscribe(result => {
          if (this.queryCount == tmpQuery) {
            if (isScroll) {
              this.articles.push(...result.contents);
            } else {
              this.articles = result.contents;
            }
          }
          if (result.contents.length > 0) {
            this._isItemsLoading = false;
          }
        });
    }
  }
}
