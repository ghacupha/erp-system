import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPaymentLabel, PaymentLabel } from '../payment-label.model';
import { PaymentLabelService } from '../service/payment-label.service';

@Component({
  selector: 'jhi-payment-label-update',
  templateUrl: './payment-label-update.component.html',
})
export class PaymentLabelUpdateComponent implements OnInit {
  isSaving = false;

  paymentLabelsSharedCollection: IPaymentLabel[] = [];

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required]],
    comments: [],
    containingPaymentLabel: [],
  });

  constructor(protected paymentLabelService: PaymentLabelService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentLabel }) => {
      this.updateForm(paymentLabel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentLabel = this.createFromForm();
    if (paymentLabel.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentLabelService.update(paymentLabel));
    } else {
      this.subscribeToSaveResponse(this.paymentLabelService.create(paymentLabel));
    }
  }

  trackPaymentLabelById(index: number, item: IPaymentLabel): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentLabel>>): void {
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

  protected updateForm(paymentLabel: IPaymentLabel): void {
    this.editForm.patchValue({
      id: paymentLabel.id,
      description: paymentLabel.description,
      comments: paymentLabel.comments,
      containingPaymentLabel: paymentLabel.containingPaymentLabel,
    });

    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      paymentLabel.containingPaymentLabel
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentLabelService
      .query()
      .pipe(map((res: HttpResponse<IPaymentLabel[]>) => res.body ?? []))
      .pipe(
        map((paymentLabels: IPaymentLabel[]) =>
          this.paymentLabelService.addPaymentLabelToCollectionIfMissing(paymentLabels, this.editForm.get('containingPaymentLabel')!.value)
        )
      )
      .subscribe((paymentLabels: IPaymentLabel[]) => (this.paymentLabelsSharedCollection = paymentLabels));
  }

  protected createFromForm(): IPaymentLabel {
    return {
      ...new PaymentLabel(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      containingPaymentLabel: this.editForm.get(['containingPaymentLabel'])!.value,
    };
  }
}
