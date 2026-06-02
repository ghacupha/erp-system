import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILoanRestructureFlag } from '../loan-restructure-flag.model';
import { LoanRestructureFlagService } from '../service/loan-restructure-flag.service';

@Component({
  templateUrl: './loan-restructure-flag-delete-dialog.component.html',
})
export class LoanRestructureFlagDeleteDialogComponent {
  loanRestructureFlag?: ILoanRestructureFlag;

  constructor(protected loanRestructureFlagService: LoanRestructureFlagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.loanRestructureFlagService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
