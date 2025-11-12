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
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAmortizationSequence, AmortizationSequence } from '../amortization-sequence.model';
import { AmortizationSequenceService } from '../service/amortization-sequence.service';
import { IPrepaymentAccount } from '../../prepayment-account/prepayment-account.model';
import { IAmortizationRecurrence } from '../../amortization-recurrence/amortization-recurrence.model';
import { IPrepaymentMapping } from '../../prepayment-mapping/prepayment-mapping.model';
import { IUniversallyUniqueMapping } from '../../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { AmortizationRecurrenceService } from '../../amortization-recurrence/service/amortization-recurrence.service';
import { PrepaymentAccountService } from '../../prepayment-account/service/prepayment-account.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { PrepaymentMappingService } from '../../prepayment-mapping/service/prepayment-mapping.service';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';

@Component({
  selector: 'jhi-amortization-sequence-update',
  templateUrl: './amortization-sequence-update.component.html',
})
export class AmortizationSequenceUpdateComponent implements OnInit {
  isSaving = false;

  prepaymentAccountsSharedCollection: IPrepaymentAccount[] = [];
  amortizationRecurrencesSharedCollection: IAmortizationRecurrence[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  prepaymentMappingsSharedCollection: IPrepaymentMapping[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];

  editForm = this.fb.group({
    id: [],
    prepaymentAccountGuid: [null, [Validators.required]],
    recurrenceGuid: [null, [Validators.required]],
    sequenceNumber: [null, [Validators.required]],
    particulars: [],
    currentAmortizationDate: [null, [Validators.required]],
    previousAmortizationDate: [],
    nextAmortizationDate: [],
    isCommencementSequence: [null, [Validators.required]],
    isTerminalSequence: [null, [Validators.required]],
    amortizationAmount: [null, [Validators.required, Validators.min(0)]],
    sequenceGuid: [null, [Validators.required]],
    prepaymentAccount: [null, Validators.required],
    amortizationRecurrence: [null, Validators.required],
    placeholders: [],
    prepaymentMappings: [],
    applicationParameters: [],
  });

  constructor(
    protected amortizationSequenceService: AmortizationSequenceService,
    protected prepaymentAccountService: PrepaymentAccountService,
    protected amortizationRecurrenceService: AmortizationRecurrenceService,
    protected placeholderService: PlaceholderService,
    protected prepaymentMappingService: PrepaymentMappingService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amortizationSequence }) => {
      this.updateForm(amortizationSequence);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const amortizationSequence = this.createFromForm();
    if (amortizationSequence.id !== undefined) {
      this.subscribeToSaveResponse(this.amortizationSequenceService.update(amortizationSequence));
    } else {
      this.subscribeToSaveResponse(this.amortizationSequenceService.create(amortizationSequence));
    }
  }

  trackPrepaymentAccountById(index: number, item: IPrepaymentAccount): number {
    return item.id!;
  }

  trackAmortizationRecurrenceById(index: number, item: IAmortizationRecurrence): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackPrepaymentMappingById(index: number, item: IPrepaymentMapping): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
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

