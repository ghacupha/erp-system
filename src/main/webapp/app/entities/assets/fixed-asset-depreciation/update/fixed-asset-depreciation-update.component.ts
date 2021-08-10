import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFixedAssetDepreciation, FixedAssetDepreciation } from '../fixed-asset-depreciation.model';
import { FixedAssetDepreciationService } from '../service/fixed-asset-depreciation.service';

@Component({
  selector: 'gha-fixed-asset-depreciation-update',
  templateUrl: './fixed-asset-depreciation-update.component.html',
})
export class FixedAssetDepreciationUpdateComponent implements OnInit {
  isSaving = false;

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
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fixedAssetDepreciation }) => {
      this.updateForm(fixedAssetDepreciation);
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFixedAssetDepreciation>>): void {
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

  protected updateForm(fixedAssetDepreciation: IFixedAssetDepreciation): void {
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

  protected createFromForm(): IFixedAssetDepreciation {
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
}
