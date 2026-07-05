import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseLiabilityReportItem } from '../lease-liability-report-item.model';

@Component({
  selector: 'jhi-lease-liability-report-item-detail',
  templateUrl: './lease-liability-report-item-detail.component.html',
})
export class LeaseLiabilityReportItemDetailComponent implements OnInit {
  leaseLiabilityReportItem: ILeaseLiabilityReportItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseLiabilityReportItem }) => {
      this.leaseLiabilityReportItem = leaseLiabilityReportItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
