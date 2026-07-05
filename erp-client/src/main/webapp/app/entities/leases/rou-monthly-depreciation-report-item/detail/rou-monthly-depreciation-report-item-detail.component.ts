import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouMonthlyDepreciationReportItem } from '../rou-monthly-depreciation-report-item.model';

@Component({
  selector: 'jhi-rou-monthly-depreciation-report-item-detail',
  templateUrl: './rou-monthly-depreciation-report-item-detail.component.html',
})
export class RouMonthlyDepreciationReportItemDetailComponent implements OnInit {
  rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouMonthlyDepreciationReportItem }) => {
      this.rouMonthlyDepreciationReportItem = rouMonthlyDepreciationReportItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
