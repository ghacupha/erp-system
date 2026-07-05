import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseRepaymentPeriodComponent } from './list/lease-repayment-period.component';
import { LeaseRepaymentPeriodDetailComponent } from './detail/lease-repayment-period-detail.component';
import { LeaseRepaymentPeriodUpdateComponent } from './update/lease-repayment-period-update.component';
import { LeaseRepaymentPeriodDeleteDialogComponent } from './delete/lease-repayment-period-delete-dialog.component';
import { LeaseRepaymentPeriodRoutingModule } from './route/lease-repayment-period-routing.module';

@NgModule({
  imports: [SharedModule, LeaseRepaymentPeriodRoutingModule],
  declarations: [
    LeaseRepaymentPeriodComponent,
    LeaseRepaymentPeriodDetailComponent,
    LeaseRepaymentPeriodUpdateComponent,
    LeaseRepaymentPeriodDeleteDialogComponent,
  ],
  entryComponents: [LeaseRepaymentPeriodDeleteDialogComponent],
})
export class LeaseRepaymentPeriodModule {}
