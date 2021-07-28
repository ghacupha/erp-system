import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFixedAssetNetBookValue } from 'app/shared/model/assets/fixed-asset-net-book-value.model';
import { FixedAssetNetBookValueService } from './fixed-asset-net-book-value.service';

@Component({
  templateUrl: './fixed-asset-net-book-value-delete-dialog.component.html',
})
export class FixedAssetNetBookValueDeleteDialogComponent {
  fixedAssetNetBookValue?: IFixedAssetNetBookValue;

  constructor(
    protected fixedAssetNetBookValueService: FixedAssetNetBookValueService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fixedAssetNetBookValueService.delete(id).subscribe(() => {
      this.eventManager.broadcast('fixedAssetNetBookValueListModification');
      this.activeModal.close();
    });
  }
}
