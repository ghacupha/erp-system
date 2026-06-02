import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFixedAssetDepreciation } from '../fixed-asset-depreciation.model';
import { FixedAssetDepreciationService } from '../service/fixed-asset-depreciation.service';

@Component({
  templateUrl: './fixed-asset-depreciation-delete-dialog.component.html',
})
export class FixedAssetDepreciationDeleteDialogComponent {
  fixedAssetDepreciation?: IFixedAssetDepreciation;

  constructor(protected fixedAssetDepreciationService: FixedAssetDepreciationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fixedAssetDepreciationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
