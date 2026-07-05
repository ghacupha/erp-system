import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentLabel } from '../payment-label.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-payment-label-detail',
  templateUrl: './payment-label-detail.component.html',
})
export class PaymentLabelDetailComponent implements OnInit {
  paymentLabel: IPaymentLabel | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentLabel }) => {
      this.paymentLabel = paymentLabel;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
