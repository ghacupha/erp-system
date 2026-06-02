import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICreditCardFacility } from '../credit-card-facility.model';
import { CreditCardFacilityService } from '../service/credit-card-facility.service';

@Component({
  templateUrl: './credit-card-facility-delete-dialog.component.html',
})
export class CreditCardFacilityDeleteDialogComponent {
  creditCardFacility?: ICreditCardFacility;

  constructor(protected creditCardFacilityService: CreditCardFacilityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.creditCardFacilityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
