import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INbvCompilationJob } from '../nbv-compilation-job.model';
import { NbvCompilationJobService } from '../service/nbv-compilation-job.service';

@Component({
  templateUrl: './nbv-compilation-job-delete-dialog.component.html',
})
export class NbvCompilationJobDeleteDialogComponent {
  nbvCompilationJob?: INbvCompilationJob;

  constructor(protected nbvCompilationJobService: NbvCompilationJobService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nbvCompilationJobService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
