import {Component, OnInit} from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPayment, Payment } from '../payment.model';
import { PaymentService } from '../service/payment.service';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';
import { DealerService } from 'app/entities/dealers/dealer/service/dealer.service';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/erpService/placeholder/service/placeholder.service';
import {IPaymentLabel} from '../../../payment-label/payment-label.model';
import {IPaymentCategory} from '../../payment-category/payment-category.model';
import {IPaymentCalculation} from '../../payment-calculation/payment-calculation.model';
import {ITaxRule} from '../../tax-rule/tax-rule.model';
import {PaymentLabelService} from '../../../payment-label/service/payment-label.service';
import {PaymentCategoryService} from '../../payment-category/service/payment-category.service';
import {TaxRuleService} from '../../tax-rule/service/tax-rule.service';
import {PaymentCalculationService} from '../../payment-calculation/service/payment-calculation.service';
import {select, Store} from "@ngrx/store";
import {State} from "../../../../store/global-store.definition";
import {
  copyingPaymentStatus,
  creatingPaymentStatus,
  editingPaymentStatus, updateSelectedPayment
} from "../../../../store/selectors/update-menu-status.selectors";
import {
  paymentCopyButtonClicked, paymentSaveButtonClicked,
  paymentUpdateButtonClicked,
  paymentUpdateCancelButtonClicked, paymentUpdateConcluded, paymentUpdateErrorHasOccurred
} from "../../../../store/actions/update-menu-status.actions";
import {
  dealerCategory,
  dealerPaymentSelectedDealer,
  dealerPaymentStatus
} from "../../../../store/selectors/dealer-workflows-status.selectors";
import {Dealer} from "../../../dealers/dealer/dealer.model";
import {
  paymentToDealerCompleted,
  paymentToDealerReset
} from "../../../../store/actions/dealer-workflows-status.actions";
import {
  dealerInvoicePaymentLabels,
  dealerInvoicePaymentState, dealerInvoicePlaceholders, dealerInvoiceSelected,
  dealerInvoiceSelectedDealer
} from "../../../../store/selectors/dealer-invoice-worklows-status.selectors";
import {IInvoice, Invoice} from "../../invoice/invoice.model";
import {
  dealerInvoiceStateReset,
  paymentToInvoiceDealerConcluded,
} from "../../../../store/actions/dealer-invoice-workflows-status.actions";
import {InvoiceService} from "../../invoice/service/invoice.service";
import {NGXLogger} from "ngx-logger";

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

  selectedPayment: IPayment = {...new Payment()};
  weAreCopyingAPayment = false;
  weAreEditingAPayment = false;
  weAreCreatingAPayment = false;
  weArePayingADealer = false;
  selectedDealer: IDealer= {...new Dealer()};
  // We emphatically should not create a new category like the dealer above
  // TODO Get default category here
  dealerCategory: IPaymentCategory = {};

  weArePayingAnInvoiceDealer = false;
  paymentLabels: IPaymentLabel[] = [];
  placeholders: IPlaceholder[] = [];
  selectedInvoice: IInvoice = {...new Invoice()};

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
    protected fb: FormBuilder,
    protected store: Store<State>,
    protected invoiceService: InvoiceService,
    protected router: Router,
    protected log: NGXLogger
  ) {

    this.store.pipe(select(copyingPaymentStatus)).subscribe(stat => this.weAreCopyingAPayment = stat);
    this.store.pipe(select(editingPaymentStatus)).subscribe(stat => this.weAreEditingAPayment = stat);
    this.store.pipe(select(creatingPaymentStatus)).subscribe(stat => this.weAreCreatingAPayment = stat);
    this.store.pipe(select(updateSelectedPayment)).subscribe(pyt => this.selectedPayment = pyt);
    this.store.pipe(select(dealerPaymentStatus)).subscribe(payingDealer => this.weArePayingADealer = payingDealer);
    this.store.pipe(select(dealerPaymentSelectedDealer)).subscribe(dealer => this.selectedDealer = dealer);
    this.store.pipe(select(dealerCategory)).subscribe(category => {
      if (category) {
        this.dealerCategory = category;
      }
    });
    this.store.select<IDealer>(dealerInvoiceSelectedDealer).subscribe(dealr => {
      this.selectedDealer = dealr;
    });

    this.store.select<boolean>(dealerInvoicePaymentState).subscribe(state => {
      this.weArePayingAnInvoiceDealer = state;

      if (state) {

        this.store.select<IPaymentLabel[]>(dealerInvoicePaymentLabels).subscribe(labels => {
          this.paymentLabels = labels;
        });

        this.store.select<IPlaceholder[]>(dealerInvoicePlaceholders).subscribe(placeholders => {
          this.placeholders = placeholders;
        });
      }
    });

    // TODO CHECK THIS SELECTOR IS WORKING
    this.store.select<IInvoice>(dealerInvoiceSelected).subscribe(inv => {
      this.selectedInvoice = inv;
    });
  }

  ngOnInit(): void {
    if (this.weAreEditingAPayment || this.weAreCopyingAPayment) {
      this.editFormUpdate(this.selectedPayment);
    } else if (this.weArePayingAnInvoiceDealer) {
      this.activatedRoute.data.subscribe(({payment}) => {
        this.invoicePaymentUpdate(payment, this.selectedDealer, this.dealerCategory, this.paymentLabels, this.placeholders);
      });
    }else {
      this.activatedRoute.data.subscribe(({payment}) => {
        this.updateForm(payment, this.selectedDealer, this.dealerCategory);
      });
    }
    this.loadRelationshipsOptions();
  }

  previousState(): void {
    this.store.dispatch(paymentUpdateCancelButtonClicked());
    this.store.dispatch(paymentToDealerReset());
    this.store.dispatch(dealerInvoiceStateReset())
    window.history.back();
  }

  addInvoiceDealer(): void {
    this.isSaving = true;
    const payment = this.createFromForm();

    this.log.debug(`Payment created ID: ${payment.paymentNumber}, DATED: ${payment.paymentDate?.format()}`);

    // TODO DISPATCH NEWLY CREATED PAYMENT TO UPDATE THE INVOICE
    // TODO CLEANUP THE STATE

    if (payment.id !== undefined) {
      this.subscribeToInvoiceDealerUpdate(this.paymentService.update(payment));
    } else {
      this.subscribeToInvoiceDealerUpdate(this.paymentService.create(payment));
    }

  }

  subscribeToInvoiceDealerUpdate(result: Observable<HttpResponse<IPayment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onInvoiceDealerUpdateSuccess(result),
      () => this.onSaveError()
    );
  }

  onInvoiceDealerUpdateSuccess(result: Observable<HttpResponse<IPayment>>): void {
    this.isSaving = false;
    result.subscribe(res => {

      // TODO prevent payment duplication
      if (!this.weArePayingAnInvoiceDealer) {
        this.store.dispatch(paymentSaveButtonClicked());
      }

      this.selectedInvoice = {
        ...this.selectedInvoice,
        payment: res.body,
      };

      this.invoiceService.update(this.selectedInvoice).subscribe( invoice => {
        this.router.navigate(['/erp/invoice', invoice.body?.id, 'view']);
      });
    });
  }

  edit(): void {
    this.save();
    this.store.dispatch(paymentUpdateButtonClicked())
  }

  copy(): void {
    this.isSaving = true;
    const payment = this.copyFromForm();
    this.subscribeToCopyResponse(this.paymentService.create(payment));
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

  invoicePaymentUpdate(payment: IPayment, dealer: IDealer, paymentCategory: IPaymentCategory, paymentLabels: IPaymentLabel[], placeholders: IPlaceholder[]): void {
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
      paymentLabels: [...(dealer.paymentLabels ?? paymentLabels)],
      dealer,
      paymentCategory,
      taxRule: payment.taxRule,
      paymentCalculation: payment.paymentCalculation,
      placeholders: [...(dealer.placeholders ?? placeholders)],
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



  updateForm(payment: IPayment, dealer: IDealer, paymentCategory: IPaymentCategory): void {
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
      paymentLabels: payment.paymentLabels,
      dealer,
      paymentCategory,
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

  editFormUpdate(payment: IPayment): void {
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

  onSaveError(): void {
    // Api for inheritance.
    this.store.dispatch(paymentToDealerReset());
    this.store.dispatch(paymentUpdateErrorHasOccurred());
    this.isSaving = false;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected subscribeToCopyResponse(result: Observable<HttpResponse<IPayment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onCopySuccess(): void {
    this.isSaving = false;
    this.store.dispatch(paymentCopyButtonClicked())
    this.previousState();
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.store.dispatch(paymentSaveButtonClicked())
    this.previousState();
  }

  protected onSaveFinalize(): void {
    // TODO conclude payment to invoiceDealer
    if (this.weArePayingAnInvoiceDealer) {
      this.store.dispatch(paymentToInvoiceDealerConcluded())
    } else {
      this.store.dispatch(paymentToDealerCompleted());
      this.store.dispatch(paymentUpdateConcluded());
    }
    this.isSaving = false;
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
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      dealer: this.editForm.get(['dealer'])!.value,
      paymentCategory: this.editForm.get(['paymentCategory'])!.value,
      taxRule: this.editForm.get(['taxRule'])!.value,
      paymentCalculation: this.editForm.get(['paymentCalculation'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      paymentGroup: this.editForm.get(['paymentGroup'])!.value,
    };
  }

  protected copyFromForm(): IPayment {
    return {
      ...new Payment(),
      paymentNumber: this.editForm.get(['paymentNumber'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      invoicedAmount: this.editForm.get(['invoicedAmount'])!.value,
      disbursementCost: this.editForm.get(['disbursementCost'])!.value,
      vatableAmount: this.editForm.get(['vatableAmount'])!.value,
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      description: this.editForm.get(['description'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      conversionRate: this.editForm.get(['conversionRate'])!.value,
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
