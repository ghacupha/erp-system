import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentLabel } from '../payment-label.model';

@Component({
  selector: 'jhi-payment-label-detail',
  templateUrl: './payment-label-detail.component.html',
})
export class PaymentLabelDetailComponent implements OnInit {
  paymentLabel: IPaymentLabel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentLabel }) => {
      this.paymentLabel = paymentLabel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
