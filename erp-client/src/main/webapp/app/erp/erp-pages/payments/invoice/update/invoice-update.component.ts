///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import { concat, Observable, of, Subject } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, filter, finalize, map, switchMap, tap } from 'rxjs/operators';

import { InvoiceService } from '../service/invoice.service';
import {Store} from "@ngrx/store";
import {State} from "../../../../store/global-store.definition";
import {
  dealerInvoicePaymentWorkflowCancelled,
  recordInvoicePaymentButtonClicked
} from "../../../../store/actions/dealer-invoice-workflows-status.actions";
import { IPaymentLabel } from '../../../payment-label/payment-label.model';
import { PaymentLabelService } from '../../../payment-label/service/payment-label.service';
import { IInvoice, Invoice } from '../invoice.model';
import { LabelSuggestionService } from '../../../../erp-common/suggestion/label-suggestion.service';
import { PlaceholderSuggestionService } from '../../../../erp-common/suggestion/placeholder-suggestion.service';
import { IPlaceholder } from '../../../placeholder/placeholder.model';
import { PlaceholderService } from '../../../placeholder/service/placeholder.service';

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
    paymentReference: [],
    dealerName: [],
    paymentLabels: [],
    placeholders: [],
  });

  minAccountLengthTerm = 3;

  labelsLoading = false;
  labelControlInput$ = new Subject<string>();
  labelLookups$: Observable<IPaymentLabel[]> = of([]);

  placeholdersLoading = false;
  placeholderControlInput$ = new Subject<string>();
  placeholderLookups$: Observable<IPlaceholder[]> = of([]);

  constructor(
    protected invoiceService: InvoiceService,
    protected paymentLabelService: PaymentLabelService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>,
    protected router: Router,
    protected labelSuggestionService: LabelSuggestionService,
    protected placeholderSuggestionService: PlaceholderSuggestionService,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.updateForm(invoice);

      this.loadRelationshipsOptions();
    });

    // fire-up typeahead items
    this.loadLabels();
    this.loadPlaceholders();
  }

  loadLabels(): void {
    this.labelLookups$ = concat(
      of([]), // default items
      this.labelControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.labelsLoading = true),
        switchMap(term => this.labelSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.labelsLoading = false)
        ))
      ),
      of([...this.paymentLabelsSharedCollection])
    );
  }

  loadPlaceholders(): void {
    this.placeholderLookups$ = concat(
      of([]), // default items
      this.placeholderControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.placeholdersLoading = true),
        switchMap(term => this.placeholderSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.placeholdersLoading = false)
        ))
      ),
      of([...this.placeholdersSharedCollection])
    );
  }

  trackPlaceholdersByFn(item: IPaymentLabel): number {
    return item.id!;
  }

  trackLabelByFn(item: IPaymentLabel): number {
    return item.id!;
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
    const invoice = this.recordInvoiceFromForm();
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

    const paymentPath = 'erp/payment/dealer/invoice';
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
      // conversionRate: invoice.conversionRate,
      paymentReference: invoice.paymentReference,
      dealerName: invoice.dealerName,
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
      // conversionRate: this.editForm.get(['conversionRate'])!.value,
      paymentReference: this.editForm.get(['paymentReference'])!.value,
      dealerName: this.editForm.get(['dealerName'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
  protected recordInvoiceFromForm(): IInvoice {
    return {
      ...new Invoice(),
      invoiceNumber: this.editForm.get(['invoiceNumber'])!.value,
      invoiceDate: this.editForm.get(['invoiceDate'])!.value,
      invoiceAmount: this.editForm.get(['invoiceAmount'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      // conversionRate: this.editForm.get(['conversionRate'])!.value,
      paymentReference: this.editForm.get(['paymentReference'])!.value,
      dealerName: this.editForm.get(['dealerName'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
