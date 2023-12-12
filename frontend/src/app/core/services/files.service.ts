import {Injectable} from '@angular/core';
import {ApiService} from "./api.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class FilesService {

  private readonly base: string = "/files/";

  constructor(private apiService: ApiService) {
  }

  sendFile(file: File, articleId: string): Observable<void> {
    const formData = new FormData();
    formData.append('file', file, "file.pdf");
    formData.append('articleId', articleId);

    return this.apiService.postForm(`${this.base}`, formData);
  }

  sendImage(file: File, articleId: string): Observable<void> {
    const formData = new FormData();
    formData.append('file', file, "file.pdf");
    formData.append('articleId', articleId);

    return this.apiService.postForm(`${this.base}savePreviewPic`, formData);
  }
}
