import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbReportViewBand } from '../crb-report-view-band.model';
import { CrbReportViewBandService } from '../service/crb-report-view-band.service';

@Component({
  templateUrl: './crb-report-view-band-delete-dialog.component.html',
})
export class CrbReportViewBandDeleteDialogComponent {
  crbReportViewBand?: ICrbReportViewBand;

  constructor(protected crbReportViewBandService: CrbReportViewBandService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbReportViewBandService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
