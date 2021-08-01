import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPaymentCalculation, PaymentCalculation } from '../payment-calculation.model';
import { PaymentCalculationService } from '../service/payment-calculation.service';

@Component({
  selector: 'jhi-payment-calculation-update',
  templateUrl: './payment-calculation-update.component.html',
})
export class PaymentCalculationUpdateComponent implements OnInit {
  isSaving = false;

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
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentCalculation }) => {
      this.updateForm(paymentCalculation);
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
      paymentCategory: paymentCalculation.paymentCategory,
      paymentExpense: paymentCalculation.paymentExpense,
      withholdingVAT: paymentCalculation.withholdingVAT,
      withholdingTax: paymentCalculation.withholdingTax,
      paymentAmount: paymentCalculation.paymentAmount,
    });
  }

  protected createFromForm(): IPaymentCalculation {
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
}
