import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICardAcquiringTransaction } from '../card-acquiring-transaction.model';
import { CardAcquiringTransactionService } from '../service/card-acquiring-transaction.service';

@Component({
  templateUrl: './card-acquiring-transaction-delete-dialog.component.html',
})
export class CardAcquiringTransactionDeleteDialogComponent {
  cardAcquiringTransaction?: ICardAcquiringTransaction;

  constructor(protected cardAcquiringTransactionService: CardAcquiringTransactionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cardAcquiringTransactionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
