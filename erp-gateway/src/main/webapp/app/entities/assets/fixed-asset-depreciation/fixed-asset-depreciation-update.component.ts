///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFixedAssetDepreciation, FixedAssetDepreciation } from 'app/shared/model/assets/fixed-asset-depreciation.model';
import { FixedAssetDepreciationService } from './fixed-asset-depreciation.service';

@Component({
  selector: 'jhi-fixed-asset-depreciation-update',
  templateUrl: './fixed-asset-depreciation-update.component.html',
})
export class FixedAssetDepreciationUpdateComponent implements OnInit {
  isSaving = false;
  depreciationDateDp: any;

  editForm = this.fb.group({
    id: [],
    assetNumber: [],
    serviceOutletCode: [],
    assetTag: [],
    assetDescription: [],
    depreciationDate: [],
    assetCategory: [],
    depreciationAmount: [],
    depreciationRegime: [],
    fileUploadToken: [],
    compilationToken: [],
  });

  constructor(
    protected fixedAssetDepreciationService: FixedAssetDepreciationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fixedAssetDepreciation }) => {
      this.updateForm(fixedAssetDepreciation);
    });
  }

  updateForm(fixedAssetDepreciation: IFixedAssetDepreciation): void {
    this.editForm.patchValue({
      id: fixedAssetDepreciation.id,
      assetNumber: fixedAssetDepreciation.assetNumber,
      serviceOutletCode: fixedAssetDepreciation.serviceOutletCode,
      assetTag: fixedAssetDepreciation.assetTag,
      assetDescription: fixedAssetDepreciation.assetDescription,
      depreciationDate: fixedAssetDepreciation.depreciationDate,
      assetCategory: fixedAssetDepreciation.assetCategory,
      depreciationAmount: fixedAssetDepreciation.depreciationAmount,
      depreciationRegime: fixedAssetDepreciation.depreciationRegime,
      fileUploadToken: fixedAssetDepreciation.fileUploadToken,
      compilationToken: fixedAssetDepreciation.compilationToken,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fixedAssetDepreciation = this.createFromForm();
    if (fixedAssetDepreciation.id !== undefined) {
      this.subscribeToSaveResponse(this.fixedAssetDepreciationService.update(fixedAssetDepreciation));
    } else {
      this.subscribeToSaveResponse(this.fixedAssetDepreciationService.create(fixedAssetDepreciation));
    }
  }

  private createFromForm(): IFixedAssetDepreciation {
    return {
      ...new FixedAssetDepreciation(),
      id: this.editForm.get(['id'])!.value,
      assetNumber: this.editForm.get(['assetNumber'])!.value,
      serviceOutletCode: this.editForm.get(['serviceOutletCode'])!.value,
      assetTag: this.editForm.get(['assetTag'])!.value,
      assetDescription: this.editForm.get(['assetDescription'])!.value,
      depreciationDate: this.editForm.get(['depreciationDate'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      depreciationAmount: this.editForm.get(['depreciationAmount'])!.value,
      depreciationRegime: this.editForm.get(['depreciationRegime'])!.value,
      fileUploadToken: this.editForm.get(['fileUploadToken'])!.value,
      compilationToken: this.editForm.get(['compilationToken'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFixedAssetDepreciation>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
