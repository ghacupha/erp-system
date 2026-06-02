import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouAssetNBVReport } from '../rou-asset-nbv-report.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-rou-asset-nbv-report-detail',
  templateUrl: './rou-asset-nbv-report-detail.component.html',
})
export class RouAssetNBVReportDetailComponent implements OnInit {
  rouAssetNBVReport: IRouAssetNBVReport | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouAssetNBVReport }) => {
      this.rouAssetNBVReport = rouAssetNBVReport;
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
