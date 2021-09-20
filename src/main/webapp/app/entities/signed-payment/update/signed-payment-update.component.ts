import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISignedPayment, SignedPayment } from '../signed-payment.model';
import { SignedPaymentService } from '../service/signed-payment.service';
import { IPaymentLabel } from 'app/entities/payment-label/payment-label.model';
import { PaymentLabelService } from 'app/entities/payment-label/service/payment-label.service';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/erpService/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-signed-payment-update',
  templateUrl: './signed-payment-update.component.html',
})
export class SignedPaymentUpdateComponent implements OnInit {
  isSaving = false;

  paymentLabelsSharedCollection: IPaymentLabel[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    paymentCategory: [null, [Validators.required]],
    transactionNumber: [null, [Validators.required]],
    transactionDate: [null, [Validators.required]],
    transactionCurrency: [null, [Validators.required]],
    transactionAmount: [null, [Validators.required, Validators.min(0)]],
    beneficiary: [],
    paymentLabels: [],
    placeholders: [],
  });

  constructor(
    protected signedPaymentService: SignedPaymentService,
    protected paymentLabelService: PaymentLabelService,
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
      paymentCategory: signedPayment.paymentCategory,
      transactionNumber: signedPayment.transactionNumber,
      transactionDate: signedPayment.transactionDate,
      transactionCurrency: signedPayment.transactionCurrency,
      transactionAmount: signedPayment.transactionAmount,
      beneficiary: signedPayment.beneficiary,
      paymentLabels: signedPayment.paymentLabels,
      placeholders: signedPayment.placeholders,
    });

    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      ...(signedPayment.paymentLabels ?? [])
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(signedPayment.placeholders ?? [])
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

  protected createFromForm(): ISignedPayment {
    return {
      ...new SignedPayment(),
      id: this.editForm.get(['id'])!.value,
      paymentCategory: this.editForm.get(['paymentCategory'])!.value,
      transactionNumber: this.editForm.get(['transactionNumber'])!.value,
      transactionDate: this.editForm.get(['transactionDate'])!.value,
      transactionCurrency: this.editForm.get(['transactionCurrency'])!.value,
      transactionAmount: this.editForm.get(['transactionAmount'])!.value,
      beneficiary: this.editForm.get(['beneficiary'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
