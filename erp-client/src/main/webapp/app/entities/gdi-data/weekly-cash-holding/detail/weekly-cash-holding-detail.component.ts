import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWeeklyCashHolding } from '../weekly-cash-holding.model';

@Component({
  selector: 'jhi-weekly-cash-holding-detail',
  templateUrl: './weekly-cash-holding-detail.component.html',
})
export class WeeklyCashHoldingDetailComponent implements OnInit {
  weeklyCashHolding: IWeeklyCashHolding | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weeklyCashHolding }) => {
      this.weeklyCashHolding = weeklyCashHolding;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
