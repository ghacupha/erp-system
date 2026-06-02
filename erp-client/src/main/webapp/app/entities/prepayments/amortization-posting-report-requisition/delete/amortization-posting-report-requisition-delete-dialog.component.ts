import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAmortizationPostingReportRequisition } from '../amortization-posting-report-requisition.model';
import { AmortizationPostingReportRequisitionService } from '../service/amortization-posting-report-requisition.service';

@Component({
  templateUrl: './amortization-posting-report-requisition-delete-dialog.component.html',
})
export class AmortizationPostingReportRequisitionDeleteDialogComponent {
  amortizationPostingReportRequisition?: IAmortizationPostingReportRequisition;

  constructor(
    protected amortizationPostingReportRequisitionService: AmortizationPostingReportRequisitionService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.amortizationPostingReportRequisitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
