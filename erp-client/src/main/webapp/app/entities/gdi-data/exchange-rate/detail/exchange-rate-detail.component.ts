import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExchangeRate } from '../exchange-rate.model';

@Component({
  selector: 'jhi-exchange-rate-detail',
  templateUrl: './exchange-rate-detail.component.html',
})
export class ExchangeRateDetailComponent implements OnInit {
  exchangeRate: IExchangeRate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exchangeRate }) => {
      this.exchangeRate = exchangeRate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
