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

import { IPaymentCalculation, PaymentCalculation } from 'app/shared/model/payments/payment-calculation.model';
import { PaymentCalculationService } from './payment-calculation.service';

@Component({
  selector: 'jhi-payment-calculation-update',
  templateUrl: './payment-calculation-update.component.html',
})
export class PaymentCalculationUpdateComponent implements OnInit {
  isSaving = false;
  paymentDateDp: any;

  editForm = this.fb.group({
    id: [],
    paymentNumber: [],
    paymentDate: [],
    paymentCategory: [],
    paymentExpense: [],
    withholdingVAT: [],
    withholdingTax: [],
    paymentAmount: [],
  });

  constructor(
    protected paymentCalculationService: PaymentCalculationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentCalculation }) => {
      this.updateForm(paymentCalculation);
    });
  }

  updateForm(paymentCalculation: IPaymentCalculation): void {
    this.editForm.patchValue({
      id: paymentCalculation.id,
      paymentNumber: paymentCalculation.paymentNumber,
      paymentDate: paymentCalculation.paymentDate,
      paymentCategory: paymentCalculation.paymentCategory,
      paymentExpense: paymentCalculation.paymentExpense,
      withholdingVAT: paymentCalculation.withholdingVAT,
      withholdingTax: paymentCalculation.withholdingTax,
      paymentAmount: paymentCalculation.paymentAmount,
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

  private createFromForm(): IPaymentCalculation {
    return {
      ...new PaymentCalculation(),
      id: this.editForm.get(['id'])!.value,
      paymentNumber: this.editForm.get(['paymentNumber'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      paymentCategory: this.editForm.get(['paymentCategory'])!.value,
      paymentExpense: this.editForm.get(['paymentExpense'])!.value,
      withholdingVAT: this.editForm.get(['withholdingVAT'])!.value,
      withholdingTax: this.editForm.get(['withholdingTax'])!.value,
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentCalculation>>): void {
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
