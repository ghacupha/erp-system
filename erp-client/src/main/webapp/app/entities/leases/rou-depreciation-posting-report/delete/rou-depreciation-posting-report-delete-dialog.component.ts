import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRouDepreciationPostingReport } from '../rou-depreciation-posting-report.model';
import { RouDepreciationPostingReportService } from '../service/rou-depreciation-posting-report.service';

@Component({
  templateUrl: './rou-depreciation-posting-report-delete-dialog.component.html',
})
export class RouDepreciationPostingReportDeleteDialogComponent {
  rouDepreciationPostingReport?: IRouDepreciationPostingReport;

  constructor(protected rouDepreciationPostingReportService: RouDepreciationPostingReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rouDepreciationPostingReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
