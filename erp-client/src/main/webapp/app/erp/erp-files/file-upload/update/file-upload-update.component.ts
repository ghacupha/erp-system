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

import { IFileUpload, FileUpload } from '../file-upload.model';
import { FileUploadService } from '../service/file-upload.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-file-upload-update',
  templateUrl: './file-upload-update.component.html',
})
export class FileUploadUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required]],
    fileName: [null, [Validators.required]],
    periodFrom: [],
    periodTo: [],
    fileTypeId: [null, [Validators.required]],
    dataFile: [null, [Validators.required]],
    dataFileContentType: [],
    uploadSuccessful: [],
    uploadProcessed: [],
    uploadToken: [null, []],
    placeholders: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected fileUploadService: FileUploadService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileUpload }) => {
      this.updateForm(fileUpload);

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
    const fileUpload = this.createFromForm();
    if (fileUpload.id !== undefined) {
      this.subscribeToSaveResponse(this.fileUploadService.update(fileUpload));
    } else {
      this.subscribeToSaveResponse(this.fileUploadService.create(fileUpload));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileUpload>>): void {
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

  protected updateForm(fileUpload: IFileUpload): void {
    this.editForm.patchValue({
      id: fileUpload.id,
      description: fileUpload.description,
      fileName: fileUpload.fileName,
      periodFrom: fileUpload.periodFrom,
      periodTo: fileUpload.periodTo,
      fileTypeId: fileUpload.fileTypeId,
      dataFile: fileUpload.dataFile,
      dataFileContentType: fileUpload.dataFileContentType,
      uploadSuccessful: fileUpload.uploadSuccessful,
      uploadProcessed: fileUpload.uploadProcessed,
      uploadToken: fileUpload.uploadToken,
      placeholders: fileUpload.placeholders,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(fileUpload.placeholders ?? [])
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

  protected createFromForm(): IFileUpload {
    return {
      ...new FileUpload(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      fileName: this.editForm.get(['fileName'])!.value,
      periodFrom: this.editForm.get(['periodFrom'])!.value,
      periodTo: this.editForm.get(['periodTo'])!.value,
      fileTypeId: this.editForm.get(['fileTypeId'])!.value,
      dataFileContentType: this.editForm.get(['dataFileContentType'])!.value,
      dataFile: this.editForm.get(['dataFile'])!.value,
      uploadSuccessful: this.editForm.get(['uploadSuccessful'])!.value,
      uploadProcessed: this.editForm.get(['uploadProcessed'])!.value,
      uploadToken: this.editForm.get(['uploadToken'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
