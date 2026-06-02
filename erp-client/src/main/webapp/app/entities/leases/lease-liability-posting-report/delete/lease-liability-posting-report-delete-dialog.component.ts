import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaseLiabilityPostingReport } from '../lease-liability-posting-report.model';
import { LeaseLiabilityPostingReportService } from '../service/lease-liability-posting-report.service';

@Component({
  templateUrl: './lease-liability-posting-report-delete-dialog.component.html',
})
export class LeaseLiabilityPostingReportDeleteDialogComponent {
  leaseLiabilityPostingReport?: ILeaseLiabilityPostingReport;

  constructor(protected leaseLiabilityPostingReportService: LeaseLiabilityPostingReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaseLiabilityPostingReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
