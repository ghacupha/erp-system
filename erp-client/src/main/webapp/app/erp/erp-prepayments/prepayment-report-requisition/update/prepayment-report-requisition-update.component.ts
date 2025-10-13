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

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPrepaymentReportRequisition, PrepaymentReportRequisition } from '../prepayment-report-requisition.model';
import { PrepaymentReportRequisitionService } from '../service/prepayment-report-requisition.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import {  uuidv7 } from 'uuidv7';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';

@Component({
  selector: 'jhi-prepayment-report-requisition-update',
  templateUrl: './prepayment-report-requisition-update.component.html',
})
export class PrepaymentReportRequisitionUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    reportName: [null, [Validators.required]],
    reportDate: [null, [Validators.required]],
    timeOfRequisition: [null, [Validators.required]],
    fileChecksum: [],
    tampered: [],
    filename: [],
    reportParameters: [],
    reportFile: [],
    reportFileContentType: [],
    requestedBy: [],
    lastAccessedBy: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected prepaymentReportRequisitionService: PrepaymentReportRequisitionService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentReportRequisition }) => {
      if (prepaymentReportRequisition.id === undefined) {
        const today = dayjs();
        prepaymentReportRequisition.timeOfRequisition = today;
        prepaymentReportRequisition.reportName = uuidv7();
      }

      this.updateForm(prepaymentReportRequisition);

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
    const prepaymentReportRequisition = this.createFromForm();
    if (prepaymentReportRequisition.id !== undefined) {
      this.subscribeToSaveResponse(this.prepaymentReportRequisitionService.update(prepaymentReportRequisition));
    } else {
      this.subscribeToSaveResponse(this.prepaymentReportRequisitionService.create(prepaymentReportRequisition));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrepaymentReportRequisition>>): void {
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

  protected updateForm(prepaymentReportRequisition: IPrepaymentReportRequisition): void {
    this.editForm.patchValue({
      id: prepaymentReportRequisition.id,
      reportName: prepaymentReportRequisition.reportName,
      reportDate: prepaymentReportRequisition.reportDate,
      timeOfRequisition: prepaymentReportRequisition.timeOfRequisition
        ? prepaymentReportRequisition.timeOfRequisition.format(DATE_TIME_FORMAT)
        : null,
      fileChecksum: prepaymentReportRequisition.fileChecksum,
      tampered: prepaymentReportRequisition.tampered,
      filename: prepaymentReportRequisition.filename,
      reportParameters: prepaymentReportRequisition.reportParameters,
      reportFile: prepaymentReportRequisition.reportFile,
      reportFileContentType: prepaymentReportRequisition.reportFileContentType,
      requestedBy: prepaymentReportRequisition.requestedBy,
      lastAccessedBy: prepaymentReportRequisition.lastAccessedBy,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      prepaymentReportRequisition.requestedBy,
      prepaymentReportRequisition.lastAccessedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(
            applicationUsers,
            this.editForm.get('requestedBy')!.value,
            this.editForm.get('lastAccessedBy')!.value
          )
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }

  protected createFromForm(): IPrepaymentReportRequisition {
    return {
      ...new PrepaymentReportRequisition(),
      id: this.editForm.get(['id'])!.value,
      reportName: this.editForm.get(['reportName'])!.value,
      reportDate: this.editForm.get(['reportDate'])!.value,
      timeOfRequisition: this.editForm.get(['timeOfRequisition'])!.value
        ? dayjs(this.editForm.get(['timeOfRequisition'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fileChecksum: this.editForm.get(['fileChecksum'])!.value,
      tampered: this.editForm.get(['tampered'])!.value,
      filename: this.editForm.get(['filename'])!.value,
      reportParameters: this.editForm.get(['reportParameters'])!.value,
      reportFileContentType: this.editForm.get(['reportFileContentType'])!.value,
      reportFile: this.editForm.get(['reportFile'])!.value,
      requestedBy: this.editForm.get(['requestedBy'])!.value,
      lastAccessedBy: this.editForm.get(['lastAccessedBy'])!.value,
    };
  }
}
