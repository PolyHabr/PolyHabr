import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {RouterModule} from "@angular/router";
import {AppRoutingModule} from "./app-routing.module";
import {SharedModule} from "./shared/shared.module";
import {DataModule} from "../data/data.module";
import {SearchComponent} from './search/search.component';
import {FeedComponent} from "./feed/feed.component";
import {CoreModule} from "./core/core.module";
import { LoginComponent } from './login/login.component';
import { ArticleComponent } from './article/article.component';
import { RegistrationComponent } from './registration/registration.component';
import { ProfileComponent } from './profile/profile.component';
import { EmailConfirmComponent } from './email-confirm/email-confirm.component';
import { UploadComponent } from './upload/upload.component';
import { ProfileSettingsComponent } from './profile-settings/profile-settings.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { VerifyComponent } from './verify/verify.component';
import { TypeComponent } from './type/type.component';
import { InterChangePasswordComponent } from './inter-change-password/inter-change-password.component';
import { ChangePasswordComponent } from './change-password/change-password.component';

@NgModule({
  declarations: [
    AppComponent,
    FeedComponent,
    SearchComponent,
    LoginComponent,
    RegistrationComponent,
    LoginComponent,
    ArticleComponent,
    ProfileComponent,
    EmailConfirmComponent,
    UploadComponent,
    ProfileSettingsComponent,
    ForgotPasswordComponent,
    VerifyComponent,
    TypeComponent,
    InterChangePasswordComponent,
    ChangePasswordComponent
  ],
    imports: [
        BrowserModule,
        RouterModule,
        AppRoutingModule,
        SharedModule,
        DataModule,
        CoreModule,
        FormsModule,
        HttpClientModule,
        ReactiveFormsModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
