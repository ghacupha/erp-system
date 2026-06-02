import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransactionAccountLedger } from '../transaction-account-ledger.model';

@Component({
  selector: 'jhi-transaction-account-ledger-detail',
  templateUrl: './transaction-account-ledger-detail.component.html',
})
export class TransactionAccountLedgerDetailComponent implements OnInit {
  transactionAccountLedger: ITransactionAccountLedger | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionAccountLedger }) => {
      this.transactionAccountLedger = transactionAccountLedger;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
