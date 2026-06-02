import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDerivativeUnderlyingAsset } from '../derivative-underlying-asset.model';
import { DerivativeUnderlyingAssetService } from '../service/derivative-underlying-asset.service';

@Component({
  templateUrl: './derivative-underlying-asset-delete-dialog.component.html',
})
export class DerivativeUnderlyingAssetDeleteDialogComponent {
  derivativeUnderlyingAsset?: IDerivativeUnderlyingAsset;

  constructor(protected derivativeUnderlyingAssetService: DerivativeUnderlyingAssetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.derivativeUnderlyingAssetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
