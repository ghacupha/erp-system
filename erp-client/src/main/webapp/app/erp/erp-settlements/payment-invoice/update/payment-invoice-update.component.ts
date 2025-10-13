///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { concat, Observable, of, Subject } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, filter, finalize, map, switchMap, tap } from 'rxjs/operators';

import { IPaymentInvoice, PaymentInvoice } from '../payment-invoice.model';
import { PaymentInvoiceService } from '../service/payment-invoice.service';
import { IPurchaseOrder } from 'app/erp/erp-settlements/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/erp/erp-settlements/purchase-order/service/purchase-order.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IPaymentLabel } from '../../../erp-pages/payment-label/payment-label.model';
import { PaymentLabelService } from '../../../erp-pages/payment-label/service/payment-label.service';
import { ISettlementCurrency } from 'app/erp/erp-settlements/settlement-currency/settlement-currency.model';
import { SettlementCurrencyService } from 'app/erp/erp-settlements/settlement-currency/service/settlement-currency.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { IPaymentCategory } from '../../payments/payment-category/payment-category.model';
import { ISettlement } from '../../settlement/settlement.model';
import { IPayment } from '../../../erp-pages/payments/payment/payment.model';
import { CategorySuggestionService } from '../../../erp-common/suggestion/category-suggestion.service';
import { LabelSuggestionService } from '../../../erp-common/suggestion/label-suggestion.service';
import { PlaceholderSuggestionService } from '../../../erp-common/suggestion/placeholder-suggestion.service';
import { SettlementSuggestionService } from '../../../erp-common/suggestion/settlement-suggestion.service';
import { SettlementCurrencySuggestionService } from '../../../erp-common/suggestion/settlement-currency-suggestion.service';
import { DealerSuggestionService } from '../../../erp-common/suggestion/dealer-suggestion.service';
import { PurchaseOrderSuggestionService } from '../../../erp-common/suggestion/purchase-order-suggestion.service';
import { DeliveryNotesSuggestionService } from '../../../erp-common/suggestion/delivery-notes-suggestion.service';
import { JobSheetSuggestionService } from '../../../erp-common/suggestion/job-sheet-suggestion.service';
import { IDeliveryNote } from '../../delivery-note/delivery-note.model';
import { IJobSheet } from '../../job-sheet/job-sheet.model';
import { DeliveryNoteService } from '../../delivery-note/service/delivery-note.service';
import { JobSheetService } from '../../job-sheet/service/job-sheet.service';
import { SearchWithPagination } from '../../../../core/request/request.model';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';
import { IBusinessDocument } from '../../../erp-pages/business-document/business-document.model';
import { BusinessDocumentService } from '../../../erp-pages/business-document/service/business-document.service';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  copyingPaymentInvoiceStatus, creatingPaymentInvoiceStatus,
  editingPaymentInvoiceStatus, paymentInvoiceUpdateSelectedInstance
} from '../../../store/selectors/payment-invoice-workflow-status.selectors';
import { paymentInvoiceDataHasMutated } from '../../../store/actions/payment-invoice-workflow-status.action';

@Component({
  selector: 'jhi-payment-invoice-update',
  templateUrl: './payment-invoice-update.component.html',
})
export class PaymentInvoiceUpdateComponent implements OnInit {
  isSaving = false;

