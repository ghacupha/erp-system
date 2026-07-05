import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBankTransactionType } from '../bank-transaction-type.model';

@Component({
  selector: 'jhi-bank-transaction-type-detail',
  templateUrl: './bank-transaction-type-detail.component.html',
})
export class BankTransactionTypeDetailComponent implements OnInit {
  bankTransactionType: IBankTransactionType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankTransactionType }) => {
      this.bankTransactionType = bankTransactionType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
