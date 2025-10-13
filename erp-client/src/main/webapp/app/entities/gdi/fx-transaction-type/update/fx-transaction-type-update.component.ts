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

import { IFxTransactionType, FxTransactionType } from '../fx-transaction-type.model';
import { FxTransactionTypeService } from '../service/fx-transaction-type.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-fx-transaction-type-update',
  templateUrl: './fx-transaction-type-update.component.html',
})
export class FxTransactionTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fxTransactionTypeCode: [null, [Validators.required]],
    fxTransactionType: [null, [Validators.required]],
    fxTransactionTypeDescription: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected fxTransactionTypeService: FxTransactionTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fxTransactionType }) => {
      this.updateForm(fxTransactionType);
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
    const fxTransactionType = this.createFromForm();
    if (fxTransactionType.id !== undefined) {
      this.subscribeToSaveResponse(this.fxTransactionTypeService.update(fxTransactionType));
    } else {
      this.subscribeToSaveResponse(this.fxTransactionTypeService.create(fxTransactionType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFxTransactionType>>): void {
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

  protected updateForm(fxTransactionType: IFxTransactionType): void {
    this.editForm.patchValue({
      id: fxTransactionType.id,
      fxTransactionTypeCode: fxTransactionType.fxTransactionTypeCode,
      fxTransactionType: fxTransactionType.fxTransactionType,
      fxTransactionTypeDescription: fxTransactionType.fxTransactionTypeDescription,
    });
  }

  protected createFromForm(): IFxTransactionType {
    return {
      ...new FxTransactionType(),
      id: this.editForm.get(['id'])!.value,
      fxTransactionTypeCode: this.editForm.get(['fxTransactionTypeCode'])!.value,
      fxTransactionType: this.editForm.get(['fxTransactionType'])!.value,
      fxTransactionTypeDescription: this.editForm.get(['fxTransactionTypeDescription'])!.value,
    };
  }
}
