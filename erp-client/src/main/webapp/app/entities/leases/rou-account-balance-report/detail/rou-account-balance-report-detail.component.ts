import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouAccountBalanceReport } from '../rou-account-balance-report.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-rou-account-balance-report-detail',
  templateUrl: './rou-account-balance-report-detail.component.html',
})
export class RouAccountBalanceReportDetailComponent implements OnInit {
  rouAccountBalanceReport: IRouAccountBalanceReport | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouAccountBalanceReport }) => {
      this.rouAccountBalanceReport = rouAccountBalanceReport;
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
