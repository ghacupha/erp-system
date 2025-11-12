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
import { finalize, map } from 'rxjs/operators';

import { IAccountAttributeMetadata, AccountAttributeMetadata } from '../account-attribute-metadata.model';
import { AccountAttributeMetadataService } from '../service/account-attribute-metadata.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IGdiMasterDataIndex } from 'app/entities/gdi/gdi-master-data-index/gdi-master-data-index.model';
import { GdiMasterDataIndexService } from 'app/entities/gdi/gdi-master-data-index/service/gdi-master-data-index.service';
import { MandatoryFieldFlagTypes } from 'app/entities/enumerations/mandatory-field-flag-types.model';

@Component({
  selector: 'jhi-account-attribute-metadata-update',
  templateUrl: './account-attribute-metadata-update.component.html',
})
export class AccountAttributeMetadataUpdateComponent implements OnInit {
  isSaving = false;
  mandatoryFieldFlagTypesValues = Object.keys(MandatoryFieldFlagTypes);

  gdiMasterDataIndicesSharedCollection: IGdiMasterDataIndex[] = [];

  editForm = this.fb.group({
    id: [],
    precedence: [null, [Validators.required]],
    columnName: [null, [Validators.required]],
    shortName: [null, [Validators.required]],
    detailedDefinition: [],
    dataType: [null, [Validators.required]],
    length: [],
    columnIndex: [],
    mandatoryFieldFlag: [null, [Validators.required]],
    businessValidation: [],
    technicalValidation: [],
    dbColumnName: [],
    metadataVersion: [],
    standardInputTemplate: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected accountAttributeMetadataService: AccountAttributeMetadataService,
    protected gdiMasterDataIndexService: GdiMasterDataIndexService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountAttributeMetadata }) => {
      this.updateForm(accountAttributeMetadata);

      this.loadRelationshipsOptions();
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
    const accountAttributeMetadata = this.createFromForm();
    if (accountAttributeMetadata.id !== undefined) {
      this.subscribeToSaveResponse(this.accountAttributeMetadataService.update(accountAttributeMetadata));
    } else {
      this.subscribeToSaveResponse(this.accountAttributeMetadataService.create(accountAttributeMetadata));
    }
  }

  trackGdiMasterDataIndexById(index: number, item: IGdiMasterDataIndex): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountAttributeMetadata>>): void {
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

  protected updateForm(accountAttributeMetadata: IAccountAttributeMetadata): void {
    this.editForm.patchValue({
      id: accountAttributeMetadata.id,
      precedence: accountAttributeMetadata.precedence,
      columnName: accountAttributeMetadata.columnName,
      shortName: accountAttributeMetadata.shortName,
      detailedDefinition: accountAttributeMetadata.detailedDefinition,
      dataType: accountAttributeMetadata.dataType,
      length: accountAttributeMetadata.length,
      columnIndex: accountAttributeMetadata.columnIndex,
      mandatoryFieldFlag: accountAttributeMetadata.mandatoryFieldFlag,
      businessValidation: accountAttributeMetadata.businessValidation,
      technicalValidation: accountAttributeMetadata.technicalValidation,
      dbColumnName: accountAttributeMetadata.dbColumnName,
      metadataVersion: accountAttributeMetadata.metadataVersion,
      standardInputTemplate: accountAttributeMetadata.standardInputTemplate,
    });

    this.gdiMasterDataIndicesSharedCollection = this.gdiMasterDataIndexService.addGdiMasterDataIndexToCollectionIfMissing(
      this.gdiMasterDataIndicesSharedCollection,
      accountAttributeMetadata.standardInputTemplate
    );
  }

  protected loadRelationshipsOptions(): void {
    this.gdiMasterDataIndexService
      .query()
      .pipe(map((res: HttpResponse<IGdiMasterDataIndex[]>) => res.body ?? []))
      .pipe(
        map((gdiMasterDataIndices: IGdiMasterDataIndex[]) =>
          this.gdiMasterDataIndexService.addGdiMasterDataIndexToCollectionIfMissing(
            gdiMasterDataIndices,
            this.editForm.get('standardInputTemplate')!.value
          )
        )
      )
      .subscribe((gdiMasterDataIndices: IGdiMasterDataIndex[]) => (this.gdiMasterDataIndicesSharedCollection = gdiMasterDataIndices));
  }

  protected createFromForm(): IAccountAttributeMetadata {
    return {
      ...new AccountAttributeMetadata(),
      id: this.editForm.get(['id'])!.value,
      precedence: this.editForm.get(['precedence'])!.value,
      columnName: this.editForm.get(['columnName'])!.value,
      shortName: this.editForm.get(['shortName'])!.value,
      detailedDefinition: this.editForm.get(['detailedDefinition'])!.value,
      dataType: this.editForm.get(['dataType'])!.value,
      length: this.editForm.get(['length'])!.value,
      columnIndex: this.editForm.get(['columnIndex'])!.value,
      mandatoryFieldFlag: this.editForm.get(['mandatoryFieldFlag'])!.value,
      businessValidation: this.editForm.get(['businessValidation'])!.value,
      technicalValidation: this.editForm.get(['technicalValidation'])!.value,
      dbColumnName: this.editForm.get(['dbColumnName'])!.value,
      metadataVersion: this.editForm.get(['metadataVersion'])!.value,
      standardInputTemplate: this.editForm.get(['standardInputTemplate'])!.value,
    };
  }
}
