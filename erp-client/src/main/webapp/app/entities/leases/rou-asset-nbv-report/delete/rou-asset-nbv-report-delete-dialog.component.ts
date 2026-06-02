import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRouAssetNBVReport } from '../rou-asset-nbv-report.model';
import { RouAssetNBVReportService } from '../service/rou-asset-nbv-report.service';

@Component({
  templateUrl: './rou-asset-nbv-report-delete-dialog.component.html',
})
export class RouAssetNBVReportDeleteDialogComponent {
  rouAssetNBVReport?: IRouAssetNBVReport;

  constructor(protected rouAssetNBVReportService: RouAssetNBVReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rouAssetNBVReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
