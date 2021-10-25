import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPayment, Payment } from '../payment.model';
import { PaymentService } from '../service/payment.service';
import { IPaymentLabel } from 'app/entities/payment-label/payment-label.model';
import { PaymentLabelService } from 'app/entities/payment-label/service/payment-label.service';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';
import { DealerService } from 'app/entities/dealers/dealer/service/dealer.service';
import { IPaymentCategory } from 'app/entities/payments/payment-category/payment-category.model';
import { PaymentCategoryService } from 'app/entities/payments/payment-category/service/payment-category.service';
import { ITaxRule } from 'app/entities/payments/tax-rule/tax-rule.model';
import { TaxRuleService } from 'app/entities/payments/tax-rule/service/tax-rule.service';
import { IPaymentCalculation } from 'app/entities/payments/payment-calculation/payment-calculation.model';
import { PaymentCalculationService } from 'app/entities/payments/payment-calculation/service/payment-calculation.service';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/erpService/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;

  paymentLabelsSharedCollection: IPaymentLabel[] = [];
  dealersSharedCollection: IDealer[] = [];
  paymentCategoriesSharedCollection: IPaymentCategory[] = [];
  taxRulesSharedCollection: ITaxRule[] = [];
  paymentCalculationsCollection: IPaymentCalculation[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  paymentsSharedCollection: IPayment[] = [];

  editForm = this.fb.group({
    id: [],
    paymentNumber: [],
    paymentDate: [],
    invoicedAmount: [],
    disbursementCost: [],
    vatableAmount: [],
    paymentAmount: [],
    description: [],
    settlementCurrency: [null, [Validators.required]],
    conversionRate: [null, [Validators.required, Validators.min(1.0)]],
    fileUploadToken: [],
    compilationToken: [],
    paymentLabels: [],
    dealer: [],
    paymentCategory: [],
    taxRule: [],
    paymentCalculation: [],
    placeholders: [],
    paymentGroup: [],
  });

  constructor(
    protected paymentService: PaymentService,
    protected paymentLabelService: PaymentLabelService,
    protected dealerService: DealerService,
    protected paymentCategoryService: PaymentCategoryService,
    protected taxRuleService: TaxRuleService,
    protected paymentCalculationService: PaymentCalculationService,
    protected placeholderService: PlaceholderService,
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

  trackPaymentLabelById(index: number, item: IPaymentLabel): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
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

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackPaymentById(index: number, item: IPayment): number {
    return item.id!;
  }

  getSelectedPaymentLabel(option: IPaymentLabel, selectedVals?: IPaymentLabel[]): IPaymentLabel {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedPlaceholder(option: IPlaceholder, selectedVals?: IPlaceholder[]): IPlaceholder {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
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
      invoicedAmount: payment.invoicedAmount,
      disbursementCost: payment.disbursementCost,
      vatableAmount: payment.vatableAmount,
      paymentAmount: payment.paymentAmount,
      description: payment.description,
      settlementCurrency: payment.settlementCurrency,
      conversionRate: payment.conversionRate,
      fileUploadToken: payment.fileUploadToken,
      compilationToken: payment.compilationToken,
      paymentLabels: payment.paymentLabels,
      dealer: payment.dealer,
      paymentCategory: payment.paymentCategory,
      taxRule: payment.taxRule,
      paymentCalculation: payment.paymentCalculation,
      placeholders: payment.placeholders,
      paymentGroup: payment.paymentGroup,
    });

    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      ...(payment.paymentLabels ?? [])
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(this.dealersSharedCollection, payment.dealer);
    this.paymentCategoriesSharedCollection = this.paymentCategoryService.addPaymentCategoryToCollectionIfMissing(
      this.paymentCategoriesSharedCollection,
      payment.paymentCategory
    );
    this.taxRulesSharedCollection = this.taxRuleService.addTaxRuleToCollectionIfMissing(this.taxRulesSharedCollection, payment.taxRule);
    this.paymentCalculationsCollection = this.paymentCalculationService.addPaymentCalculationToCollectionIfMissing(
      this.paymentCalculationsCollection,
      payment.paymentCalculation
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(payment.placeholders ?? [])
    );
    this.paymentsSharedCollection = this.paymentService.addPaymentToCollectionIfMissing(
      this.paymentsSharedCollection,
      payment.paymentGroup
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentLabelService
      .query()
      .pipe(map((res: HttpResponse<IPaymentLabel[]>) => res.body ?? []))
      .pipe(
        map((paymentLabels: IPaymentLabel[]) =>
          this.paymentLabelService.addPaymentLabelToCollectionIfMissing(paymentLabels, ...(this.editForm.get('paymentLabels')!.value ?? []))
        )
      )
      .subscribe((paymentLabels: IPaymentLabel[]) => (this.paymentLabelsSharedCollection = paymentLabels));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing(dealers, this.editForm.get('dealer')!.value)))
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

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
      .query()
      .pipe(map((res: HttpResponse<ITaxRule[]>) => res.body ?? []))
      .pipe(
        map((taxRules: ITaxRule[]) => this.taxRuleService.addTaxRuleToCollectionIfMissing(taxRules, this.editForm.get('taxRule')!.value))
      )
      .subscribe((taxRules: ITaxRule[]) => (this.taxRulesSharedCollection = taxRules));

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

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.paymentService
      .query()
      .pipe(map((res: HttpResponse<IPayment[]>) => res.body ?? []))
      .pipe(
        map((payments: IPayment[]) =>
          this.paymentService.addPaymentToCollectionIfMissing(payments, this.editForm.get('paymentGroup')!.value)
        )
      )
      .subscribe((payments: IPayment[]) => (this.paymentsSharedCollection = payments));
  }

  protected createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      paymentNumber: this.editForm.get(['paymentNumber'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      invoicedAmount: this.editForm.get(['invoicedAmount'])!.value,
      disbursementCost: this.editForm.get(['disbursementCost'])!.value,
      vatableAmount: this.editForm.get(['vatableAmount'])!.value,
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      description: this.editForm.get(['description'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      conversionRate: this.editForm.get(['conversionRate'])!.value,
      fileUploadToken: this.editForm.get(['fileUploadToken'])!.value,
      compilationToken: this.editForm.get(['compilationToken'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      dealer: this.editForm.get(['dealer'])!.value,
      paymentCategory: this.editForm.get(['paymentCategory'])!.value,
      taxRule: this.editForm.get(['taxRule'])!.value,
      paymentCalculation: this.editForm.get(['paymentCalculation'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      paymentGroup: this.editForm.get(['paymentGroup'])!.value,
    };
  }
}
