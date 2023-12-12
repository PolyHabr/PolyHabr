import {Component} from '@angular/core';
import {Destination, NavigationService} from "../core/services/navigation.service";
import {DisciplineTypesService} from "../core/services/discipline_types.service";
import {Article} from "../../data/models/article";

@Component({
  selector: 'poly-type',
  templateUrl: './type.component.html',
  styleUrls: ['./type.component.scss']
})
export class TypeComponent {

  types: Article.Type[] = [];
  selectedType!: Article.Type;
  errorText: string | null = null;

  static ErrorText = "Выберите предпочитаемую дисциплину.";

  constructor(private navigationService: NavigationService, private disciplineTypesService: DisciplineTypesService) {
    disciplineTypesService.getTypes().subscribe(result => this.types = result.contents);
  }

  toFeed(e: Event): void {
    e.preventDefault();
    this.navigationService.navigateTo(Destination.FEED);
  }

  chooseDisciplines(e: Event): void {
    e.preventDefault();
    if (this.selectedType == null) {
      this.errorText = TypeComponent.ErrorText;
    } else {
      const data = {
        namesDiscipline: [this.selectedType.name]
      };
      this.disciplineTypesService.updateMyDiscipline(data).subscribe(() => {
        this.navigationService.navigateTo(Destination.FEED);
      });
    }
  }
}
