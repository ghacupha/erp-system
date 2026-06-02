import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInterbankSectorCode } from '../interbank-sector-code.model';
import { InterbankSectorCodeService } from '../service/interbank-sector-code.service';

@Component({
  templateUrl: './interbank-sector-code-delete-dialog.component.html',
})
export class InterbankSectorCodeDeleteDialogComponent {
  interbankSectorCode?: IInterbankSectorCode;

  constructor(protected interbankSectorCodeService: InterbankSectorCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.interbankSectorCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
