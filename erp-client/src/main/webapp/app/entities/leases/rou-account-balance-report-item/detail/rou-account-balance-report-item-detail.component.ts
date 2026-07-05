import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRouAccountBalanceReportItem } from '../rou-account-balance-report-item.model';

@Component({
  selector: 'jhi-rou-account-balance-report-item-detail',
  templateUrl: './rou-account-balance-report-item-detail.component.html',
})
export class RouAccountBalanceReportItemDetailComponent implements OnInit {
  rouAccountBalanceReportItem: IRouAccountBalanceReportItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouAccountBalanceReportItem }) => {
      this.rouAccountBalanceReportItem = rouAccountBalanceReportItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
