import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouDepreciationEntryReport } from '../rou-depreciation-entry-report.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-rou-depreciation-entry-report-detail',
  templateUrl: './rou-depreciation-entry-report-detail.component.html',
})
export class RouDepreciationEntryReportDetailComponent implements OnInit {
  rouDepreciationEntryReport: IRouDepreciationEntryReport | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouDepreciationEntryReport }) => {
      this.rouDepreciationEntryReport = rouDepreciationEntryReport;
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
