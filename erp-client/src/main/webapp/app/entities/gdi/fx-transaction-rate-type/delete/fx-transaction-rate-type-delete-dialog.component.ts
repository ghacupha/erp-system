import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFxTransactionRateType } from '../fx-transaction-rate-type.model';
import { FxTransactionRateTypeService } from '../service/fx-transaction-rate-type.service';

@Component({
  templateUrl: './fx-transaction-rate-type-delete-dialog.component.html',
})
export class FxTransactionRateTypeDeleteDialogComponent {
  fxTransactionRateType?: IFxTransactionRateType;

  constructor(protected fxTransactionRateTypeService: FxTransactionRateTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fxTransactionRateTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
