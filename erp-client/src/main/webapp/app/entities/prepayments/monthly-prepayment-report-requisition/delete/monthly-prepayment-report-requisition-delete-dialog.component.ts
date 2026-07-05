import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMonthlyPrepaymentReportRequisition } from '../monthly-prepayment-report-requisition.model';
import { MonthlyPrepaymentReportRequisitionService } from '../service/monthly-prepayment-report-requisition.service';

@Component({
  templateUrl: './monthly-prepayment-report-requisition-delete-dialog.component.html',
})
export class MonthlyPrepaymentReportRequisitionDeleteDialogComponent {
  monthlyPrepaymentReportRequisition?: IMonthlyPrepaymentReportRequisition;

  constructor(
    protected monthlyPrepaymentReportRequisitionService: MonthlyPrepaymentReportRequisitionService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.monthlyPrepaymentReportRequisitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
