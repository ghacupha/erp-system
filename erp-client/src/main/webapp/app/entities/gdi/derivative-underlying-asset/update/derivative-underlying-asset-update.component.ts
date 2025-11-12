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

import { IDerivativeUnderlyingAsset, DerivativeUnderlyingAsset } from '../derivative-underlying-asset.model';
import { DerivativeUnderlyingAssetService } from '../service/derivative-underlying-asset.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-derivative-underlying-asset-update',
  templateUrl: './derivative-underlying-asset-update.component.html',
})
export class DerivativeUnderlyingAssetUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    derivativeUnderlyingAssetTypeCode: [null, [Validators.required]],
    financialDerivativeUnderlyingAssetType: [null, [Validators.required]],
    derivativeUnderlyingAssetTypeDetails: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected derivativeUnderlyingAssetService: DerivativeUnderlyingAssetService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ derivativeUnderlyingAsset }) => {
      this.updateForm(derivativeUnderlyingAsset);
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
    const derivativeUnderlyingAsset = this.createFromForm();
    if (derivativeUnderlyingAsset.id !== undefined) {
      this.subscribeToSaveResponse(this.derivativeUnderlyingAssetService.update(derivativeUnderlyingAsset));
    } else {
      this.subscribeToSaveResponse(this.derivativeUnderlyingAssetService.create(derivativeUnderlyingAsset));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDerivativeUnderlyingAsset>>): void {
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

  protected updateForm(derivativeUnderlyingAsset: IDerivativeUnderlyingAsset): void {
    this.editForm.patchValue({
      id: derivativeUnderlyingAsset.id,
      derivativeUnderlyingAssetTypeCode: derivativeUnderlyingAsset.derivativeUnderlyingAssetTypeCode,
      financialDerivativeUnderlyingAssetType: derivativeUnderlyingAsset.financialDerivativeUnderlyingAssetType,
      derivativeUnderlyingAssetTypeDetails: derivativeUnderlyingAsset.derivativeUnderlyingAssetTypeDetails,
    });
  }

  protected createFromForm(): IDerivativeUnderlyingAsset {
    return {
      ...new DerivativeUnderlyingAsset(),
      id: this.editForm.get(['id'])!.value,
      derivativeUnderlyingAssetTypeCode: this.editForm.get(['derivativeUnderlyingAssetTypeCode'])!.value,
      financialDerivativeUnderlyingAssetType: this.editForm.get(['financialDerivativeUnderlyingAssetType'])!.value,
      derivativeUnderlyingAssetTypeDetails: this.editForm.get(['derivativeUnderlyingAssetTypeDetails'])!.value,
    };
  }
}
