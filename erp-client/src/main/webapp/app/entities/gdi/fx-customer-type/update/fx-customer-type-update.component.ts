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

import { IFxCustomerType, FxCustomerType } from '../fx-customer-type.model';
import { FxCustomerTypeService } from '../service/fx-customer-type.service';

@Component({
  selector: 'jhi-fx-customer-type-update',
  templateUrl: './fx-customer-type-update.component.html',
})
export class FxCustomerTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    foreignExchangeCustomerTypeCode: [null, [Validators.required]],
    foreignCustomerType: [null, [Validators.required]],
  });

  constructor(
    protected fxCustomerTypeService: FxCustomerTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fxCustomerType }) => {
      this.updateForm(fxCustomerType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fxCustomerType = this.createFromForm();
    if (fxCustomerType.id !== undefined) {
      this.subscribeToSaveResponse(this.fxCustomerTypeService.update(fxCustomerType));
    } else {
      this.subscribeToSaveResponse(this.fxCustomerTypeService.create(fxCustomerType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFxCustomerType>>): void {
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

  protected updateForm(fxCustomerType: IFxCustomerType): void {
    this.editForm.patchValue({
      id: fxCustomerType.id,
      foreignExchangeCustomerTypeCode: fxCustomerType.foreignExchangeCustomerTypeCode,
      foreignCustomerType: fxCustomerType.foreignCustomerType,
    });
  }

  protected createFromForm(): IFxCustomerType {
    return {
      ...new FxCustomerType(),
      id: this.editForm.get(['id'])!.value,
      foreignExchangeCustomerTypeCode: this.editForm.get(['foreignExchangeCustomerTypeCode'])!.value,
      foreignCustomerType: this.editForm.get(['foreignCustomerType'])!.value,
    };
  }
}
