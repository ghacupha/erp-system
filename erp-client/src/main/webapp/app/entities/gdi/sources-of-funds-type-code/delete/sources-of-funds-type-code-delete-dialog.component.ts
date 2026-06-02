import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISourcesOfFundsTypeCode } from '../sources-of-funds-type-code.model';
import { SourcesOfFundsTypeCodeService } from '../service/sources-of-funds-type-code.service';

@Component({
  templateUrl: './sources-of-funds-type-code-delete-dialog.component.html',
})
export class SourcesOfFundsTypeCodeDeleteDialogComponent {
  sourcesOfFundsTypeCode?: ISourcesOfFundsTypeCode;

  constructor(protected sourcesOfFundsTypeCodeService: SourcesOfFundsTypeCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sourcesOfFundsTypeCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
