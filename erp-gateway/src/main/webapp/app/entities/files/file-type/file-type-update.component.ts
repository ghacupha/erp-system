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
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IFileType, FileType } from 'app/shared/model/files/file-type.model';
import { FileTypeService } from './file-type.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-file-type-update',
  templateUrl: './file-type-update.component.html',
})
export class FileTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fileTypeName: [null, [Validators.required]],
    fileMediumType: [null, [Validators.required]],
    description: [],
    fileTemplate: [],
    fileTemplateContentType: [],
    fileType: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected fileTypeService: FileTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileType }) => {
      this.updateForm(fileType);
    });
  }

  updateForm(fileType: IFileType): void {
    this.editForm.patchValue({
      id: fileType.id,
      fileTypeName: fileType.fileTypeName,
      fileMediumType: fileType.fileMediumType,
      description: fileType.description,
      fileTemplate: fileType.fileTemplate,
      fileTemplateContentType: fileType.fileTemplateContentType,
      fileType: fileType.fileType,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('erpGatewayApp.error', { message: err.message })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fileType = this.createFromForm();
    if (fileType.id !== undefined) {
      this.subscribeToSaveResponse(this.fileTypeService.update(fileType));
    } else {
      this.subscribeToSaveResponse(this.fileTypeService.create(fileType));
    }
  }

  private createFromForm(): IFileType {
    return {
      ...new FileType(),
      id: this.editForm.get(['id'])!.value,
      fileTypeName: this.editForm.get(['fileTypeName'])!.value,
      fileMediumType: this.editForm.get(['fileMediumType'])!.value,
      description: this.editForm.get(['description'])!.value,
      fileTemplateContentType: this.editForm.get(['fileTemplateContentType'])!.value,
      fileTemplate: this.editForm.get(['fileTemplate'])!.value,
      fileType: this.editForm.get(['fileType'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileType>>): void {
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
