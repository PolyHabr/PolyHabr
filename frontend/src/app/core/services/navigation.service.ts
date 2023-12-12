import {Injectable} from '@angular/core';
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  constructor(private router: Router) {
  }

  navigateTo(destination: Destination, args: Map<string, string> = new Map()): void {
    let resultDestination = destination.toPath();
    args.forEach((value, key) => {
      resultDestination = resultDestination.replace(`:${key}`, value);
    });
    this.navigateByUrl(resultDestination);
  }

  navigateByUrl(url: string): void {
    this.router.navigateByUrl(url);
  }
}

export class Destination {
  static readonly FEED = new Destination("");
  static readonly SEARCH = new Destination("search");
  static readonly LOGIN = new Destination("login");
  static readonly REGISTER = new Destination("register");
  static readonly ARTICLE = new Destination("article/:article");
  static readonly PROFILE = new Destination("profile");
  static readonly EMAIL_CONFIRM = new Destination("email-confirm")
  static readonly UPLOAD = new Destination("upload");
  static readonly UPLOAD_EDIT = new Destination("upload/:id");
  static readonly PROFILE_SETTINGS = new Destination("profile-settings");
  static readonly FORGOT_PASSWORD = new Destination("forgot-password");
  static readonly INTER_CHANGE_PASSWORD = new Destination("api/auth/changePassword");
  static readonly CHANGE_PASSWORD = new Destination("change-password");
  static readonly VERIFY = new Destination("api/auth/verify");
  static readonly TYPES = new Destination("types");

  private readonly path: string = "";

  constructor(path: string) {
    this.path = path;
  }

  public toPath(): string {
    return this.path;
  }
}
