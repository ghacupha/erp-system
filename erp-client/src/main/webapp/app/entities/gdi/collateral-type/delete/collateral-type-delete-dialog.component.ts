import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICollateralType } from '../collateral-type.model';
import { CollateralTypeService } from '../service/collateral-type.service';

@Component({
  templateUrl: './collateral-type-delete-dialog.component.html',
})
export class CollateralTypeDeleteDialogComponent {
  collateralType?: ICollateralType;

  constructor(protected collateralTypeService: CollateralTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.collateralTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
