import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouDepreciationEntryReportItem } from '../rou-depreciation-entry-report-item.model';

@Component({
  selector: 'jhi-rou-depreciation-entry-report-item-detail',
  templateUrl: './rou-depreciation-entry-report-item-detail.component.html',
})
export class RouDepreciationEntryReportItemDetailComponent implements OnInit {
  rouDepreciationEntryReportItem: IRouDepreciationEntryReportItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouDepreciationEntryReportItem }) => {
      this.rouDepreciationEntryReportItem = rouDepreciationEntryReportItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
