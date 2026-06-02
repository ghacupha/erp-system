import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFraudType } from '../fraud-type.model';
import { FraudTypeService } from '../service/fraud-type.service';

@Component({
  templateUrl: './fraud-type-delete-dialog.component.html',
})
export class FraudTypeDeleteDialogComponent {
  fraudType?: IFraudType;

  constructor(protected fraudTypeService: FraudTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fraudTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
