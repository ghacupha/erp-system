import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from '../service/ifrs-16-lease-contract.service';

@Component({
  templateUrl: './ifrs-16-lease-contract-delete-dialog.component.html',
})
export class IFRS16LeaseContractDeleteDialogComponent {
  iFRS16LeaseContract?: IIFRS16LeaseContract;

  constructor(protected iFRS16LeaseContractService: IFRS16LeaseContractService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.iFRS16LeaseContractService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
