import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPaymentCalculation, PaymentCalculation } from '../payment-calculation.model';
import { PaymentCalculationService } from '../service/payment-calculation.service';
import { IPaymentCategory } from 'app/entities/payments/payment-category/payment-category.model';
import { PaymentCategoryService } from 'app/entities/payments/payment-category/service/payment-category.service';

@Component({
  selector: 'jhi-payment-calculation-update',
  templateUrl: './payment-calculation-update.component.html',
})
export class PaymentCalculationUpdateComponent implements OnInit {
  isSaving = false;

  paymentCategoriesSharedCollection: IPaymentCategory[] = [];

  editForm = this.fb.group({
    id: [],
    paymentNumber: [],
    paymentDate: [],
    paymentExpense: [],
    withholdingVAT: [],
    withholdingTax: [],
    paymentAmount: [],
    paymentCategory: [],
  });

  constructor(
    protected paymentCalculationService: PaymentCalculationService,
    protected paymentCategoryService: PaymentCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentCalculation }) => {
      this.updateForm(paymentCalculation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentCalculation = this.createFromForm();
    if (paymentCalculation.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentCalculationService.update(paymentCalculation));
    } else {
      this.subscribeToSaveResponse(this.paymentCalculationService.create(paymentCalculation));
    }
  }

  trackPaymentCategoryById(index: number, item: IPaymentCategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentCalculation>>): void {
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

  protected updateForm(paymentCalculation: IPaymentCalculation): void {
    this.editForm.patchValue({
      id: paymentCalculation.id,
      paymentNumber: paymentCalculation.paymentNumber,
      paymentDate: paymentCalculation.paymentDate,
      paymentExpense: paymentCalculation.paymentExpense,
      withholdingVAT: paymentCalculation.withholdingVAT,
      withholdingTax: paymentCalculation.withholdingTax,
      paymentAmount: paymentCalculation.paymentAmount,
      paymentCategory: paymentCalculation.paymentCategory,
    });

    this.paymentCategoriesSharedCollection = this.paymentCategoryService.addPaymentCategoryToCollectionIfMissing(
      this.paymentCategoriesSharedCollection,
      paymentCalculation.paymentCategory
    );
  }

  protected loadRelationshipsOptions(): void {
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
  }

  protected createFromForm(): IPaymentCalculation {
    return {
      ...new PaymentCalculation(),
      id: this.editForm.get(['id'])!.value,
      paymentNumber: this.editForm.get(['paymentNumber'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      paymentExpense: this.editForm.get(['paymentExpense'])!.value,
      withholdingVAT: this.editForm.get(['withholdingVAT'])!.value,
      withholdingTax: this.editForm.get(['withholdingTax'])!.value,
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      paymentCategory: this.editForm.get(['paymentCategory'])!.value,
    };
  }
}
