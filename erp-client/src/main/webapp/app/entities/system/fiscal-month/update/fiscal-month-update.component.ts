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

import { IFiscalMonth, FiscalMonth } from '../fiscal-month.model';
import { FiscalMonthService } from '../service/fiscal-month.service';
import { IFiscalYear } from 'app/entities/system/fiscal-year/fiscal-year.model';
import { FiscalYearService } from 'app/entities/system/fiscal-year/service/fiscal-year.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';
import { IFiscalQuarter } from 'app/entities/system/fiscal-quarter/fiscal-quarter.model';
import { FiscalQuarterService } from 'app/entities/system/fiscal-quarter/service/fiscal-quarter.service';

@Component({
  selector: 'jhi-fiscal-month-update',
  templateUrl: './fiscal-month-update.component.html',
})
export class FiscalMonthUpdateComponent implements OnInit {
  isSaving = false;

  fiscalYearsSharedCollection: IFiscalYear[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  fiscalQuartersSharedCollection: IFiscalQuarter[] = [];

  editForm = this.fb.group({
    id: [],
    monthNumber: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    fiscalMonthCode: [null, [Validators.required]],
    fiscalYear: [null, Validators.required],
    placeholders: [],
    universallyUniqueMappings: [],
    fiscalQuarter: [],
  });

  constructor(
    protected fiscalMonthService: FiscalMonthService,
    protected fiscalYearService: FiscalYearService,
    protected placeholderService: PlaceholderService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected fiscalQuarterService: FiscalQuarterService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fiscalMonth }) => {
      this.updateForm(fiscalMonth);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fiscalMonth = this.createFromForm();
    if (fiscalMonth.id !== undefined) {
      this.subscribeToSaveResponse(this.fiscalMonthService.update(fiscalMonth));
    } else {
      this.subscribeToSaveResponse(this.fiscalMonthService.create(fiscalMonth));
    }
  }

  trackFiscalYearById(index: number, item: IFiscalYear): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  trackFiscalQuarterById(index: number, item: IFiscalQuarter): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFiscalMonth>>): void {
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

  protected updateForm(fiscalMonth: IFiscalMonth): void {
    this.editForm.patchValue({
      id: fiscalMonth.id,
      monthNumber: fiscalMonth.monthNumber,
      startDate: fiscalMonth.startDate,
      endDate: fiscalMonth.endDate,
      fiscalMonthCode: fiscalMonth.fiscalMonthCode,
      fiscalYear: fiscalMonth.fiscalYear,
      placeholders: fiscalMonth.placeholders,
      universallyUniqueMappings: fiscalMonth.universallyUniqueMappings,
      fiscalQuarter: fiscalMonth.fiscalQuarter,
    });

    this.fiscalYearsSharedCollection = this.fiscalYearService.addFiscalYearToCollectionIfMissing(
      this.fiscalYearsSharedCollection,
      fiscalMonth.fiscalYear
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(fiscalMonth.placeholders ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(fiscalMonth.universallyUniqueMappings ?? [])
    );
    this.fiscalQuartersSharedCollection = this.fiscalQuarterService.addFiscalQuarterToCollectionIfMissing(
      this.fiscalQuartersSharedCollection,
      fiscalMonth.fiscalQuarter
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fiscalYearService
      .query()
      .pipe(map((res: HttpResponse<IFiscalYear[]>) => res.body ?? []))
      .pipe(
        map((fiscalYears: IFiscalYear[]) =>
          this.fiscalYearService.addFiscalYearToCollectionIfMissing(fiscalYears, this.editForm.get('fiscalYear')!.value)
        )
      )
      .subscribe((fiscalYears: IFiscalYear[]) => (this.fiscalYearsSharedCollection = fiscalYears));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.universallyUniqueMappingService
      .query()
      .pipe(map((res: HttpResponse<IUniversallyUniqueMapping[]>) => res.body ?? []))
      .pipe(
        map((universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
            universallyUniqueMappings,
            ...(this.editForm.get('universallyUniqueMappings')!.value ?? [])
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
      );

    this.fiscalQuarterService
      .query()
      .pipe(map((res: HttpResponse<IFiscalQuarter[]>) => res.body ?? []))
      .pipe(
        map((fiscalQuarters: IFiscalQuarter[]) =>
          this.fiscalQuarterService.addFiscalQuarterToCollectionIfMissing(fiscalQuarters, this.editForm.get('fiscalQuarter')!.value)
        )
      )
      .subscribe((fiscalQuarters: IFiscalQuarter[]) => (this.fiscalQuartersSharedCollection = fiscalQuarters));
  }

  protected createFromForm(): IFiscalMonth {
    return {
      ...new FiscalMonth(),
      id: this.editForm.get(['id'])!.value,
      monthNumber: this.editForm.get(['monthNumber'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      fiscalMonthCode: this.editForm.get(['fiscalMonthCode'])!.value,
      fiscalYear: this.editForm.get(['fiscalYear'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      universallyUniqueMappings: this.editForm.get(['universallyUniqueMappings'])!.value,
      fiscalQuarter: this.editForm.get(['fiscalQuarter'])!.value,
    };
  }
}
