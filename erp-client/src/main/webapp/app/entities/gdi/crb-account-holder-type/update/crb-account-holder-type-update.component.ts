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

import { ICrbAccountHolderType, CrbAccountHolderType } from '../crb-account-holder-type.model';
import { CrbAccountHolderTypeService } from '../service/crb-account-holder-type.service';

@Component({
  selector: 'jhi-crb-account-holder-type-update',
  templateUrl: './crb-account-holder-type-update.component.html',
})
export class CrbAccountHolderTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    accountHolderCategoryTypeCode: [null, [Validators.required]],
    accountHolderCategoryType: [null, [Validators.required]],
  });

  constructor(
    protected crbAccountHolderTypeService: CrbAccountHolderTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbAccountHolderType }) => {
      this.updateForm(crbAccountHolderType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const crbAccountHolderType = this.createFromForm();
    if (crbAccountHolderType.id !== undefined) {
      this.subscribeToSaveResponse(this.crbAccountHolderTypeService.update(crbAccountHolderType));
    } else {
      this.subscribeToSaveResponse(this.crbAccountHolderTypeService.create(crbAccountHolderType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICrbAccountHolderType>>): void {
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

  protected updateForm(crbAccountHolderType: ICrbAccountHolderType): void {
    this.editForm.patchValue({
      id: crbAccountHolderType.id,
      accountHolderCategoryTypeCode: crbAccountHolderType.accountHolderCategoryTypeCode,
      accountHolderCategoryType: crbAccountHolderType.accountHolderCategoryType,
    });
  }

  protected createFromForm(): ICrbAccountHolderType {
    return {
      ...new CrbAccountHolderType(),
      id: this.editForm.get(['id'])!.value,
      accountHolderCategoryTypeCode: this.editForm.get(['accountHolderCategoryTypeCode'])!.value,
      accountHolderCategoryType: this.editForm.get(['accountHolderCategoryType'])!.value,
    };
  }
}
