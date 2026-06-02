import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISignedPayment } from '../signed-payment.model';
import { SignedPaymentService } from '../service/signed-payment.service';

@Component({
  templateUrl: './signed-payment-delete-dialog.component.html',
})
export class SignedPaymentDeleteDialogComponent {
  signedPayment?: ISignedPayment;

  constructor(protected signedPaymentService: SignedPaymentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.signedPaymentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
