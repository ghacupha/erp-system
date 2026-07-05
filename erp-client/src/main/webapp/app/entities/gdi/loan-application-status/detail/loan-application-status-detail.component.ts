import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILoanApplicationStatus } from '../loan-application-status.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-loan-application-status-detail',
  templateUrl: './loan-application-status-detail.component.html',
})
export class LoanApplicationStatusDetailComponent implements OnInit {
  loanApplicationStatus: ILoanApplicationStatus | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ loanApplicationStatus }) => {
      this.loanApplicationStatus = loanApplicationStatus;
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
