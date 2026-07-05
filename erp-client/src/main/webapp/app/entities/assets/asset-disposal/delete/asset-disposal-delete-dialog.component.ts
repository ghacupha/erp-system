import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssetDisposal } from '../asset-disposal.model';
import { AssetDisposalService } from '../service/asset-disposal.service';

@Component({
  templateUrl: './asset-disposal-delete-dialog.component.html',
})
export class AssetDisposalDeleteDialogComponent {
  assetDisposal?: IAssetDisposal;

  constructor(protected assetDisposalService: AssetDisposalService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetDisposalService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
