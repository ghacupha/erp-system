import { NgModule } from '@angular/core';
import { ErpGatewaySharedLibsModule } from './shared-libs.module';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { LoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import {MomentMediumDatePipe} from "app/shared/date/moment-medium-date.pipe";
import {MomentMediumDatetimePipe} from "app/shared/date/moment-medium-datetime.pipe";
import {DurationPipe} from "app/shared/date/duration.pipe";

@NgModule({
  imports: [ErpGatewaySharedLibsModule],
  declarations: [AlertComponent, AlertErrorComponent, LoginModalComponent, HasAnyAuthorityDirective, MomentMediumDatePipe, MomentMediumDatetimePipe, DurationPipe],
  entryComponents: [LoginModalComponent],
  exports: [ErpGatewaySharedLibsModule, AlertComponent, AlertErrorComponent, LoginModalComponent, HasAnyAuthorityDirective, MomentMediumDatePipe, MomentMediumDatetimePipe, DurationPipe],
})
export class ErpGatewaySharedModule {}
