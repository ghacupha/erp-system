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

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { concat, Observable, of, Subject } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, filter, finalize, map, switchMap, tap } from 'rxjs/operators';

import { IPurchaseOrder, PurchaseOrder } from '../purchase-order.model';
import { PurchaseOrderService } from '../service/purchase-order.service';
import { ISettlementCurrency } from 'app/erp/erp-settlements/settlement-currency/settlement-currency.model';
import { SettlementCurrencyService } from 'app/erp/erp-settlements/settlement-currency/service/settlement-currency.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { CategorySuggestionService } from '../../../erp-common/suggestion/category-suggestion.service';
import { LabelSuggestionService } from '../../../erp-common/suggestion/label-suggestion.service';
import { PlaceholderSuggestionService } from '../../../erp-common/suggestion/placeholder-suggestion.service';
import { SettlementSuggestionService } from '../../../erp-common/suggestion/settlement-suggestion.service';
import { SettlementCurrencySuggestionService } from '../../../erp-common/suggestion/settlement-currency-suggestion.service';
import { DealerSuggestionService } from '../../../erp-common/suggestion/dealer-suggestion.service';
import { IPayment } from '../../../erp-pages/payments/payment/payment.model';
import { IPaymentLabel } from '../../../erp-pages/payment-label/payment-label.model';
import { IBusinessDocument } from '../../../erp-pages/business-document/business-document.model';
import { BusinessDocumentService } from '../../../erp-pages/business-document/service/business-document.service';

@Component({
  selector: 'jhi-purchase-order-update',
  templateUrl: './purchase-order-update.component.html',
})
export class PurchaseOrderUpdateComponent implements OnInit {
  isSaving = false;

