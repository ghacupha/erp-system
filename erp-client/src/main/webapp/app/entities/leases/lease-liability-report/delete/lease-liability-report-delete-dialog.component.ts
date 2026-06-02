import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaseLiabilityReport } from '../lease-liability-report.model';
import { LeaseLiabilityReportService } from '../service/lease-liability-report.service';

@Component({
  templateUrl: './lease-liability-report-delete-dialog.component.html',
})
export class LeaseLiabilityReportDeleteDialogComponent {
  leaseLiabilityReport?: ILeaseLiabilityReport;

  constructor(protected leaseLiabilityReportService: LeaseLiabilityReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaseLiabilityReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
