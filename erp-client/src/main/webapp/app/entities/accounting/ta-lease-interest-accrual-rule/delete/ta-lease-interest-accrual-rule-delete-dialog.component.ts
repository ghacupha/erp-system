import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITALeaseInterestAccrualRule } from '../ta-lease-interest-accrual-rule.model';
import { TALeaseInterestAccrualRuleService } from '../service/ta-lease-interest-accrual-rule.service';

@Component({
  templateUrl: './ta-lease-interest-accrual-rule-delete-dialog.component.html',
})
export class TALeaseInterestAccrualRuleDeleteDialogComponent {
  tALeaseInterestAccrualRule?: ITALeaseInterestAccrualRule;

  constructor(protected tALeaseInterestAccrualRuleService: TALeaseInterestAccrualRuleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tALeaseInterestAccrualRuleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
