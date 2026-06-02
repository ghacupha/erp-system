import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITARecognitionROURule } from '../ta-recognition-rou-rule.model';
import { TARecognitionROURuleService } from '../service/ta-recognition-rou-rule.service';

@Component({
  templateUrl: './ta-recognition-rou-rule-delete-dialog.component.html',
})
export class TARecognitionROURuleDeleteDialogComponent {
  tARecognitionROURule?: ITARecognitionROURule;

  constructor(protected tARecognitionROURuleService: TARecognitionROURuleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tARecognitionROURuleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
