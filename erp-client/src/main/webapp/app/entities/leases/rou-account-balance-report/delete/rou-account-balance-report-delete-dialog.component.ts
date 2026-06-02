import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRouAccountBalanceReport } from '../rou-account-balance-report.model';
import { RouAccountBalanceReportService } from '../service/rou-account-balance-report.service';

@Component({
  templateUrl: './rou-account-balance-report-delete-dialog.component.html',
})
export class RouAccountBalanceReportDeleteDialogComponent {
  rouAccountBalanceReport?: IRouAccountBalanceReport;

  constructor(protected rouAccountBalanceReportService: RouAccountBalanceReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rouAccountBalanceReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
