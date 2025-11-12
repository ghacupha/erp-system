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

import { IJobSheet, JobSheet } from '../job-sheet.model';
import { JobSheetService } from '../service/job-sheet.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { IBusinessStamp } from '../../business-stamp/business-stamp.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IPaymentLabel } from '../../../erp-pages/payment-label/payment-label.model';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { BusinessStampService } from '../../business-stamp/service/business-stamp.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { PaymentLabelService } from '../../../erp-pages/payment-label/service/payment-label.service';
import { CategorySuggestionService } from '../../../erp-common/suggestion/category-suggestion.service';
import { LabelSuggestionService } from '../../../erp-common/suggestion/label-suggestion.service';
import { PlaceholderSuggestionService } from '../../../erp-common/suggestion/placeholder-suggestion.service';
import { SettlementSuggestionService } from '../../../erp-common/suggestion/settlement-suggestion.service';
import { SettlementCurrencySuggestionService } from '../../../erp-common/suggestion/settlement-currency-suggestion.service';
import { DealerSuggestionService } from '../../../erp-common/suggestion/dealer-suggestion.service';
import { PaymentInvoiceSuggestionService } from '../../../erp-common/suggestion/payment-invoice-suggestion.service';
import { BusinessStampSuggestionService } from '../../../erp-common/suggestion/business-stamp-suggestion.service';

@Component({
  selector: 'jhi-job-sheet-update',
  templateUrl: './job-sheet-update.component.html',
})
export class JobSheetUpdateComponent implements OnInit {
  isSaving = false;

