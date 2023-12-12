import {Component, ElementRef, HostListener, ViewChild} from '@angular/core';
import {SortBarState} from "../../data/models/sort-bar-state";
import {Article} from "../../data/models/article";
import {SortBarComponent} from "../shared/components/sort-bar/sort-bar.component";
import {ArticlesService} from "../core/services/articles.service";
import {CardComponent} from "../shared/components/card/card.component";

@Component({
  selector: 'poly-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent {

  @ViewChild("sortBar")
  sortBarComponent!: SortBarComponent;

  readonly SortBarState = SortBarState;

  @ViewChild("container")
  container!: ElementRef;

  @ViewChild('cardComponent') set cardComponent(cardComponent: CardComponent) {
    if (cardComponent && cardComponent.root && this.scrollDelta < 0) {
      this.scrollDelta = cardComponent.root.nativeElement.clientHeight;
    }
  }

  articles: Article.Item[] = [];

  private isItemsLoading: boolean = false;
  private queryCount = 0;
  private count = 5;
  private offset = -1;
  private scrollDelta = -1;
  private lastVerticalOffset = -1;

  private _filter: string = "";

  constructor(private articlesService: ArticlesService) {
    this.getArticles();
  }

  onSearch(text: string = this._filter, tmpQuery: number = this.queryCount, isScroll: boolean = false): void {
    this._filter = text;
    if (text.trim().length < 3) {
      this.articles = [];
      this.offset = 0;
      this.isItemsLoading = false;
    } else {
      this.articlesService.search(text, this.offset, this.count).subscribe(result => {
        if (this.queryCount == tmpQuery) {
          if (isScroll) {
            this.articles.push(...result.contents);
          } else {
            this.articles = result.contents;
          }
          this.sortBarComponent.state = this.articles.length > 0 ? SortBarState.SEARCH_SORT : SortBarState.SEARCH;
        }
        if (result.contents.length > 0) {
          this.isItemsLoading = false;
        }
      });
    }
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

  getArticles(isScroll: boolean = false): void {
    if ((!this.isItemsLoading && isScroll) || !isScroll) {
      let tmpQuery = ++this.queryCount;
      this.isItemsLoading = true;
      if (isScroll) {
        this.offset++;
      } else {
        this.offset = 0;
      }
      this.onSearch(this._filter, tmpQuery, isScroll);
    }
  }


  set filter(value: string) {
    this._filter = value;
  }
}
