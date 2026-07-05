import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransactionAccountReportItem } from '../transaction-account-report-item.model';

@Component({
  selector: 'jhi-transaction-account-report-item-detail',
  templateUrl: './transaction-account-report-item-detail.component.html',
})
export class TransactionAccountReportItemDetailComponent implements OnInit {
  transactionAccountReportItem: ITransactionAccountReportItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionAccountReportItem }) => {
      this.transactionAccountReportItem = transactionAccountReportItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
