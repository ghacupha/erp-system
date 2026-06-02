import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILoanRepaymentFrequency } from '../loan-repayment-frequency.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-loan-repayment-frequency-detail',
  templateUrl: './loan-repayment-frequency-detail.component.html',
})
export class LoanRepaymentFrequencyDetailComponent implements OnInit {
  loanRepaymentFrequency: ILoanRepaymentFrequency | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ loanRepaymentFrequency }) => {
      this.loanRepaymentFrequency = loanRepaymentFrequency;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
