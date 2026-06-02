import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrepaymentReportRequisition } from '../prepayment-report-requisition.model';
import { PrepaymentReportRequisitionService } from '../service/prepayment-report-requisition.service';

@Component({
  templateUrl: './prepayment-report-requisition-delete-dialog.component.html',
})
export class PrepaymentReportRequisitionDeleteDialogComponent {
  prepaymentReportRequisition?: IPrepaymentReportRequisition;

  constructor(protected prepaymentReportRequisitionService: PrepaymentReportRequisitionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prepaymentReportRequisitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
