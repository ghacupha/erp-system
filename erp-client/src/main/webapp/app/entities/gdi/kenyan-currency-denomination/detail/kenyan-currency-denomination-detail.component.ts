import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IKenyanCurrencyDenomination } from '../kenyan-currency-denomination.model';

@Component({
  selector: 'jhi-kenyan-currency-denomination-detail',
  templateUrl: './kenyan-currency-denomination-detail.component.html',
})
export class KenyanCurrencyDenominationDetailComponent implements OnInit {
  kenyanCurrencyDenomination: IKenyanCurrencyDenomination | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ kenyanCurrencyDenomination }) => {
      this.kenyanCurrencyDenomination = kenyanCurrencyDenomination;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
