import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFixedAssetNetBookValue } from '../fixed-asset-net-book-value.model';
import { FixedAssetNetBookValueService } from '../service/fixed-asset-net-book-value.service';

@Component({
  templateUrl: './fixed-asset-net-book-value-delete-dialog.component.html',
})
export class FixedAssetNetBookValueDeleteDialogComponent {
  fixedAssetNetBookValue?: IFixedAssetNetBookValue;

  constructor(protected fixedAssetNetBookValueService: FixedAssetNetBookValueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fixedAssetNetBookValueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
