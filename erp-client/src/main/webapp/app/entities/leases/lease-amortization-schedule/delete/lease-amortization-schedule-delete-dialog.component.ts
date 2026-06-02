import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaseAmortizationSchedule } from '../lease-amortization-schedule.model';
import { LeaseAmortizationScheduleService } from '../service/lease-amortization-schedule.service';

@Component({
  templateUrl: './lease-amortization-schedule-delete-dialog.component.html',
})
export class LeaseAmortizationScheduleDeleteDialogComponent {
  leaseAmortizationSchedule?: ILeaseAmortizationSchedule;

  constructor(protected leaseAmortizationScheduleService: LeaseAmortizationScheduleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaseAmortizationScheduleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
