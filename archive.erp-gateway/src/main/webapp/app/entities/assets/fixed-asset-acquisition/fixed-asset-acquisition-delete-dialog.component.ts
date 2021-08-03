import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFixedAssetAcquisition } from 'app/shared/model/assets/fixed-asset-acquisition.model';
import { FixedAssetAcquisitionService } from './fixed-asset-acquisition.service';

@Component({
  templateUrl: './fixed-asset-acquisition-delete-dialog.component.html',
})
export class FixedAssetAcquisitionDeleteDialogComponent {
  fixedAssetAcquisition?: IFixedAssetAcquisition;

  constructor(
    protected fixedAssetAcquisitionService: FixedAssetAcquisitionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fixedAssetAcquisitionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('fixedAssetAcquisitionListModification');
      this.activeModal.close();
    });
  }
}
