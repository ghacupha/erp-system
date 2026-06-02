import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFxCustomerType } from '../fx-customer-type.model';
import { FxCustomerTypeService } from '../service/fx-customer-type.service';

@Component({
  templateUrl: './fx-customer-type-delete-dialog.component.html',
})
export class FxCustomerTypeDeleteDialogComponent {
  fxCustomerType?: IFxCustomerType;

  constructor(protected fxCustomerTypeService: FxCustomerTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fxCustomerTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
