///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

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
