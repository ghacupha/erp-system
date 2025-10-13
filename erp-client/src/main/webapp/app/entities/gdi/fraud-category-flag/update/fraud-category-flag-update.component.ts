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

import { IFraudCategoryFlag, FraudCategoryFlag } from '../fraud-category-flag.model';
import { FraudCategoryFlagService } from '../service/fraud-category-flag.service';
import { FlagCodes } from 'app/entities/enumerations/flag-codes.model';

@Component({
  selector: 'jhi-fraud-category-flag-update',
  templateUrl: './fraud-category-flag-update.component.html',
})
export class FraudCategoryFlagUpdateComponent implements OnInit {
  isSaving = false;
  flagCodesValues = Object.keys(FlagCodes);

  editForm = this.fb.group({
    id: [],
    fraudCategoryFlag: [null, [Validators.required]],
    fraudCategoryTypeDetails: [],
  });

  constructor(
    protected fraudCategoryFlagService: FraudCategoryFlagService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fraudCategoryFlag }) => {
      this.updateForm(fraudCategoryFlag);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fraudCategoryFlag = this.createFromForm();
    if (fraudCategoryFlag.id !== undefined) {
      this.subscribeToSaveResponse(this.fraudCategoryFlagService.update(fraudCategoryFlag));
    } else {
      this.subscribeToSaveResponse(this.fraudCategoryFlagService.create(fraudCategoryFlag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFraudCategoryFlag>>): void {
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

  protected updateForm(fraudCategoryFlag: IFraudCategoryFlag): void {
    this.editForm.patchValue({
      id: fraudCategoryFlag.id,
      fraudCategoryFlag: fraudCategoryFlag.fraudCategoryFlag,
      fraudCategoryTypeDetails: fraudCategoryFlag.fraudCategoryTypeDetails,
    });
  }

  protected createFromForm(): IFraudCategoryFlag {
    return {
      ...new FraudCategoryFlag(),
      id: this.editForm.get(['id'])!.value,
      fraudCategoryFlag: this.editForm.get(['fraudCategoryFlag'])!.value,
      fraudCategoryTypeDetails: this.editForm.get(['fraudCategoryTypeDetails'])!.value,
    };
  }
}
