import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentCategory } from '../payment-category.model';

@Component({
  selector: 'gha-payment-category-detail',
  templateUrl: './payment-category-detail.component.html',
})
export class PaymentCategoryDetailComponent implements OnInit {
  paymentCategory: IPaymentCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentCategory }) => {
      this.paymentCategory = paymentCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
