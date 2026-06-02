import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILoanPerformanceClassification } from '../loan-performance-classification.model';
import { LoanPerformanceClassificationService } from '../service/loan-performance-classification.service';

@Component({
  templateUrl: './loan-performance-classification-delete-dialog.component.html',
})
export class LoanPerformanceClassificationDeleteDialogComponent {
  loanPerformanceClassification?: ILoanPerformanceClassification;

  constructor(
    protected loanPerformanceClassificationService: LoanPerformanceClassificationService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.loanPerformanceClassificationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
