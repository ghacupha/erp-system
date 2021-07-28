import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPaymentRequisition } from 'app/shared/model/payments/payment-requisition.model';
import { PaymentRequisitionService } from './payment-requisition.service';

@Component({
  templateUrl: './payment-requisition-delete-dialog.component.html',
})
export class PaymentRequisitionDeleteDialogComponent {
  paymentRequisition?: IPaymentRequisition;

  constructor(
    protected paymentRequisitionService: PaymentRequisitionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentRequisitionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('paymentRequisitionListModification');
      this.activeModal.close();
    });
  }
}
