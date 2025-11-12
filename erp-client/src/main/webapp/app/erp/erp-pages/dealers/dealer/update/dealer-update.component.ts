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

import { IPaymentLabel } from '../../../payment-label/payment-label.model';
import { Dealer, IDealer } from '../dealer.model';
import { DealerService } from '../service/dealer.service';
import { PaymentLabelService } from '../../../payment-label/service/payment-label.service';
import { DealerSuggestionService } from '../../../../erp-common/suggestion/dealer-suggestion.service';
import { LabelSuggestionService } from '../../../../erp-common/suggestion/label-suggestion.service';
import { PlaceholderSuggestionService } from '../../../../erp-common/suggestion/placeholder-suggestion.service';
import { IPlaceholder } from '../../../placeholder/placeholder.model';
import { PlaceholderService } from '../../../placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-dealer-update',
  templateUrl: './dealer-update.component.html',
})
export class DealerUpdateComponent implements OnInit {
  isSaving = false;

  paymentLabelsSharedCollection: IPaymentLabel[] = [];
  dealersSharedCollection: IDealer[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    dealerName: [null, [Validators.required]],
    taxNumber: [],
    identificationDocumentNumber: [],
    organizationName: [],
    department: [],
    position: [],
    postalAddress: [],
    physicalAddress: [],
    accountName: [],
    accountNumber: [],
    bankersName: [],
    bankersBranch: [],
    bankersSwiftCode: [],
    paymentLabels: [],
    remarks: [],
    dealerGroup: [],
    placeholders: [],
    otherNames: [],
  });

  minAccountLengthTerm = 3;

  labelsLoading = false;
  labelControlInput$ = new Subject<string>();
  labelLookups$: Observable<IPaymentLabel[]> = of([]);

  dealersLoading = false;
  dealerGroupInput$ = new Subject<string>();
  dealerLookups$: Observable<IDealer[]> = of([]);

  placeholdersLoading = false;
  placeholderControlInput$ = new Subject<string>();
  placeholderLookups$: Observable<IPlaceholder[]> = of([]);

  constructor(
    protected dealerService: DealerService,
    protected paymentLabelService: PaymentLabelService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected dealerSuggestionService: DealerSuggestionService,
    protected labelSuggestionService: LabelSuggestionService,
    protected placeholderSuggestionService: PlaceholderSuggestionService,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dealer }) => {
      this.updateForm(dealer);

      this.loadRelationshipsOptions();
    });

    // fire-up typeahead items
    this.loadDealers();
    this.loadLabels();
    this.loadPlaceholders();
  }

  loadDealers(): void {
    this.dealerLookups$ = concat(
      of([]), // default items
      this.dealerGroupInput$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.dealersLoading = true),
        switchMap(term => this.dealerSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.dealersLoading = false)
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

  trackPlaceholdersByFn(item: IPaymentLabel): number {
    return item.id!;
  }

  trackLabelByFn(item: IPaymentLabel): number {
    return item.id!;
  }

  trackDealerGroupByFn(item: IDealer): number {
    return item.id!;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dealer = this.createFromForm();
    if (dealer.id !== undefined) {
      this.subscribeToSaveResponse(this.dealerService.update(dealer));
    } else {
      this.subscribeToSaveResponse(this.dealerService.create(dealer));
    }
  }

  trackPaymentLabelById(index: number, item: IPaymentLabel): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDealer>>): void {
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

  protected updateForm(dealer: IDealer): void {
    this.editForm.patchValue({
      id: dealer.id,
      dealerName: dealer.dealerName,
      taxNumber: dealer.taxNumber,
      identificationDocumentNumber: dealer.identificationDocumentNumber,
      organizationName: dealer.organizationName,
      department: dealer.department,
      position: dealer.position,
      postalAddress: dealer.postalAddress,
      physicalAddress: dealer.physicalAddress,
      accountName: dealer.accountName,
      accountNumber: dealer.accountNumber,
      bankersName: dealer.bankersName,
      bankersBranch: dealer.bankersBranch,
      bankersSwiftCode: dealer.bankersSwiftCode,
      paymentLabels: dealer.paymentLabels,
      dealerGroup: dealer.dealerGroup,
      placeholders: dealer.placeholders,
      otherNames: dealer.otherNames,
    });

    this.paymentLabelsSharedCollection = this.paymentLabelService.addPaymentLabelToCollectionIfMissing(
      this.paymentLabelsSharedCollection,
      ...(dealer.paymentLabels ?? [])
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(this.dealersSharedCollection, dealer.dealerGroup);
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(dealer.placeholders ?? [])
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
      .pipe(
        map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing(dealers, this.editForm.get('dealerGroup')!.value))
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

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

  protected createFromForm(): IDealer {
    return {
      ...new Dealer(),
      id: this.editForm.get(['id'])!.value,
      dealerName: this.editForm.get(['dealerName'])!.value,
      taxNumber: this.editForm.get(['taxNumber'])!.value,
      identificationDocumentNumber: this.editForm.get(['identificationDocumentNumber'])!.value,
      organizationName: this.editForm.get(['organizationName'])!.value,
      department: this.editForm.get(['department'])!.value,
      position: this.editForm.get(['position'])!.value,
      postalAddress: this.editForm.get(['postalAddress'])!.value,
      physicalAddress: this.editForm.get(['physicalAddress'])!.value,
      accountName: this.editForm.get(['accountName'])!.value,
      accountNumber: this.editForm.get(['accountNumber'])!.value,
      bankersName: this.editForm.get(['bankersName'])!.value,
      bankersBranch: this.editForm.get(['bankersBranch'])!.value,
      bankersSwiftCode: this.editForm.get(['bankersSwiftCode'])!.value,
      paymentLabels: this.editForm.get(['paymentLabels'])!.value,
      dealerGroup: this.editForm.get(['dealerGroup'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      otherNames: this.editForm.get(['otherNames'])!.value,
    };
  }
}
