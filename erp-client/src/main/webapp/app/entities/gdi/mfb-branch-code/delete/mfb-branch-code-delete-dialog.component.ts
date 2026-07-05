import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMfbBranchCode } from '../mfb-branch-code.model';
import { MfbBranchCodeService } from '../service/mfb-branch-code.service';

@Component({
  templateUrl: './mfb-branch-code-delete-dialog.component.html',
})
export class MfbBranchCodeDeleteDialogComponent {
  mfbBranchCode?: IMfbBranchCode;

  constructor(protected mfbBranchCodeService: MfbBranchCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mfbBranchCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
