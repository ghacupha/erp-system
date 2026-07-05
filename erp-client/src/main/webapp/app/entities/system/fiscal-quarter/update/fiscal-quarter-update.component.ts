import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFiscalQuarter, FiscalQuarter } from '../fiscal-quarter.model';
import { FiscalQuarterService } from '../service/fiscal-quarter.service';
import { IFiscalYear } from 'app/entities/system/fiscal-year/fiscal-year.model';
import { FiscalYearService } from 'app/entities/system/fiscal-year/service/fiscal-year.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';

@Component({
  selector: 'jhi-fiscal-quarter-update',
  templateUrl: './fiscal-quarter-update.component.html',
})
export class FiscalQuarterUpdateComponent implements OnInit {
  isSaving = false;

  fiscalYearsSharedCollection: IFiscalYear[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];

  editForm = this.fb.group({
    id: [],
    quarterNumber: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    fiscalQuarterCode: [null, [Validators.required]],
    fiscalYear: [null, Validators.required],
    placeholders: [],
    universallyUniqueMappings: [],
  });

  constructor(
    protected fiscalQuarterService: FiscalQuarterService,
    protected fiscalYearService: FiscalYearService,
    protected placeholderService: PlaceholderService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fiscalQuarter }) => {
      this.updateForm(fiscalQuarter);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fiscalQuarter = this.createFromForm();
    if (fiscalQuarter.id !== undefined) {
      this.subscribeToSaveResponse(this.fiscalQuarterService.update(fiscalQuarter));
    } else {
      this.subscribeToSaveResponse(this.fiscalQuarterService.create(fiscalQuarter));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFiscalQuarter>>): void {
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

  protected updateForm(fiscalQuarter: IFiscalQuarter): void {
    this.editForm.patchValue({
      id: fiscalQuarter.id,
      quarterNumber: fiscalQuarter.quarterNumber,
      startDate: fiscalQuarter.startDate,
      endDate: fiscalQuarter.endDate,
      fiscalQuarterCode: fiscalQuarter.fiscalQuarterCode,
      fiscalYear: fiscalQuarter.fiscalYear,
      placeholders: fiscalQuarter.placeholders,
      universallyUniqueMappings: fiscalQuarter.universallyUniqueMappings,
    });

    this.fiscalYearsSharedCollection = this.fiscalYearService.addFiscalYearToCollectionIfMissing(
      this.fiscalYearsSharedCollection,
      fiscalQuarter.fiscalYear
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(fiscalQuarter.placeholders ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(fiscalQuarter.universallyUniqueMappings ?? [])
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
  }

  protected createFromForm(): IFiscalQuarter {
    return {
      ...new FiscalQuarter(),
      id: this.editForm.get(['id'])!.value,
      quarterNumber: this.editForm.get(['quarterNumber'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      fiscalQuarterCode: this.editForm.get(['fiscalQuarterCode'])!.value,
      fiscalYear: this.editForm.get(['fiscalYear'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      universallyUniqueMappings: this.editForm.get(['universallyUniqueMappings'])!.value,
    };
  }
}
