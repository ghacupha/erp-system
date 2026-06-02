import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepreciationPeriod } from '../depreciation-period.model';
import { DepreciationPeriodService } from '../service/depreciation-period.service';

@Component({
  templateUrl: './depreciation-period-delete-dialog.component.html',
})
export class DepreciationPeriodDeleteDialogComponent {
  depreciationPeriod?: IDepreciationPeriod;

  constructor(protected depreciationPeriodService: DepreciationPeriodService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.depreciationPeriodService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
