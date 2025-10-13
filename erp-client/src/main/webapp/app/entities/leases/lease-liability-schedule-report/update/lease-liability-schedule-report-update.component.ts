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

import { ILeaseLiabilityScheduleReport, LeaseLiabilityScheduleReport } from '../lease-liability-schedule-report.model';
import { LeaseLiabilityScheduleReportService } from '../service/lease-liability-schedule-report.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { ILeaseAmortizationSchedule } from 'app/entities/leases/lease-amortization-schedule/lease-amortization-schedule.model';
import { LeaseAmortizationScheduleService } from 'app/entities/leases/lease-amortization-schedule/service/lease-amortization-schedule.service';

@Component({
  selector: 'jhi-lease-liability-schedule-report-update',
  templateUrl: './lease-liability-schedule-report-update.component.html',
})
export class LeaseLiabilityScheduleReportUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUser[] = [];
  leaseAmortizationSchedulesSharedCollection: ILeaseAmortizationSchedule[] = [];

  editForm = this.fb.group({
    id: [],
    requestId: [null, [Validators.required]],
    timeOfRequest: [null, [Validators.required]],
    fileChecksum: [],
    tampered: [],
    filename: [],
    reportParameters: [],
    reportFile: [],
    reportFileContentType: [],
    requestedBy: [],
    leaseAmortizationSchedule: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected leaseLiabilityScheduleReportService: LeaseLiabilityScheduleReportService,
    protected applicationUserService: ApplicationUserService,
    protected leaseAmortizationScheduleService: LeaseAmortizationScheduleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseLiabilityScheduleReport }) => {
      if (leaseLiabilityScheduleReport.id === undefined) {
        const today = dayjs().startOf('day');
        leaseLiabilityScheduleReport.timeOfRequest = today;
      }

      this.updateForm(leaseLiabilityScheduleReport);

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
    const leaseLiabilityScheduleReport = this.createFromForm();
    if (leaseLiabilityScheduleReport.id !== undefined) {
      this.subscribeToSaveResponse(this.leaseLiabilityScheduleReportService.update(leaseLiabilityScheduleReport));
    } else {
      this.subscribeToSaveResponse(this.leaseLiabilityScheduleReportService.create(leaseLiabilityScheduleReport));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  trackLeaseAmortizationScheduleById(index: number, item: ILeaseAmortizationSchedule): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaseLiabilityScheduleReport>>): void {
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

  protected updateForm(leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport): void {
    this.editForm.patchValue({
      id: leaseLiabilityScheduleReport.id,
      requestId: leaseLiabilityScheduleReport.requestId,
      timeOfRequest: leaseLiabilityScheduleReport.timeOfRequest
        ? leaseLiabilityScheduleReport.timeOfRequest.format(DATE_TIME_FORMAT)
        : null,
      fileChecksum: leaseLiabilityScheduleReport.fileChecksum,
      tampered: leaseLiabilityScheduleReport.tampered,
      filename: leaseLiabilityScheduleReport.filename,
      reportParameters: leaseLiabilityScheduleReport.reportParameters,
      reportFile: leaseLiabilityScheduleReport.reportFile,
      reportFileContentType: leaseLiabilityScheduleReport.reportFileContentType,
      requestedBy: leaseLiabilityScheduleReport.requestedBy,
      leaseAmortizationSchedule: leaseLiabilityScheduleReport.leaseAmortizationSchedule,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      leaseLiabilityScheduleReport.requestedBy
    );
    this.leaseAmortizationSchedulesSharedCollection =
      this.leaseAmortizationScheduleService.addLeaseAmortizationScheduleToCollectionIfMissing(
        this.leaseAmortizationSchedulesSharedCollection,
        leaseLiabilityScheduleReport.leaseAmortizationSchedule
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

    this.leaseAmortizationScheduleService
      .query()
      .pipe(map((res: HttpResponse<ILeaseAmortizationSchedule[]>) => res.body ?? []))
      .pipe(
        map((leaseAmortizationSchedules: ILeaseAmortizationSchedule[]) =>
          this.leaseAmortizationScheduleService.addLeaseAmortizationScheduleToCollectionIfMissing(
            leaseAmortizationSchedules,
            this.editForm.get('leaseAmortizationSchedule')!.value
          )
        )
      )
      .subscribe(
        (leaseAmortizationSchedules: ILeaseAmortizationSchedule[]) =>
          (this.leaseAmortizationSchedulesSharedCollection = leaseAmortizationSchedules)
      );
  }

  protected createFromForm(): ILeaseLiabilityScheduleReport {
    return {
      ...new LeaseLiabilityScheduleReport(),
      id: this.editForm.get(['id'])!.value,
      requestId: this.editForm.get(['requestId'])!.value,
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
      leaseAmortizationSchedule: this.editForm.get(['leaseAmortizationSchedule'])!.value,
    };
  }
}
