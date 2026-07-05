import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouAssetNBVReportItem } from '../rou-asset-nbv-report-item.model';

@Component({
  selector: 'jhi-rou-asset-nbv-report-item-detail',
  templateUrl: './rou-asset-nbv-report-item-detail.component.html',
})
export class RouAssetNBVReportItemDetailComponent implements OnInit {
  rouAssetNBVReportItem: IRouAssetNBVReportItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouAssetNBVReportItem }) => {
      this.rouAssetNBVReportItem = rouAssetNBVReportItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
