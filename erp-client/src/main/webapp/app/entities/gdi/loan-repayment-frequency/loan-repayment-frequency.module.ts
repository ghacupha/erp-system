import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LoanRepaymentFrequencyComponent } from './list/loan-repayment-frequency.component';
import { LoanRepaymentFrequencyDetailComponent } from './detail/loan-repayment-frequency-detail.component';
import { LoanRepaymentFrequencyUpdateComponent } from './update/loan-repayment-frequency-update.component';
import { LoanRepaymentFrequencyDeleteDialogComponent } from './delete/loan-repayment-frequency-delete-dialog.component';
import { LoanRepaymentFrequencyRoutingModule } from './route/loan-repayment-frequency-routing.module';

@NgModule({
  imports: [SharedModule, LoanRepaymentFrequencyRoutingModule],
  declarations: [
    LoanRepaymentFrequencyComponent,
    LoanRepaymentFrequencyDetailComponent,
    LoanRepaymentFrequencyUpdateComponent,
    LoanRepaymentFrequencyDeleteDialogComponent,
  ],
  entryComponents: [LoanRepaymentFrequencyDeleteDialogComponent],
})
export class LoanRepaymentFrequencyModule {}
