import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILoanDeclineReason } from '../loan-decline-reason.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-loan-decline-reason-detail',
  templateUrl: './loan-decline-reason-detail.component.html',
})
export class LoanDeclineReasonDetailComponent implements OnInit {
  loanDeclineReason: ILoanDeclineReason | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ loanDeclineReason }) => {
      this.loanDeclineReason = loanDeclineReason;
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
