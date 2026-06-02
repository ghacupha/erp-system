import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExchangeRate } from '../exchange-rate.model';
import { ExchangeRateService } from '../service/exchange-rate.service';

@Component({
  templateUrl: './exchange-rate-delete-dialog.component.html',
})
export class ExchangeRateDeleteDialogComponent {
  exchangeRate?: IExchangeRate;

  constructor(protected exchangeRateService: ExchangeRateService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.exchangeRateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
