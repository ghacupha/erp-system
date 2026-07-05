import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentInvoice } from '../payment-invoice.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-payment-invoice-detail',
  templateUrl: './payment-invoice-detail.component.html',
})
export class PaymentInvoiceDetailComponent implements OnInit {
  paymentInvoice: IPaymentInvoice | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentInvoice }) => {
      this.paymentInvoice = paymentInvoice;
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
