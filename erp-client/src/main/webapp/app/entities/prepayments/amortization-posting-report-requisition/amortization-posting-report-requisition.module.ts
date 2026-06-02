import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AmortizationPostingReportRequisitionComponent } from './list/amortization-posting-report-requisition.component';
import { AmortizationPostingReportRequisitionDetailComponent } from './detail/amortization-posting-report-requisition-detail.component';
import { AmortizationPostingReportRequisitionUpdateComponent } from './update/amortization-posting-report-requisition-update.component';
import { AmortizationPostingReportRequisitionDeleteDialogComponent } from './delete/amortization-posting-report-requisition-delete-dialog.component';
import { AmortizationPostingReportRequisitionRoutingModule } from './route/amortization-posting-report-requisition-routing.module';

@NgModule({
  imports: [SharedModule, AmortizationPostingReportRequisitionRoutingModule],
  declarations: [
    AmortizationPostingReportRequisitionComponent,
    AmortizationPostingReportRequisitionDetailComponent,
    AmortizationPostingReportRequisitionUpdateComponent,
    AmortizationPostingReportRequisitionDeleteDialogComponent,
  ],
  entryComponents: [AmortizationPostingReportRequisitionDeleteDialogComponent],
})
export class AmortizationPostingReportRequisitionModule {}
