import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbReportRequestReasons } from '../crb-report-request-reasons.model';
import { CrbReportRequestReasonsService } from '../service/crb-report-request-reasons.service';

@Component({
  templateUrl: './crb-report-request-reasons-delete-dialog.component.html',
})
export class CrbReportRequestReasonsDeleteDialogComponent {
  crbReportRequestReasons?: ICrbReportRequestReasons;

  constructor(protected crbReportRequestReasonsService: CrbReportRequestReasonsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbReportRequestReasonsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
