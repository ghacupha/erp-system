import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITransactionAccountCategory } from '../transaction-account-category.model';
import { TransactionAccountCategoryService } from '../service/transaction-account-category.service';

@Component({
  templateUrl: './transaction-account-category-delete-dialog.component.html',
})
export class TransactionAccountCategoryDeleteDialogComponent {
  transactionAccountCategory?: ITransactionAccountCategory;

  constructor(protected transactionAccountCategoryService: TransactionAccountCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transactionAccountCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
