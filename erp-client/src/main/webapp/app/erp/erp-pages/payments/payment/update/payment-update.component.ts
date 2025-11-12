///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import {Component, OnInit} from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import { concat, Observable, of, Subject } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, filter, finalize, map, switchMap, tap } from 'rxjs/operators';

import {ITaxRule} from '../../tax-rule/tax-rule.model';
import {select, Store} from "@ngrx/store";
import {State} from "../../../../store/global-store.definition";
import {
  copyingPaymentStatus,
  creatingPaymentStatus,
  editingPaymentStatus,
  updateSelectedPayment
} from "../../../../store/selectors/update-menu-status.selectors";
import {
  paymentCopyButtonClicked,
  paymentSaveButtonClicked,
  paymentUpdateButtonClicked,
  paymentUpdateCancelButtonClicked,
  paymentUpdateConcluded,
  paymentUpdateErrorHasOccurred
} from "../../../../store/actions/update-menu-status.actions";
import {
  dealerCategory,
  dealerPaymentSelectedDealer,
  dealerPaymentStatus
} from "../../../../store/selectors/dealer-workflows-status.selectors";
import {
  paymentToDealerCompleted,
  paymentToDealerReset
} from "../../../../store/actions/dealer-workflows-status.actions";
import {
  dealerInvoicePaymentLabels,
  dealerInvoicePaymentState,
  dealerInvoicePlaceholders,
  dealerInvoiceSelected,
  dealerInvoiceSelectedDealer
} from "../../../../store/selectors/dealer-invoice-worklows-status.selectors";
import {
  dealerInvoiceStateReset,
} from "../../../../store/actions/dealer-invoice-workflows-status.actions";
import {InvoiceService} from "../../invoice/service/invoice.service";
import {NGXLogger} from "ngx-logger";
import * as dayjs from 'dayjs';
import { DataUtils, FileLoadError } from '../../../../../core/util/data-util.service';
import { EventManager, EventWithContent } from '../../../../../core/util/event-manager.service';
import { AlertError } from '../../../../../shared/alert/alert-error.model';
import { IPaymentLabel } from '../../../payment-label/payment-label.model';
import { Dealer, IDealer } from '../../../dealers/dealer/dealer.model';
import { IPayment, Payment } from '../payment.model';
import { IInvoice, Invoice } from '../../invoice/invoice.model';
import { PaymentService } from '../service/payment.service';
import { PaymentLabelService } from '../../../payment-label/service/payment-label.service';
import { DealerService } from '../../../dealers/dealer/service/dealer.service';
import { LabelSuggestionService } from '../../../../erp-common/suggestion/label-suggestion.service';
import { PlaceholderSuggestionService } from '../../../../erp-common/suggestion/placeholder-suggestion.service';
import { CategorySuggestionService } from '../../../../erp-common/suggestion/category-suggestion.service';
import { PaymentSuggestionService } from '../../../../erp-common/suggestion/payment-suggestion.service';
import { IPaymentCategory } from '../../../../erp-settlements/payments/payment-category/payment-category.model';
import { IPlaceholder } from '../../../placeholder/placeholder.model';
import { PaymentCategoryService } from '../../../../erp-settlements/payments/payment-category/service/payment-category.service';
import { PlaceholderService } from '../../../placeholder/service/placeholder.service';
import { IPaymentCalculation } from '../../payment-calculation/payment-calculation.model';
import { TaxRuleService } from '../../tax-rule/service/tax-rule.service';
import { SignedPaymentService } from '../../../signed-payment/service/signed-payment.service';
import { PaymentCalculationService } from '../../payment-calculation/service/payment-calculation.service';
import { ISignedPayment, SignedPayment } from '../../../signed-payment/signed-payment.model';

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
  invoiceDealer: IDealer= {...new Dealer()};

  editForm = this.fb.group({
    id: [],
    paymentNumber: [],
    paymentDate: [],
    invoicedAmount: [],
    paymentAmount: [],
    description: [],
    settlementCurrency: [null, [Validators.required]],
    calculationFile: [],
    calculationFileContentType: [],
    dealerName: [],
    purchaseOrderNumber: [],
    fileUploadToken: [],
    compilationToken: [],
    paymentLabels: [],
    paymentCategory: [],
    placeholders: [],
    paymentGroup: [],
  });

  minAccountLengthTerm = 3;

  categoriesLoading = false;
  categoryControlInput$ = new Subject<string>();
  categoryLookups$: Observable<IPaymentCategory[]> = of([]);

  labelsLoading = false;
  labelControlInput$ = new Subject<string>();
  labelLookups$: Observable<IPaymentLabel[]> = of([]);

  placeholdersLoading = false;
  placeholderControlInput$ = new Subject<string>();
  placeholderLookups$: Observable<IPlaceholder[]> = of([]);

  paymentsLoading = false;
  paymentControlInput$ = new Subject<string>();
  paymentLookups$: Observable<IPayment[]> = of([]);

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
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
    protected signedPaymentService: SignedPaymentService,
    protected router: Router,
    protected log: NGXLogger,
    protected categorySuggestionService: CategorySuggestionService,
    protected labelSuggestionService: LabelSuggestionService,
    protected placeholderSuggestionService: PlaceholderSuggestionService,
    protected paymentSuggestionService: PaymentSuggestionService,
  ) {

    this.store.pipe(select(copyingPaymentStatus)).subscribe(stat => this.weAreCopyingAPayment = stat);
    this.store.pipe(select(editingPaymentStatus)).subscribe(stat => this.weAreEditingAPayment = stat);
    this.store.pipe(select(creatingPaymentStatus)).subscribe(stat => this.weAreCreatingAPayment = stat);
    this.store.pipe(select(updateSelectedPayment)).subscribe(pyt => this.selectedPayment = pyt);
    this.store.pipe(select(dealerPaymentStatus)).subscribe(payingDealer => this.weArePayingADealer = payingDealer);
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    this.store.pipe(select(dealerPaymentSelectedDealer)).subscribe(dealer => this.selectedDealer = dealer);
    this.store.pipe(select(dealerCategory)).subscribe(category => {
      if (category) {
        this.dealerCategory = category;
      }
    });
    this.store.select<IDealer>(dealerInvoiceSelectedDealer).subscribe(dealr => {
      this.invoiceDealer = dealr;
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

    this.store.select<IInvoice>(dealerInvoiceSelected).subscribe(inv => {
      this.selectedInvoice = inv;
    });
  }

  ngOnInit(): void {
    if (this.weAreEditingAPayment || this.weAreCopyingAPayment) {
      this.editFormUpdate(this.selectedPayment);
    } else if (this.weArePayingAnInvoiceDealer) {
      this.activatedRoute.data.subscribe(({payment}) => {
        this.invoicePaymentUpdate(payment, this.invoiceDealer, this.dealerCategory, this.paymentLabels, this.placeholders);
      });
    }else {
      this.activatedRoute.data.subscribe(({payment}) => {
        this.updateForm(payment, this.selectedDealer, this.dealerCategory);
      });
    }
    this.loadRelationshipsOptions();

    // fire-up typeahead items
    this.loadLabels();
    this.loadPlaceholders();
    this.loadCategories();
    this.loadPayments();
  }

  loadCategories(): void {
    this.categoryLookups$ = concat(
      of([]), // default items
      this.categoryControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.categoriesLoading = true),
        switchMap(term => this.categorySuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.categoriesLoading = false)
        ))
      ),
      of([...this.paymentCategoriesSharedCollection])
    );
  }

  loadLabels(): void {
    this.labelLookups$ = concat(
      of([]), // default items
      this.labelControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.labelsLoading = true),
        switchMap(term => this.labelSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.labelsLoading = false)
        ))
      ),
      of([...this.paymentLabelsSharedCollection])
    );
  }

  loadPlaceholders(): void {
    this.placeholderLookups$ = concat(
      of([]), // default items
      this.placeholderControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.placeholdersLoading = true),
        switchMap(term => this.placeholderSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.placeholdersLoading = false)
        ))
      ),
      of([...this.placeholdersSharedCollection])
    );
  }

  loadPayments(): void {
    this.paymentLookups$ = concat(
      of([]), // default items
      this.paymentControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.paymentsLoading = true),
        switchMap(term => this.paymentSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.paymentsLoading = false)
        ))
      ),
      of([...this.paymentsSharedCollection])
    );
  }

  trackPaymentByFn(item: IPayment): number {
    return item.id!;
  }

  trackPlaceholdersByFn(item: IPaymentLabel): number {
    return item.id!;
  }

  trackCategoryByFn(item: IPaymentCategory): number {
    return item.id!;
  }

  trackLabelByFn(item: IPaymentLabel): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('erpSystemApp.error', { message: err.message })),
    });
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

    this.log.debug(`[PAYMENT-UPDATE: #addInvoiceDealer]Payment created ID: ${payment.paymentNumber}, DATED: ${payment.paymentDate?.format()}`);

    if (payment.id !== undefined) {
      this.subscribeToInvoiceDealerUpdate(this.paymentService.update(payment));
    } else {
      this.subscribeToInvoiceDealerUpdate(this.paymentService.create(payment));
    }

  }

  subscribeToInvoiceDealerUpdate(result: Observable<HttpResponse<IPayment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      (response: HttpResponse<IPayment>) => this.onInvoiceDealerUpdateSuccess(response),
      () => this.onSaveError()
    );
  }

  onInvoiceDealerUpdateSuccess(result: HttpResponse<IPayment>): void {
    this.isSaving = false;

    const payment: IPayment = {
      ...result.body
    };

    if (!this.weArePayingAnInvoiceDealer) {
      this.store.dispatch(paymentSaveButtonClicked());
    }

    this.selectedInvoice = {
      ...this.selectedInvoice,
      paymentReference: `${payment.paymentNumber};${payment.paymentDate?.format("YYYY-MM-DD")}`,
    };

    this.invoiceService.update(this.selectedInvoice).subscribe( invoice => {
      this.router.navigate(['/erp/invoice', invoice.body?.id, 'view']);
       this.createSignedPayment(payment, this.invoiceDealer).subscribe();
    });
  }

  createSignedPayment(payment: IPayment, invoiceDealer: IDealer): Observable<HttpResponse<ISignedPayment>> {
    let signedPayment: ISignedPayment = {
      ... new SignedPayment(),
    };

    signedPayment = {
      ...signedPayment,
      transactionNumber: payment.paymentNumber ?? '',
      transactionDate: payment.paymentDate ?? dayjs(),
      transactionCurrency: payment.settlementCurrency,
      transactionAmount: payment.paymentAmount ?? 0,
      dealerName: invoiceDealer.dealerName,
      paymentLabels: payment.paymentLabels,
      paymentCategory: payment.paymentCategory,
      placeholders: payment.placeholders,
      signedPaymentGroup: payment.paymentGroup
    };

    return this.signedPaymentService.create(signedPayment)
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
      paymentAmount: payment.paymentAmount,
      description: payment.description,
      settlementCurrency: payment.settlementCurrency,
      calculationFile: payment.calculationFile,
      calculationFileContentType: payment.calculationFileContentType,
      purchaseOrderNumber: payment.purchaseOrderNumber,
      paymentLabels: [...(dealer.paymentLabels ?? paymentLabels)],
      dealerName: dealer.dealerName,
      paymentCategory,
      placeholders: [...(dealer.placeholders ?? placeholders)],
      paymentGroup: payment.paymentGroup,
      fileUploadToken: payment.fileUploadToken,
      compilationToken: payment.compilationToken,
    });

    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      ...(payment.paymentLabels ?? [])
    );
    this.paymentCategoriesSharedCollection = this.paymentCategoryService.addPaymentCategoryToCollectionIfMissing(
      this.paymentCategoriesSharedCollection,
      payment.paymentCategory
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
      paymentAmount: payment.paymentAmount,
      description: payment.description,
      settlementCurrency: payment.settlementCurrency,
      calculationFile: payment.calculationFile,
      calculationFileContentType: payment.calculationFileContentType,
      purchaseOrderNumber: payment.purchaseOrderNumber,
      paymentLabels: payment.paymentLabels,
      dealerName: dealer.dealerName,
      paymentCategory,
      placeholders: payment.placeholders,
      paymentGroup: payment.paymentGroup,
      fileUploadToken: payment.fileUploadToken,
      compilationToken: payment.compilationToken,
    });

    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      ...(payment.paymentLabels ?? [])
    );
    this.paymentCategoriesSharedCollection = this.paymentCategoryService.addPaymentCategoryToCollectionIfMissing(
      this.paymentCategoriesSharedCollection,
      payment.paymentCategory
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
      paymentAmount: payment.paymentAmount,
      description: payment.description,
      settlementCurrency: payment.settlementCurrency,
      calculationFile: payment.calculationFile,
      calculationFileContentType: payment.calculationFileContentType,
      purchaseOrderNumber: payment.purchaseOrderNumber,
      dealerName: payment.dealerName,
      fileUploadToken: payment.fileUploadToken,
      compilationToken: payment.compilationToken,
      paymentLabels: payment.paymentLabels,
      paymentCategory: payment.paymentCategory,
      placeholders: payment.placeholders,
      paymentGroup: payment.paymentGroup,
    });

    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      ...(payment.paymentLabels ?? [])
    );
    this.paymentCategoriesSharedCollection = this.paymentCategoryService.addPaymentCategoryToCollectionIfMissing(
      this.paymentCategoriesSharedCollection,
      payment.paymentCategory
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
      (response: HttpResponse<IPayment>) => this.onSaveSuccess(response, this.selectedDealer),
      () => this.onSaveError()
    );
  }

  protected subscribeToCopyResponse(result: Observable<HttpResponse<IPayment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      (response: HttpResponse<IPayment>) => this.onSaveSuccess(response, this.selectedDealer),
      () => this.onSaveError()
    );
  }

  protected onCopySuccess(): void {
    this.isSaving = false;
    this.store.dispatch(paymentCopyButtonClicked())
    this.previousState();
  }

  protected onSaveSuccess(response: HttpResponse<IPayment>, invoiceDealer: IDealer): void {
    this.isSaving = false;
    this.createSignedPayment({...response.body}, invoiceDealer).subscribe();
    this.previousState();
  }

  protected onSaveFinalize(): void {
    // TODO conclude payment to invoiceDealer
    if (!this.weArePayingAnInvoiceDealer) {
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

    // this.dealerService
    //   .query()
    //   .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
    //   .pipe(map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing(dealers, this.editForm.get('dealer')!.value)))
    //   .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

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

    // this.taxRuleService
    //   .query()
    //   .pipe(map((res: HttpResponse<ITaxRule[]>) => res.body ?? []))
    //   .pipe(
    //     map((taxRules: ITaxRule[]) => this.taxRuleService.addTaxRuleToCollectionIfMissing(taxRules, this.editForm.get('taxRule')!.value))
    //   )
    //   .subscribe((taxRules: ITaxRule[]) => (this.taxRulesSharedCollection = taxRules));

    // this.paymentCalculationService
    //   .query({ 'paymentId.specified': 'false' })
    //   .pipe(map((res: HttpResponse<IPaymentCalculation[]>) => res.body ?? []))
    //   .pipe(
    //     map((paymentCalculations: IPaymentCalculation[]) =>
    //       this.paymentCalculationService.addPaymentCalculationToCollectionIfMissing(
    //         paymentCalculations,
    //         this.editForm.get('paymentCalculation')!.value
    //       )
    //     )
    //   )
    //   .subscribe((paymentCalculations: IPaymentCalculation[]) => (this.paymentCalculationsCollection = paymentCalculations));

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
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      description: this.editForm.get(['description'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      calculationFile: this.editForm.get(['calculationFile'])!.value,
      calculationFileContentType: this.editForm.get(['calculationFileContentType'])!.value,
      purchaseOrderNumber: this.editForm.get(['purchaseOrderNumber'])!.value,
      dealerName: this.editForm.get(['dealerName'])!.value,
      fileUploadToken: this.editForm.get(['fileUploadToken'])!.value,
      compilationToken: this.editForm.get(['compilationToken'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      paymentCategory: this.editForm.get(['paymentCategory'])!.value,
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
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      description: this.editForm.get(['description'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      calculationFile: this.editForm.get(['calculationFile'])!.value,
      calculationFileContentType: this.editForm.get(['calculationFileContentType'])!.value,
      purchaseOrderNumber: this.editForm.get(['purchaseOrderNumber'])!.value,
      dealerName: this.editForm.get(['dealerName'])!.value,
      fileUploadToken: this.editForm.get(['fileUploadToken'])!.value,
      compilationToken: this.editForm.get(['compilationToken'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      paymentCategory: this.editForm.get(['paymentCategory'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      paymentGroup: this.editForm.get(['paymentGroup'])!.value,
    };
  }
}
