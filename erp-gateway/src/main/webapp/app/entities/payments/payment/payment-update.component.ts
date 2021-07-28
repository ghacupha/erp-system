import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPayment, Payment } from 'app/shared/model/payments/payment.model';
import { PaymentService } from './payment.service';
import { IPaymentCalculation } from 'app/shared/model/payments/payment-calculation.model';
import { PaymentCalculationService } from 'app/entities/payments/payment-calculation/payment-calculation.service';
import { IPaymentRequisition } from 'app/shared/model/payments/payment-requisition.model';
import { PaymentRequisitionService } from 'app/entities/payments/payment-requisition/payment-requisition.service';
import { ITaxRule } from 'app/shared/model/payments/tax-rule.model';
import { TaxRuleService } from 'app/entities/payments/tax-rule/tax-rule.service';

type SelectableEntity = IPaymentCalculation | IPaymentRequisition | ITaxRule;

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  paymentcalculations: IPaymentCalculation[] = [];
  paymentrequisitions: IPaymentRequisition[] = [];
  taxrules: ITaxRule[] = [];
  paymentDateDp: any;

  editForm = this.fb.group({
    id: [],
    paymentNumber: [],
    paymentDate: [],
    paymentAmount: [],
    dealerName: [],
    paymentCategory: [],
    paymentCalculationId: [],
    paymentRequisitionId: [],
    taxRuleId: [],
  });

  constructor(
    protected paymentService: PaymentService,
    protected paymentCalculationService: PaymentCalculationService,
    protected paymentRequisitionService: PaymentRequisitionService,
    protected taxRuleService: TaxRuleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.updateForm(payment);

      this.paymentCalculationService
        .query({ 'paymentId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPaymentCalculation[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPaymentCalculation[]) => {
          if (!payment.paymentCalculationId) {
            this.paymentcalculations = resBody;
          } else {
            this.paymentCalculationService
              .find(payment.paymentCalculationId)
              .pipe(
                map((subRes: HttpResponse<IPaymentCalculation>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPaymentCalculation[]) => (this.paymentcalculations = concatRes));
          }
        });

      this.paymentRequisitionService
        .query()
        .subscribe((res: HttpResponse<IPaymentRequisition[]>) => (this.paymentrequisitions = res.body || []));

      this.taxRuleService
        .query({ 'paymentId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<ITaxRule[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ITaxRule[]) => {
          if (!payment.taxRuleId) {
            this.taxrules = resBody;
          } else {
            this.taxRuleService
              .find(payment.taxRuleId)
              .pipe(
                map((subRes: HttpResponse<ITaxRule>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ITaxRule[]) => (this.taxrules = concatRes));
          }
        });
    });
  }

  updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      paymentNumber: payment.paymentNumber,
      paymentDate: payment.paymentDate,
      paymentAmount: payment.paymentAmount,
      dealerName: payment.dealerName,
      paymentCategory: payment.paymentCategory,
      paymentCalculationId: payment.paymentCalculationId,
      paymentRequisitionId: payment.paymentRequisitionId,
      taxRuleId: payment.taxRuleId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.createFromForm();
    if (payment.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  private createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      paymentNumber: this.editForm.get(['paymentNumber'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      dealerName: this.editForm.get(['dealerName'])!.value,
      paymentCategory: this.editForm.get(['paymentCategory'])!.value,
      paymentCalculationId: this.editForm.get(['paymentCalculationId'])!.value,
      paymentRequisitionId: this.editForm.get(['paymentRequisitionId'])!.value,
      taxRuleId: this.editForm.get(['taxRuleId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
