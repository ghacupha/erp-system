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

import { ILeaseLiabilityByAccountReport, LeaseLiabilityByAccountReport } from '../lease-liability-by-account-report.model';
import { LeaseLiabilityByAccountReportService } from '../service/lease-liability-by-account-report.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ILeasePeriod } from '../../lease-period/lease-period.model';
import { LeasePeriodService } from '../../lease-period/service/lease-period.service';

@Component({
  selector: 'jhi-lease-liability-by-account-report-update',
  templateUrl: './lease-liability-by-account-report-update.component.html',
})
export class LeaseLiabilityByAccountReportUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUser[] = [];
  leasePeriodsSharedCollection: ILeasePeriod[] = [];

  editForm = this.fb.group({
    id: [],
    reportId: [null, [Validators.required]],
    timeOfRequest: [],
    fileChecksum: [],
    tampered: [],
    filename: [],
    reportParameters: [],
    reportFile: [],
    reportFileContentType: [],
    requestedBy: [],
    leasePeriod: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected leaseLiabilityByAccountReportService: LeaseLiabilityByAccountReportService,
    protected applicationUserService: ApplicationUserService,
    protected leasePeriodService: LeasePeriodService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseLiabilityByAccountReport }) => {
      if (leaseLiabilityByAccountReport.id === undefined) {
        const today = dayjs().startOf('day');
        leaseLiabilityByAccountReport.timeOfRequest = today;
      }

      this.updateForm(leaseLiabilityByAccountReport);

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
    const leaseLiabilityByAccountReport = this.createFromForm();
    if (leaseLiabilityByAccountReport.id !== undefined) {
      this.subscribeToSaveResponse(this.leaseLiabilityByAccountReportService.update(leaseLiabilityByAccountReport));
    } else {
      this.subscribeToSaveResponse(this.leaseLiabilityByAccountReportService.create(leaseLiabilityByAccountReport));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  trackLeasePeriodById(index: number, item: ILeasePeriod): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaseLiabilityByAccountReport>>): void {
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

  protected updateForm(leaseLiabilityByAccountReport: ILeaseLiabilityByAccountReport): void {
    this.editForm.patchValue({
      id: leaseLiabilityByAccountReport.id,
      reportId: leaseLiabilityByAccountReport.reportId,
      timeOfRequest: leaseLiabilityByAccountReport.timeOfRequest
        ? leaseLiabilityByAccountReport.timeOfRequest.format(DATE_TIME_FORMAT)
        : null,
      fileChecksum: leaseLiabilityByAccountReport.fileChecksum,
      tampered: leaseLiabilityByAccountReport.tampered,
      filename: leaseLiabilityByAccountReport.filename,
      reportParameters: leaseLiabilityByAccountReport.reportParameters,
      reportFile: leaseLiabilityByAccountReport.reportFile,
      reportFileContentType: leaseLiabilityByAccountReport.reportFileContentType,
      requestedBy: leaseLiabilityByAccountReport.requestedBy,
      leasePeriod: leaseLiabilityByAccountReport.leasePeriod,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      leaseLiabilityByAccountReport.requestedBy
    );
    this.leasePeriodsSharedCollection = this.leasePeriodService.addLeasePeriodToCollectionIfMissing(
      this.leasePeriodsSharedCollection,
      leaseLiabilityByAccountReport.leasePeriod
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

    this.leasePeriodService
      .query()
      .pipe(map((res: HttpResponse<ILeasePeriod[]>) => res.body ?? []))
      .pipe(
        map((leasePeriods: ILeasePeriod[]) =>
          this.leasePeriodService.addLeasePeriodToCollectionIfMissing(leasePeriods, this.editForm.get('leasePeriod')!.value)
        )
      )
      .subscribe((leasePeriods: ILeasePeriod[]) => (this.leasePeriodsSharedCollection = leasePeriods));
  }

  protected createFromForm(): ILeaseLiabilityByAccountReport {
    return {
      ...new LeaseLiabilityByAccountReport(),
      id: this.editForm.get(['id'])!.value,
      reportId: this.editForm.get(['reportId'])!.value,
      timeOfRequest: this.editForm.get(['timeOfRequest'])!.value
        ? dayjs(this.editForm.get(['timeOfRequest'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fileChecksum: this.editForm.get(['fileChecksum'])!.value,
      tampered: this.editForm.get(['tampered'])!.value,
      filename: this.editForm.get(['filename'])!.value,
      reportParameters: this.editForm.get(['reportParameters'])!.value,
      reportFileContentType: this.editForm.get(['reportFileContentType'])!.value,
      reportFile: this.editForm.get(['reportFile'])!.value,
      requestedBy: this.editForm.get(['requestedBy'])!.value,
      leasePeriod: this.editForm.get(['leasePeriod'])!.value,
    };
  }
}
