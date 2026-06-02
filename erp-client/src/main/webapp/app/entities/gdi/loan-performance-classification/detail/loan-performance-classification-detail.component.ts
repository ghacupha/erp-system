import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILoanPerformanceClassification } from '../loan-performance-classification.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-loan-performance-classification-detail',
  templateUrl: './loan-performance-classification-detail.component.html',
})
export class LoanPerformanceClassificationDetailComponent implements OnInit {
  loanPerformanceClassification: ILoanPerformanceClassification | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ loanPerformanceClassification }) => {
      this.loanPerformanceClassification = loanPerformanceClassification;
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
