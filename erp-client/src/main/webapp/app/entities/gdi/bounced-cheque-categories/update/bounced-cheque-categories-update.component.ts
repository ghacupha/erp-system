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

import { IBouncedChequeCategories, BouncedChequeCategories } from '../bounced-cheque-categories.model';
import { BouncedChequeCategoriesService } from '../service/bounced-cheque-categories.service';

@Component({
  selector: 'jhi-bounced-cheque-categories-update',
  templateUrl: './bounced-cheque-categories-update.component.html',
})
export class BouncedChequeCategoriesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    bouncedChequeCategoryTypeCode: [null, [Validators.required]],
    bouncedChequeCategoryType: [null, [Validators.required]],
  });

  constructor(
    protected bouncedChequeCategoriesService: BouncedChequeCategoriesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bouncedChequeCategories }) => {
      this.updateForm(bouncedChequeCategories);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bouncedChequeCategories = this.createFromForm();
    if (bouncedChequeCategories.id !== undefined) {
      this.subscribeToSaveResponse(this.bouncedChequeCategoriesService.update(bouncedChequeCategories));
    } else {
      this.subscribeToSaveResponse(this.bouncedChequeCategoriesService.create(bouncedChequeCategories));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBouncedChequeCategories>>): void {
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

  protected updateForm(bouncedChequeCategories: IBouncedChequeCategories): void {
    this.editForm.patchValue({
      id: bouncedChequeCategories.id,
      bouncedChequeCategoryTypeCode: bouncedChequeCategories.bouncedChequeCategoryTypeCode,
      bouncedChequeCategoryType: bouncedChequeCategories.bouncedChequeCategoryType,
    });
  }

  protected createFromForm(): IBouncedChequeCategories {
    return {
      ...new BouncedChequeCategories(),
      id: this.editForm.get(['id'])!.value,
      bouncedChequeCategoryTypeCode: this.editForm.get(['bouncedChequeCategoryTypeCode'])!.value,
      bouncedChequeCategoryType: this.editForm.get(['bouncedChequeCategoryType'])!.value,
    };
  }
}
