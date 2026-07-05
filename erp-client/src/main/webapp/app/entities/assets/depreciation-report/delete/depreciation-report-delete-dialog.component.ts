import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepreciationReport } from '../depreciation-report.model';
import { DepreciationReportService } from '../service/depreciation-report.service';

@Component({
  templateUrl: './depreciation-report-delete-dialog.component.html',
})
export class DepreciationReportDeleteDialogComponent {
  depreciationReport?: IDepreciationReport;

  constructor(protected depreciationReportService: DepreciationReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.depreciationReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
