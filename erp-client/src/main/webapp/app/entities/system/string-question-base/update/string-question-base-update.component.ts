import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStringQuestionBase, StringQuestionBase } from '../string-question-base.model';
import { StringQuestionBaseService } from '../service/string-question-base.service';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { ControlTypes } from 'app/entities/enumerations/control-types.model';

@Component({
  selector: 'jhi-string-question-base-update',
  templateUrl: './string-question-base-update.component.html',
})
export class StringQuestionBaseUpdateComponent implements OnInit {
  isSaving = false;
  controlTypesValues = Object.keys(ControlTypes);

  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    value: [],
    key: [null, [Validators.required]],
    label: [null, [Validators.required]],
    required: [],
    order: [null, [Validators.required]],
    controlType: [null, [Validators.required]],
    placeholder: [],
    iterable: [],
    parameters: [],
    placeholderItems: [],
  });

  constructor(
    protected stringQuestionBaseService: StringQuestionBaseService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stringQuestionBase }) => {
      this.updateForm(stringQuestionBase);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stringQuestionBase = this.createFromForm();
    if (stringQuestionBase.id !== undefined) {
      this.subscribeToSaveResponse(this.stringQuestionBaseService.update(stringQuestionBase));
    } else {
      this.subscribeToSaveResponse(this.stringQuestionBaseService.create(stringQuestionBase));
    }
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStringQuestionBase>>): void {
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

  protected updateForm(stringQuestionBase: IStringQuestionBase): void {
    this.editForm.patchValue({
      id: stringQuestionBase.id,
      value: stringQuestionBase.value,
      key: stringQuestionBase.key,
      label: stringQuestionBase.label,
      required: stringQuestionBase.required,
      order: stringQuestionBase.order,
      controlType: stringQuestionBase.controlType,
      placeholder: stringQuestionBase.placeholder,
      iterable: stringQuestionBase.iterable,
      parameters: stringQuestionBase.parameters,
      placeholderItems: stringQuestionBase.placeholderItems,
    });

    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(stringQuestionBase.parameters ?? [])
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(stringQuestionBase.placeholderItems ?? [])
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
            ...(this.editForm.get('parameters')!.value ?? [])
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
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholderItems')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  }

  protected createFromForm(): IStringQuestionBase {
    return {
      ...new StringQuestionBase(),
      id: this.editForm.get(['id'])!.value,
      value: this.editForm.get(['value'])!.value,
      key: this.editForm.get(['key'])!.value,
      label: this.editForm.get(['label'])!.value,
      required: this.editForm.get(['required'])!.value,
      order: this.editForm.get(['order'])!.value,
      controlType: this.editForm.get(['controlType'])!.value,
      placeholder: this.editForm.get(['placeholder'])!.value,
      iterable: this.editForm.get(['iterable'])!.value,
      parameters: this.editForm.get(['parameters'])!.value,
      placeholderItems: this.editForm.get(['placeholderItems'])!.value,
    };
  }
}
