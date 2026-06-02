import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReportStatus } from '../report-status.model';
import { ReportStatusService } from '../service/report-status.service';

@Component({
  templateUrl: './report-status-delete-dialog.component.html',
})
export class ReportStatusDeleteDialogComponent {
  reportStatus?: IReportStatus;

  constructor(protected reportStatusService: ReportStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reportStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
