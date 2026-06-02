import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIsicEconomicActivity } from '../isic-economic-activity.model';
import { IsicEconomicActivityService } from '../service/isic-economic-activity.service';

@Component({
  templateUrl: './isic-economic-activity-delete-dialog.component.html',
})
export class IsicEconomicActivityDeleteDialogComponent {
  isicEconomicActivity?: IIsicEconomicActivity;

  constructor(protected isicEconomicActivityService: IsicEconomicActivityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.isicEconomicActivityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
