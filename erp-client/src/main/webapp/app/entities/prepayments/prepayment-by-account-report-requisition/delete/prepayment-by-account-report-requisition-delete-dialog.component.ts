import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrepaymentByAccountReportRequisition } from '../prepayment-by-account-report-requisition.model';
import { PrepaymentByAccountReportRequisitionService } from '../service/prepayment-by-account-report-requisition.service';

@Component({
  templateUrl: './prepayment-by-account-report-requisition-delete-dialog.component.html',
})
export class PrepaymentByAccountReportRequisitionDeleteDialogComponent {
  prepaymentByAccountReportRequisition?: IPrepaymentByAccountReportRequisition;

  constructor(
    protected prepaymentByAccountReportRequisitionService: PrepaymentByAccountReportRequisitionService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prepaymentByAccountReportRequisitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
