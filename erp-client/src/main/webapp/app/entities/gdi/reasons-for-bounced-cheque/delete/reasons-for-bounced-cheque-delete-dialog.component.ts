import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReasonsForBouncedCheque } from '../reasons-for-bounced-cheque.model';
import { ReasonsForBouncedChequeService } from '../service/reasons-for-bounced-cheque.service';

@Component({
  templateUrl: './reasons-for-bounced-cheque-delete-dialog.component.html',
})
export class ReasonsForBouncedChequeDeleteDialogComponent {
  reasonsForBouncedCheque?: IReasonsForBouncedCheque;

  constructor(protected reasonsForBouncedChequeService: ReasonsForBouncedChequeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reasonsForBouncedChequeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
