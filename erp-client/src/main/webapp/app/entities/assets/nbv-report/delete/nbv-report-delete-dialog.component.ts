import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INbvReport } from '../nbv-report.model';
import { NbvReportService } from '../service/nbv-report.service';

@Component({
  templateUrl: './nbv-report-delete-dialog.component.html',
})
export class NbvReportDeleteDialogComponent {
  nbvReport?: INbvReport;

  constructor(protected nbvReportService: NbvReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nbvReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
