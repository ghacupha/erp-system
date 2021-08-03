import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPaymentCategory, PaymentCategory } from 'app/shared/model/payments/payment-category.model';
import { PaymentCategoryService } from './payment-category.service';

@Component({
  selector: 'jhi-payment-category-update',
  templateUrl: './payment-category-update.component.html',
})
export class PaymentCategoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    categoryName: [null, [Validators.required]],
    categoryDescription: [],
    categoryType: [null, [Validators.required]],
  });

  constructor(
    protected paymentCategoryService: PaymentCategoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentCategory }) => {
      this.updateForm(paymentCategory);
    });
  }

  updateForm(paymentCategory: IPaymentCategory): void {
    this.editForm.patchValue({
      id: paymentCategory.id,
      categoryName: paymentCategory.categoryName,
      categoryDescription: paymentCategory.categoryDescription,
      categoryType: paymentCategory.categoryType,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentCategory = this.createFromForm();
    if (paymentCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentCategoryService.update(paymentCategory));
    } else {
      this.subscribeToSaveResponse(this.paymentCategoryService.create(paymentCategory));
    }
  }

  private createFromForm(): IPaymentCategory {
    return {
      ...new PaymentCategory(),
      id: this.editForm.get(['id'])!.value,
      categoryName: this.editForm.get(['categoryName'])!.value,
      categoryDescription: this.editForm.get(['categoryDescription'])!.value,
      categoryType: this.editForm.get(['categoryType'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentCategory>>): void {
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
}
