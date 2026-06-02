import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAmortizationPeriod } from '../amortization-period.model';

@Component({
  selector: 'jhi-amortization-period-detail',
  templateUrl: './amortization-period-detail.component.html',
})
export class AmortizationPeriodDetailComponent implements OnInit {
  amortizationPeriod: IAmortizationPeriod | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amortizationPeriod }) => {
      this.amortizationPeriod = amortizationPeriod;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
