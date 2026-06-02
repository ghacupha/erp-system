import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOutletStatus } from '../outlet-status.model';
import { OutletStatusService } from '../service/outlet-status.service';

@Component({
  templateUrl: './outlet-status-delete-dialog.component.html',
})
export class OutletStatusDeleteDialogComponent {
  outletStatus?: IOutletStatus;

  constructor(protected outletStatusService: OutletStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.outletStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
