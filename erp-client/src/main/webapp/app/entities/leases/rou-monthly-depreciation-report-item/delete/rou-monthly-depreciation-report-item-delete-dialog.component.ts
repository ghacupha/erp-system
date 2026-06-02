import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRouMonthlyDepreciationReportItem } from '../rou-monthly-depreciation-report-item.model';
import { RouMonthlyDepreciationReportItemService } from '../service/rou-monthly-depreciation-report-item.service';

@Component({
  templateUrl: './rou-monthly-depreciation-report-item-delete-dialog.component.html',
})
export class RouMonthlyDepreciationReportItemDeleteDialogComponent {
  rouMonthlyDepreciationReportItem?: IRouMonthlyDepreciationReportItem;

  constructor(
    protected rouMonthlyDepreciationReportItemService: RouMonthlyDepreciationReportItemService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rouMonthlyDepreciationReportItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
