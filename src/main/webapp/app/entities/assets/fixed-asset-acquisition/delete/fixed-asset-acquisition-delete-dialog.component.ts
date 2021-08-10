import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFixedAssetAcquisition } from '../fixed-asset-acquisition.model';
import { FixedAssetAcquisitionService } from '../service/fixed-asset-acquisition.service';

@Component({
  templateUrl: './fixed-asset-acquisition-delete-dialog.component.html',
})
export class FixedAssetAcquisitionDeleteDialogComponent {
  fixedAssetAcquisition?: IFixedAssetAcquisition;

  constructor(protected fixedAssetAcquisitionService: FixedAssetAcquisitionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fixedAssetAcquisitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
