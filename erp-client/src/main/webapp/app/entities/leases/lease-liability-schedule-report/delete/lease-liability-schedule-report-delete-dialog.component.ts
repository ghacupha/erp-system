import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaseLiabilityScheduleReport } from '../lease-liability-schedule-report.model';
import { LeaseLiabilityScheduleReportService } from '../service/lease-liability-schedule-report.service';

@Component({
  templateUrl: './lease-liability-schedule-report-delete-dialog.component.html',
})
export class LeaseLiabilityScheduleReportDeleteDialogComponent {
  leaseLiabilityScheduleReport?: ILeaseLiabilityScheduleReport;

  constructor(protected leaseLiabilityScheduleReportService: LeaseLiabilityScheduleReportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaseLiabilityScheduleReportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
