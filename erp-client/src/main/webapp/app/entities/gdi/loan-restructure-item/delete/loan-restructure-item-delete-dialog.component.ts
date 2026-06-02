import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILoanRestructureItem } from '../loan-restructure-item.model';
import { LoanRestructureItemService } from '../service/loan-restructure-item.service';

@Component({
  templateUrl: './loan-restructure-item-delete-dialog.component.html',
})
export class LoanRestructureItemDeleteDialogComponent {
  loanRestructureItem?: ILoanRestructureItem;

  constructor(protected loanRestructureItemService: LoanRestructureItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.loanRestructureItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
