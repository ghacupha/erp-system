import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILoanRepaymentFrequency } from '../loan-repayment-frequency.model';
import { LoanRepaymentFrequencyService } from '../service/loan-repayment-frequency.service';

@Component({
  templateUrl: './loan-repayment-frequency-delete-dialog.component.html',
})
export class LoanRepaymentFrequencyDeleteDialogComponent {
  loanRepaymentFrequency?: ILoanRepaymentFrequency;

  constructor(protected loanRepaymentFrequencyService: LoanRepaymentFrequencyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.loanRepaymentFrequencyService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
