import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRemittanceFlag } from '../remittance-flag.model';
import { RemittanceFlagService } from '../service/remittance-flag.service';

@Component({
  templateUrl: './remittance-flag-delete-dialog.component.html',
})
export class RemittanceFlagDeleteDialogComponent {
  remittanceFlag?: IRemittanceFlag;

  constructor(protected remittanceFlagService: RemittanceFlagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.remittanceFlagService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
