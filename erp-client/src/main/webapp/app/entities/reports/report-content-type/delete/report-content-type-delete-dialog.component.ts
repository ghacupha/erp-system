import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReportContentType } from '../report-content-type.model';
import { ReportContentTypeService } from '../service/report-content-type.service';

@Component({
  templateUrl: './report-content-type-delete-dialog.component.html',
})
export class ReportContentTypeDeleteDialogComponent {
  reportContentType?: IReportContentType;

  constructor(protected reportContentTypeService: ReportContentTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reportContentTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
