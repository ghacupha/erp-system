import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaseLiabilityCompilation } from '../lease-liability-compilation.model';
import { LeaseLiabilityCompilationService } from '../service/lease-liability-compilation.service';

@Component({
  templateUrl: './lease-liability-compilation-delete-dialog.component.html',
})
export class LeaseLiabilityCompilationDeleteDialogComponent {
  leaseLiabilityCompilation?: ILeaseLiabilityCompilation;

  constructor(protected leaseLiabilityCompilationService: LeaseLiabilityCompilationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaseLiabilityCompilationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
