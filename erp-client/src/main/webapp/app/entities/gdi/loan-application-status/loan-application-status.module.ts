import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LoanApplicationStatusComponent } from './list/loan-application-status.component';
import { LoanApplicationStatusDetailComponent } from './detail/loan-application-status-detail.component';
import { LoanApplicationStatusUpdateComponent } from './update/loan-application-status-update.component';
import { LoanApplicationStatusDeleteDialogComponent } from './delete/loan-application-status-delete-dialog.component';
import { LoanApplicationStatusRoutingModule } from './route/loan-application-status-routing.module';

@NgModule({
  imports: [SharedModule, LoanApplicationStatusRoutingModule],
  declarations: [
    LoanApplicationStatusComponent,
    LoanApplicationStatusDetailComponent,
    LoanApplicationStatusUpdateComponent,
    LoanApplicationStatusDeleteDialogComponent,
  ],
  entryComponents: [LoanApplicationStatusDeleteDialogComponent],
})
export class LoanApplicationStatusModule {}
