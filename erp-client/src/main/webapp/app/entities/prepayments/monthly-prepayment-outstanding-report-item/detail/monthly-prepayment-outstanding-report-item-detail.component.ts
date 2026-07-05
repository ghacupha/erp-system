import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMonthlyPrepaymentOutstandingReportItem } from '../monthly-prepayment-outstanding-report-item.model';

@Component({
  selector: 'jhi-monthly-prepayment-outstanding-report-item-detail',
  templateUrl: './monthly-prepayment-outstanding-report-item-detail.component.html',
})
export class MonthlyPrepaymentOutstandingReportItemDetailComponent implements OnInit {
  monthlyPrepaymentOutstandingReportItem: IMonthlyPrepaymentOutstandingReportItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ monthlyPrepaymentOutstandingReportItem }) => {
      this.monthlyPrepaymentOutstandingReportItem = monthlyPrepaymentOutstandingReportItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
