import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrepaymentMapping } from '../prepayment-mapping.model';
import { PrepaymentMappingService } from '../service/prepayment-mapping.service';

@Component({
  templateUrl: './prepayment-mapping-delete-dialog.component.html',
})
export class PrepaymentMappingDeleteDialogComponent {
  prepaymentMapping?: IPrepaymentMapping;

  constructor(protected prepaymentMappingService: PrepaymentMappingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prepaymentMappingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
