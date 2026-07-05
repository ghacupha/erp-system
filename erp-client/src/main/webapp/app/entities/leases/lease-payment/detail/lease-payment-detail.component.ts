import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeasePayment } from '../lease-payment.model';

@Component({
  selector: 'jhi-lease-payment-detail',
  templateUrl: './lease-payment-detail.component.html',
})
export class LeasePaymentDetailComponent implements OnInit {
  leasePayment: ILeasePayment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leasePayment }) => {
      this.leasePayment = leasePayment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
