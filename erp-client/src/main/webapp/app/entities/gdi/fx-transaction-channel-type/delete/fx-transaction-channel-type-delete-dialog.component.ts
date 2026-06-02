import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFxTransactionChannelType } from '../fx-transaction-channel-type.model';
import { FxTransactionChannelTypeService } from '../service/fx-transaction-channel-type.service';

@Component({
  templateUrl: './fx-transaction-channel-type-delete-dialog.component.html',
})
export class FxTransactionChannelTypeDeleteDialogComponent {
  fxTransactionChannelType?: IFxTransactionChannelType;

  constructor(protected fxTransactionChannelTypeService: FxTransactionChannelTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fxTransactionChannelTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
