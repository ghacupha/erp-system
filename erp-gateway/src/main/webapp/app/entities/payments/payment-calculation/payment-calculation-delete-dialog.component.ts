import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPaymentCalculation } from 'app/shared/model/payments/payment-calculation.model';
import { PaymentCalculationService } from './payment-calculation.service';

@Component({
  templateUrl: './payment-calculation-delete-dialog.component.html',
})
export class PaymentCalculationDeleteDialogComponent {
  paymentCalculation?: IPaymentCalculation;

  constructor(
    protected paymentCalculationService: PaymentCalculationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentCalculationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('paymentCalculationListModification');
      this.activeModal.close();
    });
  }
}
