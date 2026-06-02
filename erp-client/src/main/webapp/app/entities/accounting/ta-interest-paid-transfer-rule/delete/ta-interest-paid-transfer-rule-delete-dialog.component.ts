import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITAInterestPaidTransferRule } from '../ta-interest-paid-transfer-rule.model';
import { TAInterestPaidTransferRuleService } from '../service/ta-interest-paid-transfer-rule.service';

@Component({
  templateUrl: './ta-interest-paid-transfer-rule-delete-dialog.component.html',
})
export class TAInterestPaidTransferRuleDeleteDialogComponent {
  tAInterestPaidTransferRule?: ITAInterestPaidTransferRule;

  constructor(protected tAInterestPaidTransferRuleService: TAInterestPaidTransferRuleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tAInterestPaidTransferRuleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
