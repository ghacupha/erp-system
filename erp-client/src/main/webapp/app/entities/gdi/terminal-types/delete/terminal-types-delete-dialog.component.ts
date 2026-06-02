import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITerminalTypes } from '../terminal-types.model';
import { TerminalTypesService } from '../service/terminal-types.service';

@Component({
  templateUrl: './terminal-types-delete-dialog.component.html',
})
export class TerminalTypesDeleteDialogComponent {
  terminalTypes?: ITerminalTypes;

  constructor(protected terminalTypesService: TerminalTypesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.terminalTypesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
