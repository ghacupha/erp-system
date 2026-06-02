import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPerformanceOfForeignSubsidiaries } from '../performance-of-foreign-subsidiaries.model';
import { PerformanceOfForeignSubsidiariesService } from '../service/performance-of-foreign-subsidiaries.service';

@Component({
  templateUrl: './performance-of-foreign-subsidiaries-delete-dialog.component.html',
})
export class PerformanceOfForeignSubsidiariesDeleteDialogComponent {
  performanceOfForeignSubsidiaries?: IPerformanceOfForeignSubsidiaries;

  constructor(
    protected performanceOfForeignSubsidiariesService: PerformanceOfForeignSubsidiariesService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.performanceOfForeignSubsidiariesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
