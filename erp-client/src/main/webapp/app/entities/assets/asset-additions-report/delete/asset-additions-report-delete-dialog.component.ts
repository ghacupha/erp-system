import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssetAdditionsReport } from '../asset-additions-report.model';
import { AssetAdditionsReportService } from '../service/asset-additions-report.service';

@Component({
  templateUrl: './asset-additions-report-delete-dialog.component.html',
})
export class AssetAdditionsReportDeleteDialogComponent {
  assetAdditionsReport?: IAssetAdditionsReport;

  constructor(protected assetAdditionsReportService: AssetAdditionsReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetAdditionsReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
