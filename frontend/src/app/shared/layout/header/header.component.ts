import {Component} from '@angular/core';
import {Destination, NavigationService} from "../../../core/services/navigation.service";
import {DataHelper} from "../../../core/helpers/data.helper";

@Component({
  selector: 'poly-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  readonly DataHelper = DataHelper;

  constructor(private navigationService: NavigationService) {
  }

  toFeed(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.FEED);
  }

  toProfile(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.PROFILE);
  }

  toSearch(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.SEARCH);
  }

  toUpload(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.UPLOAD);
  }
}
