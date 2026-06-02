import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrepaymentMarshalling } from '../prepayment-marshalling.model';
import { PrepaymentMarshallingService } from '../service/prepayment-marshalling.service';

@Component({
  templateUrl: './prepayment-marshalling-delete-dialog.component.html',
})
export class PrepaymentMarshallingDeleteDialogComponent {
  prepaymentMarshalling?: IPrepaymentMarshalling;

  constructor(protected prepaymentMarshallingService: PrepaymentMarshallingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prepaymentMarshallingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
