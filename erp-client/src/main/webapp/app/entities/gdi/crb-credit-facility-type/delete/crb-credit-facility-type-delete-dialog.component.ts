import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbCreditFacilityType } from '../crb-credit-facility-type.model';
import { CrbCreditFacilityTypeService } from '../service/crb-credit-facility-type.service';

@Component({
  templateUrl: './crb-credit-facility-type-delete-dialog.component.html',
})
export class CrbCreditFacilityTypeDeleteDialogComponent {
  crbCreditFacilityType?: ICrbCreditFacilityType;

  constructor(protected crbCreditFacilityTypeService: CrbCreditFacilityTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbCreditFacilityTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