  dealersSharedCollection: IDealer[] = [];
  businessStampsSharedCollection: IBusinessStamp[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  paymentLabelsSharedCollection: IPaymentLabel[] = [];

  editForm = this.fb.group({
    id: [],
    serialNumber: [null, [Validators.required]],
    jobSheetDate: [],
    details: [],
    remarks: [],
    biller: [null, Validators.required],
    signatories: [],
    contactPerson: [],
    businessStamps: [],
    placeholders: [],
    paymentLabels: [],
  });

  minAccountLengthTerm = 3;

  placeholdersLoading = false;
  placeholderControlInput$ = new Subject<string>();
  placeholderLookups$: Observable<IPlaceholder[]> = of([]);

  billersLoading = false;
  billersInput$ = new Subject<string>();
  billerLookups$: Observable<IDealer[]> = of([]);

  contactPersonsLoading = false;
  contactPersonInput$ = new Subject<string>();
  contactPersonLookups$: Observable<IDealer[]> = of([]);

  signatoriesLoading = false;
  signatoryControlInput$ = new Subject<string>();
  signatoryLookups$: Observable<IDealer[]> = of([]);

  labelsLoading = false;
  labelControlInput$ = new Subject<string>();
  labelLookups$: Observable<IPaymentLabel[]> = of([]);

  businessStampsLoading = false;
  businessStampsControlInput$ = new Subject<string>();
  businessStampLookups$: Observable<IBusinessStamp[]> = of([]);

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected jobSheetService: JobSheetService,
    protected dealerService: DealerService,
    protected businessStampService: BusinessStampService,
    protected placeholderService: PlaceholderService,
    protected paymentLabelService: PaymentLabelService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected businessStampSuggestionService: BusinessStampSuggestionService,
    protected categorySuggestionService: CategorySuggestionService,
    protected labelSuggestionService: LabelSuggestionService,
    protected placeholderSuggestionService: PlaceholderSuggestionService,
    protected settlementSuggestionService: SettlementSuggestionService,
    protected settlementCurrencySuggestionService: SettlementCurrencySuggestionService,
    protected dealerSuggestionService: DealerSuggestionService,
    protected paymentInvoiceSuggestionService: PaymentInvoiceSuggestionService,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobSheet }) => {
      this.updateForm(jobSheet);

      this.loadRelationshipsOptions();
    });

    this.loadLabels();
    this.loadPlaceholders();
    this.loadBillers();
    this.loadSignatories();
    this.loadContactPersons();
    this.loadBusinessStamps();
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

  loadContactPersons(): void {
    this.contactPersonLookups$ = concat(
      of([]), // default items
      this.contactPersonInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.contactPersonsLoading = true),
        switchMap(term => this.dealerSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.contactPersonsLoading = false)
        ))
      ),
      of([...this.dealersSharedCollection])
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

  loadBusinessStamps(): void {
    this.businessStampLookups$ = concat(
      of([]), // default items
      this.businessStampsControlInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.businessStampsLoading = true),
        switchMap(term => this.businessStampSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.businessStampsLoading = false)
        ))
      ),
      of([...this.businessStampsSharedCollection])
    );
  }

  trackBusinessStampByFn(item: IBusinessStamp): number {
    return item.id!;
  }

  trackLabelByFn(item: IPaymentLabel): number {
    return item.id!;
  }

  trackBillerByFn(item: IDealer): number {
    return item.id!;
  }

  trackPlaceholdersByFn(item: IPaymentLabel): number {
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
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobSheet = this.createFromForm();
    if (jobSheet.id !== undefined) {
      this.subscribeToSaveResponse(this.jobSheetService.update(jobSheet));
    } else {
      this.subscribeToSaveResponse(this.jobSheetService.create(jobSheet));
    }
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackBusinessStampById(index: number, item: IBusinessStamp): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackPaymentLabelById(index: number, item: IPaymentLabel): number {
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

  getSelectedBusinessStamp(option: IBusinessStamp, selectedVals?: IBusinessStamp[]): IBusinessStamp {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobSheet>>): void {
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

  protected updateForm(jobSheet: IJobSheet): void {
    this.editForm.patchValue({
      id: jobSheet.id,
      serialNumber: jobSheet.serialNumber,
      jobSheetDate: jobSheet.jobSheetDate,
      details: jobSheet.details,
      remarks: jobSheet.remarks,
      biller: jobSheet.biller,
      signatories: jobSheet.signatories,
      contactPerson: jobSheet.contactPerson,
      businessStamps: jobSheet.businessStamps,
      placeholders: jobSheet.placeholders,
      paymentLabels: jobSheet.paymentLabels,
    });

    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      jobSheet.biller,
      ...(jobSheet.signatories ?? []),
      jobSheet.contactPerson
    );
    this.businessStampsSharedCollection = this.businessStampService.addBusinessStampToCollectionIfMissing(
      this.businessStampsSharedCollection,
      ...(jobSheet.businessStamps ?? [])
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(jobSheet.placeholders ?? [])
    );
    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      ...(jobSheet.paymentLabels ?? [])
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
            this.editForm.get('biller')!.value,
            ...(this.editForm.get('signatories')!.value ?? []),
            this.editForm.get('contactPerson')!.value
          )
        )
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

    this.businessStampService
      .query()
      .pipe(map((res: HttpResponse<IBusinessStamp[]>) => res.body ?? []))
      .pipe(
        map((businessStamps: IBusinessStamp[]) =>
          this.businessStampService.addBusinessStampToCollectionIfMissing(
            businessStamps,
            ...(this.editForm.get('businessStamps')!.value ?? [])
          )
        )
      )
      .subscribe((businessStamps: IBusinessStamp[]) => (this.businessStampsSharedCollection = businessStamps));

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
  }

  protected createFromForm(): IJobSheet {
    return {
      ...new JobSheet(),
      id: this.editForm.get(['id'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      jobSheetDate: this.editForm.get(['jobSheetDate'])!.value,
      details: this.editForm.get(['details'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      biller: this.editForm.get(['biller'])!.value,
      signatories: this.editForm.get(['signatories'])!.value,
      contactPerson: this.editForm.get(['contactPerson'])!.value,
      businessStamps: this.editForm.get(['businessStamps'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
    };
  }
}
