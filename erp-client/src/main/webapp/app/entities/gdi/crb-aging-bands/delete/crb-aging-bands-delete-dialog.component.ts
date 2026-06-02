import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbAgingBands } from '../crb-aging-bands.model';
import { CrbAgingBandsService } from '../service/crb-aging-bands.service';

@Component({
  templateUrl: './crb-aging-bands-delete-dialog.component.html',
})
export class CrbAgingBandsDeleteDialogComponent {
  crbAgingBands?: ICrbAgingBands;

  constructor(protected crbAgingBandsService: CrbAgingBandsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbAgingBandsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
