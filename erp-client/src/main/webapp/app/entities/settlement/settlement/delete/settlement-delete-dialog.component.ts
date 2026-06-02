import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISettlement } from '../settlement.model';
import { SettlementService } from '../service/settlement.service';

@Component({
  templateUrl: './settlement-delete-dialog.component.html',
})
export class SettlementDeleteDialogComponent {
  settlement?: ISettlement;

  constructor(protected settlementService: SettlementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.settlementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
