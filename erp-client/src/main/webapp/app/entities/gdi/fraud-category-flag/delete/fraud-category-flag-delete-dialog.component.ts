import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFraudCategoryFlag } from '../fraud-category-flag.model';
import { FraudCategoryFlagService } from '../service/fraud-category-flag.service';

@Component({
  templateUrl: './fraud-category-flag-delete-dialog.component.html',
})
export class FraudCategoryFlagDeleteDialogComponent {
  fraudCategoryFlag?: IFraudCategoryFlag;

  constructor(protected fraudCategoryFlagService: FraudCategoryFlagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fraudCategoryFlagService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
