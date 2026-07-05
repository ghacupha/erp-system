import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWIPTransferListReport } from '../wip-transfer-list-report.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-wip-transfer-list-report-detail',
  templateUrl: './wip-transfer-list-report-detail.component.html',
})
export class WIPTransferListReportDetailComponent implements OnInit {
  wIPTransferListReport: IWIPTransferListReport | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wIPTransferListReport }) => {
      this.wIPTransferListReport = wIPTransferListReport;
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
