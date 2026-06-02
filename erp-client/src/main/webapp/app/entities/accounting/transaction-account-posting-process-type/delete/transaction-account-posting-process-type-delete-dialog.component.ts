import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITransactionAccountPostingProcessType } from '../transaction-account-posting-process-type.model';
import { TransactionAccountPostingProcessTypeService } from '../service/transaction-account-posting-process-type.service';

@Component({
  templateUrl: './transaction-account-posting-process-type-delete-dialog.component.html',
})
export class TransactionAccountPostingProcessTypeDeleteDialogComponent {
  transactionAccountPostingProcessType?: ITransactionAccountPostingProcessType;

  constructor(
    protected transactionAccountPostingProcessTypeService: TransactionAccountPostingProcessTypeService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transactionAccountPostingProcessTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
