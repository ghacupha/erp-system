import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IXlsxReportRequisition } from '../xlsx-report-requisition.model';
import { XlsxReportRequisitionService } from '../service/xlsx-report-requisition.service';

@Component({
  templateUrl: './xlsx-report-requisition-delete-dialog.component.html',
})
export class XlsxReportRequisitionDeleteDialogComponent {
  xlsxReportRequisition?: IXlsxReportRequisition;

  constructor(protected xlsxReportRequisitionService: XlsxReportRequisitionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.xlsxReportRequisitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
