import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssetGeneralAdjustment } from '../asset-general-adjustment.model';
import { AssetGeneralAdjustmentService } from '../service/asset-general-adjustment.service';

@Component({
  templateUrl: './asset-general-adjustment-delete-dialog.component.html',
})
export class AssetGeneralAdjustmentDeleteDialogComponent {
  assetGeneralAdjustment?: IAssetGeneralAdjustment;

  constructor(protected assetGeneralAdjustmentService: AssetGeneralAdjustmentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetGeneralAdjustmentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