  settlementCurrenciesSharedCollection: ISettlementCurrency[] = [];
  businessDocumentsSharedCollection: IBusinessDocument[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  dealersSharedCollection: IDealer[] = [];

  editForm = this.fb.group({
    id: [],
    purchaseOrderNumber: [null, [Validators.required]],
    purchaseOrderDate: [],
    purchaseOrderAmount: [],
    description: [],
    notes: [],
    fileUploadToken: [],
    compilationToken: [],
    remarks: [],
    settlementCurrency: [],
    placeholders: [],
    signatories: [],
    vendor: [null, Validators.required],
    businessDocuments: [],
  });

  minAccountLengthTerm = 3;

  settlementCurrenciesLoading = false;
  settlementCurrencyControlInput$ = new Subject<string>();
  settlementCurrencyLookups$: Observable<ISettlementCurrency[]> = of([]);

  vendorsLoading = false;
  vendorsInput$ = new Subject<string>();
  vendorLookups$: Observable<IDealer[]> = of([]);

  signatoriesLoading = false;
  signatoryControlInput$ = new Subject<string>();
  signatoryLookups$: Observable<IDealer[]> = of([]);

  placeholdersLoading = false;
  placeholderControlInput$ = new Subject<string>();
  placeholderLookups$: Observable<IPlaceholder[]> = of([]);

  constructor(
    protected purchaseOrderService: PurchaseOrderService,
    protected settlementCurrencyService: SettlementCurrencyService,
    protected placeholderService: PlaceholderService,
    protected dealerService: DealerService,
    protected activatedRoute: ActivatedRoute,
    protected categorySuggestionService: CategorySuggestionService,
    protected labelSuggestionService: LabelSuggestionService,
    protected placeholderSuggestionService: PlaceholderSuggestionService,
    protected settlementSuggestionService: SettlementSuggestionService,
    protected settlementCurrencySuggestionService: SettlementCurrencySuggestionService,
    protected dealerSuggestionService: DealerSuggestionService,
    protected businessDocumentService: BusinessDocumentService,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseOrder }) => {
      this.updateForm(purchaseOrder);

      this.loadRelationshipsOptions();
    });

    // fire-up typeahead items
    // this.loadPlaceholders();
    this.loadCurrencies();
    this.loadSignatories();
    this.loadVendors();
  }

  loadSignatories(): void {
    this.signatoryLookups$ = concat(
      of([]), // default items
      this.signatoryControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.signatoriesLoading = true),
        switchMap(term => this.dealerSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.signatoriesLoading = false)
        ))
      ),
      of([...this.dealersSharedCollection])
    );
  }

  loadVendors(): void {
    this.vendorLookups$ = concat(
      of([]), // default items
      this.vendorsInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.vendorsLoading = true),
        switchMap(term => this.dealerSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.vendorsLoading = false)
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

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateBusinessDocument(update: IBusinessDocument[]): void {
    this.editForm.patchValue({
      businessDocuments: [...update],
    });
  }

  updatePlaceholders(update: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [...update]
    });
  }

  trackBillerByFn(item: IDealer): number {
    return item.id!;
  }

  trackCurrencyByFn(item: ISettlementCurrency): number {
    return item.id!;
  }

  trackPaymentByFn(item: IPayment): number {
    return item.id!;
  }

  trackPlaceholdersByFn(item: IPlaceholder): number {
    return item.id!;
  }

  // trackCategoryByFn(item: IPaymentCategory): number {
  //   return item.id!;
  // }

  trackLabelByFn(item: IPaymentLabel): number {
    return item.id!;
  }


  trackVendorByFn(item: IDealer): number {
    return item.id!;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchaseOrder = this.createFromForm();
    if (purchaseOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.purchaseOrderService.update(purchaseOrder));
    } else {
      this.subscribeToSaveResponse(this.purchaseOrderService.create(purchaseOrder));
    }
  }

  trackSettlementCurrencyById(index: number, item: ISettlementCurrency): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
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

  getSelectedDealer(option: IDealer, selectedVals?: IDealer[]): IDealer {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseOrder>>): void {
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

  protected updateForm(purchaseOrder: IPurchaseOrder): void {
    this.editForm.patchValue({
      id: purchaseOrder.id,
      purchaseOrderNumber: purchaseOrder.purchaseOrderNumber,
      purchaseOrderDate: purchaseOrder.purchaseOrderDate,
      purchaseOrderAmount: purchaseOrder.purchaseOrderAmount,
      description: purchaseOrder.description,
      notes: purchaseOrder.notes,
      fileUploadToken: purchaseOrder.fileUploadToken,
      compilationToken: purchaseOrder.compilationToken,
      settlementCurrency: purchaseOrder.settlementCurrency,
      placeholders: purchaseOrder.placeholders,
      signatories: purchaseOrder.signatories,
      vendor: purchaseOrder.vendor,
      businessDocuments: purchaseOrder.businessDocuments,
    });

    this.settlementCurrenciesSharedCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
      this.settlementCurrenciesSharedCollection,
      purchaseOrder.settlementCurrency
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(purchaseOrder.placeholders ?? [])
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      ...(purchaseOrder.signatories ?? []),
      purchaseOrder.vendor
    );
    this.businessDocumentsSharedCollection = this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
      this.businessDocumentsSharedCollection,
      ...(purchaseOrder.businessDocuments ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
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

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(
        map((dealers: IDealer[]) =>
          this.dealerService.addDealerToCollectionIfMissing(
            dealers,
            ...(this.editForm.get('signatories')!.value ?? []),
            this.editForm.get('vendor')!.value
          )
        )
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

    this.businessDocumentService
      .query()
      .pipe(map((res: HttpResponse<IBusinessDocument[]>) => res.body ?? []))
      .pipe(
        map((businessDocuments: IBusinessDocument[]) =>
          this.businessDocumentService.addBusinessDocumentToCollectionIfMissing(
            businessDocuments,
            ...(this.editForm.get('businessDocuments')!.value ?? [])
          )
        )
      )
      .subscribe((businessDocuments: IBusinessDocument[]) => (this.businessDocumentsSharedCollection = businessDocuments));
  }

  protected createFromForm(): IPurchaseOrder {
    return {
      ...new PurchaseOrder(),
      id: this.editForm.get(['id'])!.value,
      purchaseOrderNumber: this.editForm.get(['purchaseOrderNumber'])!.value,
      purchaseOrderDate: this.editForm.get(['purchaseOrderDate'])!.value,
      purchaseOrderAmount: this.editForm.get(['purchaseOrderAmount'])!.value,
      description: this.editForm.get(['description'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      fileUploadToken: this.editForm.get(['fileUploadToken'])!.value,
      compilationToken: this.editForm.get(['compilationToken'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      signatories: this.editForm.get(['signatories'])!.value,
      vendor: this.editForm.get(['vendor'])!.value,
      businessDocuments: this.editForm.get(['businessDocuments'])!.value,
    };
  }
}
