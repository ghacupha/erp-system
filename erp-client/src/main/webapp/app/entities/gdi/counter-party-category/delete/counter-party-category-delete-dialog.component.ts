import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICounterPartyCategory } from '../counter-party-category.model';
import { CounterPartyCategoryService } from '../service/counter-party-category.service';

@Component({
  templateUrl: './counter-party-category-delete-dialog.component.html',
})
export class CounterPartyCategoryDeleteDialogComponent {
  counterPartyCategory?: ICounterPartyCategory;

  constructor(protected counterPartyCategoryService: CounterPartyCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.counterPartyCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
