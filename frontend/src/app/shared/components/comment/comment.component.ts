import {Component, Input, OnInit} from '@angular/core';
import {Article} from "../../../../data/models/article";

@Component({
  selector: 'poly-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.scss']
})
export class CommentComponent implements OnInit {

  @Input()
  comment!: Article.Comment;

  @Input()
  isLast: boolean = false;

  constructor() {
  }

  ngOnInit(): void {
  }

}
