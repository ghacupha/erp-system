import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFiscalQuarter } from '../fiscal-quarter.model';
import { FiscalQuarterService } from '../service/fiscal-quarter.service';

@Component({
  templateUrl: './fiscal-quarter-delete-dialog.component.html',
})
export class FiscalQuarterDeleteDialogComponent {
  fiscalQuarter?: IFiscalQuarter;

  constructor(protected fiscalQuarterService: FiscalQuarterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fiscalQuarterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
