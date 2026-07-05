import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAmortizationPostingReport } from '../amortization-posting-report.model';

@Component({
  selector: 'jhi-amortization-posting-report-detail',
  templateUrl: './amortization-posting-report-detail.component.html',
})
export class AmortizationPostingReportDetailComponent implements OnInit {
  amortizationPostingReport: IAmortizationPostingReport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amortizationPostingReport }) => {
      this.amortizationPostingReport = amortizationPostingReport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
