import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUniversallyUniqueMapping, UniversallyUniqueMapping } from '../universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from '../service/universally-unique-mapping.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-universally-unique-mapping-update',
  templateUrl: './universally-unique-mapping-update.component.html',
})
export class UniversallyUniqueMappingUpdateComponent implements OnInit {
  isSaving = false;

  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    universalKey: [null, [Validators.required]],
    mappedValue: [],
    parentMapping: [],
    placeholders: [],
  });

  constructor(
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ universallyUniqueMapping }) => {
      this.updateForm(universallyUniqueMapping);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const universallyUniqueMapping = this.createFromForm();
    if (universallyUniqueMapping.id !== undefined) {
      this.subscribeToSaveResponse(this.universallyUniqueMappingService.update(universallyUniqueMapping));
    } else {
      this.subscribeToSaveResponse(this.universallyUniqueMappingService.create(universallyUniqueMapping));
    }
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUniversallyUniqueMapping>>): void {
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

  protected updateForm(universallyUniqueMapping: IUniversallyUniqueMapping): void {
    this.editForm.patchValue({
      id: universallyUniqueMapping.id,
      universalKey: universallyUniqueMapping.universalKey,
      mappedValue: universallyUniqueMapping.mappedValue,
      parentMapping: universallyUniqueMapping.parentMapping,
      placeholders: universallyUniqueMapping.placeholders,
    });

    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      universallyUniqueMapping.parentMapping
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(universallyUniqueMapping.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.universallyUniqueMappingService
      .query()
      .pipe(map((res: HttpResponse<IUniversallyUniqueMapping[]>) => res.body ?? []))
      .pipe(
        map((universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
            universallyUniqueMappings,
            this.editForm.get('parentMapping')!.value
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
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
  }

  protected createFromForm(): IUniversallyUniqueMapping {
    return {
      ...new UniversallyUniqueMapping(),
      id: this.editForm.get(['id'])!.value,
      universalKey: this.editForm.get(['universalKey'])!.value,
      mappedValue: this.editForm.get(['mappedValue'])!.value,
      parentMapping: this.editForm.get(['parentMapping'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
