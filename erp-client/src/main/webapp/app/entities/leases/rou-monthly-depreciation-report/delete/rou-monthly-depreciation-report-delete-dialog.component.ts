import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRouMonthlyDepreciationReport } from '../rou-monthly-depreciation-report.model';
import { RouMonthlyDepreciationReportService } from '../service/rou-monthly-depreciation-report.service';

@Component({
  templateUrl: './rou-monthly-depreciation-report-delete-dialog.component.html',
})
export class RouMonthlyDepreciationReportDeleteDialogComponent {
  rouMonthlyDepreciationReport?: IRouMonthlyDepreciationReport;

  constructor(protected rouMonthlyDepreciationReportService: RouMonthlyDepreciationReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rouMonthlyDepreciationReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
