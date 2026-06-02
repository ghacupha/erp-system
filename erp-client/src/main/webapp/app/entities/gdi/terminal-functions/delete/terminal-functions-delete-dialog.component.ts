import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITerminalFunctions } from '../terminal-functions.model';
import { TerminalFunctionsService } from '../service/terminal-functions.service';

@Component({
  templateUrl: './terminal-functions-delete-dialog.component.html',
})
export class TerminalFunctionsDeleteDialogComponent {
  terminalFunctions?: ITerminalFunctions;

  constructor(protected terminalFunctionsService: TerminalFunctionsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.terminalFunctionsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
