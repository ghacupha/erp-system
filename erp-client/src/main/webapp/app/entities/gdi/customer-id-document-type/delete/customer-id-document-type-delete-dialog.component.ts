import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomerIDDocumentType } from '../customer-id-document-type.model';
import { CustomerIDDocumentTypeService } from '../service/customer-id-document-type.service';

@Component({
  templateUrl: './customer-id-document-type-delete-dialog.component.html',
})
export class CustomerIDDocumentTypeDeleteDialogComponent {
  customerIDDocumentType?: ICustomerIDDocumentType;

  constructor(protected customerIDDocumentTypeService: CustomerIDDocumentTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customerIDDocumentTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
