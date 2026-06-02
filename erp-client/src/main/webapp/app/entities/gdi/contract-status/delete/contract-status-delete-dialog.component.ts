import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContractStatus } from '../contract-status.model';
import { ContractStatusService } from '../service/contract-status.service';

@Component({
  templateUrl: './contract-status-delete-dialog.component.html',
})
export class ContractStatusDeleteDialogComponent {
  contractStatus?: IContractStatus;

  constructor(protected contractStatusService: ContractStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
