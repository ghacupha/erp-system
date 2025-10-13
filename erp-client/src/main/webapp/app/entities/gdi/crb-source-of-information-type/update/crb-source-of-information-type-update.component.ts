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
import { finalize } from 'rxjs/operators';

import { ICrbSourceOfInformationType, CrbSourceOfInformationType } from '../crb-source-of-information-type.model';
import { CrbSourceOfInformationTypeService } from '../service/crb-source-of-information-type.service';

@Component({
  selector: 'jhi-crb-source-of-information-type-update',
  templateUrl: './crb-source-of-information-type-update.component.html',
})
export class CrbSourceOfInformationTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    sourceOfInformationTypeCode: [null, [Validators.required]],
    sourceOfInformationTypeDescription: [null, []],
  });

  constructor(
    protected crbSourceOfInformationTypeService: CrbSourceOfInformationTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbSourceOfInformationType }) => {
      this.updateForm(crbSourceOfInformationType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const crbSourceOfInformationType = this.createFromForm();
    if (crbSourceOfInformationType.id !== undefined) {
      this.subscribeToSaveResponse(this.crbSourceOfInformationTypeService.update(crbSourceOfInformationType));
    } else {
      this.subscribeToSaveResponse(this.crbSourceOfInformationTypeService.create(crbSourceOfInformationType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICrbSourceOfInformationType>>): void {
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

  protected updateForm(crbSourceOfInformationType: ICrbSourceOfInformationType): void {
    this.editForm.patchValue({
      id: crbSourceOfInformationType.id,
      sourceOfInformationTypeCode: crbSourceOfInformationType.sourceOfInformationTypeCode,
      sourceOfInformationTypeDescription: crbSourceOfInformationType.sourceOfInformationTypeDescription,
    });
  }

  protected createFromForm(): ICrbSourceOfInformationType {
    return {
      ...new CrbSourceOfInformationType(),
      id: this.editForm.get(['id'])!.value,
      sourceOfInformationTypeCode: this.editForm.get(['sourceOfInformationTypeCode'])!.value,
      sourceOfInformationTypeDescription: this.editForm.get(['sourceOfInformationTypeDescription'])!.value,
    };
  }
}
