import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaseLiabilityByAccountReport } from '../lease-liability-by-account-report.model';
import { LeaseLiabilityByAccountReportService } from '../service/lease-liability-by-account-report.service';

@Component({
  templateUrl: './lease-liability-by-account-report-delete-dialog.component.html',
})
export class LeaseLiabilityByAccountReportDeleteDialogComponent {
  leaseLiabilityByAccountReport?: ILeaseLiabilityByAccountReport;

  constructor(
    protected leaseLiabilityByAccountReportService: LeaseLiabilityByAccountReportService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaseLiabilityByAccountReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
