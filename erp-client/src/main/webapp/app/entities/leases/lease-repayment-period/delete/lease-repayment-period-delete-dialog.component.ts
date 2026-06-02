import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaseRepaymentPeriod } from '../lease-repayment-period.model';
import { LeaseRepaymentPeriodService } from '../service/lease-repayment-period.service';

@Component({
  templateUrl: './lease-repayment-period-delete-dialog.component.html',
})
export class LeaseRepaymentPeriodDeleteDialogComponent {
  leaseRepaymentPeriod?: ILeaseRepaymentPeriod;

  constructor(protected leaseRepaymentPeriodService: LeaseRepaymentPeriodService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaseRepaymentPeriodService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
