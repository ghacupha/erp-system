import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouAssetListReport } from '../rou-asset-list-report.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-rou-asset-list-report-detail',
  templateUrl: './rou-asset-list-report-detail.component.html',
})
export class RouAssetListReportDetailComponent implements OnInit {
  rouAssetListReport: IRouAssetListReport | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouAssetListReport }) => {
      this.rouAssetListReport = rouAssetListReport;
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
