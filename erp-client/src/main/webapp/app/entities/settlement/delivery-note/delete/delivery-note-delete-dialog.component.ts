import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeliveryNote } from '../delivery-note.model';
import { DeliveryNoteService } from '../service/delivery-note.service';

@Component({
  templateUrl: './delivery-note-delete-dialog.component.html',
})
export class DeliveryNoteDeleteDialogComponent {
  deliveryNote?: IDeliveryNote;

  constructor(protected deliveryNoteService: DeliveryNoteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deliveryNoteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
