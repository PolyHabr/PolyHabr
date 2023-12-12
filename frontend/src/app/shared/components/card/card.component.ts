import {Component, ElementRef, Input, ViewChild, ViewEncapsulation} from '@angular/core';
import {Article} from "../../../../data/models/article";
import {Destination, NavigationService} from "../../../core/services/navigation.service";
import {environment} from "../../../../environments/environment";
import {ArticlesService} from "../../../core/services/articles.service";
import {DataHelper} from "../../../core/helpers/data.helper";

@Component({
  selector: 'poly-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CardComponent {

  readonly DataHelper = DataHelper;

  @ViewChild("root")
  root!: ElementRef;

  @Input()
  article!: Article.Item;

  @Input()
  type: CardType = CardType.DEFAULT;

  @Input()
  isLast: boolean = false;

  @Input()
  isEditable: boolean = false;

  constructor(private navigationService: NavigationService, private articleService: ArticlesService) {
  }

  likeDislike(): void {
    this.articleService.like(this.article.id, () => {
      this.articleService.disLike(this.article.id).subscribe(() => this.article.likes--);
    }).subscribe(() => this.article.likes++);
  }

  addRemoveFav(): void {
    if (!this.article.isSaveToFavourite) {
      this.articleService.addFav(this.article.id).subscribe(() => this.article.isSaveToFavourite = true);
    } else {
      this.articleService.removeFav(this.article.id).subscribe(() => this.article.isSaveToFavourite = false);
    }
  }

  getPreview(): string {
    return this.article.previewText.split('\n').join('<br/>');
  }

  getText(): string {
    return this.article.text.split('\n').join('<br/>');
  }

  toArticle(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.ARTICLE, new Map([["article", this.article.id.toString()]]));
  }

  toFile(e: Event): void {
    e.preventDefault();
    window.open(this.getFileUrl());
  }

  toUpload(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.UPLOAD_EDIT, new Map([["id", this.article.id.toString()]]));
  }

  isFull(): boolean {
    return this.type == CardType.FULL;
  }

  isShort(): boolean {
    return this.type == CardType.SHORT;
  }

  isDefault(): boolean {
    return this.type == CardType.DEFAULT;
  }

  getFileUrl(): string {
    return `${environment.api_url}/files/${this.article.fileId}/download`;
  }

  getImageUrl(): string {
    return `${environment.api_url}/files/${this.article.previewImgId}/download`;
  }

  getPreviewUrl(): string {
    return `http://docs.google.com/viewer?url=${this.getFileUrl()}&embedded=true`;
  }
}

export enum CardType {
  DEFAULT, FULL, SHORT
}
