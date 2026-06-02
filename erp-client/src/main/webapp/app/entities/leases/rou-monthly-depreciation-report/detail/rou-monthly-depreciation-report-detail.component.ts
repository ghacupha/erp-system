import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouMonthlyDepreciationReport } from '../rou-monthly-depreciation-report.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-rou-monthly-depreciation-report-detail',
  templateUrl: './rou-monthly-depreciation-report-detail.component.html',
})
export class RouMonthlyDepreciationReportDetailComponent implements OnInit {
  rouMonthlyDepreciationReport: IRouMonthlyDepreciationReport | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouMonthlyDepreciationReport }) => {
      this.rouMonthlyDepreciationReport = rouMonthlyDepreciationReport;
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
