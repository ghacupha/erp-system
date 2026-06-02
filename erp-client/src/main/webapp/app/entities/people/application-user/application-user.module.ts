import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ApplicationUserComponent } from './list/application-user.component';
import { ApplicationUserDetailComponent } from './detail/application-user-detail.component';
import { ApplicationUserUpdateComponent } from './update/application-user-update.component';
import { ApplicationUserDeleteDialogComponent } from './delete/application-user-delete-dialog.component';
import { ApplicationUserRoutingModule } from './route/application-user-routing.module';

@NgModule({
  imports: [SharedModule, ApplicationUserRoutingModule],
  declarations: [
    ApplicationUserComponent,
    ApplicationUserDetailComponent,
    ApplicationUserUpdateComponent,
    ApplicationUserDeleteDialogComponent,
  ],
  entryComponents: [ApplicationUserDeleteDialogComponent],
})
export class ApplicationUserModule {}
