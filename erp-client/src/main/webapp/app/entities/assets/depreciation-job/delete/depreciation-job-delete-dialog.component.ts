import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepreciationJob } from '../depreciation-job.model';
import { DepreciationJobService } from '../service/depreciation-job.service';

@Component({
  templateUrl: './depreciation-job-delete-dialog.component.html',
})
export class DepreciationJobDeleteDialogComponent {
  depreciationJob?: IDepreciationJob;

  constructor(protected depreciationJobService: DepreciationJobService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.depreciationJobService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
