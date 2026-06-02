import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaseLiability } from '../lease-liability.model';
import { LeaseLiabilityService } from '../service/lease-liability.service';

@Component({
  templateUrl: './lease-liability-delete-dialog.component.html',
})
export class LeaseLiabilityDeleteDialogComponent {
  leaseLiability?: ILeaseLiability;

  constructor(protected leaseLiabilityService: LeaseLiabilityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaseLiabilityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
