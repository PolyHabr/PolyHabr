import {Component} from '@angular/core';
import {UsersService} from "./core/services/users.service";
import {DataHelper} from "./core/helpers/data.helper";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'PolyHabr';

  constructor(private usersService: UsersService) {
    usersService.getMe().subscribe(result => DataHelper.user = result);
  }
}
