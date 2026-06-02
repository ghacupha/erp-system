import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITransactionAccountLedger } from '../transaction-account-ledger.model';
import { TransactionAccountLedgerService } from '../service/transaction-account-ledger.service';

@Component({
  templateUrl: './transaction-account-ledger-delete-dialog.component.html',
})
export class TransactionAccountLedgerDeleteDialogComponent {
  transactionAccountLedger?: ITransactionAccountLedger;

  constructor(protected transactionAccountLedgerService: TransactionAccountLedgerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transactionAccountLedgerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