  getSelectedPrepaymentMapping(option: IPrepaymentMapping, selectedVals?: IPrepaymentMapping[]): IPrepaymentMapping {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedUniversallyUniqueMapping(
    option: IUniversallyUniqueMapping,
    selectedVals?: IUniversallyUniqueMapping[]
  ): IUniversallyUniqueMapping {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAmortizationSequence>>): void {
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

  protected updateForm(amortizationSequence: IAmortizationSequence): void {
    this.editForm.patchValue({
      id: amortizationSequence.id,
      prepaymentAccountGuid: amortizationSequence.prepaymentAccountGuid,
      recurrenceGuid: amortizationSequence.recurrenceGuid,
      sequenceNumber: amortizationSequence.sequenceNumber,
      particulars: amortizationSequence.particulars,
      currentAmortizationDate: amortizationSequence.currentAmortizationDate,
      previousAmortizationDate: amortizationSequence.previousAmortizationDate,
      nextAmortizationDate: amortizationSequence.nextAmortizationDate,
      isCommencementSequence: amortizationSequence.isCommencementSequence,
      isTerminalSequence: amortizationSequence.isTerminalSequence,
      amortizationAmount: amortizationSequence.amortizationAmount,
      sequenceGuid: amortizationSequence.sequenceGuid,
      prepaymentAccount: amortizationSequence.prepaymentAccount,
      amortizationRecurrence: amortizationSequence.amortizationRecurrence,
      placeholders: amortizationSequence.placeholders,
      prepaymentMappings: amortizationSequence.prepaymentMappings,
      applicationParameters: amortizationSequence.applicationParameters,
    });

    this.prepaymentAccountsSharedCollection = this.prepaymentAccountService.addPrepaymentAccountToCollectionIfMissing(
      this.prepaymentAccountsSharedCollection,
      amortizationSequence.prepaymentAccount
    );
    this.amortizationRecurrencesSharedCollection = this.amortizationRecurrenceService.addAmortizationRecurrenceToCollectionIfMissing(
      this.amortizationRecurrencesSharedCollection,
      amortizationSequence.amortizationRecurrence
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(amortizationSequence.placeholders ?? [])
    );
    this.prepaymentMappingsSharedCollection = this.prepaymentMappingService.addPrepaymentMappingToCollectionIfMissing(
      this.prepaymentMappingsSharedCollection,
      ...(amortizationSequence.prepaymentMappings ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(amortizationSequence.applicationParameters ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.prepaymentAccountService
      .query()
      .pipe(map((res: HttpResponse<IPrepaymentAccount[]>) => res.body ?? []))
      .pipe(
        map((prepaymentAccounts: IPrepaymentAccount[]) =>
          this.prepaymentAccountService.addPrepaymentAccountToCollectionIfMissing(
            prepaymentAccounts,
            this.editForm.get('prepaymentAccount')!.value
          )
        )
      )
      .subscribe((prepaymentAccounts: IPrepaymentAccount[]) => (this.prepaymentAccountsSharedCollection = prepaymentAccounts));

    this.amortizationRecurrenceService
      .query()
      .pipe(map((res: HttpResponse<IAmortizationRecurrence[]>) => res.body ?? []))
      .pipe(
        map((amortizationRecurrences: IAmortizationRecurrence[]) =>
          this.amortizationRecurrenceService.addAmortizationRecurrenceToCollectionIfMissing(
            amortizationRecurrences,
            this.editForm.get('amortizationRecurrence')!.value
          )
        )
      )
      .subscribe(
        (amortizationRecurrences: IAmortizationRecurrence[]) => (this.amortizationRecurrencesSharedCollection = amortizationRecurrences)
      );

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.prepaymentMappingService
      .query()
      .pipe(map((res: HttpResponse<IPrepaymentMapping[]>) => res.body ?? []))
      .pipe(
        map((prepaymentMappings: IPrepaymentMapping[]) =>
          this.prepaymentMappingService.addPrepaymentMappingToCollectionIfMissing(
            prepaymentMappings,
            ...(this.editForm.get('prepaymentMappings')!.value ?? [])
          )
        )
      )
      .subscribe((prepaymentMappings: IPrepaymentMapping[]) => (this.prepaymentMappingsSharedCollection = prepaymentMappings));

    this.universallyUniqueMappingService
      .query()
      .pipe(map((res: HttpResponse<IUniversallyUniqueMapping[]>) => res.body ?? []))
      .pipe(
        map((universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
            universallyUniqueMappings,
            ...(this.editForm.get('applicationParameters')!.value ?? [])
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
      );
  }

  protected createFromForm(): IAmortizationSequence {
    return {
      ...new AmortizationSequence(),
      id: this.editForm.get(['id'])!.value,
      prepaymentAccountGuid: this.editForm.get(['prepaymentAccountGuid'])!.value,
      recurrenceGuid: this.editForm.get(['recurrenceGuid'])!.value,
      sequenceNumber: this.editForm.get(['sequenceNumber'])!.value,
      particulars: this.editForm.get(['particulars'])!.value,
      currentAmortizationDate: this.editForm.get(['currentAmortizationDate'])!.value,
      previousAmortizationDate: this.editForm.get(['previousAmortizationDate'])!.value,
      nextAmortizationDate: this.editForm.get(['nextAmortizationDate'])!.value,
      isCommencementSequence: this.editForm.get(['isCommencementSequence'])!.value,
      isTerminalSequence: this.editForm.get(['isTerminalSequence'])!.value,
      amortizationAmount: this.editForm.get(['amortizationAmount'])!.value,
      sequenceGuid: this.editForm.get(['sequenceGuid'])!.value,
      prepaymentAccount: this.editForm.get(['prepaymentAccount'])!.value,
      amortizationRecurrence: this.editForm.get(['amortizationRecurrence'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      prepaymentMappings: this.editForm.get(['prepaymentMappings'])!.value,
      applicationParameters: this.editForm.get(['applicationParameters'])!.value,
    };
  }
}
