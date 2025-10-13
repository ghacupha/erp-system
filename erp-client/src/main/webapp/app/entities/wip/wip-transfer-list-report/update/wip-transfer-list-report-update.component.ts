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

import { IWIPTransferListReport, WIPTransferListReport } from '../wip-transfer-list-report.model';
import { WIPTransferListReportService } from '../service/wip-transfer-list-report.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';

@Component({
  selector: 'jhi-wip-transfer-list-report-update',
  templateUrl: './wip-transfer-list-report-update.component.html',
})
export class WIPTransferListReportUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    timeOfRequest: [null, [Validators.required]],
    requestId: [null, [Validators.required]],
    fileChecksum: [],
    tempered: [],
    filename: [null, [Validators.required]],
    reportParameters: [],
    reportFile: [],
    reportFileContentType: [],
    requestedBy: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected wIPTransferListReportService: WIPTransferListReportService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wIPTransferListReport }) => {
      if (wIPTransferListReport.id === undefined) {
        const today = dayjs().startOf('day');
        wIPTransferListReport.timeOfRequest = today;
      }

      this.updateForm(wIPTransferListReport);

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
    const wIPTransferListReport = this.createFromForm();
    if (wIPTransferListReport.id !== undefined) {
      this.subscribeToSaveResponse(this.wIPTransferListReportService.update(wIPTransferListReport));
    } else {
      this.subscribeToSaveResponse(this.wIPTransferListReportService.create(wIPTransferListReport));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWIPTransferListReport>>): void {
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

  protected updateForm(wIPTransferListReport: IWIPTransferListReport): void {
    this.editForm.patchValue({
      id: wIPTransferListReport.id,
      timeOfRequest: wIPTransferListReport.timeOfRequest ? wIPTransferListReport.timeOfRequest.format(DATE_TIME_FORMAT) : null,
      requestId: wIPTransferListReport.requestId,
      fileChecksum: wIPTransferListReport.fileChecksum,
      tempered: wIPTransferListReport.tempered,
      filename: wIPTransferListReport.filename,
      reportParameters: wIPTransferListReport.reportParameters,
      reportFile: wIPTransferListReport.reportFile,
      reportFileContentType: wIPTransferListReport.reportFileContentType,
      requestedBy: wIPTransferListReport.requestedBy,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      wIPTransferListReport.requestedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(applicationUsers, this.editForm.get('requestedBy')!.value)
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }

  protected createFromForm(): IWIPTransferListReport {
    return {
      ...new WIPTransferListReport(),
      id: this.editForm.get(['id'])!.value,
      timeOfRequest: this.editForm.get(['timeOfRequest'])!.value
        ? dayjs(this.editForm.get(['timeOfRequest'])!.value, DATE_TIME_FORMAT)
        : undefined,
      requestId: this.editForm.get(['requestId'])!.value,
      fileChecksum: this.editForm.get(['fileChecksum'])!.value,
      tempered: this.editForm.get(['tempered'])!.value,
      filename: this.editForm.get(['filename'])!.value,
      reportParameters: this.editForm.get(['reportParameters'])!.value,
      reportFileContentType: this.editForm.get(['reportFileContentType'])!.value,
      reportFile: this.editForm.get(['reportFile'])!.value,
      requestedBy: this.editForm.get(['requestedBy'])!.value,
    };
  }
}
