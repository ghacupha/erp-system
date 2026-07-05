import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepaymentOutstandingOverviewReport } from '../prepayment-outstanding-overview-report.model';

@Component({
  selector: 'jhi-prepayment-outstanding-overview-report-detail',
  templateUrl: './prepayment-outstanding-overview-report-detail.component.html',
})
export class PrepaymentOutstandingOverviewReportDetailComponent implements OnInit {
  prepaymentOutstandingOverviewReport: IPrepaymentOutstandingOverviewReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentOutstandingOverviewReport }) => {
      this.prepaymentOutstandingOverviewReport = prepaymentOutstandingOverviewReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
