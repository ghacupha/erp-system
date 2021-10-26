import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInvoice, Invoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/erpService/placeholder/service/placeholder.service';
import {IPaymentLabel} from '../../../payment-label/payment-label.model';
import {PaymentLabelService} from '../../../payment-label/service/payment-label.service';
import {Store} from "@ngrx/store";
import {State} from "../../../../store/global-store.definition";
import {
  dealerInvoicePaymentWorkflowCancelled,
  recordInvoicePaymentButtonClicked
} from "../../../../store/actions/dealer-invoice-workflows-status.actions";

@Component({
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;

  paymentLabelsSharedCollection: IPaymentLabel[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    invoiceNumber: [null, [Validators.required]],
    invoiceDate: [],
    invoiceAmount: [],
    currency: [null, [Validators.required]],
    conversionRate: [null, [Validators.required, Validators.min(1.0)]],
    paymentId: [],
    dealerId: [],
    paymentLabels: [],
    placeholders: [],
  });

  constructor(
    protected invoiceService: InvoiceService,
    protected paymentLabelService: PaymentLabelService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>,
    protected router: Router,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.updateForm(invoice);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    this.store.dispatch(dealerInvoicePaymentWorkflowCancelled());
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

  trackPaymentLabelById(index: number, item: IPaymentLabel): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  getSelectedPaymentLabel(option: IPaymentLabel, selectedVals?: IPaymentLabel[]): IPaymentLabel {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedPlaceholder(option: IPlaceholder, selectedVals?: IPlaceholder[]): IPlaceholder {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  recordPayment(): void {
    this.isSaving = true;
    const invoice = this.createFromForm();
    if (invoice.id !== undefined) {
      this.subscribeToRecordPaymentResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToRecordPaymentResponse(this.invoiceService.create(invoice));
    }
  }

  protected subscribeToRecordPaymentResponse(result: Observable<HttpResponse<IInvoice>>): void {

    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      (res: HttpResponse<IInvoice>) => this.navigateToPayment(res),
      () => this.onSaveError()
    );
  }

  protected navigateToPayment(res: HttpResponse<IInvoice>): void {

    // TODO Add placeholders, payment-labels, ownedInvoices in the store
    if (res.body) {
      this.store.dispatch(recordInvoicePaymentButtonClicked({selectedInvoice: res.body}));
    }

    const paymentPath = 'payment/dealer/invoice';
    this.router.navigate([paymentPath]);
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    // TODO Amend accordingly if need be to update payment in this progression
    // this.store.dispatch(dealerInvoiceStateReset());
    this.isSaving = false;
  }

  protected updateForm(invoice: IInvoice): void {
    this.editForm.patchValue({
      id: invoice.id,
      invoiceNumber: invoice.invoiceNumber,
      invoiceDate: invoice.invoiceDate,
      invoiceAmount: invoice.invoiceAmount,
      currency: invoice.currency,
      conversionRate: invoice.conversionRate,
      paymentId: invoice.paymentId,
      dealerId: invoice.dealerId,
      paymentLabels: invoice.paymentLabels,
      placeholders: invoice.placeholders,
    });

    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      ...(invoice.paymentLabels ?? [])
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(invoice.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentLabelService
      .query()
      .pipe(map((res: HttpResponse<IPaymentLabel[]>) => res.body ?? []))
      .pipe(
        map((paymentLabels: IPaymentLabel[]) =>
          this.paymentLabelService.addPaymentLabelToCollectionIfMissing(paymentLabels, ...(this.editForm.get('paymentLabels')!.value ?? []))
        )
      )
      .subscribe((paymentLabels: IPaymentLabel[]) => (this.paymentLabelsSharedCollection = paymentLabels));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  }

  protected createFromForm(): IInvoice {
    return {
      ...new Invoice(),
      id: this.editForm.get(['id'])!.value,
      invoiceNumber: this.editForm.get(['invoiceNumber'])!.value,
      invoiceDate: this.editForm.get(['invoiceDate'])!.value,
      invoiceAmount: this.editForm.get(['invoiceAmount'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      conversionRate: this.editForm.get(['conversionRate'])!.value,
      paymentId: this.editForm.get(['paymentId'])!.value,
      dealerId: this.editForm.get(['dealerId'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
