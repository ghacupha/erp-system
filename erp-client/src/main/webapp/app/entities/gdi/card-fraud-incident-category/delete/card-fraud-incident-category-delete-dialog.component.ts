import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICardFraudIncidentCategory } from '../card-fraud-incident-category.model';
import { CardFraudIncidentCategoryService } from '../service/card-fraud-incident-category.service';

@Component({
  templateUrl: './card-fraud-incident-category-delete-dialog.component.html',
})
export class CardFraudIncidentCategoryDeleteDialogComponent {
  cardFraudIncidentCategory?: ICardFraudIncidentCategory;

  constructor(protected cardFraudIncidentCategoryService: CardFraudIncidentCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cardFraudIncidentCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
