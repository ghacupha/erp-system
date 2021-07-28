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

import { IFixedAssetNetBookValue, FixedAssetNetBookValue } from 'app/shared/model/assets/fixed-asset-net-book-value.model';
import { FixedAssetNetBookValueService } from './fixed-asset-net-book-value.service';

@Component({
  selector: 'jhi-fixed-asset-net-book-value-update',
  templateUrl: './fixed-asset-net-book-value-update.component.html',
})
export class FixedAssetNetBookValueUpdateComponent implements OnInit {
  isSaving = false;
  netBookValueDateDp: any;

  editForm = this.fb.group({
    id: [],
    assetNumber: [],
    serviceOutletCode: [],
    assetTag: [],
    assetDescription: [],
    netBookValueDate: [],
    assetCategory: [],
    netBookValue: [],
    depreciationRegime: [],
    fileUploadToken: [],
    compilationToken: [],
  });

  constructor(
    protected fixedAssetNetBookValueService: FixedAssetNetBookValueService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fixedAssetNetBookValue }) => {
      this.updateForm(fixedAssetNetBookValue);
    });
  }

  updateForm(fixedAssetNetBookValue: IFixedAssetNetBookValue): void {
    this.editForm.patchValue({
      id: fixedAssetNetBookValue.id,
      assetNumber: fixedAssetNetBookValue.assetNumber,
      serviceOutletCode: fixedAssetNetBookValue.serviceOutletCode,
      assetTag: fixedAssetNetBookValue.assetTag,
      assetDescription: fixedAssetNetBookValue.assetDescription,
      netBookValueDate: fixedAssetNetBookValue.netBookValueDate,
      assetCategory: fixedAssetNetBookValue.assetCategory,
      netBookValue: fixedAssetNetBookValue.netBookValue,
      depreciationRegime: fixedAssetNetBookValue.depreciationRegime,
      fileUploadToken: fixedAssetNetBookValue.fileUploadToken,
      compilationToken: fixedAssetNetBookValue.compilationToken,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fixedAssetNetBookValue = this.createFromForm();
    if (fixedAssetNetBookValue.id !== undefined) {
      this.subscribeToSaveResponse(this.fixedAssetNetBookValueService.update(fixedAssetNetBookValue));
    } else {
      this.subscribeToSaveResponse(this.fixedAssetNetBookValueService.create(fixedAssetNetBookValue));
    }
  }

  private createFromForm(): IFixedAssetNetBookValue {
    return {
      ...new FixedAssetNetBookValue(),
      id: this.editForm.get(['id'])!.value,
      assetNumber: this.editForm.get(['assetNumber'])!.value,
      serviceOutletCode: this.editForm.get(['serviceOutletCode'])!.value,
      assetTag: this.editForm.get(['assetTag'])!.value,
      assetDescription: this.editForm.get(['assetDescription'])!.value,
      netBookValueDate: this.editForm.get(['netBookValueDate'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      netBookValue: this.editForm.get(['netBookValue'])!.value,
      depreciationRegime: this.editForm.get(['depreciationRegime'])!.value,
      fileUploadToken: this.editForm.get(['fileUploadToken'])!.value,
      compilationToken: this.editForm.get(['compilationToken'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFixedAssetNetBookValue>>): void {
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
