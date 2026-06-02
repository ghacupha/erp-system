import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBankBranchCode } from '../bank-branch-code.model';
import { BankBranchCodeService } from '../service/bank-branch-code.service';

@Component({
  templateUrl: './bank-branch-code-delete-dialog.component.html',
})
export class BankBranchCodeDeleteDialogComponent {
  bankBranchCode?: IBankBranchCode;

  constructor(protected bankBranchCodeService: BankBranchCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bankBranchCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
