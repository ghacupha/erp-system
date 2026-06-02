import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRouDepreciationEntryReport } from '../rou-depreciation-entry-report.model';
import { RouDepreciationEntryReportService } from '../service/rou-depreciation-entry-report.service';

@Component({
  templateUrl: './rou-depreciation-entry-report-delete-dialog.component.html',
})
export class RouDepreciationEntryReportDeleteDialogComponent {
  rouDepreciationEntryReport?: IRouDepreciationEntryReport;

  constructor(protected rouDepreciationEntryReportService: RouDepreciationEntryReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rouDepreciationEntryReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
