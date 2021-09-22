import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISignedPayment } from '../signed-payment.model';

@Component({
  selector: 'jhi-signed-payment-detail',
  templateUrl: './signed-payment-detail.component.html',
})
export class SignedPaymentDetailComponent implements OnInit {
  signedPayment: ISignedPayment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ signedPayment }) => {
      this.signedPayment = signedPayment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
