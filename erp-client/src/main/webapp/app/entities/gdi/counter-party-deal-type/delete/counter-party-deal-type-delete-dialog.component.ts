import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICounterPartyDealType } from '../counter-party-deal-type.model';
import { CounterPartyDealTypeService } from '../service/counter-party-deal-type.service';

@Component({
  templateUrl: './counter-party-deal-type-delete-dialog.component.html',
})
export class CounterPartyDealTypeDeleteDialogComponent {
  counterPartyDealType?: ICounterPartyDealType;

  constructor(protected counterPartyDealTypeService: CounterPartyDealTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.counterPartyDealTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
