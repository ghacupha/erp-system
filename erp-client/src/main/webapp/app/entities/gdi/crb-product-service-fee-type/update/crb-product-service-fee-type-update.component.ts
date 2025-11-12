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

import { ICrbProductServiceFeeType, CrbProductServiceFeeType } from '../crb-product-service-fee-type.model';
import { CrbProductServiceFeeTypeService } from '../service/crb-product-service-fee-type.service';

@Component({
  selector: 'jhi-crb-product-service-fee-type-update',
  templateUrl: './crb-product-service-fee-type-update.component.html',
})
export class CrbProductServiceFeeTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    chargeTypeCode: [null, [Validators.required]],
    chargeTypeDescription: [null, []],
    chargeTypeCategory: [null, [Validators.required]],
  });

  constructor(
    protected crbProductServiceFeeTypeService: CrbProductServiceFeeTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbProductServiceFeeType }) => {
      this.updateForm(crbProductServiceFeeType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const crbProductServiceFeeType = this.createFromForm();
    if (crbProductServiceFeeType.id !== undefined) {
      this.subscribeToSaveResponse(this.crbProductServiceFeeTypeService.update(crbProductServiceFeeType));
    } else {
      this.subscribeToSaveResponse(this.crbProductServiceFeeTypeService.create(crbProductServiceFeeType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICrbProductServiceFeeType>>): void {
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

  protected updateForm(crbProductServiceFeeType: ICrbProductServiceFeeType): void {
    this.editForm.patchValue({
      id: crbProductServiceFeeType.id,
      chargeTypeCode: crbProductServiceFeeType.chargeTypeCode,
      chargeTypeDescription: crbProductServiceFeeType.chargeTypeDescription,
      chargeTypeCategory: crbProductServiceFeeType.chargeTypeCategory,
    });
  }

  protected createFromForm(): ICrbProductServiceFeeType {
    return {
      ...new CrbProductServiceFeeType(),
      id: this.editForm.get(['id'])!.value,
      chargeTypeCode: this.editForm.get(['chargeTypeCode'])!.value,
      chargeTypeDescription: this.editForm.get(['chargeTypeDescription'])!.value,
      chargeTypeCategory: this.editForm.get(['chargeTypeCategory'])!.value,
    };
  }
}
