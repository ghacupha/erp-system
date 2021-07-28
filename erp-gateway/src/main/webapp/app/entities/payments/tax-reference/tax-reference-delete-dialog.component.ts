import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITaxReference } from 'app/shared/model/payments/tax-reference.model';
import { TaxReferenceService } from './tax-reference.service';

@Component({
  templateUrl: './tax-reference-delete-dialog.component.html',
})
export class TaxReferenceDeleteDialogComponent {
  taxReference?: ITaxReference;

  constructor(
    protected taxReferenceService: TaxReferenceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.taxReferenceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('taxReferenceListModification');
      this.activeModal.close();
    });
  }
}
