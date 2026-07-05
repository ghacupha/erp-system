import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBouncedChequeCategories } from '../bounced-cheque-categories.model';
import { BouncedChequeCategoriesService } from '../service/bounced-cheque-categories.service';

@Component({
  templateUrl: './bounced-cheque-categories-delete-dialog.component.html',
})
export class BouncedChequeCategoriesDeleteDialogComponent {
  bouncedChequeCategories?: IBouncedChequeCategories;

  constructor(protected bouncedChequeCategoriesService: BouncedChequeCategoriesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bouncedChequeCategoriesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
