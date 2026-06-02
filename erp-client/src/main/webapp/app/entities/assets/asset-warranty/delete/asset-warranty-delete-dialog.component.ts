import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssetWarranty } from '../asset-warranty.model';
import { AssetWarrantyService } from '../service/asset-warranty.service';

@Component({
  templateUrl: './asset-warranty-delete-dialog.component.html',
})
export class AssetWarrantyDeleteDialogComponent {
  assetWarranty?: IAssetWarranty;

  constructor(protected assetWarrantyService: AssetWarrantyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetWarrantyService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
