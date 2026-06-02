import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITaxReference } from '../tax-reference.model';
import { TaxReferenceService } from '../service/tax-reference.service';

@Component({
  templateUrl: './tax-reference-delete-dialog.component.html',
})
export class TaxReferenceDeleteDialogComponent {
  taxReference?: ITaxReference;

  constructor(protected taxReferenceService: TaxReferenceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.taxReferenceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
