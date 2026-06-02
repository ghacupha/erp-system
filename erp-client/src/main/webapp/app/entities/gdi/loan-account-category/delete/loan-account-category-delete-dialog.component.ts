import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILoanAccountCategory } from '../loan-account-category.model';
import { LoanAccountCategoryService } from '../service/loan-account-category.service';

@Component({
  templateUrl: './loan-account-category-delete-dialog.component.html',
})
export class LoanAccountCategoryDeleteDialogComponent {
  loanAccountCategory?: ILoanAccountCategory;

  constructor(protected loanAccountCategoryService: LoanAccountCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.loanAccountCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
