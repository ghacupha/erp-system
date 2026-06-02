import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRouAssetListReport } from '../rou-asset-list-report.model';
import { RouAssetListReportService } from '../service/rou-asset-list-report.service';

@Component({
  templateUrl: './rou-asset-list-report-delete-dialog.component.html',
})
export class RouAssetListReportDeleteDialogComponent {
  rouAssetListReport?: IRouAssetListReport;

  constructor(protected rouAssetListReportService: RouAssetListReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rouAssetListReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
