import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnticipatedMaturityPeriood } from '../anticipated-maturity-periood.model';
import { AnticipatedMaturityPerioodService } from '../service/anticipated-maturity-periood.service';

@Component({
  templateUrl: './anticipated-maturity-periood-delete-dialog.component.html',
})
export class AnticipatedMaturityPerioodDeleteDialogComponent {
  anticipatedMaturityPeriood?: IAnticipatedMaturityPeriood;

  constructor(protected anticipatedMaturityPerioodService: AnticipatedMaturityPerioodService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.anticipatedMaturityPerioodService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
