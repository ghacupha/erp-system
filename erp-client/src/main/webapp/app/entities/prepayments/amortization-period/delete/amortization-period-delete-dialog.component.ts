import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAmortizationPeriod } from '../amortization-period.model';
import { AmortizationPeriodService } from '../service/amortization-period.service';

@Component({
  templateUrl: './amortization-period-delete-dialog.component.html',
})
export class AmortizationPeriodDeleteDialogComponent {
  amortizationPeriod?: IAmortizationPeriod;

  constructor(protected amortizationPeriodService: AmortizationPeriodService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.amortizationPeriodService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
