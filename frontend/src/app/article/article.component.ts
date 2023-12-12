import {Component, OnInit} from '@angular/core';
import {Article} from "../../data/models/article";
import {CardType} from "../shared/components/card/card.component";
import {ArticlesService} from "../core/services/articles.service";
import {ActivatedRoute} from "@angular/router";
import {CommentsService} from "../core/services/comments.service";
import {DataHelper} from "../core/helpers/data.helper";

@Component({
  selector: 'poly-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss']
})
export class ArticleComponent implements OnInit {

  readonly CardType = CardType;
  readonly DataHelper = DataHelper;

  article!: Article.Item;
  comments: Article.Comment[] = [];

  others: Article.Item[] = [];

  isArticleEditable: boolean = false;

  constructor(private articlesService: ArticlesService, private route: ActivatedRoute,
              private commentsService: CommentsService) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(param => {
      if (param["article"]) {
        this.articlesService.getArticle(Number(param["article"])).subscribe(articleResult => {
          this.article = articleResult;
          this.articlesService.search(this.article.listDisciplineName[0]).subscribe(result => {
            this.others = result.contents;
            this.getComments();
            this.isArticleEditable = this.isOwnedArticle(articleResult.user.id);
          });
        });
      }
    });
  }

  getComments(): void {
    this.commentsService.getComments(this.article.id).subscribe(result => {
      this.comments = result.contents;
    });
  }

  sendComment(input: HTMLTextAreaElement) {
    this.commentsService.sendComment(input.value, this.article.id).subscribe(() => {
      this.getComments();
      input.value = "";
    })
  }

  isOwnedArticle(userTaskId: number) {
    return DataHelper.user?.id != undefined && DataHelper.user.id == userTaskId;
  }
}
