import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouDepreciationPostingReportItem } from '../rou-depreciation-posting-report-item.model';

@Component({
  selector: 'jhi-rou-depreciation-posting-report-item-detail',
  templateUrl: './rou-depreciation-posting-report-item-detail.component.html',
})
export class RouDepreciationPostingReportItemDetailComponent implements OnInit {
  rouDepreciationPostingReportItem: IRouDepreciationPostingReportItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouDepreciationPostingReportItem }) => {
      this.rouDepreciationPostingReportItem = rouDepreciationPostingReportItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
