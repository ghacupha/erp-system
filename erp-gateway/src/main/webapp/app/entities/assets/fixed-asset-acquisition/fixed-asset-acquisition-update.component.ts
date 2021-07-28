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

import { IFixedAssetAcquisition, FixedAssetAcquisition } from 'app/shared/model/assets/fixed-asset-acquisition.model';
import { FixedAssetAcquisitionService } from './fixed-asset-acquisition.service';

@Component({
  selector: 'jhi-fixed-asset-acquisition-update',
  templateUrl: './fixed-asset-acquisition-update.component.html',
})
export class FixedAssetAcquisitionUpdateComponent implements OnInit {
  isSaving = false;
  purchaseDateDp: any;

  editForm = this.fb.group({
    id: [],
    assetNumber: [null, []],
    serviceOutletCode: [],
    assetTag: [],
    assetDescription: [],
    purchaseDate: [],
    assetCategory: [],
    purchasePrice: [],
    fileUploadToken: [],
  });

  constructor(
    protected fixedAssetAcquisitionService: FixedAssetAcquisitionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fixedAssetAcquisition }) => {
      this.updateForm(fixedAssetAcquisition);
    });
  }

  updateForm(fixedAssetAcquisition: IFixedAssetAcquisition): void {
    this.editForm.patchValue({
      id: fixedAssetAcquisition.id,
      assetNumber: fixedAssetAcquisition.assetNumber,
      serviceOutletCode: fixedAssetAcquisition.serviceOutletCode,
      assetTag: fixedAssetAcquisition.assetTag,
      assetDescription: fixedAssetAcquisition.assetDescription,
      purchaseDate: fixedAssetAcquisition.purchaseDate,
      assetCategory: fixedAssetAcquisition.assetCategory,
      purchasePrice: fixedAssetAcquisition.purchasePrice,
      fileUploadToken: fixedAssetAcquisition.fileUploadToken,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fixedAssetAcquisition = this.createFromForm();
    if (fixedAssetAcquisition.id !== undefined) {
      this.subscribeToSaveResponse(this.fixedAssetAcquisitionService.update(fixedAssetAcquisition));
    } else {
      this.subscribeToSaveResponse(this.fixedAssetAcquisitionService.create(fixedAssetAcquisition));
    }
  }

  private createFromForm(): IFixedAssetAcquisition {
    return {
      ...new FixedAssetAcquisition(),
      id: this.editForm.get(['id'])!.value,
      assetNumber: this.editForm.get(['assetNumber'])!.value,
      serviceOutletCode: this.editForm.get(['serviceOutletCode'])!.value,
      assetTag: this.editForm.get(['assetTag'])!.value,
      assetDescription: this.editForm.get(['assetDescription'])!.value,
      purchaseDate: this.editForm.get(['purchaseDate'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      purchasePrice: this.editForm.get(['purchasePrice'])!.value,
      fileUploadToken: this.editForm.get(['fileUploadToken'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFixedAssetAcquisition>>): void {
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
