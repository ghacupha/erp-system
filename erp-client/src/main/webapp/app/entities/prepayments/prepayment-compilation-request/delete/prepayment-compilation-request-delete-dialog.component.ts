import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrepaymentCompilationRequest } from '../prepayment-compilation-request.model';
import { PrepaymentCompilationRequestService } from '../service/prepayment-compilation-request.service';

@Component({
  templateUrl: './prepayment-compilation-request-delete-dialog.component.html',
})
export class PrepaymentCompilationRequestDeleteDialogComponent {
  prepaymentCompilationRequest?: IPrepaymentCompilationRequest;

  constructor(protected prepaymentCompilationRequestService: PrepaymentCompilationRequestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prepaymentCompilationRequestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