  purchaseOrdersSharedCollection: IPurchaseOrder[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  paymentLabelsSharedCollection: IPaymentLabel[] = [];
  settlementCurrenciesSharedCollection: ISettlementCurrency[] = [];
  dealersSharedCollection: IDealer[] = [];
  deliveryNotesSharedCollection: IDeliveryNote[] = [];
  jobSheetsSharedCollection: IJobSheet[] = [];
  businessDocumentCollection: IBusinessDocument[] = [];

  editForm = this.fb.group({
    id: [],
    invoiceNumber: [null, [Validators.required]],
    invoiceDate: [],
    invoiceAmount: [],
    fileUploadToken: [],
    compilationToken: [],
    remarks: [],
    purchaseOrders: [],
    placeholders: [],
    paymentLabels: [],
    settlementCurrency: [null, Validators.required],
    biller: [null, Validators.required],
    deliveryNotes: [],
    jobSheets: [],
    businessDocuments: [],
  });

  minAccountLengthTerm = 3;

  placeholdersLoading = false;
  placeholderControlInput$ = new Subject<string>();
  placeholderLookups$: Observable<IPlaceholder[]> = of([]);

  labelsLoading = false;
  labelControlInput$ = new Subject<string>();
  labelLookups$: Observable<IPaymentLabel[]> = of([]);

  purchaseOrdersLoading = false;
  purchaseOrderControlInput$ = new Subject<string>();
  purchaseOrderLookups$: Observable<IPurchaseOrder[]> = of([]);

  settlementCurrenciesLoading = false;
  settlementCurrencyControlInput$ = new Subject<string>();
  settlementCurrencyLookups$: Observable<ISettlementCurrency[]> = of([]);

  billersLoading = false;
  billersInput$ = new Subject<string>();
  billerLookups$: Observable<IDealer[]> = of([]);

  deliveryNotesLoading = false;
  deliveryNotesControlInput$ = new Subject<string>();
  deliveryNoteLookups$: Observable<IDeliveryNote[]> = of([]);

  jobSheetsLoading = false;
  jobSheetsControlInput$ = new Subject<string>();
  jobSheetLookups$: Observable<IJobSheet[]> = of([]);

  // Setting up default form states
  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = {...new PaymentInvoice()}

  constructor(
    protected paymentInvoiceService: PaymentInvoiceService,
    protected purchaseOrderService: PurchaseOrderService,
    protected placeholderService: PlaceholderService,
    protected paymentLabelService: PaymentLabelService,
    protected settlementCurrencyService: SettlementCurrencyService,
    protected dealerService: DealerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected categorySuggestionService: CategorySuggestionService,
    protected labelSuggestionService: LabelSuggestionService,
    protected placeholderSuggestionService: PlaceholderSuggestionService,
    protected settlementSuggestionService: SettlementSuggestionService,
    protected settlementCurrencySuggestionService: SettlementCurrencySuggestionService,
    protected dealerSuggestionService: DealerSuggestionService,
    protected purchaseOrderSuggestionService: PurchaseOrderSuggestionService,
    protected deliveryNotesSuggestionService: DeliveryNotesSuggestionService,
    protected jobSheetsSuggestionService: JobSheetSuggestionService,
    protected deliveryNoteService: DeliveryNoteService,
    protected jobSheetService: JobSheetService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected businessDocumentService: BusinessDocumentService,
    protected store: Store<State>
  ) {
    this.store.pipe(select(copyingPaymentInvoiceStatus)).subscribe(stat => this.weAreCopying = stat);
    this.store.pipe(select(editingPaymentInvoiceStatus)).subscribe(stat => this.weAreEditing = stat);
    this.store.pipe(select(creatingPaymentInvoiceStatus)).subscribe(stat => this.weAreCreating = stat);
    this.store.pipe(select(paymentInvoiceUpdateSelectedInstance)).subscribe(copied => this.selectedItem = copied);
  }

  ngOnInit(): void {

    if (this.weAreEditing) {
      this.updateForm(this.selectedItem);
    }

    if (this.weAreCopying) {
      this.copyForm(this.selectedItem)
    }

    if (this.weAreCreating) {
      this.loadRelationshipsOptions();
    }

    // fire-up typeahead items
    this.loadLabels();
    this.loadPlaceholders();
    this.loadCurrencies();
    this.loadBillers();
    this.loadPurchaseOrders();
    this.loadDeliveryNotes();
    this.loadJobSheets();
    this.updatePreferredCurrency();
    this.updatePreferredPaymentLabels();
    this.updateInputsGivenPurchaseOrder();
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateBusinessDocument(update: IBusinessDocument[]): void {
    this.editForm.patchValue({
      businessDocuments: [...update],
    });
  }

  updateInputsGivenPurchaseOrder(): void {
    this.editForm.get(['purchaseOrders'])?.valueChanges.subscribe((orders) => {
      const p_labels: IPaymentLabel[] = [];
      let p_amount: number | null | undefined = 0;
      orders.forEach((ordered: IPurchaseOrder) => {
          ({ purchaseOrderAmount: p_amount } = ordered);
      });

      this.editForm.patchValue({
        invoiceAmount: p_amount,
        biller: orders[0].vendor,
        remarks: orders[0].remarks,
        settlementCurrency: orders[0].settlementCurrency
      })

      const labels = [...this.editForm.get(['paymentLabels'])?.value, ...p_labels];
      this.editForm.get(['paymentLabels'])?.setValue(labels)
    });
  }

  updatePreferredCurrency(): void {
    this.universallyUniqueMappingService.search({ page: 0, size: 0, sort: [], query: "globallyPreferredSettlementIso4217CurrencyCode"})
      .subscribe(({ body }) => {
        if (body!.length > 0) {
          if (body) {
            this.settlementCurrencyService.search(<SearchWithPagination>{ page: 0, size: 0, sort: [], query: body[0].mappedValue })
              .subscribe(({ body: currencies }) => {
                if (currencies) {
                  this.editForm.get(['settlementCurrency'])?.setValue(currencies[0]);
                }
              });
          }
        }
      });
  }

  updatePreferredPaymentLabels(): void {
    this.universallyUniqueMappingService.search({ page: 0, size: 0, sort: [], query: "globallyPreferredSettlementUpdatePaymentLabel"})
      .subscribe(({ body }) => {
        if (body!.length > 0) {
          if (body) {
            this.paymentLabelService.search(<SearchWithPagination>{ page: 0, size: 0, sort: [], query: body[0].mappedValue })
              .subscribe(({ body: vals }) => {
                if (vals) {
                  this.editForm.patchValue({
                    paymentLabels: [...vals]
                  });
                }
              });
          }
        }
      });
  }

  loadJobSheets(): void {
    this.jobSheetLookups$ = concat(
      of([]), // default items
      this.jobSheetsControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.jobSheetsLoading = true),
        switchMap(term => this.jobSheetsSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.jobSheetsLoading = false)
        ))
      ),
      of([...this.jobSheetsSharedCollection])
    );
  }

  // Load dynamic DeliveryNotes from input stream
  loadDeliveryNotes(): void {
    this.deliveryNoteLookups$ = concat(
      of([]), // default items
      this.deliveryNotesControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.deliveryNotesLoading = true),
        switchMap(term => this.deliveryNotesSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.deliveryNotesLoading = false)
        ))
      ),
      of([...this.deliveryNotesSharedCollection])
    );
  }

  loadPurchaseOrders(): void {
    this.purchaseOrderLookups$ = concat(
      of([]), // default items
      this.purchaseOrderControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this. purchaseOrdersLoading = true),
        switchMap(term => this.purchaseOrderSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this. purchaseOrdersLoading = false)
        ))
      ),
      of([...this.purchaseOrdersSharedCollection])
    );
  }


  loadBillers(): void {
    this.billerLookups$ = concat(
      of([]), // default items
      this.billersInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.billersLoading = true),
        switchMap(term => this.dealerSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.billersLoading = false)
        ))
      ),
      of([...this.dealersSharedCollection])
    );
  }

  loadCurrencies(): void {
    this.settlementCurrencyLookups$ = concat(
      of([]), // default items
      this.settlementCurrencyControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.settlementCurrenciesLoading = true),
        switchMap(term => this.settlementCurrencySuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.settlementCurrenciesLoading = false)
        ))
      ),
      of([...this.settlementCurrenciesSharedCollection])
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

  trackBillerByFn(item: IDealer): number {
    return item.id!;
  }

  trackPurchaseOrderByFn(item: IPurchaseOrder): number {
    return item.id!;
  }

  trackCurrencyByFn(item: ISettlementCurrency): number {
    return item.id!;
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

  trackSettlementByFn(item: ISettlement): number {
    return item.id!;
  }

  trackDeliveryNotesByFn(item: IDeliveryNote): number {
    return item.id!;
  }

  trackJobSheetByFn(item: IJobSheet): number {
    return item.id!;
  }

  previousState(): void {
    this.store.dispatch(paymentInvoiceDataHasMutated());
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.paymentInvoiceService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.paymentInvoiceService.update(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.paymentInvoiceService.create(this.copyFromForm()));
  }

  trackPurchaseOrderById(index: number, item: IPurchaseOrder): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackPaymentLabelById(index: number, item: IPaymentLabel): number {
    return item.id!;
  }

  trackSettlementCurrencyById(index: number, item: ISettlementCurrency): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackDeliveryNoteById(index: number, item: IDeliveryNote): number {
    return item.id!;
  }

  trackJobSheetById(index: number, item: IJobSheet): number {
    return item.id!;
  }

  getSelectedPurchaseOrder(option: IPurchaseOrder, selectedVals?: IPurchaseOrder[]): IPurchaseOrder {
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

  getSelectedDeliveryNote(option: IDeliveryNote, selectedVals?: IDeliveryNote[]): IDeliveryNote {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedJobSheet(option: IJobSheet, selectedVals?: IJobSheet[]): IJobSheet {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentInvoice>>): void {
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

  protected updateForm(paymentInvoice: IPaymentInvoice): void {
    this.editForm.patchValue({
      id: paymentInvoice.id,
      invoiceNumber: paymentInvoice.invoiceNumber,
      invoiceDate: paymentInvoice.invoiceDate,
      invoiceAmount: paymentInvoice.invoiceAmount,
      fileUploadToken: paymentInvoice.fileUploadToken,
      compilationToken: paymentInvoice.compilationToken,
      purchaseOrders: paymentInvoice.purchaseOrders,
      placeholders: paymentInvoice.placeholders,
      paymentLabels: paymentInvoice.paymentLabels,
      settlementCurrency: paymentInvoice.settlementCurrency,
      biller: paymentInvoice.biller,
      remarks: paymentInvoice.remarks,
      deliveryNotes: paymentInvoice.deliveryNotes,
      jobSheets: paymentInvoice.jobSheets,
      businessDocuments: paymentInvoice.businessDocuments,
    });

    this.businessDocumentCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentCollection,
      ...(paymentInvoice.businessDocuments ?? [])
    );

    this.purchaseOrdersSharedCollection = this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(
      this.purchaseOrdersSharedCollection,
      ...(paymentInvoice.purchaseOrders ?? [])
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(paymentInvoice.placeholders ?? [])
    );
    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      ...(paymentInvoice.paymentLabels ?? [])
    );
    this.settlementCurrenciesSharedCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
      this.settlementCurrenciesSharedCollection,
      paymentInvoice.settlementCurrency
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(this.dealersSharedCollection, paymentInvoice.biller);
    this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(
      this.deliveryNotesSharedCollection,
      ...(paymentInvoice.deliveryNotes ?? [])
    );
    this.jobSheetsSharedCollection = this.jobSheetService.addJobSheetToCollectionIfMissing(
      this.jobSheetsSharedCollection,
      ...(paymentInvoice.jobSheets ?? [])
    );
  }

  protected copyForm(paymentInvoice: IPaymentInvoice): void {
    this.editForm.patchValue({
      id: paymentInvoice.id,
      invoiceNumber: paymentInvoice.invoiceNumber,
      invoiceDate: paymentInvoice.invoiceDate,
      invoiceAmount: paymentInvoice.invoiceAmount,
      fileUploadToken: paymentInvoice.fileUploadToken,
      compilationToken: paymentInvoice.compilationToken,
      purchaseOrders: paymentInvoice.purchaseOrders,
      placeholders: paymentInvoice.placeholders,
      paymentLabels: paymentInvoice.paymentLabels,
      settlementCurrency: paymentInvoice.settlementCurrency,
      biller: paymentInvoice.biller,
      remarks: paymentInvoice.remarks,
      deliveryNotes: paymentInvoice.deliveryNotes,
      jobSheets: paymentInvoice.jobSheets,
      businessDocuments: paymentInvoice.businessDocuments,
    });

    this.businessDocumentCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentCollection,
      ...(paymentInvoice.businessDocuments ?? [])
    );

    this.purchaseOrdersSharedCollection = this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(
      this.purchaseOrdersSharedCollection,
      ...(paymentInvoice.purchaseOrders ?? [])
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(paymentInvoice.placeholders ?? [])
    );
    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      ...(paymentInvoice.paymentLabels ?? [])
    );
    this.settlementCurrenciesSharedCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
      this.settlementCurrenciesSharedCollection,
      paymentInvoice.settlementCurrency
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(this.dealersSharedCollection, paymentInvoice.biller);
    this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(
      this.deliveryNotesSharedCollection,
      ...(paymentInvoice.deliveryNotes ?? [])
    );
    this.jobSheetsSharedCollection = this.jobSheetService.addJobSheetToCollectionIfMissing(
      this.jobSheetsSharedCollection,
      ...(paymentInvoice.jobSheets ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.businessDocumentService
      .query()
      .pipe(map((res: HttpResponse<IBusinessDocument[]>) => res.body ?? []))
      .pipe(
        map((businessDocuments: IBusinessDocument[]) =>
          this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
            businessDocuments,
            this.editForm.get('businessDocument')!.value
          )
        )
      )
      .subscribe((businessDocuments: IBusinessDocument[]) => (this.businessDocumentCollection = businessDocuments));

    this.purchaseOrderService
      .query()
      .pipe(map((res: HttpResponse<IPurchaseOrder[]>) => res.body ?? []))
      .pipe(
        map((purchaseOrders: IPurchaseOrder[]) =>
          this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(
            purchaseOrders,
            ...(this.editForm.get('purchaseOrders')!.value ?? [])
          )
        )
      )
      .subscribe((purchaseOrders: IPurchaseOrder[]) => (this.purchaseOrdersSharedCollection = purchaseOrders));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.paymentLabelService
      .query()
      .pipe(map((res: HttpResponse<IPaymentLabel[]>) => res.body ?? []))
      .pipe(
        map((paymentLabels: IPaymentLabel[]) =>
          this.paymentLabelService.addPaymentLabelToCollectionIfMissing(paymentLabels, ...(this.editForm.get('paymentLabels')!.value ?? []))
        )
      )
      .subscribe((paymentLabels: IPaymentLabel[]) => (this.paymentLabelsSharedCollection = paymentLabels));

    this.settlementCurrencyService
      .query()
      .pipe(map((res: HttpResponse<ISettlementCurrency[]>) => res.body ?? []))
      .pipe(
        map((settlementCurrencies: ISettlementCurrency[]) =>
          this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
            settlementCurrencies,
            this.editForm.get('settlementCurrency')!.value
          )
        )
      )
      .subscribe((settlementCurrencies: ISettlementCurrency[]) => (this.settlementCurrenciesSharedCollection = settlementCurrencies));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing(dealers, this.editForm.get('biller')!.value)))
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

    this.deliveryNoteService
      .query()
      .pipe(map((res: HttpResponse<IDeliveryNote[]>) => res.body ?? []))
      .pipe(
        map((deliveryNotes: IDeliveryNote[]) =>
          this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(deliveryNotes, ...(this.editForm.get('deliveryNotes')!.value ?? []))
        )
      )
      .subscribe((deliveryNotes: IDeliveryNote[]) => (this.deliveryNotesSharedCollection = deliveryNotes));

    this.jobSheetService
      .query()
      .pipe(map((res: HttpResponse<IJobSheet[]>) => res.body ?? []))
      .pipe(
        map((jobSheets: IJobSheet[]) =>
          this.jobSheetService.addJobSheetToCollectionIfMissing(jobSheets, ...(this.editForm.get('jobSheets')!.value ?? []))
        )
      )
      .subscribe((jobSheets: IJobSheet[]) => (this.jobSheetsSharedCollection = jobSheets));
  }

  protected createFromForm(): IPaymentInvoice {
    return {
      ...new PaymentInvoice(),
      id: this.editForm.get(['id'])!.value,
      invoiceNumber: this.editForm.get(['invoiceNumber'])!.value,
      invoiceDate: this.editForm.get(['invoiceDate'])!.value,
      invoiceAmount: this.editForm.get(['invoiceAmount'])!.value,
      fileUploadToken: this.editForm.get(['fileUploadToken'])!.value,
      compilationToken: this.editForm.get(['compilationToken'])!.value,
      purchaseOrders: this.editForm.get(['purchaseOrders'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      biller: this.editForm.get(['biller'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      deliveryNotes: this.editForm.get(['deliveryNotes'])!.value,
      jobSheets: this.editForm.get(['jobSheets'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
    };
  }
  protected copyFromForm(): IPaymentInvoice {
    return {
      ...new PaymentInvoice(),
      // id: this.editForm.get(['id'])!.value,
      invoiceNumber: this.editForm.get(['invoiceNumber'])!.value,
      invoiceDate: this.editForm.get(['invoiceDate'])!.value,
      invoiceAmount: this.editForm.get(['invoiceAmount'])!.value,
      fileUploadToken: this.editForm.get(['fileUploadToken'])!.value,
      compilationToken: this.editForm.get(['compilationToken'])!.value,
      purchaseOrders: this.editForm.get(['purchaseOrders'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      biller: this.editForm.get(['biller'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      deliveryNotes: this.editForm.get(['deliveryNotes'])!.value,
      jobSheets: this.editForm.get(['jobSheets'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
    };
  }
}
