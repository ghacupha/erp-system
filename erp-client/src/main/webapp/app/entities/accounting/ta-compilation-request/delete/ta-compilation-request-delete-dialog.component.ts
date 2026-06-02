import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITACompilationRequest } from '../ta-compilation-request.model';
import { TACompilationRequestService } from '../service/ta-compilation-request.service';

@Component({
  templateUrl: './ta-compilation-request-delete-dialog.component.html',
})
export class TACompilationRequestDeleteDialogComponent {
  tACompilationRequest?: ITACompilationRequest;

  constructor(protected tACompilationRequestService: TACompilationRequestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tACompilationRequestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
