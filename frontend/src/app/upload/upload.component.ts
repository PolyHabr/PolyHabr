import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ArticleTypesService} from "../core/services/article_types.service";
import {Article} from "../../data/models/article";
import {ArticlesService} from "../core/services/articles.service";
import {Destination, NavigationService} from "../core/services/navigation.service";
import {Data} from "../core/types/Data";
import {DisciplineTypesService} from "../core/services/discipline_types.service";
import {FilesService} from "../core/services/files.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'poly-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss']
})
export class UploadComponent implements OnInit {

  @ViewChild("titleInputComponent")
  titleInputComponent!: ElementRef;

  @ViewChild("tagInputComponent")
  tagInputComponent!: ElementRef;

  @ViewChild("textInputComponent")
  textInputComponent!: ElementRef;

  @ViewChild("previewTextInputComponent")
  previewTextInputComponent!: ElementRef;

  types: Article.Type[] = [];
  disciplines: Article.Type[] = [];
  file: File | null = null;
  preview: File | null = null;
  hasError: boolean = false;
  hasPreviewError: boolean = false;
  selectedType!: Article.Type;
  selectedDiscipline!: Article.Type;
  id: number | undefined = undefined;

  constructor(private articleTypesService: ArticleTypesService, private articlesService: ArticlesService,
              private navigationService: NavigationService, private disciplineTypesService: DisciplineTypesService,
              private filesService: FilesService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.articleTypesService.getTypes().subscribe(result => {
      this.types = result.contents;
      this.selectedType = this.types[0];

      this.disciplineTypesService.getTypes().subscribe(result => {
        this.disciplines = result.contents;
        this.selectedDiscipline = this.disciplines[0];
        this.route.params.subscribe(param => {
          if (param["id"]) {
            this.id = Number(param["id"]);
            this.articlesService.getArticle(this.id!).subscribe(result => {
              this.titleInputComponent.nativeElement.value = result.title;
              this.selectedType = this.types.find(element => element.name == result.typeId.name)!
              this.selectedDiscipline = this.disciplines.find(element => element.name == result.listDisciplineName[0])!;
              this.previewTextInputComponent.nativeElement.value = result.previewText;
              this.textInputComponent.nativeElement.value = result.text;
              this.tagInputComponent.nativeElement.value = result.listTag.join(",");
              if (result.fileId) {
                this.file = new File([""], "");
              }
              if (result.previewImgId) {
                this.preview = new File([""], "");
              }
            })
          }
        });
      });
    });
  }

  onFileSelected(files: File[]): void {
    if (files.length > 0) {
      this.hasError = false;
      if (files[0].size < 15 * 1024 * 1024) {
        this.file = files[0];
      } else {
        this.hasError = true;
      }
    }
  }

  onPreviewSelected(files: File[]): void {
    if (files.length > 0) {
      this.hasPreviewError = false;
      if (files[0].size < 15 * 1024 * 1024) {
        this.preview = files[0];
      } else {
        this.hasPreviewError = true;
      }
    }
  }

  toArticle(id: string): void {
    this.navigationService.navigateTo(Destination.ARTICLE, new Map([["article", id]]));
  }

  add(e: Event): void {
    e.preventDefault();
    if (this.id == undefined) {
      let body: Data = {
        title: this.titleInputComponent.nativeElement.value,
        text: this.textInputComponent.nativeElement.value,
        previewText: this.previewTextInputComponent.nativeElement.value,
        listTag: this.tagInputComponent.nativeElement.value.toString().trim().split(","),
        listDisciplineName: [this.selectedDiscipline.name],
        articleType: this.selectedType.name
      };
      let uploadFile = (id: string, onComplete: () => void) => {
        this.filesService.sendFile(this.file!!, id).subscribe(() => {
          onComplete();
        });
      };
      let uploadImage = (id: string, onComplete: () => void) => {
        this.filesService.sendImage(this.preview!!, id).subscribe(() => {
          onComplete();
        });
      };
      this.articlesService.add(body).subscribe((result) => {
        if (this.file && this.file.name.length > 0) {
          uploadFile(result["id"] as string, () => {
            if (this.preview && this.preview.name.length > 0) {
              uploadImage(result["id"] as string, () => {
                this.toArticle(result["id"] as string);
              });
            } else {
              this.toArticle(result["id"] as string);
            }
          });
        } else {
          if (this.preview && this.preview.name.length > 0) {
            uploadImage(result["id"] as string, () => {
              this.toArticle(result["id"] as string);
            });
          } else {
            this.toArticle(result["id"] as string);
          }
        }
      });
    } else {
      let body: Data = {
        title: this.titleInputComponent.nativeElement.value,
        text: this.textInputComponent.nativeElement.value,
        previewText: this.previewTextInputComponent.nativeElement.value,
        typeName: this.selectedType.name
      }
      let uploadFile = (id: string, onComplete: () => void) => {
        this.filesService.sendFile(this.file!!, id).subscribe(() => {
          onComplete();
        });
      };
      let uploadImage = (id: string, onComplete: () => void) => {
        this.filesService.sendImage(this.preview!!, id).subscribe(() => {
          onComplete();
        });
      };
      this.articlesService.update(body, this.id!!).subscribe(() => {
        if (this.file && this.file.name.length > 0) {
          uploadFile(String(this.id!!) as string, () => {
            if (this.preview && this.preview.name.length > 0) {
              uploadImage(String(this.id!!) as string, () => {
                this.toArticle(String(this.id!!) as string);
              });
            } else {
              this.toArticle(String(this.id!!) as string);
            }
          });
        } else {
          if (this.preview && this.preview.name.length > 0) {
            uploadImage(String(this.id!!) as string, () => {
              this.toArticle(String(this.id!!) as string);
            });
          } else {
            this.toArticle(String(this.id!!) as string);
          }
        }
      });
    }
  }
}
