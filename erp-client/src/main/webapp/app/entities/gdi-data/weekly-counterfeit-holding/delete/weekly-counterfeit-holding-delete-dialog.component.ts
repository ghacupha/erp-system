import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWeeklyCounterfeitHolding } from '../weekly-counterfeit-holding.model';
import { WeeklyCounterfeitHoldingService } from '../service/weekly-counterfeit-holding.service';

@Component({
  templateUrl: './weekly-counterfeit-holding-delete-dialog.component.html',
})
export class WeeklyCounterfeitHoldingDeleteDialogComponent {
  weeklyCounterfeitHolding?: IWeeklyCounterfeitHolding;

  constructor(protected weeklyCounterfeitHoldingService: WeeklyCounterfeitHoldingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.weeklyCounterfeitHoldingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
