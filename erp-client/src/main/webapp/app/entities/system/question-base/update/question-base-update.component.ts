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
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestionBase, QuestionBase } from '../question-base.model';
import { QuestionBaseService } from '../service/question-base.service';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { ControlTypes } from 'app/entities/enumerations/control-types.model';

@Component({
  selector: 'jhi-question-base-update',
  templateUrl: './question-base-update.component.html',
})
export class QuestionBaseUpdateComponent implements OnInit {
  isSaving = false;
  controlTypesValues = Object.keys(ControlTypes);

  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    context: [null, [Validators.required]],
    serial: [null, [Validators.required]],
    questionBaseValue: [],
    questionBaseKey: [null, [Validators.required]],
    questionBaseLabel: [null, [Validators.required]],
    required: [],
    order: [null, [Validators.required]],
    controlType: [null, [Validators.required]],
    placeholder: [],
    iterable: [],
    parameters: [],
    placeholderItems: [],
  });

  constructor(
    protected questionBaseService: QuestionBaseService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionBase }) => {
      this.updateForm(questionBase);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionBase = this.createFromForm();
    if (questionBase.id !== undefined) {
      this.subscribeToSaveResponse(this.questionBaseService.update(questionBase));
    } else {
      this.subscribeToSaveResponse(this.questionBaseService.create(questionBase));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionBase>>): void {
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

  protected updateForm(questionBase: IQuestionBase): void {
    this.editForm.patchValue({
      id: questionBase.id,
      context: questionBase.context,
      serial: questionBase.serial,
      questionBaseValue: questionBase.questionBaseValue,
      questionBaseKey: questionBase.questionBaseKey,
      questionBaseLabel: questionBase.questionBaseLabel,
      required: questionBase.required,
      order: questionBase.order,
      controlType: questionBase.controlType,
      placeholder: questionBase.placeholder,
      iterable: questionBase.iterable,
      parameters: questionBase.parameters,
      placeholderItems: questionBase.placeholderItems,
    });

    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(questionBase.parameters ?? [])
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(questionBase.placeholderItems ?? [])
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

  protected createFromForm(): IQuestionBase {
    return {
      ...new QuestionBase(),
      id: this.editForm.get(['id'])!.value,
      context: this.editForm.get(['context'])!.value,
      serial: this.editForm.get(['serial'])!.value,
      questionBaseValue: this.editForm.get(['questionBaseValue'])!.value,
      questionBaseKey: this.editForm.get(['questionBaseKey'])!.value,
      questionBaseLabel: this.editForm.get(['questionBaseLabel'])!.value,
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
