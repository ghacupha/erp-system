import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepaymentAccountReport } from '../prepayment-account-report.model';

@Component({
  selector: 'jhi-prepayment-account-report-detail',
  templateUrl: './prepayment-account-report-detail.component.html',
})
export class PrepaymentAccountReportDetailComponent implements OnInit {
  prepaymentAccountReport: IPrepaymentAccountReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentAccountReport }) => {
      this.prepaymentAccountReport = prepaymentAccountReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
