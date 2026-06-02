import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFxReceiptPurposeType } from '../fx-receipt-purpose-type.model';
import { FxReceiptPurposeTypeService } from '../service/fx-receipt-purpose-type.service';

@Component({
  templateUrl: './fx-receipt-purpose-type-delete-dialog.component.html',
})
export class FxReceiptPurposeTypeDeleteDialogComponent {
  fxReceiptPurposeType?: IFxReceiptPurposeType;

  constructor(protected fxReceiptPurposeTypeService: FxReceiptPurposeTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fxReceiptPurposeTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
