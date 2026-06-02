import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IParticularsOfOutlet } from '../particulars-of-outlet.model';
import { ParticularsOfOutletService } from '../service/particulars-of-outlet.service';

@Component({
  templateUrl: './particulars-of-outlet-delete-dialog.component.html',
})
export class ParticularsOfOutletDeleteDialogComponent {
  particularsOfOutlet?: IParticularsOfOutlet;

  constructor(protected particularsOfOutletService: ParticularsOfOutletService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.particularsOfOutletService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
