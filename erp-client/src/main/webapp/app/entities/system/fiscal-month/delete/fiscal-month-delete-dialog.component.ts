import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFiscalMonth } from '../fiscal-month.model';
import { FiscalMonthService } from '../service/fiscal-month.service';

@Component({
  templateUrl: './fiscal-month-delete-dialog.component.html',
})
export class FiscalMonthDeleteDialogComponent {
  fiscalMonth?: IFiscalMonth;

  constructor(protected fiscalMonthService: FiscalMonthService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fiscalMonthService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
