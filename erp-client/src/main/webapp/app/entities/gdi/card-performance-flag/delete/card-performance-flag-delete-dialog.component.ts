import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICardPerformanceFlag } from '../card-performance-flag.model';
import { CardPerformanceFlagService } from '../service/card-performance-flag.service';

@Component({
  templateUrl: './card-performance-flag-delete-dialog.component.html',
})
export class CardPerformanceFlagDeleteDialogComponent {
  cardPerformanceFlag?: ICardPerformanceFlag;

  constructor(protected cardPerformanceFlagService: CardPerformanceFlagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cardPerformanceFlagService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
