import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILoanRestructureFlag } from '../loan-restructure-flag.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-loan-restructure-flag-detail',
  templateUrl: './loan-restructure-flag-detail.component.html',
})
export class LoanRestructureFlagDetailComponent implements OnInit {
  loanRestructureFlag: ILoanRestructureFlag | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ loanRestructureFlag }) => {
      this.loanRestructureFlag = loanRestructureFlag;
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
