import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkInProgressOutstandingReportRequisition } from '../work-in-progress-outstanding-report-requisition.model';
import { WorkInProgressOutstandingReportRequisitionService } from '../service/work-in-progress-outstanding-report-requisition.service';

@Component({
  templateUrl: './work-in-progress-outstanding-report-requisition-delete-dialog.component.html',
})
export class WorkInProgressOutstandingReportRequisitionDeleteDialogComponent {
  workInProgressOutstandingReportRequisition?: IWorkInProgressOutstandingReportRequisition;

  constructor(
    protected workInProgressOutstandingReportRequisitionService: WorkInProgressOutstandingReportRequisitionService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workInProgressOutstandingReportRequisitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
