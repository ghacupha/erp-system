import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrepaymentAmortization } from '../prepayment-amortization.model';
import { PrepaymentAmortizationService } from '../service/prepayment-amortization.service';

@Component({
  templateUrl: './prepayment-amortization-delete-dialog.component.html',
})
export class PrepaymentAmortizationDeleteDialogComponent {
  prepaymentAmortization?: IPrepaymentAmortization;

  constructor(protected prepaymentAmortizationService: PrepaymentAmortizationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prepaymentAmortizationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
