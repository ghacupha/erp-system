import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITALeaseRepaymentRule } from '../ta-lease-repayment-rule.model';
import { TALeaseRepaymentRuleService } from '../service/ta-lease-repayment-rule.service';

@Component({
  templateUrl: './ta-lease-repayment-rule-delete-dialog.component.html',
})
export class TALeaseRepaymentRuleDeleteDialogComponent {
  tALeaseRepaymentRule?: ITALeaseRepaymentRule;

  constructor(protected tALeaseRepaymentRuleService: TALeaseRepaymentRuleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tALeaseRepaymentRuleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
