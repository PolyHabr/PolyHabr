import {Component} from '@angular/core';
import {Destination, NavigationService} from "../../../core/services/navigation.service";
import {DataHelper} from "../../../core/helpers/data.helper";
import {StorageHelper} from "../../../core/helpers/storage.helper";

@Component({
  selector: 'poly-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent {

  readonly DataHelper = DataHelper;

  state: MenuState = MenuState.HIDDEN;

  constructor(private navigationService: NavigationService) {
  }

  logout(e: Event): void {
    e.preventDefault();
    StorageHelper.deleteCookie("accessToken");
    DataHelper.user = undefined;
    this.toFeed();
  }

  showMenu(): void {
    this.state = MenuState.SHOWING;
    setTimeout(() => {
      this.state = MenuState.SHOWN;
    }, 100);
  }

  isShown(): Boolean {
    return this.state == MenuState.SHOWING || this.state == MenuState.SHOWN;
  }

  hideMenu(): void {
    if (this.state == MenuState.SHOWN) {
      this.state = MenuState.HIDDEN;
    }
  }

  toLogin(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.LOGIN);
  }

  toFeed(): void {
    this.navigationService.navigateTo(Destination.FEED);
  }

  toRegistration(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.REGISTER);
  }

}

export enum MenuState {
  HIDDEN, SHOWING, SHOWN
}
