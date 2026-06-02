import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICardBrandType } from '../card-brand-type.model';
import { CardBrandTypeService } from '../service/card-brand-type.service';

@Component({
  templateUrl: './card-brand-type-delete-dialog.component.html',
})
export class CardBrandTypeDeleteDialogComponent {
  cardBrandType?: ICardBrandType;

  constructor(protected cardBrandTypeService: CardBrandTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cardBrandTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
