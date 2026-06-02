import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkInProgressTransfer } from '../work-in-progress-transfer.model';
import { WorkInProgressTransferService } from '../service/work-in-progress-transfer.service';

@Component({
  templateUrl: './work-in-progress-transfer-delete-dialog.component.html',
})
export class WorkInProgressTransferDeleteDialogComponent {
  workInProgressTransfer?: IWorkInProgressTransfer;

  constructor(protected workInProgressTransferService: WorkInProgressTransferService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workInProgressTransferService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
