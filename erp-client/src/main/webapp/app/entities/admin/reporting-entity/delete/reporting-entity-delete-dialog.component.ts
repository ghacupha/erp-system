import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReportingEntity } from '../reporting-entity.model';
import { ReportingEntityService } from '../service/reporting-entity.service';

@Component({
  templateUrl: './reporting-entity-delete-dialog.component.html',
})
export class ReportingEntityDeleteDialogComponent {
  reportingEntity?: IReportingEntity;

  constructor(protected reportingEntityService: ReportingEntityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reportingEntityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
