import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInvoice } from 'app/shared/model/payments/invoice.model';
import { InvoiceService } from './invoice.service';

@Component({
  templateUrl: './invoice-delete-dialog.component.html',
})
export class InvoiceDeleteDialogComponent {
  invoice?: IInvoice;

  constructor(protected invoiceService: InvoiceService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.invoiceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('invoiceListModification');
      this.activeModal.close();
    });
  }
}
