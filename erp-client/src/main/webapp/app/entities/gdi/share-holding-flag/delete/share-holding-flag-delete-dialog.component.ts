import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IShareHoldingFlag } from '../share-holding-flag.model';
import { ShareHoldingFlagService } from '../service/share-holding-flag.service';

@Component({
  templateUrl: './share-holding-flag-delete-dialog.component.html',
})
export class ShareHoldingFlagDeleteDialogComponent {
  shareHoldingFlag?: IShareHoldingFlag;

  constructor(protected shareHoldingFlagService: ShareHoldingFlagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.shareHoldingFlagService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
