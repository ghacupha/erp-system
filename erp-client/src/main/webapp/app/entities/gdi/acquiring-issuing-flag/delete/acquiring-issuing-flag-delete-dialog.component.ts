import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAcquiringIssuingFlag } from '../acquiring-issuing-flag.model';
import { AcquiringIssuingFlagService } from '../service/acquiring-issuing-flag.service';

@Component({
  templateUrl: './acquiring-issuing-flag-delete-dialog.component.html',
})
export class AcquiringIssuingFlagDeleteDialogComponent {
  acquiringIssuingFlag?: IAcquiringIssuingFlag;

  constructor(protected acquiringIssuingFlagService: AcquiringIssuingFlagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.acquiringIssuingFlagService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
