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

import { IRouMonthlyDepreciationReport, RouMonthlyDepreciationReport } from '../rou-monthly-depreciation-report.model';
import { RouMonthlyDepreciationReportService } from '../service/rou-monthly-depreciation-report.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { IFiscalYear } from 'app/entities/system/fiscal-year/fiscal-year.model';
import { FiscalYearService } from 'app/entities/system/fiscal-year/service/fiscal-year.service';

@Component({
  selector: 'jhi-rou-monthly-depreciation-report-update',
  templateUrl: './rou-monthly-depreciation-report-update.component.html',
})
export class RouMonthlyDepreciationReportUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUser[] = [];
  fiscalYearsSharedCollection: IFiscalYear[] = [];

  editForm = this.fb.group({
    id: [],
    requestId: [null, [Validators.required]],
    timeOfRequest: [null, [Validators.required]],
    reportIsCompiled: [],
    fileChecksum: [],
    tampered: [],
    filename: [null, []],
    reportParameters: [],
    reportFile: [],
    reportFileContentType: [],
    requestedBy: [],
    reportingYear: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected rouMonthlyDepreciationReportService: RouMonthlyDepreciationReportService,
    protected applicationUserService: ApplicationUserService,
    protected fiscalYearService: FiscalYearService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouMonthlyDepreciationReport }) => {
      if (rouMonthlyDepreciationReport.id === undefined) {
        const today = dayjs().startOf('day');
        rouMonthlyDepreciationReport.timeOfRequest = today;
      }

      this.updateForm(rouMonthlyDepreciationReport);

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
    const rouMonthlyDepreciationReport = this.createFromForm();
    if (rouMonthlyDepreciationReport.id !== undefined) {
      this.subscribeToSaveResponse(this.rouMonthlyDepreciationReportService.update(rouMonthlyDepreciationReport));
    } else {
      this.subscribeToSaveResponse(this.rouMonthlyDepreciationReportService.create(rouMonthlyDepreciationReport));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  trackFiscalYearById(index: number, item: IFiscalYear): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRouMonthlyDepreciationReport>>): void {
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

  protected updateForm(rouMonthlyDepreciationReport: IRouMonthlyDepreciationReport): void {
    this.editForm.patchValue({
      id: rouMonthlyDepreciationReport.id,
      requestId: rouMonthlyDepreciationReport.requestId,
      timeOfRequest: rouMonthlyDepreciationReport.timeOfRequest
        ? rouMonthlyDepreciationReport.timeOfRequest.format(DATE_TIME_FORMAT)
        : null,
      reportIsCompiled: rouMonthlyDepreciationReport.reportIsCompiled,
      fileChecksum: rouMonthlyDepreciationReport.fileChecksum,
      tampered: rouMonthlyDepreciationReport.tampered,
      filename: rouMonthlyDepreciationReport.filename,
      reportParameters: rouMonthlyDepreciationReport.reportParameters,
      reportFile: rouMonthlyDepreciationReport.reportFile,
      reportFileContentType: rouMonthlyDepreciationReport.reportFileContentType,
      requestedBy: rouMonthlyDepreciationReport.requestedBy,
      reportingYear: rouMonthlyDepreciationReport.reportingYear,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      rouMonthlyDepreciationReport.requestedBy
    );
    this.fiscalYearsSharedCollection = this.fiscalYearService.addFiscalYearToCollectionIfMissing(
      this.fiscalYearsSharedCollection,
      rouMonthlyDepreciationReport.reportingYear
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

    this.fiscalYearService
      .query()
      .pipe(map((res: HttpResponse<IFiscalYear[]>) => res.body ?? []))
      .pipe(
        map((fiscalYears: IFiscalYear[]) =>
          this.fiscalYearService.addFiscalYearToCollectionIfMissing(fiscalYears, this.editForm.get('reportingYear')!.value)
        )
      )
      .subscribe((fiscalYears: IFiscalYear[]) => (this.fiscalYearsSharedCollection = fiscalYears));
  }

  protected createFromForm(): IRouMonthlyDepreciationReport {
    return {
      ...new RouMonthlyDepreciationReport(),
      id: this.editForm.get(['id'])!.value,
      requestId: this.editForm.get(['requestId'])!.value,
      timeOfRequest: this.editForm.get(['timeOfRequest'])!.value
        ? dayjs(this.editForm.get(['timeOfRequest'])!.value, DATE_TIME_FORMAT)
        : undefined,
      reportIsCompiled: this.editForm.get(['reportIsCompiled'])!.value,
      fileChecksum: this.editForm.get(['fileChecksum'])!.value,
      tampered: this.editForm.get(['tampered'])!.value,
      filename: this.editForm.get(['filename'])!.value,
      reportParameters: this.editForm.get(['reportParameters'])!.value,
      reportFileContentType: this.editForm.get(['reportFileContentType'])!.value,
      reportFile: this.editForm.get(['reportFile'])!.value,
      requestedBy: this.editForm.get(['requestedBy'])!.value,
      reportingYear: this.editForm.get(['reportingYear'])!.value,
    };
  }
}
