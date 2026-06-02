import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouAssetListReportItem } from '../rou-asset-list-report-item.model';

@Component({
  selector: 'jhi-rou-asset-list-report-item-detail',
  templateUrl: './rou-asset-list-report-item-detail.component.html',
})
export class RouAssetListReportItemDetailComponent implements OnInit {
  rouAssetListReportItem: IRouAssetListReportItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouAssetListReportItem }) => {
      this.rouAssetListReportItem = rouAssetListReportItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
