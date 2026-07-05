import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGdiTransactionDataIndex } from '../gdi-transaction-data-index.model';
import { GdiTransactionDataIndexService } from '../service/gdi-transaction-data-index.service';

@Component({
  templateUrl: './gdi-transaction-data-index-delete-dialog.component.html',
})
export class GdiTransactionDataIndexDeleteDialogComponent {
  gdiTransactionDataIndex?: IGdiTransactionDataIndex;

  constructor(protected gdiTransactionDataIndexService: GdiTransactionDataIndexService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gdiTransactionDataIndexService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
