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
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import {
  IPrepaymentByAccountReportRequisition,
  PrepaymentByAccountReportRequisition,
} from '../prepayment-by-account-report-requisition.model';
import { PrepaymentByAccountReportRequisitionService } from '../service/prepayment-by-account-report-requisition.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { uuidv7 } from 'uuidv7';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';

@Component({
  selector: 'jhi-prepayment-by-account-report-requisition-update',
  templateUrl: './prepayment-by-account-report-requisition-update.component.html',
})
export class PrepaymentByAccountReportRequisitionUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    requestId: [],
    timeOfRequisition: [null, [Validators.required]],
    fileChecksum: [],
    filename: [],
    reportParameters: [],
    reportFile: [],
    reportFileContentType: [],
    reportDate: [null, [Validators.required]],
    tampered: [],
    requestedBy: [],
    lastAccessedBy: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected prepaymentByAccountReportRequisitionService: PrepaymentByAccountReportRequisitionService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentByAccountReportRequisition }) => {
      if (prepaymentByAccountReportRequisition.id === undefined) {
        const today = dayjs();
        prepaymentByAccountReportRequisition.timeOfRequisition = today;
        prepaymentByAccountReportRequisition.requestId = uuidv7();
      }

      this.updateForm(prepaymentByAccountReportRequisition);

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
    const prepaymentByAccountReportRequisition = this.createFromForm();
    if (prepaymentByAccountReportRequisition.id !== undefined) {
      this.subscribeToSaveResponse(this.prepaymentByAccountReportRequisitionService.update(prepaymentByAccountReportRequisition));
    } else {
      this.subscribeToSaveResponse(this.prepaymentByAccountReportRequisitionService.create(prepaymentByAccountReportRequisition));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrepaymentByAccountReportRequisition>>): void {
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

  protected updateForm(prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition): void {
    this.editForm.patchValue({
      id: prepaymentByAccountReportRequisition.id,
      requestId: prepaymentByAccountReportRequisition.requestId,
      timeOfRequisition: prepaymentByAccountReportRequisition.timeOfRequisition
        ? prepaymentByAccountReportRequisition.timeOfRequisition.format(DATE_TIME_FORMAT)
        : null,
      fileChecksum: prepaymentByAccountReportRequisition.fileChecksum,
      filename: prepaymentByAccountReportRequisition.filename,
      reportParameters: prepaymentByAccountReportRequisition.reportParameters,
      reportFile: prepaymentByAccountReportRequisition.reportFile,
      reportFileContentType: prepaymentByAccountReportRequisition.reportFileContentType,
      reportDate: prepaymentByAccountReportRequisition.reportDate,
      tampered: prepaymentByAccountReportRequisition.tampered,
      requestedBy: prepaymentByAccountReportRequisition.requestedBy,
      lastAccessedBy: prepaymentByAccountReportRequisition.lastAccessedBy,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      prepaymentByAccountReportRequisition.requestedBy,
      prepaymentByAccountReportRequisition.lastAccessedBy
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

  protected createFromForm(): IPrepaymentByAccountReportRequisition {
    return {
      ...new PrepaymentByAccountReportRequisition(),
      id: this.editForm.get(['id'])!.value,
      requestId: this.editForm.get(['requestId'])!.value,
      timeOfRequisition: this.editForm.get(['timeOfRequisition'])!.value
        ? dayjs(this.editForm.get(['timeOfRequisition'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fileChecksum: this.editForm.get(['fileChecksum'])!.value,
      filename: this.editForm.get(['filename'])!.value,
      reportParameters: this.editForm.get(['reportParameters'])!.value,
      reportFileContentType: this.editForm.get(['reportFileContentType'])!.value,
      reportFile: this.editForm.get(['reportFile'])!.value,
      reportDate: this.editForm.get(['reportDate'])!.value,
      tampered: this.editForm.get(['tampered'])!.value,
      requestedBy: this.editForm.get(['requestedBy'])!.value,
      lastAccessedBy: this.editForm.get(['lastAccessedBy'])!.value,
    };
  }
}
