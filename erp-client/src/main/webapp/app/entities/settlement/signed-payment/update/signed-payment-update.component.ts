///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISignedPayment, SignedPayment } from '../signed-payment.model';
import { SignedPaymentService } from '../service/signed-payment.service';
import { IPaymentLabel } from 'app/entities/settlement/payment-label/payment-label.model';
import { PaymentLabelService } from 'app/entities/settlement/payment-label/service/payment-label.service';
import { IPaymentCategory } from 'app/entities/settlement/payment-category/payment-category.model';
import { PaymentCategoryService } from 'app/entities/settlement/payment-category/service/payment-category.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { CurrencyTypes } from 'app/entities/enumerations/currency-types.model';

@Component({
  selector: 'jhi-signed-payment-update',
  templateUrl: './signed-payment-update.component.html',
})
export class SignedPaymentUpdateComponent implements OnInit {
  isSaving = false;
  currencyTypesValues = Object.keys(CurrencyTypes);

  paymentLabelsSharedCollection: IPaymentLabel[] = [];
  paymentCategoriesSharedCollection: IPaymentCategory[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  signedPaymentsSharedCollection: ISignedPayment[] = [];

  editForm = this.fb.group({
    id: [],
    transactionNumber: [null, [Validators.required]],
    transactionDate: [null, [Validators.required]],
    transactionCurrency: [null, [Validators.required]],
    transactionAmount: [null, [Validators.required, Validators.min(0)]],
    dealerName: [],
    fileUploadToken: [],
    compilationToken: [],
    paymentLabels: [],
    paymentCategory: [],
    placeholders: [],
    signedPaymentGroup: [],
  });

  constructor(
    protected signedPaymentService: SignedPaymentService,
    protected paymentLabelService: PaymentLabelService,
    protected paymentCategoryService: PaymentCategoryService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
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
      dealerName: signedPayment.dealerName,
      fileUploadToken: signedPayment.fileUploadToken,
      compilationToken: signedPayment.compilationToken,
      paymentLabels: signedPayment.paymentLabels,
      paymentCategory: signedPayment.paymentCategory,
      placeholders: signedPayment.placeholders,
      signedPaymentGroup: signedPayment.signedPaymentGroup,
    });

    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      ...(signedPayment.paymentLabels ?? [])
    );
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
      dealerName: this.editForm.get(['dealerName'])!.value,
      fileUploadToken: this.editForm.get(['fileUploadToken'])!.value,
      compilationToken: this.editForm.get(['compilationToken'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      paymentCategory: this.editForm.get(['paymentCategory'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      signedPaymentGroup: this.editForm.get(['signedPaymentGroup'])!.value,
    };
  }
}
