import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomerComplaintStatusType } from '../customer-complaint-status-type.model';
import { CustomerComplaintStatusTypeService } from '../service/customer-complaint-status-type.service';

@Component({
  templateUrl: './customer-complaint-status-type-delete-dialog.component.html',
})
export class CustomerComplaintStatusTypeDeleteDialogComponent {
  customerComplaintStatusType?: ICustomerComplaintStatusType;

  constructor(protected customerComplaintStatusTypeService: CustomerComplaintStatusTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customerComplaintStatusTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
