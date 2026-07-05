import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICardCategoryType } from '../card-category-type.model';
import { CardCategoryTypeService } from '../service/card-category-type.service';

@Component({
  templateUrl: './card-category-type-delete-dialog.component.html',
})
export class CardCategoryTypeDeleteDialogComponent {
  cardCategoryType?: ICardCategoryType;

  constructor(protected cardCategoryTypeService: CardCategoryTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cardCategoryTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
