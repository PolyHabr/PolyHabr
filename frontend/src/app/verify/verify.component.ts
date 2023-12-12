import {Component, OnInit} from '@angular/core';
import {Destination, NavigationService} from "../core/services/navigation.service";
import {AuthorizationService} from "../core/services/authorization.service";

@Component({
  selector: 'poly-verify',
  templateUrl: './verify.component.html',
  styleUrls: ['./verify.component.scss']
})
export class VerifyComponent implements OnInit {

  constructor(private navigationService: NavigationService, private authorizationService: AuthorizationService) { }

  ngOnInit(): void {
    this.authorizationService.verify(window.location.search.replace("?code=", "")).subscribe();
  }

  toFeed(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.FEED);
  }

  toLogin(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.LOGIN);
  }

}
