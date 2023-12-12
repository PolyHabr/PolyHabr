import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './layout/header/header.component';
import { CardComponent } from './components/card/card.component';
import {SortBarComponent} from "./components/sort-bar/sort-bar.component";
import { MenuComponent } from './layout/menu/menu.component';
import {ClickOutsideModule} from "ng-click-outside";
import { CommentComponent } from './components/comment/comment.component';
import {CoreModule} from "../core/core.module";



@NgModule({
    declarations: [
        HeaderComponent,
        SortBarComponent,
        CardComponent,
        MenuComponent,
        CommentComponent
    ],
    exports: [
        HeaderComponent,
        SortBarComponent,
        CardComponent,
        CommentComponent
    ],
    imports: [
        CommonModule,
        ClickOutsideModule,
        CoreModule
    ]
})
export class SharedModule { }
