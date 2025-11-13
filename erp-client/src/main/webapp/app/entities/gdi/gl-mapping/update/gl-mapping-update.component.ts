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
import { finalize } from 'rxjs/operators';

import { IGlMapping, GlMapping } from '../gl-mapping.model';
import { GlMappingService } from '../service/gl-mapping.service';

@Component({
  selector: 'jhi-gl-mapping-update',
  templateUrl: './gl-mapping-update.component.html',
})
export class GlMappingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    subGLCode: [null, [Validators.required]],
    subGLDescription: [],
    mainGLCode: [null, [Validators.required]],
    mainGLDescription: [],
    glType: [null, [Validators.required]],
  });

  constructor(protected glMappingService: GlMappingService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ glMapping }) => {
      this.updateForm(glMapping);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const glMapping = this.createFromForm();
    if (glMapping.id !== undefined) {
      this.subscribeToSaveResponse(this.glMappingService.update(glMapping));
    } else {
      this.subscribeToSaveResponse(this.glMappingService.create(glMapping));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGlMapping>>): void {
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

  protected updateForm(glMapping: IGlMapping): void {
    this.editForm.patchValue({
      id: glMapping.id,
      subGLCode: glMapping.subGLCode,
      subGLDescription: glMapping.subGLDescription,
      mainGLCode: glMapping.mainGLCode,
      mainGLDescription: glMapping.mainGLDescription,
      glType: glMapping.glType,
    });
  }

  protected createFromForm(): IGlMapping {
    return {
      ...new GlMapping(),
      id: this.editForm.get(['id'])!.value,
      subGLCode: this.editForm.get(['subGLCode'])!.value,
      subGLDescription: this.editForm.get(['subGLDescription'])!.value,
      mainGLCode: this.editForm.get(['mainGLCode'])!.value,
      mainGLDescription: this.editForm.get(['mainGLDescription'])!.value,
      glType: this.editForm.get(['glType'])!.value,
    };
  }
}
