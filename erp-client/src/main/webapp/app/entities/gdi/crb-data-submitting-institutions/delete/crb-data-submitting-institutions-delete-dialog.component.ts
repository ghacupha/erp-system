import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbDataSubmittingInstitutions } from '../crb-data-submitting-institutions.model';
import { CrbDataSubmittingInstitutionsService } from '../service/crb-data-submitting-institutions.service';

@Component({
  templateUrl: './crb-data-submitting-institutions-delete-dialog.component.html',
})
export class CrbDataSubmittingInstitutionsDeleteDialogComponent {
  crbDataSubmittingInstitutions?: ICrbDataSubmittingInstitutions;

  constructor(
    protected crbDataSubmittingInstitutionsService: CrbDataSubmittingInstitutionsService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbDataSubmittingInstitutionsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
