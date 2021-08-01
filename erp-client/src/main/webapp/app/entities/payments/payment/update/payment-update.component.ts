import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPayment, Payment } from '../payment.model';
import { PaymentService } from '../service/payment.service';
import { IPaymentRequisition } from 'app/entities/payments/payment-requisition/payment-requisition.model';
import { PaymentRequisitionService } from 'app/entities/payments/payment-requisition/service/payment-requisition.service';
import { ITaxRule } from 'app/entities/payments/tax-rule/tax-rule.model';
import { TaxRuleService } from 'app/entities/payments/tax-rule/service/tax-rule.service';
import { IPaymentCategory } from 'app/entities/payments/payment-category/payment-category.model';
import { PaymentCategoryService } from 'app/entities/payments/payment-category/service/payment-category.service';
import { IPaymentCalculation } from 'app/entities/payments/payment-calculation/payment-calculation.model';
import { PaymentCalculationService } from 'app/entities/payments/payment-calculation/service/payment-calculation.service';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;

  paymentRequisitionsSharedCollection: IPaymentRequisition[] = [];
  taxRulesCollection: ITaxRule[] = [];
  paymentCategoriesCollection: IPaymentCategory[] = [];
  paymentCalculationsCollection: IPaymentCalculation[] = [];

  editForm = this.fb.group({
    id: [],
    paymentNumber: [],
    paymentDate: [],
    paymentAmount: [],
    description: [],
    paymentRequisition: [],
    taxRule: [],
    paymentCategory: [null, Validators.required],
    paymentCalculation: [null, Validators.required],
  });

  constructor(
    protected paymentService: PaymentService,
    protected paymentRequisitionService: PaymentRequisitionService,
    protected taxRuleService: TaxRuleService,
    protected paymentCategoryService: PaymentCategoryService,
    protected paymentCalculationService: PaymentCalculationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.updateForm(payment);

      this.loadRelationshipsOptions();
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

  trackPaymentRequisitionById(index: number, item: IPaymentRequisition): number {
    return item.id!;
  }

  trackTaxRuleById(index: number, item: ITaxRule): number {
    return item.id!;
  }

  trackPaymentCategoryById(index: number, item: IPaymentCategory): number {
    return item.id!;
  }

  trackPaymentCalculationById(index: number, item: IPaymentCalculation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
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

  protected updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      paymentNumber: payment.paymentNumber,
      paymentDate: payment.paymentDate,
      paymentAmount: payment.paymentAmount,
      description: payment.description,
      paymentRequisition: payment.paymentRequisition,
      taxRule: payment.taxRule,
      paymentCategory: payment.paymentCategory,
      paymentCalculation: payment.paymentCalculation,
    });

    this.paymentRequisitionsSharedCollection = this.paymentRequisitionService.addPaymentRequisitionToCollectionIfMissing(
      this.paymentRequisitionsSharedCollection,
      payment.paymentRequisition
    );
    this.taxRulesCollection = this.taxRuleService.addTaxRuleToCollectionIfMissing(this.taxRulesCollection, payment.taxRule);
    this.paymentCategoriesCollection = this.paymentCategoryService.addPaymentCategoryToCollectionIfMissing(
      this.paymentCategoriesCollection,
      payment.paymentCategory
    );
    this.paymentCalculationsCollection = this.paymentCalculationService.addPaymentCalculationToCollectionIfMissing(
      this.paymentCalculationsCollection,
      payment.paymentCalculation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentRequisitionService
      .query()
      .pipe(map((res: HttpResponse<IPaymentRequisition[]>) => res.body ?? []))
      .pipe(
        map((paymentRequisitions: IPaymentRequisition[]) =>
          this.paymentRequisitionService.addPaymentRequisitionToCollectionIfMissing(
            paymentRequisitions,
            this.editForm.get('paymentRequisition')!.value
          )
        )
      )
      .subscribe((paymentRequisitions: IPaymentRequisition[]) => (this.paymentRequisitionsSharedCollection = paymentRequisitions));

    this.taxRuleService
      .query({ 'paymentId.specified': 'false' })
      .pipe(map((res: HttpResponse<ITaxRule[]>) => res.body ?? []))
      .pipe(
        map((taxRules: ITaxRule[]) => this.taxRuleService.addTaxRuleToCollectionIfMissing(taxRules, this.editForm.get('taxRule')!.value))
      )
      .subscribe((taxRules: ITaxRule[]) => (this.taxRulesCollection = taxRules));

    this.paymentCategoryService
      .query({ 'paymentId.specified': 'false' })
      .pipe(map((res: HttpResponse<IPaymentCategory[]>) => res.body ?? []))
      .pipe(
        map((paymentCategories: IPaymentCategory[]) =>
          this.paymentCategoryService.addPaymentCategoryToCollectionIfMissing(
            paymentCategories,
            this.editForm.get('paymentCategory')!.value
          )
        )
      )
      .subscribe((paymentCategories: IPaymentCategory[]) => (this.paymentCategoriesCollection = paymentCategories));

    this.paymentCalculationService
      .query({ 'paymentId.specified': 'false' })
      .pipe(map((res: HttpResponse<IPaymentCalculation[]>) => res.body ?? []))
      .pipe(
        map((paymentCalculations: IPaymentCalculation[]) =>
          this.paymentCalculationService.addPaymentCalculationToCollectionIfMissing(
            paymentCalculations,
            this.editForm.get('paymentCalculation')!.value
          )
        )
      )
      .subscribe((paymentCalculations: IPaymentCalculation[]) => (this.paymentCalculationsCollection = paymentCalculations));
  }

  protected createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      paymentNumber: this.editForm.get(['paymentNumber'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      description: this.editForm.get(['description'])!.value,
      paymentRequisition: this.editForm.get(['paymentRequisition'])!.value,
      taxRule: this.editForm.get(['taxRule'])!.value,
      paymentCategory: this.editForm.get(['paymentCategory'])!.value,
      paymentCalculation: this.editForm.get(['paymentCalculation'])!.value,
    };
  }
}
