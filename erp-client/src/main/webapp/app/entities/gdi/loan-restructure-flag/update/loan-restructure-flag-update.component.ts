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

import { ILoanRestructureFlag, LoanRestructureFlag } from '../loan-restructure-flag.model';
import { LoanRestructureFlagService } from '../service/loan-restructure-flag.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { FlagCodes } from 'app/entities/enumerations/flag-codes.model';

@Component({
  selector: 'jhi-loan-restructure-flag-update',
  templateUrl: './loan-restructure-flag-update.component.html',
})
export class LoanRestructureFlagUpdateComponent implements OnInit {
  isSaving = false;
  flagCodesValues = Object.keys(FlagCodes);

  editForm = this.fb.group({
    id: [],
    loanRestructureFlagCode: [null, [Validators.required]],
    loanRestructureFlagType: [null, [Validators.required]],
    loanRestructureFlagDetails: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected loanRestructureFlagService: LoanRestructureFlagService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ loanRestructureFlag }) => {
      this.updateForm(loanRestructureFlag);
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
    const loanRestructureFlag = this.createFromForm();
    if (loanRestructureFlag.id !== undefined) {
      this.subscribeToSaveResponse(this.loanRestructureFlagService.update(loanRestructureFlag));
    } else {
      this.subscribeToSaveResponse(this.loanRestructureFlagService.create(loanRestructureFlag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILoanRestructureFlag>>): void {
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

  protected updateForm(loanRestructureFlag: ILoanRestructureFlag): void {
    this.editForm.patchValue({
      id: loanRestructureFlag.id,
      loanRestructureFlagCode: loanRestructureFlag.loanRestructureFlagCode,
      loanRestructureFlagType: loanRestructureFlag.loanRestructureFlagType,
      loanRestructureFlagDetails: loanRestructureFlag.loanRestructureFlagDetails,
    });
  }

  protected createFromForm(): ILoanRestructureFlag {
    return {
      ...new LoanRestructureFlag(),
      id: this.editForm.get(['id'])!.value,
      loanRestructureFlagCode: this.editForm.get(['loanRestructureFlagCode'])!.value,
      loanRestructureFlagType: this.editForm.get(['loanRestructureFlagType'])!.value,
      loanRestructureFlagDetails: this.editForm.get(['loanRestructureFlagDetails'])!.value,
    };
  }
}
