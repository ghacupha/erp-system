import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILoanProductType } from '../loan-product-type.model';
import { LoanProductTypeService } from '../service/loan-product-type.service';

@Component({
  templateUrl: './loan-product-type-delete-dialog.component.html',
})
export class LoanProductTypeDeleteDialogComponent {
  loanProductType?: ILoanProductType;

  constructor(protected loanProductTypeService: LoanProductTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.loanProductTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
