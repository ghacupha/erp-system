import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICardAcquiringTransaction } from '../card-acquiring-transaction.model';

@Component({
  selector: 'jhi-card-acquiring-transaction-detail',
  templateUrl: './card-acquiring-transaction-detail.component.html',
})
export class CardAcquiringTransactionDetailComponent implements OnInit {
  cardAcquiringTransaction: ICardAcquiringTransaction | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cardAcquiringTransaction }) => {
      this.cardAcquiringTransaction = cardAcquiringTransaction;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
