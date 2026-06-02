import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICardUsageInformation } from '../card-usage-information.model';
import { CardUsageInformationService } from '../service/card-usage-information.service';

@Component({
  templateUrl: './card-usage-information-delete-dialog.component.html',
})
export class CardUsageInformationDeleteDialogComponent {
  cardUsageInformation?: ICardUsageInformation;

  constructor(protected cardUsageInformationService: CardUsageInformationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cardUsageInformationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
