import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILegalStatus } from '../legal-status.model';
import { LegalStatusService } from '../service/legal-status.service';

@Component({
  templateUrl: './legal-status-delete-dialog.component.html',
})
export class LegalStatusDeleteDialogComponent {
  legalStatus?: ILegalStatus;

  constructor(protected legalStatusService: LegalStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.legalStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
