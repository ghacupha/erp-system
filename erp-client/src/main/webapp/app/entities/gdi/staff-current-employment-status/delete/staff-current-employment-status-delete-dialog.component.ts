import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStaffCurrentEmploymentStatus } from '../staff-current-employment-status.model';
import { StaffCurrentEmploymentStatusService } from '../service/staff-current-employment-status.service';

@Component({
  templateUrl: './staff-current-employment-status-delete-dialog.component.html',
})
export class StaffCurrentEmploymentStatusDeleteDialogComponent {
  staffCurrentEmploymentStatus?: IStaffCurrentEmploymentStatus;

  constructor(protected staffCurrentEmploymentStatusService: StaffCurrentEmploymentStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.staffCurrentEmploymentStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
