import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICardIssuerCharges } from '../card-issuer-charges.model';
import { CardIssuerChargesService } from '../service/card-issuer-charges.service';

@Component({
  templateUrl: './card-issuer-charges-delete-dialog.component.html',
})
export class CardIssuerChargesDeleteDialogComponent {
  cardIssuerCharges?: ICardIssuerCharges;

  constructor(protected cardIssuerChargesService: CardIssuerChargesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cardIssuerChargesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
