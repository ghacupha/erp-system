import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICardClassType } from '../card-class-type.model';
import { CardClassTypeService } from '../service/card-class-type.service';

@Component({
  templateUrl: './card-class-type-delete-dialog.component.html',
})
export class CardClassTypeDeleteDialogComponent {
  cardClassType?: ICardClassType;

  constructor(protected cardClassTypeService: CardClassTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cardClassTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
