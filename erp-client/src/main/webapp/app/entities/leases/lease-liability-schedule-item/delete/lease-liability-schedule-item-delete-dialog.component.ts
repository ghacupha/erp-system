import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaseLiabilityScheduleItem } from '../lease-liability-schedule-item.model';
import { LeaseLiabilityScheduleItemService } from '../service/lease-liability-schedule-item.service';

@Component({
  templateUrl: './lease-liability-schedule-item-delete-dialog.component.html',
})
export class LeaseLiabilityScheduleItemDeleteDialogComponent {
  leaseLiabilityScheduleItem?: ILeaseLiabilityScheduleItem;

  constructor(protected leaseLiabilityScheduleItemService: LeaseLiabilityScheduleItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaseLiabilityScheduleItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
