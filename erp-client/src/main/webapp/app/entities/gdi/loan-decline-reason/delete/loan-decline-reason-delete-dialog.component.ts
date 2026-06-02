import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILoanDeclineReason } from '../loan-decline-reason.model';
import { LoanDeclineReasonService } from '../service/loan-decline-reason.service';

@Component({
  templateUrl: './loan-decline-reason-delete-dialog.component.html',
})
export class LoanDeclineReasonDeleteDialogComponent {
  loanDeclineReason?: ILoanDeclineReason;

  constructor(protected loanDeclineReasonService: LoanDeclineReasonService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.loanDeclineReasonService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
