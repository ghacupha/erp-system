import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWIPTransferListReport } from '../wip-transfer-list-report.model';
import { WIPTransferListReportService } from '../service/wip-transfer-list-report.service';

@Component({
  templateUrl: './wip-transfer-list-report-delete-dialog.component.html',
})
export class WIPTransferListReportDeleteDialogComponent {
  wIPTransferListReport?: IWIPTransferListReport;

  constructor(protected wIPTransferListReportService: WIPTransferListReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wIPTransferListReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
