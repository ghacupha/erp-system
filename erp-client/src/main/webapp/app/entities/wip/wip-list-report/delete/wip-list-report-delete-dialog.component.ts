import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWIPListReport } from '../wip-list-report.model';
import { WIPListReportService } from '../service/wip-list-report.service';

@Component({
  templateUrl: './wip-list-report-delete-dialog.component.html',
})
export class WIPListReportDeleteDialogComponent {
  wIPListReport?: IWIPListReport;

  constructor(protected wIPListReportService: WIPListReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wIPListReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
