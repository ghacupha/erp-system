import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICurrencyAuthenticityFlag } from '../currency-authenticity-flag.model';
import { CurrencyAuthenticityFlagService } from '../service/currency-authenticity-flag.service';

@Component({
  templateUrl: './currency-authenticity-flag-delete-dialog.component.html',
})
export class CurrencyAuthenticityFlagDeleteDialogComponent {
  currencyAuthenticityFlag?: ICurrencyAuthenticityFlag;

  constructor(protected currencyAuthenticityFlagService: CurrencyAuthenticityFlagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.currencyAuthenticityFlagService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
