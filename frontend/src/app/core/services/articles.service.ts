import {Injectable} from '@angular/core';
import {ApiService} from "./api.service";
import {Observable} from "rxjs";
import {HttpParams} from "@angular/common/http";
import {ApiError} from "../types/api-error";
import {Article} from "../../../data/models/article";
import {ApiResult} from "../types/api-result";
import {Data} from "../types/Data";

@Injectable({
  providedIn: 'root'
})
export class ArticlesService {

  private readonly base: string = "/articles/";

  constructor(private apiService: ApiService) {
  }

  getArticles(offset: number,
              size: number,
              fieldView: boolean | null,
              fieldRating: boolean | null,
              datRange: String | null): Observable<ApiResult<Array<Article.Item>>> {
    let result = `${this.base}?offset=${offset}&size=${size}`;
    let body: Data = {}
    if (fieldView) {
      body['fieldView'] = false;
      body['datRange'] = datRange;
    } else if (fieldRating) {
      body['fieldRating'] = false;
      body['datRange'] = datRange;
    }
    return this.apiService.post(result, body);
  }

  getUserArticles(userId: number, offset: number, size: number): Observable<ApiResult<Array<Article.Item>>> {
    return this.apiService.get(`${this.base}byUser?id=${userId}&offset=${offset}&size=${size}`, new HttpParams());
  }

  getFavArticles(offset: number, size: number): Observable<ApiResult<Array<Article.Item>>> {
    return this.apiService.get(`${this.base}getFavArticles?offset=${offset}&size=${size}`, new HttpParams());
  }

  getArticle(id: number): Observable<Article.Item> {
    return this.apiService.get(`${this.base}byId?id=${id}`, new HttpParams());
  }

  like(articleId: number, onError?: ApiError): Observable<void> {
    return this.apiService.post(`${this.base}add_like?articleId=${articleId}`, new HttpParams(), onError);
  }

  disLike(articleId: number, onError?: ApiError): Observable<void> {
    return this.apiService.post(`${this.base}decrease_like?articleId=${articleId}`, new HttpParams(), onError);
  }

  addFav(articleId: number, onError?: ApiError): Observable<void> {
    return this.apiService.post(`${this.base}addFavArticles?articleId=${articleId}`, new HttpParams(), onError);
  }

  removeFav(articleId: number, onError?: ApiError): Observable<void> {
    return this.apiService.post(`${this.base}removeFromArticles?articleId=${articleId}`, new HttpParams(), onError);
  }

  search(prefix: string, offset: number = 0, size: number = 1): Observable<ApiResult<Array<Article.Item>>> {
    return this.apiService.get(`${this.base}searchByTittle?prefix=${prefix}&offset=${offset}&size=${size}`, new HttpParams());
  }

  add(body: Data): Observable<Data> {
    return this.apiService.post(`${this.base}create`, body);
  }

  update(body: Data, id: number): Observable<Data> {
    return this.apiService.put(`${this.base}update?id=${id}`, body);
  }
}
