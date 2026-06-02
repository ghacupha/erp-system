import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WorkInProgressRegistrationComponent } from './list/work-in-progress-registration.component';
import { WorkInProgressRegistrationDetailComponent } from './detail/work-in-progress-registration-detail.component';
import { WorkInProgressRegistrationUpdateComponent } from './update/work-in-progress-registration-update.component';
import { WorkInProgressRegistrationDeleteDialogComponent } from './delete/work-in-progress-registration-delete-dialog.component';
import { WorkInProgressRegistrationRoutingModule } from './route/work-in-progress-registration-routing.module';

@NgModule({
  imports: [SharedModule, WorkInProgressRegistrationRoutingModule],
  declarations: [
    WorkInProgressRegistrationComponent,
    WorkInProgressRegistrationDetailComponent,
    WorkInProgressRegistrationUpdateComponent,
    WorkInProgressRegistrationDeleteDialogComponent,
  ],
  entryComponents: [WorkInProgressRegistrationDeleteDialogComponent],
})
export class WorkInProgressRegistrationModule {}
