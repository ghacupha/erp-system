import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseLiabilityPostingReportItem } from '../lease-liability-posting-report-item.model';

@Component({
  selector: 'jhi-lease-liability-posting-report-item-detail',
  templateUrl: './lease-liability-posting-report-item-detail.component.html',
})
export class LeaseLiabilityPostingReportItemDetailComponent implements OnInit {
  leaseLiabilityPostingReportItem: ILeaseLiabilityPostingReportItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseLiabilityPostingReportItem }) => {
      this.leaseLiabilityPostingReportItem = leaseLiabilityPostingReportItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
