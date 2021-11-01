import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDealer } from '../dealer.model';
import { DealerService } from '../service/dealer.service';

@Component({
  templateUrl: './dealer-delete-dialog.component.html',
})
export class DealerDeleteDialogComponent {
  dealer?: IDealer;

  constructor(protected dealerService: DealerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dealerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
