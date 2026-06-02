import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICounterpartyType } from '../counterparty-type.model';
import { CounterpartyTypeService } from '../service/counterparty-type.service';

@Component({
  templateUrl: './counterparty-type-delete-dialog.component.html',
})
export class CounterpartyTypeDeleteDialogComponent {
  counterpartyType?: ICounterpartyType;

  constructor(protected counterpartyTypeService: CounterpartyTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.counterpartyTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
