import {Component, OnInit} from '@angular/core';
import {Destination, NavigationService} from "../core/services/navigation.service";
import {StorageHelper} from "../core/helpers/storage.helper";

@Component({
  selector: 'poly-email-confirm',
  templateUrl: './email-confirm.component.html',
  styleUrls: ['./email-confirm.component.scss']
})
export class EmailConfirmComponent implements OnInit {

  emailString: string = "";

  constructor(private navigationService: NavigationService) {
  }

  ngOnInit(): void {
    this.getEmail();
  }

  toFeed(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.FEED);
  }

  getEmail(): void {
    const email = StorageHelper.getCookie("email");
    if (email != undefined) {
      this.emailString = email;
      StorageHelper.deleteCookie("email");
    }
  }

}
