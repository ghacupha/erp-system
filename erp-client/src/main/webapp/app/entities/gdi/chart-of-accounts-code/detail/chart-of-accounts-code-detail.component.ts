import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChartOfAccountsCode } from '../chart-of-accounts-code.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-chart-of-accounts-code-detail',
  templateUrl: './chart-of-accounts-code-detail.component.html',
})
export class ChartOfAccountsCodeDetailComponent implements OnInit {
  chartOfAccountsCode: IChartOfAccountsCode | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chartOfAccountsCode }) => {
      this.chartOfAccountsCode = chartOfAccountsCode;
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
