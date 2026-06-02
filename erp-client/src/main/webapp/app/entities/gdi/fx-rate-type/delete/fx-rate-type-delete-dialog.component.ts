import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFxRateType } from '../fx-rate-type.model';
import { FxRateTypeService } from '../service/fx-rate-type.service';

@Component({
  templateUrl: './fx-rate-type-delete-dialog.component.html',
})
export class FxRateTypeDeleteDialogComponent {
  fxRateType?: IFxRateType;

  constructor(protected fxRateTypeService: FxRateTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fxRateTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
