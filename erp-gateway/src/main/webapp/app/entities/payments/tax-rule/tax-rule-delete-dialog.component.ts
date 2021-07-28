import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITaxRule } from 'app/shared/model/payments/tax-rule.model';
import { TaxRuleService } from './tax-rule.service';

@Component({
  templateUrl: './tax-rule-delete-dialog.component.html',
})
export class TaxRuleDeleteDialogComponent {
  taxRule?: ITaxRule;

  constructor(protected taxRuleService: TaxRuleService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.taxRuleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('taxRuleListModification');
      this.activeModal.close();
    });
  }
}
