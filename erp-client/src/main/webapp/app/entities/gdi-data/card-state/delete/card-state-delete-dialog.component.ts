import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICardState } from '../card-state.model';
import { CardStateService } from '../service/card-state.service';

@Component({
  templateUrl: './card-state-delete-dialog.component.html',
})
export class CardStateDeleteDialogComponent {
  cardState?: ICardState;

  constructor(protected cardStateService: CardStateService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cardStateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
