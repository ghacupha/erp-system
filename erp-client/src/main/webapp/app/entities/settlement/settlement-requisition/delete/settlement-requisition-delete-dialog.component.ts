import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISettlementRequisition } from '../settlement-requisition.model';
import { SettlementRequisitionService } from '../service/settlement-requisition.service';

@Component({
  templateUrl: './settlement-requisition-delete-dialog.component.html',
})
export class SettlementRequisitionDeleteDialogComponent {
  settlementRequisition?: ISettlementRequisition;

  constructor(protected settlementRequisitionService: SettlementRequisitionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.settlementRequisitionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
