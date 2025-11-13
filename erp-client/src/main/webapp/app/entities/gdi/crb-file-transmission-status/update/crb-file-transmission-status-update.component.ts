///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
import { finalize } from 'rxjs/operators';

import { ICrbFileTransmissionStatus, CrbFileTransmissionStatus } from '../crb-file-transmission-status.model';
import { CrbFileTransmissionStatusService } from '../service/crb-file-transmission-status.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { SubmittedFileStatusTypes } from 'app/entities/enumerations/submitted-file-status-types.model';

@Component({
  selector: 'jhi-crb-file-transmission-status-update',
  templateUrl: './crb-file-transmission-status-update.component.html',
})
export class CrbFileTransmissionStatusUpdateComponent implements OnInit {
  isSaving = false;
  submittedFileStatusTypesValues = Object.keys(SubmittedFileStatusTypes);

  editForm = this.fb.group({
    id: [],
    submittedFileStatusTypeCode: [null, [Validators.required]],
    submittedFileStatusType: [null, [Validators.required]],
    submittedFileStatusTypeDescription: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected crbFileTransmissionStatusService: CrbFileTransmissionStatusService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crbFileTransmissionStatus }) => {
      this.updateForm(crbFileTransmissionStatus);
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
    const crbFileTransmissionStatus = this.createFromForm();
    if (crbFileTransmissionStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.crbFileTransmissionStatusService.update(crbFileTransmissionStatus));
    } else {
      this.subscribeToSaveResponse(this.crbFileTransmissionStatusService.create(crbFileTransmissionStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICrbFileTransmissionStatus>>): void {
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

  protected updateForm(crbFileTransmissionStatus: ICrbFileTransmissionStatus): void {
    this.editForm.patchValue({
      id: crbFileTransmissionStatus.id,
      submittedFileStatusTypeCode: crbFileTransmissionStatus.submittedFileStatusTypeCode,
      submittedFileStatusType: crbFileTransmissionStatus.submittedFileStatusType,
      submittedFileStatusTypeDescription: crbFileTransmissionStatus.submittedFileStatusTypeDescription,
    });
  }

  protected createFromForm(): ICrbFileTransmissionStatus {
    return {
      ...new CrbFileTransmissionStatus(),
      id: this.editForm.get(['id'])!.value,
      submittedFileStatusTypeCode: this.editForm.get(['submittedFileStatusTypeCode'])!.value,
      submittedFileStatusType: this.editForm.get(['submittedFileStatusType'])!.value,
      submittedFileStatusTypeDescription: this.editForm.get(['submittedFileStatusTypeDescription'])!.value,
    };
  }
}
