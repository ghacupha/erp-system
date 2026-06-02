import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITerminalsAndPOS } from '../terminals-and-pos.model';
import { TerminalsAndPOSService } from '../service/terminals-and-pos.service';

@Component({
  templateUrl: './terminals-and-pos-delete-dialog.component.html',
})
export class TerminalsAndPOSDeleteDialogComponent {
  terminalsAndPOS?: ITerminalsAndPOS;

  constructor(protected terminalsAndPOSService: TerminalsAndPOSService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.terminalsAndPOSService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
