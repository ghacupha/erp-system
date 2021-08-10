import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPaymentCategory, PaymentCategory } from '../payment-category.model';
import { PaymentCategoryService } from '../service/payment-category.service';

@Component({
  selector: 'gha-payment-category-update',
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
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentCategory }) => {
      this.updateForm(paymentCategory);
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentCategory>>): void {
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

  protected updateForm(paymentCategory: IPaymentCategory): void {
    this.editForm.patchValue({
      id: paymentCategory.id,
      categoryName: paymentCategory.categoryName,
      categoryDescription: paymentCategory.categoryDescription,
      categoryType: paymentCategory.categoryType,
    });
  }

  protected createFromForm(): IPaymentCategory {
    return {
      ...new PaymentCategory(),
      id: this.editForm.get(['id'])!.value,
      categoryName: this.editForm.get(['categoryName'])!.value,
      categoryDescription: this.editForm.get(['categoryDescription'])!.value,
      categoryType: this.editForm.get(['categoryType'])!.value,
    };
  }
}
