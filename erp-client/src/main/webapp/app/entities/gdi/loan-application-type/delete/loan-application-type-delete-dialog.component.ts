import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILoanApplicationType } from '../loan-application-type.model';
import { LoanApplicationTypeService } from '../service/loan-application-type.service';

@Component({
  templateUrl: './loan-application-type-delete-dialog.component.html',
})
export class LoanApplicationTypeDeleteDialogComponent {
  loanApplicationType?: ILoanApplicationType;

  constructor(protected loanApplicationTypeService: LoanApplicationTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.loanApplicationTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
