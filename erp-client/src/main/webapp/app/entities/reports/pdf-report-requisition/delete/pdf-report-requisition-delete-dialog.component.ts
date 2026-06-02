import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPdfReportRequisition } from '../pdf-report-requisition.model';
import { PdfReportRequisitionService } from '../service/pdf-report-requisition.service';

@Component({
  templateUrl: './pdf-report-requisition-delete-dialog.component.html',
})
export class PdfReportRequisitionDeleteDialogComponent {
  pdfReportRequisition?: IPdfReportRequisition;

  constructor(protected pdfReportRequisitionService: PdfReportRequisitionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pdfReportRequisitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
