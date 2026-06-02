import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LoanDeclineReasonComponent } from './list/loan-decline-reason.component';
import { LoanDeclineReasonDetailComponent } from './detail/loan-decline-reason-detail.component';
import { LoanDeclineReasonUpdateComponent } from './update/loan-decline-reason-update.component';
import { LoanDeclineReasonDeleteDialogComponent } from './delete/loan-decline-reason-delete-dialog.component';
import { LoanDeclineReasonRoutingModule } from './route/loan-decline-reason-routing.module';

@NgModule({
  imports: [SharedModule, LoanDeclineReasonRoutingModule],
  declarations: [
    LoanDeclineReasonComponent,
    LoanDeclineReasonDetailComponent,
    LoanDeclineReasonUpdateComponent,
    LoanDeclineReasonDeleteDialogComponent,
  ],
  entryComponents: [LoanDeclineReasonDeleteDialogComponent],
})
export class LoanDeclineReasonModule {}
