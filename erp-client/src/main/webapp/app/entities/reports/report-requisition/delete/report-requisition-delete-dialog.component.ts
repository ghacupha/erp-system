import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReportRequisition } from '../report-requisition.model';
import { ReportRequisitionService } from '../service/report-requisition.service';

@Component({
  templateUrl: './report-requisition-delete-dialog.component.html',
})
export class ReportRequisitionDeleteDialogComponent {
  reportRequisition?: IReportRequisition;

  constructor(protected reportRequisitionService: ReportRequisitionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reportRequisitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
