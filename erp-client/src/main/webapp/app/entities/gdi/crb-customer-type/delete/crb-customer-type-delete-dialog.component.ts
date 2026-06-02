import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbCustomerType } from '../crb-customer-type.model';
import { CrbCustomerTypeService } from '../service/crb-customer-type.service';

@Component({
  templateUrl: './crb-customer-type-delete-dialog.component.html',
})
export class CrbCustomerTypeDeleteDialogComponent {
  crbCustomerType?: ICrbCustomerType;

  constructor(protected crbCustomerTypeService: CrbCustomerTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbCustomerTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
