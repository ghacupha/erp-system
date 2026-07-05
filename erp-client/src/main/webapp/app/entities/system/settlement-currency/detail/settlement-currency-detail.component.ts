import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISettlementCurrency } from '../settlement-currency.model';

@Component({
  selector: 'jhi-settlement-currency-detail',
  templateUrl: './settlement-currency-detail.component.html',
})
export class SettlementCurrencyDetailComponent implements OnInit {
  settlementCurrency: ISettlementCurrency | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ settlementCurrency }) => {
      this.settlementCurrency = settlementCurrency;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
