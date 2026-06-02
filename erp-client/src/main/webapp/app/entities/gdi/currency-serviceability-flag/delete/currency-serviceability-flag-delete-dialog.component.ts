import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICurrencyServiceabilityFlag } from '../currency-serviceability-flag.model';
import { CurrencyServiceabilityFlagService } from '../service/currency-serviceability-flag.service';

@Component({
  templateUrl: './currency-serviceability-flag-delete-dialog.component.html',
})
export class CurrencyServiceabilityFlagDeleteDialogComponent {
  currencyServiceabilityFlag?: ICurrencyServiceabilityFlag;

  constructor(protected currencyServiceabilityFlagService: CurrencyServiceabilityFlagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.currencyServiceabilityFlagService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
