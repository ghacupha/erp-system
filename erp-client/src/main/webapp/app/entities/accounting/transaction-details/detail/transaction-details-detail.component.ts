import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransactionDetails } from '../transaction-details.model';

@Component({
  selector: 'jhi-transaction-details-detail',
  templateUrl: './transaction-details-detail.component.html',
})
export class TransactionDetailsDetailComponent implements OnInit {
  transactionDetails: ITransactionDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionDetails }) => {
      this.transactionDetails = transactionDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
