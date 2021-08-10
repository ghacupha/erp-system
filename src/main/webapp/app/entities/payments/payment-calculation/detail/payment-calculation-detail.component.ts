import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentCalculation } from '../payment-calculation.model';

@Component({
  selector: 'gha-payment-calculation-detail',
  templateUrl: './payment-calculation-detail.component.html',
})
export class PaymentCalculationDetailComponent implements OnInit {
  paymentCalculation: IPaymentCalculation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentCalculation }) => {
      this.paymentCalculation = paymentCalculation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
