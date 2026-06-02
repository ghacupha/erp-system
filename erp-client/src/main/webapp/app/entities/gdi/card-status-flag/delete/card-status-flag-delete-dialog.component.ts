import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICardStatusFlag } from '../card-status-flag.model';
import { CardStatusFlagService } from '../service/card-status-flag.service';

@Component({
  templateUrl: './card-status-flag-delete-dialog.component.html',
})
export class CardStatusFlagDeleteDialogComponent {
  cardStatusFlag?: ICardStatusFlag;

  constructor(protected cardStatusFlagService: CardStatusFlagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cardStatusFlagService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
