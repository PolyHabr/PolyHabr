import {Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {Article} from "../../data/models/article";
import {ProfileSortState} from "../../data/models/profile-sort-state";
import {ActivatedRoute} from "@angular/router";
import {CardComponent} from "../shared/components/card/card.component";
import User = Article.User;
import {UsersService} from "../core/services/users.service";
import {ArticlesService} from "../core/services/articles.service";
import {NavigationService} from "../core/services/navigation.service";

@Component({
  selector: 'poly-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  readonly ProfileSortState = ProfileSortState;
  user!: User;
  activeTab: ProfileSortState = ProfileSortState.PUBLISHED;
  articles: Article.Item[] = [];

  @ViewChild("container")
  container!: ElementRef;

  @ViewChild('cardComponent') set cardComponent(cardComponent: CardComponent) {
    if (cardComponent && cardComponent.root && this.scrollDelta < 0) {
      this.scrollDelta = cardComponent.root.nativeElement.clientHeight;
    }
  }

  private isItemsLoading: boolean = false;
  private queryCount = 0;
  private count = 5;
  private offset = -1;
  private scrollDelta = -1;
  private lastVerticalOffset = -1;

  constructor(private route: ActivatedRoute, private usersService: UsersService,
              private articlesService: ArticlesService, private navigationService:NavigationService) {
  }

  ngOnInit(): void {
    this.usersService.getMe().subscribe(result => {
      this.user = result;
      this.getArticles();
    });
    this.route.url.subscribe(_ => {
      if (this.route.snapshot.fragment == ProfileSortState.PUBLISHED) {
        this.offset = -1;
        this.count = 5;
        this.activeTab = ProfileSortState.PUBLISHED;
        this.articles = [];
      } else if (this.route.snapshot.fragment == ProfileSortState.FAVOURITES) {
        this.offset = -1;
        this.count = 5;
        this.activeTab = ProfileSortState.FAVOURITES;
        this.articles = [];
      }
    });
  }

  onTabClick(state: ProfileSortState): void {
    this.activeTab = state;
    switch (state) {
      case ProfileSortState.PUBLISHED:
        this.navigationService.navigateByUrl("profile#published");
        this.articles = [];
        this.getArticles();
        break;
      case ProfileSortState.FAVOURITES:
        this.navigationService.navigateByUrl("profile#favourites");
        this.articles = [];
        this.getArticles();
        break;
    }
  }

  isTabActive(state: ProfileSortState): boolean {
    return this.activeTab == state;
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(): void {
    if (this.user) {
      const verticalOffset = document.documentElement.scrollTop
        || document.body.scrollTop || 0;
      if (window.scrollY + this.container.nativeElement.offsetTop > this.container.nativeElement.clientHeight - this.scrollDelta * 3
        && this.lastVerticalOffset < verticalOffset) {

        this.getArticles(true);
      }
      this.lastVerticalOffset = verticalOffset;
    }
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
      if (this.isTabActive(ProfileSortState.PUBLISHED)) {
        this.articlesService.getUserArticles(this.user.id, this.offset, this.count).subscribe(result => {
          if (this.queryCount == tmpQuery) {
            if (isScroll) {
              this.articles.push(...result.contents);
            } else {
              this.articles = result.contents;
            }
          }
          if (result.contents.length > 0) {
            this.isItemsLoading = false;
          }
        });
      } else {
        this.articlesService.getFavArticles(this.offset, this.count).subscribe(result => {
          if (this.queryCount == tmpQuery) {
            if (isScroll) {
              this.articles.push(...result.contents);
            } else {
              this.articles = result.contents;
            }
          }
          if (result.contents.length > 0) {
            this.isItemsLoading = false;
          }
        });
      }
    }
  }
}
