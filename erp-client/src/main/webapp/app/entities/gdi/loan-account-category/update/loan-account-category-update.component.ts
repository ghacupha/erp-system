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

import { ILoanAccountCategory, LoanAccountCategory } from '../loan-account-category.model';
import { LoanAccountCategoryService } from '../service/loan-account-category.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { LoanAccountMutationTypes } from 'app/entities/enumerations/loan-account-mutation-types.model';

@Component({
  selector: 'jhi-loan-account-category-update',
  templateUrl: './loan-account-category-update.component.html',
})
export class LoanAccountCategoryUpdateComponent implements OnInit {
  isSaving = false;
  loanAccountMutationTypesValues = Object.keys(LoanAccountMutationTypes);

  editForm = this.fb.group({
    id: [],
    loanAccountMutationCode: [null, [Validators.required]],
    loanAccountMutationType: [null, [Validators.required]],
    loanAccountMutationDetails: [null, [Validators.required]],
    loanAccountMutationDescription: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected loanAccountCategoryService: LoanAccountCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ loanAccountCategory }) => {
      this.updateForm(loanAccountCategory);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('erpSystemApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const loanAccountCategory = this.createFromForm();
    if (loanAccountCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.loanAccountCategoryService.update(loanAccountCategory));
    } else {
      this.subscribeToSaveResponse(this.loanAccountCategoryService.create(loanAccountCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILoanAccountCategory>>): void {
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

  protected updateForm(loanAccountCategory: ILoanAccountCategory): void {
    this.editForm.patchValue({
      id: loanAccountCategory.id,
      loanAccountMutationCode: loanAccountCategory.loanAccountMutationCode,
      loanAccountMutationType: loanAccountCategory.loanAccountMutationType,
      loanAccountMutationDetails: loanAccountCategory.loanAccountMutationDetails,
      loanAccountMutationDescription: loanAccountCategory.loanAccountMutationDescription,
    });
  }

  protected createFromForm(): ILoanAccountCategory {
    return {
      ...new LoanAccountCategory(),
      id: this.editForm.get(['id'])!.value,
      loanAccountMutationCode: this.editForm.get(['loanAccountMutationCode'])!.value,
      loanAccountMutationType: this.editForm.get(['loanAccountMutationType'])!.value,
      loanAccountMutationDetails: this.editForm.get(['loanAccountMutationDetails'])!.value,
      loanAccountMutationDescription: this.editForm.get(['loanAccountMutationDescription'])!.value,
    };
  }
}
