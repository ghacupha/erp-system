import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbAccountStatus } from '../crb-account-status.model';
import { CrbAccountStatusService } from '../service/crb-account-status.service';

@Component({
  templateUrl: './crb-account-status-delete-dialog.component.html',
})
export class CrbAccountStatusDeleteDialogComponent {
  crbAccountStatus?: ICrbAccountStatus;

  constructor(protected crbAccountStatusService: CrbAccountStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbAccountStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
