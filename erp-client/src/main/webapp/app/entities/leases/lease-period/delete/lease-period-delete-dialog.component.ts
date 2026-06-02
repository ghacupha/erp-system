import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeasePeriod } from '../lease-period.model';
import { LeasePeriodService } from '../service/lease-period.service';

@Component({
  templateUrl: './lease-period-delete-dialog.component.html',
})
export class LeasePeriodDeleteDialogComponent {
  leasePeriod?: ILeasePeriod;

  constructor(protected leasePeriodService: LeasePeriodService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leasePeriodService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
