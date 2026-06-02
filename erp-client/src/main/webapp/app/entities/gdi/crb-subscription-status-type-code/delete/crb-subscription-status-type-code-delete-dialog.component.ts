import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbSubscriptionStatusTypeCode } from '../crb-subscription-status-type-code.model';
import { CrbSubscriptionStatusTypeCodeService } from '../service/crb-subscription-status-type-code.service';

@Component({
  templateUrl: './crb-subscription-status-type-code-delete-dialog.component.html',
})
export class CrbSubscriptionStatusTypeCodeDeleteDialogComponent {
  crbSubscriptionStatusTypeCode?: ICrbSubscriptionStatusTypeCode;

  constructor(
    protected crbSubscriptionStatusTypeCodeService: CrbSubscriptionStatusTypeCodeService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbSubscriptionStatusTypeCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
