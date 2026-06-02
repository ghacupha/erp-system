import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReportDesign } from '../report-design.model';
import { ReportDesignService } from '../service/report-design.service';

@Component({
  templateUrl: './report-design-delete-dialog.component.html',
})
export class ReportDesignDeleteDialogComponent {
  reportDesign?: IReportDesign;

  constructor(protected reportDesignService: ReportDesignService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reportDesignService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
