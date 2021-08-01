import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFixedAssetAcquisition, FixedAssetAcquisition } from '../fixed-asset-acquisition.model';
import { FixedAssetAcquisitionService } from '../service/fixed-asset-acquisition.service';

@Component({
  selector: 'jhi-fixed-asset-acquisition-update',
  templateUrl: './fixed-asset-acquisition-update.component.html',
})
export class FixedAssetAcquisitionUpdateComponent implements OnInit {
  isSaving = false;

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
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fixedAssetAcquisition }) => {
      this.updateForm(fixedAssetAcquisition);
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFixedAssetAcquisition>>): void {
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

  protected updateForm(fixedAssetAcquisition: IFixedAssetAcquisition): void {
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

  protected createFromForm(): IFixedAssetAcquisition {
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
}
