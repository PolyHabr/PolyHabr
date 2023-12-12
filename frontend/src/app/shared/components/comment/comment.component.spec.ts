import {ComponentFixture, TestBed} from "@angular/core/testing";
import {CommentComponent} from "./comment.component";
import {Article} from "../../../../data/models/article";

describe('Comment Test', () => {
  let component: CommentComponent;
  let fixture: ComponentFixture<CommentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CommentComponent]
    });
    fixture = TestBed.createComponent(CommentComponent);
    component = fixture.componentInstance;
    component.comment = new Article.Comment("test");
  });

  it('Comment init test', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });
});
