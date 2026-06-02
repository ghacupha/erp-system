import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INatureOfCustomerComplaints } from '../nature-of-customer-complaints.model';
import { NatureOfCustomerComplaintsService } from '../service/nature-of-customer-complaints.service';

@Component({
  templateUrl: './nature-of-customer-complaints-delete-dialog.component.html',
})
export class NatureOfCustomerComplaintsDeleteDialogComponent {
  natureOfCustomerComplaints?: INatureOfCustomerComplaints;

  constructor(protected natureOfCustomerComplaintsService: NatureOfCustomerComplaintsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.natureOfCustomerComplaintsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
