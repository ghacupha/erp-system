import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbGlCode } from '../crb-gl-code.model';
import { CrbGlCodeService } from '../service/crb-gl-code.service';

@Component({
  templateUrl: './crb-gl-code-delete-dialog.component.html',
})
export class CrbGlCodeDeleteDialogComponent {
  crbGlCode?: ICrbGlCode;

  constructor(protected crbGlCodeService: CrbGlCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbGlCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
