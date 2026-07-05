import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepaymentAmortization } from '../prepayment-amortization.model';

@Component({
  selector: 'jhi-prepayment-amortization-detail',
  templateUrl: './prepayment-amortization-detail.component.html',
})
export class PrepaymentAmortizationDetailComponent implements OnInit {
  prepaymentAmortization: IPrepaymentAmortization | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentAmortization }) => {
      this.prepaymentAmortization = prepaymentAmortization;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
