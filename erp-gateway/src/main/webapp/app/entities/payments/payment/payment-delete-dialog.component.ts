import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPayment } from 'app/shared/model/payments/payment.model';
import { PaymentService } from './payment.service';

@Component({
  templateUrl: './payment-delete-dialog.component.html',
})
export class PaymentDeleteDialogComponent {
  payment?: IPayment;

  constructor(protected paymentService: PaymentService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('paymentListModification');
      this.activeModal.close();
    });
  }
}
