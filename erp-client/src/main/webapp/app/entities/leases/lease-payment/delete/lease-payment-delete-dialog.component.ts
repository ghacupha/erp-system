import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeasePayment } from '../lease-payment.model';
import { LeasePaymentService } from '../service/lease-payment.service';

@Component({
  templateUrl: './lease-payment-delete-dialog.component.html',
})
export class LeasePaymentDeleteDialogComponent {
  leasePayment?: ILeasePayment;

  constructor(protected leasePaymentService: LeasePaymentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leasePaymentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
