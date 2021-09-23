import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISignedPayment, SignedPayment } from '../signed-payment.model';
import { SignedPaymentService } from '../service/signed-payment.service';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';
import { DealerService } from 'app/entities/dealers/dealer/service/dealer.service';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/erpService/placeholder/service/placeholder.service';
import {IPaymentLabel} from '../../payment-label/payment-label.model';
import {IPaymentCategory} from '../../payments/payment-category/payment-category.model';
import {PaymentLabelService} from '../../payment-label/service/payment-label.service';
import {PaymentCategoryService} from '../../payments/payment-category/service/payment-category.service';
import {NGXLogger} from "ngx-logger";

@Component({
  selector: 'jhi-signed-payment-update',
  templateUrl: './signed-payment-update.component.html',
})
export class SignedPaymentUpdateComponent implements OnInit {
  isSaving = false;

  paymentLabelsSharedCollection: IPaymentLabel[] = [];
  dealersSharedCollection: IDealer[] = [];
  paymentCategoriesSharedCollection: IPaymentCategory[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  signedPaymentsSharedCollection: ISignedPayment[] = [];

  editForm = this.fb.group({
    id: [],
    transactionNumber: [null, [Validators.required]],
    transactionDate: [null, [Validators.required]],
    transactionCurrency: [null, [Validators.required]],
    transactionAmount: [null, [Validators.required, Validators.min(0)]],
    paymentLabels: [],
    dealer: [],
    paymentCategory: [],
    placeholders: [],
    signedPaymentGroup: [],
  });

  currencies = [
    "KES",
    "USD",
    "GBP",
    "EUR",
    "INR",
    "ZAR",
    "AED",
    "CNY",
    "CHF",
    "UGX",
    "TZS",
    "JPY",
    "CAD",
  ];

  constructor(
    protected signedPaymentService: SignedPaymentService,
    protected paymentLabelService: PaymentLabelService,
    protected dealerService: DealerService,
    protected paymentCategoryService: PaymentCategoryService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ signedPayment }) => {
      this.updateForm(signedPayment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const signedPayment = this.createFromForm();
    if (signedPayment.id !== undefined) {
      this.subscribeToSaveResponse(this.signedPaymentService.update(signedPayment));
    } else {
      this.subscribeToSaveResponse(this.signedPaymentService.create(signedPayment));
    }
  }

  trackPaymentLabelById(index: number, item: IPaymentLabel): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackPaymentCategoryById(index: number, item: IPaymentCategory): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackSignedPaymentById(index: number, item: ISignedPayment): number {
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

  updateCurrency($event: Event): void {
    this.editForm.setValue({transactionCurrency: $event})
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISignedPayment>>): void {
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
    this.isSaving = false;
  }

  protected updateForm(signedPayment: ISignedPayment): void {
    this.editForm.patchValue({
      id: signedPayment.id,
      transactionNumber: signedPayment.transactionNumber,
      transactionDate: signedPayment.transactionDate,
      transactionCurrency: signedPayment.transactionCurrency,
      transactionAmount: signedPayment.transactionAmount,
      paymentLabels: signedPayment.paymentLabels,
      dealer: signedPayment.dealer,
      paymentCategory: signedPayment.paymentCategory,
      placeholders: signedPayment.placeholders,
      signedPaymentGroup: signedPayment.signedPaymentGroup,
    });

    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      ...(signedPayment.paymentLabels ?? [])
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(this.dealersSharedCollection, signedPayment.dealer);
    this.paymentCategoriesSharedCollection = this.paymentCategoryService.addPaymentCategoryToCollectionIfMissing(
      this.paymentCategoriesSharedCollection,
      signedPayment.paymentCategory
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(signedPayment.placeholders ?? [])
    );
    this.signedPaymentsSharedCollection = this.signedPaymentService.addSignedPaymentToCollectionIfMissing(
      this.signedPaymentsSharedCollection,
      signedPayment.signedPaymentGroup
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

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing(dealers, this.editForm.get('dealer')!.value)))
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

    this.paymentCategoryService
      .query()
      .pipe(map((res: HttpResponse<IPaymentCategory[]>) => res.body ?? []))
      .pipe(
        map((paymentCategories: IPaymentCategory[]) =>
          this.paymentCategoryService.addPaymentCategoryToCollectionIfMissing(
            paymentCategories,
            this.editForm.get('paymentCategory')!.value
          )
        )
      )
      .subscribe((paymentCategories: IPaymentCategory[]) => (this.paymentCategoriesSharedCollection = paymentCategories));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.signedPaymentService
      .query()
      .pipe(map((res: HttpResponse<ISignedPayment[]>) => res.body ?? []))
      .pipe(
        map((signedPayments: ISignedPayment[]) =>
          this.signedPaymentService.addSignedPaymentToCollectionIfMissing(signedPayments, this.editForm.get('signedPaymentGroup')!.value)
        )
      )
      .subscribe((signedPayments: ISignedPayment[]) => (this.signedPaymentsSharedCollection = signedPayments));
  }

  protected createFromForm(): ISignedPayment {
    return {
      ...new SignedPayment(),
      id: this.editForm.get(['id'])!.value,
      transactionNumber: this.editForm.get(['transactionNumber'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionCurrency: this.editForm.get(['transactionCurrency'])!.value,
      transactionAmount: this.editForm.get(['transactionAmount'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      dealer: this.editForm.get(['dealer'])!.value,
      paymentCategory: this.editForm.get(['paymentCategory'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      signedPaymentGroup: this.editForm.get(['signedPaymentGroup'])!.value,
    };
  }
}

