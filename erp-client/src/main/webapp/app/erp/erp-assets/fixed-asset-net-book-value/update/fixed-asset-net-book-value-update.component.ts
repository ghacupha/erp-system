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
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFixedAssetNetBookValue, FixedAssetNetBookValue } from '../fixed-asset-net-book-value.model';
import { FixedAssetNetBookValueService } from '../service/fixed-asset-net-book-value.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { DepreciationRegime } from '../../../erp-common/enumerations/depreciation-regime.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-fixed-asset-net-book-value-update',
  templateUrl: './fixed-asset-net-book-value-update.component.html',
})
export class FixedAssetNetBookValueUpdateComponent implements OnInit {
  isSaving = false;
  depreciationRegimeValues = Object.keys(DepreciationRegime);

  placeholdersSharedCollection: IPlaceholder[] = [];

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
    placeholders: [],
  });

  constructor(
    protected fixedAssetNetBookValueService: FixedAssetNetBookValueService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fixedAssetNetBookValue }) => {
      this.updateForm(fixedAssetNetBookValue);

      this.loadRelationshipsOptions();
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

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  getSelectedPlaceholder(option: IPlaceholder, selectedVals?: IPlaceholder[]): IPlaceholder {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFixedAssetNetBookValue>>): void {
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

  protected updateForm(fixedAssetNetBookValue: IFixedAssetNetBookValue): void {
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
      placeholders: fixedAssetNetBookValue.placeholders,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(fixedAssetNetBookValue.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  }

  protected createFromForm(): IFixedAssetNetBookValue {
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
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
