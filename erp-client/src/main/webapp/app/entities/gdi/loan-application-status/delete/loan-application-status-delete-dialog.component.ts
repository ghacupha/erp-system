import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILoanApplicationStatus } from '../loan-application-status.model';
import { LoanApplicationStatusService } from '../service/loan-application-status.service';

@Component({
  templateUrl: './loan-application-status-delete-dialog.component.html',
})
export class LoanApplicationStatusDeleteDialogComponent {
  loanApplicationStatus?: ILoanApplicationStatus;

  constructor(protected loanApplicationStatusService: LoanApplicationStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.loanApplicationStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
