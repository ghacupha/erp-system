import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbComplaintStatusType } from '../crb-complaint-status-type.model';
import { CrbComplaintStatusTypeService } from '../service/crb-complaint-status-type.service';

@Component({
  templateUrl: './crb-complaint-status-type-delete-dialog.component.html',
})
export class CrbComplaintStatusTypeDeleteDialogComponent {
  crbComplaintStatusType?: ICrbComplaintStatusType;

  constructor(protected crbComplaintStatusTypeService: CrbComplaintStatusTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbComplaintStatusTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
