import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbFileTransmissionStatus } from '../crb-file-transmission-status.model';
import { CrbFileTransmissionStatusService } from '../service/crb-file-transmission-status.service';

@Component({
  templateUrl: './crb-file-transmission-status-delete-dialog.component.html',
})
export class CrbFileTransmissionStatusDeleteDialogComponent {
  crbFileTransmissionStatus?: ICrbFileTransmissionStatus;

  constructor(protected crbFileTransmissionStatusService: CrbFileTransmissionStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbFileTransmissionStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
