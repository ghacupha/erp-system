import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseLiabilityByAccountReportItem } from '../lease-liability-by-account-report-item.model';

@Component({
  selector: 'jhi-lease-liability-by-account-report-item-detail',
  templateUrl: './lease-liability-by-account-report-item-detail.component.html',
})
export class LeaseLiabilityByAccountReportItemDetailComponent implements OnInit {
  leaseLiabilityByAccountReportItem: ILeaseLiabilityByAccountReportItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseLiabilityByAccountReportItem }) => {
      this.leaseLiabilityByAccountReportItem = leaseLiabilityByAccountReportItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
