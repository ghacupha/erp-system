import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseLiabilityByAccountReportComponent } from './list/lease-liability-by-account-report.component';
import { LeaseLiabilityByAccountReportDetailComponent } from './detail/lease-liability-by-account-report-detail.component';
import { LeaseLiabilityByAccountReportUpdateComponent } from './update/lease-liability-by-account-report-update.component';
import { LeaseLiabilityByAccountReportDeleteDialogComponent } from './delete/lease-liability-by-account-report-delete-dialog.component';
import { LeaseLiabilityByAccountReportRoutingModule } from './route/lease-liability-by-account-report-routing.module';

@NgModule({
  imports: [SharedModule, LeaseLiabilityByAccountReportRoutingModule],
  declarations: [
    LeaseLiabilityByAccountReportComponent,
    LeaseLiabilityByAccountReportDetailComponent,
    LeaseLiabilityByAccountReportUpdateComponent,
    LeaseLiabilityByAccountReportDeleteDialogComponent,
  ],
  entryComponents: [LeaseLiabilityByAccountReportDeleteDialogComponent],
})
export class LeaseLiabilityByAccountReportModule {}
