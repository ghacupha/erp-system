import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepreciationBatchSequence } from '../depreciation-batch-sequence.model';
import { DepreciationBatchSequenceService } from '../service/depreciation-batch-sequence.service';

@Component({
  templateUrl: './depreciation-batch-sequence-delete-dialog.component.html',
})
export class DepreciationBatchSequenceDeleteDialogComponent {
  depreciationBatchSequence?: IDepreciationBatchSequence;

  constructor(protected depreciationBatchSequenceService: DepreciationBatchSequenceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.depreciationBatchSequenceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
