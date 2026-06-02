import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITALeaseRecognitionRule } from '../ta-lease-recognition-rule.model';
import { TALeaseRecognitionRuleService } from '../service/ta-lease-recognition-rule.service';

@Component({
  templateUrl: './ta-lease-recognition-rule-delete-dialog.component.html',
})
export class TALeaseRecognitionRuleDeleteDialogComponent {
  tALeaseRecognitionRule?: ITALeaseRecognitionRule;

  constructor(protected tALeaseRecognitionRuleService: TALeaseRecognitionRuleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tALeaseRecognitionRuleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
