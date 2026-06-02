import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseLiabilityReportComponent } from './list/lease-liability-report.component';
import { LeaseLiabilityReportDetailComponent } from './detail/lease-liability-report-detail.component';
import { LeaseLiabilityReportUpdateComponent } from './update/lease-liability-report-update.component';
import { LeaseLiabilityReportDeleteDialogComponent } from './delete/lease-liability-report-delete-dialog.component';
import { LeaseLiabilityReportRoutingModule } from './route/lease-liability-report-routing.module';

@NgModule({
  imports: [SharedModule, LeaseLiabilityReportRoutingModule],
  declarations: [
    LeaseLiabilityReportComponent,
    LeaseLiabilityReportDetailComponent,
    LeaseLiabilityReportUpdateComponent,
    LeaseLiabilityReportDeleteDialogComponent,
  ],
  entryComponents: [LeaseLiabilityReportDeleteDialogComponent],
})
export class LeaseLiabilityReportModule {}
