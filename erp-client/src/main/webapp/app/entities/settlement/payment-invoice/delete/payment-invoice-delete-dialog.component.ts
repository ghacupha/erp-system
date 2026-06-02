import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentInvoice } from '../payment-invoice.model';
import { PaymentInvoiceService } from '../service/payment-invoice.service';

@Component({
  templateUrl: './payment-invoice-delete-dialog.component.html',
})
export class PaymentInvoiceDeleteDialogComponent {
  paymentInvoice?: IPaymentInvoice;

  constructor(protected paymentInvoiceService: PaymentInvoiceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentInvoiceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
