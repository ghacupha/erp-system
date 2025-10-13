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
import { finalize, map } from 'rxjs/operators';

import { IGdiTransactionDataIndex, GdiTransactionDataIndex } from '../gdi-transaction-data-index.model';
import { GdiTransactionDataIndexService } from '../service/gdi-transaction-data-index.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IGdiMasterDataIndex } from 'app/entities/gdi/gdi-master-data-index/gdi-master-data-index.model';
import { GdiMasterDataIndexService } from 'app/entities/gdi/gdi-master-data-index/service/gdi-master-data-index.service';
import { UpdateFrequencyTypes } from 'app/entities/enumerations/update-frequency-types.model';
import { DatasetBehaviorTypes } from 'app/entities/enumerations/dataset-behavior-types.model';

@Component({
  selector: 'jhi-gdi-transaction-data-index-update',
  templateUrl: './gdi-transaction-data-index-update.component.html',
})
export class GdiTransactionDataIndexUpdateComponent implements OnInit {
  isSaving = false;
  updateFrequencyTypesValues = Object.keys(UpdateFrequencyTypes);
  datasetBehaviorTypesValues = Object.keys(DatasetBehaviorTypes);

  gdiMasterDataIndicesSharedCollection: IGdiMasterDataIndex[] = [];

  editForm = this.fb.group({
    id: [],
    datasetName: [null, [Validators.required]],
    databaseName: [null, [Validators.required]],
    updateFrequency: [null, [Validators.required]],
    datasetBehavior: [null, [Validators.required]],
    minimumDatarowsPerRequest: [],
    maximumDataRowsPerRequest: [],
    datasetDescription: [],
    dataTemplate: [],
    dataTemplateContentType: [],
    masterDataItems: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected gdiTransactionDataIndexService: GdiTransactionDataIndexService,
    protected gdiMasterDataIndexService: GdiMasterDataIndexService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gdiTransactionDataIndex }) => {
      this.updateForm(gdiTransactionDataIndex);

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
    const gdiTransactionDataIndex = this.createFromForm();
    if (gdiTransactionDataIndex.id !== undefined) {
      this.subscribeToSaveResponse(this.gdiTransactionDataIndexService.update(gdiTransactionDataIndex));
    } else {
      this.subscribeToSaveResponse(this.gdiTransactionDataIndexService.create(gdiTransactionDataIndex));
    }
  }

  trackGdiMasterDataIndexById(index: number, item: IGdiMasterDataIndex): number {
    return item.id!;
  }

  getSelectedGdiMasterDataIndex(option: IGdiMasterDataIndex, selectedVals?: IGdiMasterDataIndex[]): IGdiMasterDataIndex {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGdiTransactionDataIndex>>): void {
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

  protected updateForm(gdiTransactionDataIndex: IGdiTransactionDataIndex): void {
    this.editForm.patchValue({
      id: gdiTransactionDataIndex.id,
      datasetName: gdiTransactionDataIndex.datasetName,
      databaseName: gdiTransactionDataIndex.databaseName,
      updateFrequency: gdiTransactionDataIndex.updateFrequency,
      datasetBehavior: gdiTransactionDataIndex.datasetBehavior,
      minimumDatarowsPerRequest: gdiTransactionDataIndex.minimumDatarowsPerRequest,
      maximumDataRowsPerRequest: gdiTransactionDataIndex.maximumDataRowsPerRequest,
      datasetDescription: gdiTransactionDataIndex.datasetDescription,
      dataTemplate: gdiTransactionDataIndex.dataTemplate,
      dataTemplateContentType: gdiTransactionDataIndex.dataTemplateContentType,
      masterDataItems: gdiTransactionDataIndex.masterDataItems,
    });

    this.gdiMasterDataIndicesSharedCollection = this.gdiMasterDataIndexService.addGdiMasterDataIndexToCollectionIfMissing(
      this.gdiMasterDataIndicesSharedCollection,
      ...(gdiTransactionDataIndex.masterDataItems ?? [])
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
            ...(this.editForm.get('masterDataItems')!.value ?? [])
          )
        )
      )
      .subscribe((gdiMasterDataIndices: IGdiMasterDataIndex[]) => (this.gdiMasterDataIndicesSharedCollection = gdiMasterDataIndices));
  }

  protected createFromForm(): IGdiTransactionDataIndex {
    return {
      ...new GdiTransactionDataIndex(),
      id: this.editForm.get(['id'])!.value,
      datasetName: this.editForm.get(['datasetName'])!.value,
      databaseName: this.editForm.get(['databaseName'])!.value,
      updateFrequency: this.editForm.get(['updateFrequency'])!.value,
      datasetBehavior: this.editForm.get(['datasetBehavior'])!.value,
      minimumDatarowsPerRequest: this.editForm.get(['minimumDatarowsPerRequest'])!.value,
      maximumDataRowsPerRequest: this.editForm.get(['maximumDataRowsPerRequest'])!.value,
      datasetDescription: this.editForm.get(['datasetDescription'])!.value,
      dataTemplateContentType: this.editForm.get(['dataTemplateContentType'])!.value,
      dataTemplate: this.editForm.get(['dataTemplate'])!.value,
      masterDataItems: this.editForm.get(['masterDataItems'])!.value,
    };
  }
}
