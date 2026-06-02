import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWeeklyCashHolding } from '../weekly-cash-holding.model';
import { WeeklyCashHoldingService } from '../service/weekly-cash-holding.service';

@Component({
  templateUrl: './weekly-cash-holding-delete-dialog.component.html',
})
export class WeeklyCashHoldingDeleteDialogComponent {
  weeklyCashHolding?: IWeeklyCashHolding;

  constructor(protected weeklyCashHoldingService: WeeklyCashHoldingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.weeklyCashHoldingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
