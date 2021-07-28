import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFixedAssetDepreciation } from 'app/shared/model/assets/fixed-asset-depreciation.model';
import { FixedAssetDepreciationService } from './fixed-asset-depreciation.service';

@Component({
  templateUrl: './fixed-asset-depreciation-delete-dialog.component.html',
})
export class FixedAssetDepreciationDeleteDialogComponent {
  fixedAssetDepreciation?: IFixedAssetDepreciation;

  constructor(
    protected fixedAssetDepreciationService: FixedAssetDepreciationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fixedAssetDepreciationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('fixedAssetDepreciationListModification');
      this.activeModal.close();
    });
  }
}
