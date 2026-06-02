import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbCreditApplicationStatus } from '../crb-credit-application-status.model';
import { CrbCreditApplicationStatusService } from '../service/crb-credit-application-status.service';

@Component({
  templateUrl: './crb-credit-application-status-delete-dialog.component.html',
})
export class CrbCreditApplicationStatusDeleteDialogComponent {
  crbCreditApplicationStatus?: ICrbCreditApplicationStatus;

  constructor(protected crbCreditApplicationStatusService: CrbCreditApplicationStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbCreditApplicationStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
