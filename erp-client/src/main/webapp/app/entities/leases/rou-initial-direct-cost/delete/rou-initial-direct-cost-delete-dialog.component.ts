import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRouInitialDirectCost } from '../rou-initial-direct-cost.model';
import { RouInitialDirectCostService } from '../service/rou-initial-direct-cost.service';

@Component({
  templateUrl: './rou-initial-direct-cost-delete-dialog.component.html',
})
export class RouInitialDirectCostDeleteDialogComponent {
  rouInitialDirectCost?: IRouInitialDirectCost;

  constructor(protected rouInitialDirectCostService: RouInitialDirectCostService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rouInitialDirectCostService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
