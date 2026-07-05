import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepaymentReport } from '../prepayment-report.model';

@Component({
  selector: 'jhi-prepayment-report-detail',
  templateUrl: './prepayment-report-detail.component.html',
})
export class PrepaymentReportDetailComponent implements OnInit {
  prepaymentReport: IPrepaymentReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentReport }) => {
      this.prepaymentReport = prepaymentReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
