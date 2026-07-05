import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbProductServiceFeeType } from '../crb-product-service-fee-type.model';
import { CrbProductServiceFeeTypeService } from '../service/crb-product-service-fee-type.service';

@Component({
  templateUrl: './crb-product-service-fee-type-delete-dialog.component.html',
})
export class CrbProductServiceFeeTypeDeleteDialogComponent {
  crbProductServiceFeeType?: ICrbProductServiceFeeType;

  constructor(protected crbProductServiceFeeTypeService: CrbProductServiceFeeTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbProductServiceFeeTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
