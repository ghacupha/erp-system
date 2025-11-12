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
import { finalize } from 'rxjs/operators';

import { IUniversallyUniqueMapping, UniversallyUniqueMapping } from '../universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from '../service/universally-unique-mapping.service';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'jhi-universally-unique-mapping-update',
  templateUrl: './universally-unique-mapping-update.component.html',
})
export class UniversallyUniqueMappingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    universalKey: [null, [Validators.required]],
    mappedValue: [],
  });

  constructor(
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ universallyUniqueMapping }) => {
      if (!universallyUniqueMapping.id) {
        this.editForm.patchValue({
          universalKey: uuidv4(),
        });
      }
      this.updateForm(universallyUniqueMapping);
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
    });
  }

  protected createFromForm(): IUniversallyUniqueMapping {
    return {
      ...new UniversallyUniqueMapping(),
      id: this.editForm.get(['id'])!.value,
      universalKey: this.editForm.get(['universalKey'])!.value,
      mappedValue: this.editForm.get(['mappedValue'])!.value,
    };
  }
}
