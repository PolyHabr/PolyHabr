import {Injectable} from "@angular/core";
import {Article} from "../../../data/models/article";
import User = Article.User;

@Injectable({
  providedIn: 'root'
})
export class DataHelper {
  static user: User | undefined = undefined;
}
