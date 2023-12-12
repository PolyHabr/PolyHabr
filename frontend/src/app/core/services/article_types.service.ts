import {Injectable} from '@angular/core';
import {ApiService} from "./api.service";
import {Observable} from "rxjs";
import {HttpParams} from "@angular/common/http";
import {Article} from "../../../data/models/article";
import {ApiResult} from "../types/api-result";

@Injectable({
  providedIn: 'root'
})
export class ArticleTypesService {

  private readonly base: string = "/article_types/";

  constructor(private apiService: ApiService) {
  }

  getTypes(): Observable<ApiResult<Array<Article.Type>>> {
    return this.apiService.get(`${this.base}?offset=0&size=100`, new HttpParams());
  }
}
