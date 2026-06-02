import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssetAccessory } from '../asset-accessory.model';
import { AssetAccessoryService } from '../service/asset-accessory.service';

@Component({
  templateUrl: './asset-accessory-delete-dialog.component.html',
})
export class AssetAccessoryDeleteDialogComponent {
  assetAccessory?: IAssetAccessory;

  constructor(protected assetAccessoryService: AssetAccessoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetAccessoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
