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

import { IRouDepreciationEntryReport, RouDepreciationEntryReport } from '../rou-depreciation-entry-report.model';
import { RouDepreciationEntryReportService } from '../service/rou-depreciation-entry-report.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { uuidv7 } from 'uuidv7';

@Component({
  selector: 'jhi-rou-depreciation-entry-report-update',
  templateUrl: './rou-depreciation-entry-report-update.component.html',
})
export class RouDepreciationEntryReportUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    requestId: [null, [Validators.required]],
    timeOfRequest: [],
    reportIsCompiled: [],
    periodStartDate: [],
    periodEndDate: [],
    fileChecksum: [],
    tampered: [],
    filename: [null, []],
    reportParameters: [],
    reportFile: [],
    reportFileContentType: [],
    requestedBy: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected rouDepreciationEntryReportService: RouDepreciationEntryReportService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouDepreciationEntryReport }) => {
      if (rouDepreciationEntryReport.id === undefined) {
        const today = dayjs();
        rouDepreciationEntryReport.timeOfRequest = today;

        rouDepreciationEntryReport.requestId = uuidv7();

        rouDepreciationEntryReport.filename = uuidv7();
      }

      this.updateForm(rouDepreciationEntryReport);

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
    const rouDepreciationEntryReport = this.createFromForm();
    if (rouDepreciationEntryReport.id !== undefined) {
      this.subscribeToSaveResponse(this.rouDepreciationEntryReportService.update(rouDepreciationEntryReport));
    } else {
      this.subscribeToSaveResponse(this.rouDepreciationEntryReportService.create(rouDepreciationEntryReport));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRouDepreciationEntryReport>>): void {
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

  protected updateForm(rouDepreciationEntryReport: IRouDepreciationEntryReport): void {
    this.editForm.patchValue({
      id: rouDepreciationEntryReport.id,
      requestId: rouDepreciationEntryReport.requestId,
      timeOfRequest: rouDepreciationEntryReport.timeOfRequest ? rouDepreciationEntryReport.timeOfRequest.format(DATE_TIME_FORMAT) : null,
      reportIsCompiled: rouDepreciationEntryReport.reportIsCompiled,
      periodStartDate: rouDepreciationEntryReport.periodStartDate,
      periodEndDate: rouDepreciationEntryReport.periodEndDate,
      fileChecksum: rouDepreciationEntryReport.fileChecksum,
      tampered: rouDepreciationEntryReport.tampered,
      filename: rouDepreciationEntryReport.filename,
      reportParameters: rouDepreciationEntryReport.reportParameters,
      reportFile: rouDepreciationEntryReport.reportFile,
      reportFileContentType: rouDepreciationEntryReport.reportFileContentType,
      requestedBy: rouDepreciationEntryReport.requestedBy,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      rouDepreciationEntryReport.requestedBy
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

  protected createFromForm(): IRouDepreciationEntryReport {
    return {
      ...new RouDepreciationEntryReport(),
      id: this.editForm.get(['id'])!.value,
      requestId: this.editForm.get(['requestId'])!.value,
      timeOfRequest: this.editForm.get(['timeOfRequest'])!.value
        ? dayjs(this.editForm.get(['timeOfRequest'])!.value, DATE_TIME_FORMAT)
        : undefined,
      reportIsCompiled: this.editForm.get(['reportIsCompiled'])!.value,
      periodStartDate: this.editForm.get(['periodStartDate'])!.value,
      periodEndDate: this.editForm.get(['periodEndDate'])!.value,
      fileChecksum: this.editForm.get(['fileChecksum'])!.value,
      tampered: this.editForm.get(['tampered'])!.value,
      filename: this.editForm.get(['filename'])!.value,
      reportParameters: this.editForm.get(['reportParameters'])!.value,
      reportFileContentType: this.editForm.get(['reportFileContentType'])!.value,
      reportFile: this.editForm.get(['reportFile'])!.value,
      requestedBy: this.editForm.get(['requestedBy'])!.value,
    };
  }
}
