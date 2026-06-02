import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INbvCompilationBatch } from '../nbv-compilation-batch.model';
import { NbvCompilationBatchService } from '../service/nbv-compilation-batch.service';

@Component({
  templateUrl: './nbv-compilation-batch-delete-dialog.component.html',
})
export class NbvCompilationBatchDeleteDialogComponent {
  nbvCompilationBatch?: INbvCompilationBatch;

  constructor(protected nbvCompilationBatchService: NbvCompilationBatchService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nbvCompilationBatchService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
