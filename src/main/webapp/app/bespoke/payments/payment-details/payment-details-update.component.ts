import {Component, OnInit} from "@angular/core";
import {Observable} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {HttpResponse} from "@angular/common/http";
import {FormBuilder, Validators} from "@angular/forms";
import {finalize, map} from "rxjs/operators";
import { IPaymentRequisition } from '../../../entities/payments/payment-requisition/payment-requisition.model';
import { ITaxRule } from '../../../entities/payments/tax-rule/tax-rule.model';
import { IPaymentCategory } from '../../../entities/payments/payment-category/payment-category.model';
import { IPaymentCalculation } from '../../../entities/payments/payment-calculation/payment-calculation.model';
import { PaymentService } from '../../../entities/payments/payment/service/payment.service';
import { PaymentRequisitionService } from '../../../entities/payments/payment-requisition/service/payment-requisition.service';
import { TaxRuleService } from '../../../entities/payments/tax-rule/service/tax-rule.service';
import { PaymentCategoryService } from '../../../entities/payments/payment-category/service/payment-category.service';
import { PaymentCalculationService } from '../../../entities/payments/payment-calculation/service/payment-calculation.service';
import { IPayment, Payment } from '../../../entities/payments/payment/payment.model';

@Component({
  selector: 'gha-payment-details-update',
  templateUrl: './payment-details-update.component.html',
})
export class PaymentDetailsUpdateComponent implements OnInit {
  isSaving = false;

  paymentCategoriesSharedCollection: IPaymentCategory[] = [];
  taxRulesCollection: ITaxRule[] = [];
  paymentCalculationsCollection: IPaymentCalculation[] = [];
  paymentRequisitionsCollection: IPaymentRequisition[] = [];

  editForm = this.fb.group({
    id: [],
    paymentNumber: [],
    paymentDate: [],
    paymentAmount: [],
    description: [],
    currency: [null, [Validators.required]],
    conversionRate: [null, [Validators.required, Validators.min(1.0)]],
    paymentCategory: [],
    taxRule: [],
    paymentCalculation: [],
    paymentRequisition: [],
  });

  constructor(
    protected paymentService: PaymentService,
    protected paymentCategoryService: PaymentCategoryService,
    protected taxRuleService: TaxRuleService,
    protected paymentCalculationService: PaymentCalculationService,
    protected paymentRequisitionService: PaymentRequisitionService,
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

  trackPaymentCategoryById(index: number, item: IPaymentCategory): number {
    return item.id!;
  }

  trackTaxRuleById(index: number, item: ITaxRule): number {
    return item.id!;
  }

  trackPaymentCalculationById(index: number, item: IPaymentCalculation): number {
    return item.id!;
  }

  trackPaymentRequisitionById(index: number, item: IPaymentRequisition): number {
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
      currency: payment.currency,
      conversionRate: payment.conversionRate,
      paymentCategory: payment.paymentCategory,
      taxRule: payment.taxRule,
      paymentCalculation: payment.paymentCalculation,
      paymentRequisition: payment.paymentRequisition,
    });

    this.paymentCategoriesSharedCollection = this.paymentCategoryService.addPaymentCategoryToCollectionIfMissing(
      this.paymentCategoriesSharedCollection,
      payment.paymentCategory
    );
    this.taxRulesCollection = this.taxRuleService.addTaxRuleToCollectionIfMissing(this.taxRulesCollection, payment.taxRule);
    this.paymentCalculationsCollection = this.paymentCalculationService.addPaymentCalculationToCollectionIfMissing(
      this.paymentCalculationsCollection,
      payment.paymentCalculation
    );
    this.paymentRequisitionsCollection = this.paymentRequisitionService.addPaymentRequisitionToCollectionIfMissing(
      this.paymentRequisitionsCollection,
      payment.paymentRequisition
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

    this.taxRuleService
      .query({ 'paymentId.specified': 'false' })
      .pipe(map((res: HttpResponse<ITaxRule[]>) => res.body ?? []))
      .pipe(
        map((taxRules: ITaxRule[]) => this.taxRuleService.addTaxRuleToCollectionIfMissing(taxRules, this.editForm.get('taxRule')!.value))
      )
      .subscribe((taxRules: ITaxRule[]) => (this.taxRulesCollection = taxRules));

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

    this.paymentRequisitionService
      .query({ 'paymentId.specified': 'false' })
      .pipe(map((res: HttpResponse<IPaymentRequisition[]>) => res.body ?? []))
      .pipe(
        map((paymentRequisitions: IPaymentRequisition[]) =>
          this.paymentRequisitionService.addPaymentRequisitionToCollectionIfMissing(
            paymentRequisitions,
            this.editForm.get('paymentRequisition')!.value
          )
        )
      )
      .subscribe((paymentRequisitions: IPaymentRequisition[]) => (this.paymentRequisitionsCollection = paymentRequisitions));
  }

  protected createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      paymentNumber: this.editForm.get(['paymentNumber'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      description: this.editForm.get(['description'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      conversionRate: this.editForm.get(['conversionRate'])!.value,
      paymentCategory: this.editForm.get(['paymentCategory'])!.value,
      taxRule: this.editForm.get(['taxRule'])!.value,
      paymentCalculation: this.editForm.get(['paymentCalculation'])!.value,
      paymentRequisition: this.editForm.get(['paymentRequisition'])!.value,
    };
  }
}
