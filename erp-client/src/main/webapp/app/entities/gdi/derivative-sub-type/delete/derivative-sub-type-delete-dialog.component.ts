import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDerivativeSubType } from '../derivative-sub-type.model';
import { DerivativeSubTypeService } from '../service/derivative-sub-type.service';

@Component({
  templateUrl: './derivative-sub-type-delete-dialog.component.html',
})
export class DerivativeSubTypeDeleteDialogComponent {
  derivativeSubType?: IDerivativeSubType;

  constructor(protected derivativeSubTypeService: DerivativeSubTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.derivativeSubTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
