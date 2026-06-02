import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssetRevaluation } from '../asset-revaluation.model';
import { AssetRevaluationService } from '../service/asset-revaluation.service';

@Component({
  templateUrl: './asset-revaluation-delete-dialog.component.html',
})
export class AssetRevaluationDeleteDialogComponent {
  assetRevaluation?: IAssetRevaluation;

  constructor(protected assetRevaluationService: AssetRevaluationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetRevaluationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
