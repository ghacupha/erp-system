import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbComplaintType } from '../crb-complaint-type.model';
import { CrbComplaintTypeService } from '../service/crb-complaint-type.service';

@Component({
  templateUrl: './crb-complaint-type-delete-dialog.component.html',
})
export class CrbComplaintTypeDeleteDialogComponent {
  crbComplaintType?: ICrbComplaintType;

  constructor(protected crbComplaintTypeService: CrbComplaintTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbComplaintTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
