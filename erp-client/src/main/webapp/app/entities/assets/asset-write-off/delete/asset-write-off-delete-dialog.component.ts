import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssetWriteOff } from '../asset-write-off.model';
import { AssetWriteOffService } from '../service/asset-write-off.service';

@Component({
  templateUrl: './asset-write-off-delete-dialog.component.html',
})
export class AssetWriteOffDeleteDialogComponent {
  assetWriteOff?: IAssetWriteOff;

  constructor(protected assetWriteOffService: AssetWriteOffService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetWriteOffService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
