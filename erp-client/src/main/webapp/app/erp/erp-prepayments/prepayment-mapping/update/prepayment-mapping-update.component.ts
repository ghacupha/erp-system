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

import { IPrepaymentMapping, PrepaymentMapping } from '../prepayment-mapping.model';
import { PrepaymentMappingService } from '../service/prepayment-mapping.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'jhi-prepayment-mapping-update',
  templateUrl: './prepayment-mapping-update.component.html',
})
export class PrepaymentMappingUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    parameterKey: [null, [Validators.required]],
    parameterGuid: [null, [Validators.required]],
    parameter: [null, [Validators.required]],
    placeholders: [],
  });

  constructor(
    protected prepaymentMappingService: PrepaymentMappingService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentMapping }) => {
      this.updateForm(prepaymentMapping);

      this.loadRelationshipsOptions();
    });

    this.editForm.patchValue({
      parameterGuid: uuidv4(),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prepaymentMapping = this.createFromForm();
    if (prepaymentMapping.id !== undefined) {
      this.subscribeToSaveResponse(this.prepaymentMappingService.update(prepaymentMapping));
    } else {
      this.subscribeToSaveResponse(this.prepaymentMappingService.create(prepaymentMapping));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrepaymentMapping>>): void {
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

  protected updateForm(prepaymentMapping: IPrepaymentMapping): void {
    this.editForm.patchValue({
      id: prepaymentMapping.id,
      parameterKey: prepaymentMapping.parameterKey,
      parameterGuid: prepaymentMapping.parameterGuid,
      parameter: prepaymentMapping.parameter,
      placeholders: prepaymentMapping.placeholders,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(prepaymentMapping.placeholders ?? [])
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

  protected createFromForm(): IPrepaymentMapping {
    return {
      ...new PrepaymentMapping(),
      id: this.editForm.get(['id'])!.value,
      parameterKey: this.editForm.get(['parameterKey'])!.value,
      parameterGuid: this.editForm.get(['parameterGuid'])!.value,
      parameter: this.editForm.get(['parameter'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
