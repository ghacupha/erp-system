import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITAAmortizationRule } from '../ta-amortization-rule.model';
import { TAAmortizationRuleService } from '../service/ta-amortization-rule.service';

@Component({
  templateUrl: './ta-amortization-rule-delete-dialog.component.html',
})
export class TAAmortizationRuleDeleteDialogComponent {
  tAAmortizationRule?: ITAAmortizationRule;

  constructor(protected tAAmortizationRuleService: TAAmortizationRuleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tAAmortizationRuleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
