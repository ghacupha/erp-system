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

import { IAgencyNotice, AgencyNotice } from '../agency-notice.model';
import { AgencyNoticeService } from '../service/agency-notice.service';
import { CategorySuggestionService } from '../../../erp-common/suggestion/category-suggestion.service';
import { LabelSuggestionService } from '../../../erp-common/suggestion/label-suggestion.service';
import { PlaceholderSuggestionService } from '../../../erp-common/suggestion/placeholder-suggestion.service';
import { SettlementSuggestionService } from '../../../erp-common/suggestion/settlement-suggestion.service';
import { SettlementCurrencySuggestionService } from '../../../erp-common/suggestion/settlement-currency-suggestion.service';
import { DealerSuggestionService } from '../../../erp-common/suggestion/dealer-suggestion.service';
import { PaymentInvoiceSuggestionService } from '../../../erp-common/suggestion/payment-invoice-suggestion.service';
import { IPaymentInvoice } from '../../../erp-settlements/payment-invoice/payment-invoice.model';
import { DataUtils, FileLoadError } from '../../../../core/util/data-util.service';
import { EventManager, EventWithContent } from '../../../../core/util/event-manager.service';
import { AlertError } from '../../../../shared/alert/alert-error.model';
import { AgencyStatusType } from 'app/erp/erp-common/enumerations/agency-status-type.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IDealer } from 'app/erp/erp-pages/dealers/dealer/dealer.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { ISettlementCurrency } from '../../../erp-settlements/settlement-currency/settlement-currency.model';
import { SettlementCurrencyService } from '../../../erp-settlements/settlement-currency/service/settlement-currency.service';

@Component({
  selector: 'jhi-agency-notice-update',
  templateUrl: './agency-notice-update.component.html',
})
export class AgencyNoticeUpdateComponent implements OnInit {
  isSaving = false;
  agencyStatusTypeValues = Object.keys(AgencyStatusType);

  dealersSharedCollection: IDealer[] = [];
  settlementCurrenciesSharedCollection: ISettlementCurrency[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    referenceNumber: [null, [Validators.required]],
    referenceDate: [],
    assessmentAmount: [null, [Validators.required]],
    agencyStatus: [null, [Validators.required]],
    assessmentNotice: [],
    assessmentNoticeContentType: [],
    correspondents: [],
    settlementCurrency: [],
    assessor: [],
    placeholders: [],
  });

  minAccountLengthTerm = 3;

  placeholdersLoading = false;
  placeholderControlInput$ = new Subject<string>();
  placeholderLookups$: Observable<IPlaceholder[]> = of([]);

  correspondentsLoading = false;
  correspondentControlInput$ = new Subject<string>();
  correspondentLookups$: Observable<IDealer[]> = of([]);

  settlementCurrenciesLoading = false;
  settlementCurrencyControlInput$ = new Subject<string>();
  settlementCurrencyLookups$: Observable<ISettlementCurrency[]> = of([]);

  assessorsLoading = false;
  assessorInput$ = new Subject<string>();
  assessorLookup$: Observable<IDealer[]> = of([]);

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected agencyNoticeService: AgencyNoticeService,
    protected dealerService: DealerService,
    protected settlementCurrencyService: SettlementCurrencyService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected categorySuggestionService: CategorySuggestionService,
    protected labelSuggestionService: LabelSuggestionService,
    protected placeholderSuggestionService: PlaceholderSuggestionService,
    protected settlementSuggestionService: SettlementSuggestionService,
    protected settlementCurrencySuggestionService: SettlementCurrencySuggestionService,
    protected dealerSuggestionService: DealerSuggestionService,
    protected paymentInvoiceSuggestionService: PaymentInvoiceSuggestionService,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agencyNotice }) => {
      this.updateForm(agencyNotice);

      this.loadRelationshipsOptions();
    });

    // fire-up typeahead items
    this.loadPlaceholders();
    this.loadCurrencies();
    this.loadAssessors();
    this.loadCorrespondents();
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

  loadAssessors(): void {
    this.assessorLookup$ = concat(
      of([]), // default items
      this.assessorInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.assessorsLoading = true),
        switchMap(term => this.dealerSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.assessorsLoading = false)
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

  loadCorrespondents(): void {
    this.correspondentLookups$ = concat(
      of([]), // default items
      this.correspondentControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.correspondentsLoading = true),
        switchMap(term => this.dealerSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.correspondentsLoading = false)
        ))
      ),
      of([...this.dealersSharedCollection])
    );
  }

  trackPlaceholdersByFn(item: IPlaceholder): number {
    return item.id!;
  }

  trackPaymentInvoiceByFn(item: IPaymentInvoice): number {
    return item.id!;
  }

  trackAssessorByFn(item: IDealer): number {
    return item.id!;
  }

  trackCorrespondentByFn(item: IDealer): number {
    return item.id!;
  }

  trackCurrencyByFn(item: ISettlementCurrency): number {
    return item.id!;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agencyNotice = this.createFromForm();
    if (agencyNotice.id !== undefined) {
      this.subscribeToSaveResponse(this.agencyNoticeService.update(agencyNotice));
    } else {
      this.subscribeToSaveResponse(this.agencyNoticeService.create(agencyNotice));
    }
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackSettlementCurrencyById(index: number, item: ISettlementCurrency): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgencyNotice>>): void {
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

  protected updateForm(agencyNotice: IAgencyNotice): void {
    this.editForm.patchValue({
      id: agencyNotice.id,
      referenceNumber: agencyNotice.referenceNumber,
      referenceDate: agencyNotice.referenceDate,
      /* taxCode: agencyNotice.taxCode, */
      assessmentAmount: agencyNotice.assessmentAmount,
      agencyStatus: agencyNotice.agencyStatus,
      assessmentNotice: agencyNotice.assessmentNotice,
      assessmentNoticeContentType: agencyNotice.assessmentNoticeContentType,
      correspondents: agencyNotice.correspondents,
      settlementCurrency: agencyNotice.settlementCurrency,
      assessor: agencyNotice.assessor,
      placeholders: agencyNotice.placeholders,
    });

    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      ...(agencyNotice.correspondents ?? []),
      agencyNotice.assessor
    );
    this.settlementCurrenciesSharedCollection = this.settlementCurrencyService.addSettlementCurrencyToCollectionIfMissing(
      this.settlementCurrenciesSharedCollection,
      agencyNotice.settlementCurrency
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(agencyNotice.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(
        map((dealers: IDealer[]) =>
          this.dealerService.addDealerToCollectionIfMissing(
            dealers,
            ...(this.editForm.get('correspondents')!.value ?? []),
            this.editForm.get('assessor')!.value
          )
        )
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

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
  }

  protected createFromForm(): IAgencyNotice {
    return {
      ...new AgencyNotice(),
      id: this.editForm.get(['id'])!.value,
      referenceNumber: this.editForm.get(['referenceNumber'])!.value,
      referenceDate: this.editForm.get(['referenceDate'])!.value,
      assessmentAmount: this.editForm.get(['assessmentAmount'])!.value,
      agencyStatus: this.editForm.get(['agencyStatus'])!.value,
      assessmentNoticeContentType: this.editForm.get(['assessmentNoticeContentType'])!.value,
      assessmentNotice: this.editForm.get(['assessmentNotice'])!.value,
      correspondents: this.editForm.get(['correspondents'])!.value,
      settlementCurrency: this.editForm.get(['settlementCurrency'])!.value,
      assessor: this.editForm.get(['assessor'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
