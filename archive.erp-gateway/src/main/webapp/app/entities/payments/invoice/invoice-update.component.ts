import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IInvoice, Invoice } from 'app/shared/model/payments/invoice.model';
import { InvoiceService } from './invoice.service';
import { IPayment } from 'app/shared/model/payments/payment.model';
import { PaymentService } from 'app/entities/payments/payment/payment.service';
import { IDealer } from 'app/shared/model/dealers/dealer.model';
import { DealerService } from 'app/entities/dealers/dealer/dealer.service';

type SelectableEntity = IPayment | IDealer;

@Component({
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;
  payments: IPayment[] = [];
  dealers: IDealer[] = [];
  invoiceDateDp: any;

  editForm = this.fb.group({
    id: [],
    invoiceNumber: [],
    invoiceDate: [],
    invoiceAmount: [],
    paymentId: [],
    dealerId: [],
  });

  constructor(
    protected invoiceService: InvoiceService,
    protected paymentService: PaymentService,
    protected dealerService: DealerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.updateForm(invoice);

      this.paymentService.query().subscribe((res: HttpResponse<IPayment[]>) => (this.payments = res.body || []));

      this.dealerService.query().subscribe((res: HttpResponse<IDealer[]>) => (this.dealers = res.body || []));
    });
  }

  updateForm(invoice: IInvoice): void {
    this.editForm.patchValue({
      id: invoice.id,
      invoiceNumber: invoice.invoiceNumber,
      invoiceDate: invoice.invoiceDate,
      invoiceAmount: invoice.invoiceAmount,
      paymentId: invoice.paymentId,
      dealerId: invoice.dealerId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.createFromForm();
    if (invoice.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  private createFromForm(): IInvoice {
    return {
      ...new Invoice(),
      id: this.editForm.get(['id'])!.value,
      invoiceNumber: this.editForm.get(['invoiceNumber'])!.value,
      invoiceDate: this.editForm.get(['invoiceDate'])!.value,
      invoiceAmount: this.editForm.get(['invoiceAmount'])!.value,
      paymentId: this.editForm.get(['paymentId'])!.value,
      dealerId: this.editForm.get(['dealerId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
