///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPrepaymentCompilationRequest, PrepaymentCompilationRequest } from '../prepayment-compilation-request.model';
import { PrepaymentCompilationRequestService } from '../service/prepayment-compilation-request.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { CompilationStatusTypes } from 'app/entities/enumerations/compilation-status-types.model';

@Component({
  selector: 'jhi-prepayment-compilation-request-update',
  templateUrl: './prepayment-compilation-request-update.component.html',
})
export class PrepaymentCompilationRequestUpdateComponent implements OnInit {
  isSaving = false;
  compilationStatusTypesValues = Object.keys(CompilationStatusTypes);

  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    timeOfRequest: [],
    compilationStatus: [],
    itemsProcessed: [],
    compilationToken: [null, [Validators.required]],
    placeholders: [],
  });

  constructor(
    protected prepaymentCompilationRequestService: PrepaymentCompilationRequestService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentCompilationRequest }) => {
      if (prepaymentCompilationRequest.id === undefined) {
        const today = dayjs().startOf('day');
        prepaymentCompilationRequest.timeOfRequest = today;
      }

      this.updateForm(prepaymentCompilationRequest);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prepaymentCompilationRequest = this.createFromForm();
    if (prepaymentCompilationRequest.id !== undefined) {
      this.subscribeToSaveResponse(this.prepaymentCompilationRequestService.update(prepaymentCompilationRequest));
    } else {
      this.subscribeToSaveResponse(this.prepaymentCompilationRequestService.create(prepaymentCompilationRequest));
    }
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrepaymentCompilationRequest>>): void {
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

  protected updateForm(prepaymentCompilationRequest: IPrepaymentCompilationRequest): void {
    this.editForm.patchValue({
      id: prepaymentCompilationRequest.id,
      timeOfRequest: prepaymentCompilationRequest.timeOfRequest
        ? prepaymentCompilationRequest.timeOfRequest.format(DATE_TIME_FORMAT)
        : null,
      compilationStatus: prepaymentCompilationRequest.compilationStatus,
      itemsProcessed: prepaymentCompilationRequest.itemsProcessed,
      compilationToken: prepaymentCompilationRequest.compilationToken,
      placeholders: prepaymentCompilationRequest.placeholders,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(prepaymentCompilationRequest.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IPrepaymentCompilationRequest {
    return {
      ...new PrepaymentCompilationRequest(),
      id: this.editForm.get(['id'])!.value,
      timeOfRequest: this.editForm.get(['timeOfRequest'])!.value
        ? dayjs(this.editForm.get(['timeOfRequest'])!.value, DATE_TIME_FORMAT)
        : undefined,
      compilationStatus: this.editForm.get(['compilationStatus'])!.value,
      itemsProcessed: this.editForm.get(['itemsProcessed'])!.value,
      compilationToken: this.editForm.get(['compilationToken'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
