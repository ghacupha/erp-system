import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbAccountHolderType } from '../crb-account-holder-type.model';
import { CrbAccountHolderTypeService } from '../service/crb-account-holder-type.service';

@Component({
  templateUrl: './crb-account-holder-type-delete-dialog.component.html',
})
export class CrbAccountHolderTypeDeleteDialogComponent {
  crbAccountHolderType?: ICrbAccountHolderType;

  constructor(protected crbAccountHolderTypeService: CrbAccountHolderTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbAccountHolderTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
