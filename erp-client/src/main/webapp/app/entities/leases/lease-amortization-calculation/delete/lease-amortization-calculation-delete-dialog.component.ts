import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaseAmortizationCalculation } from '../lease-amortization-calculation.model';
import { LeaseAmortizationCalculationService } from '../service/lease-amortization-calculation.service';

@Component({
  templateUrl: './lease-amortization-calculation-delete-dialog.component.html',
})
export class LeaseAmortizationCalculationDeleteDialogComponent {
  leaseAmortizationCalculation?: ILeaseAmortizationCalculation;

  constructor(protected leaseAmortizationCalculationService: LeaseAmortizationCalculationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaseAmortizationCalculationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
