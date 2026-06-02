import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseLiabilityPostingReportComponent } from './list/lease-liability-posting-report.component';
import { LeaseLiabilityPostingReportDetailComponent } from './detail/lease-liability-posting-report-detail.component';
import { LeaseLiabilityPostingReportUpdateComponent } from './update/lease-liability-posting-report-update.component';
import { LeaseLiabilityPostingReportDeleteDialogComponent } from './delete/lease-liability-posting-report-delete-dialog.component';
import { LeaseLiabilityPostingReportRoutingModule } from './route/lease-liability-posting-report-routing.module';

@NgModule({
  imports: [SharedModule, LeaseLiabilityPostingReportRoutingModule],
  declarations: [
    LeaseLiabilityPostingReportComponent,
    LeaseLiabilityPostingReportDetailComponent,
    LeaseLiabilityPostingReportUpdateComponent,
    LeaseLiabilityPostingReportDeleteDialogComponent,
  ],
  entryComponents: [LeaseLiabilityPostingReportDeleteDialogComponent],
})
export class LeaseLiabilityPostingReportModule {}
