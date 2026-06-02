import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomerType } from '../customer-type.model';
import { CustomerTypeService } from '../service/customer-type.service';

@Component({
  templateUrl: './customer-type-delete-dialog.component.html',
})
export class CustomerTypeDeleteDialogComponent {
  customerType?: ICustomerType;

  constructor(protected customerTypeService: CustomerTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customerTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
