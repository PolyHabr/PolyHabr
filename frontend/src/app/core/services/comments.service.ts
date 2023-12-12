import {Injectable} from '@angular/core';
import {ApiService} from "./api.service";
import {Observable} from "rxjs";
import {HttpParams} from "@angular/common/http";
import {Article} from "../../../data/models/article";
import {ApiResult} from "../types/api-result";

@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  private readonly base: string = "/comment/";

  constructor(private apiService: ApiService) {
  }

  getComments(articleId: number, offset: number = 0, size: number = 100): Observable<ApiResult<Array<Article.Comment>>> {
    return this.apiService.get(`${this.base}byArticleId?articleId=${articleId}&offset=${offset}&size=${size}`, new HttpParams());
  }

  sendComment(text: string, articleId: number): Observable<void> {
    return this.apiService.post(`${this.base}create`, {articleId: articleId, text: text});
  }
}
