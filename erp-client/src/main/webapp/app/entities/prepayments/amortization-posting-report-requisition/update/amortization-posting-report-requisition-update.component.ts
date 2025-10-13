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

import {
  IAmortizationPostingReportRequisition,
  AmortizationPostingReportRequisition,
} from '../amortization-posting-report-requisition.model';
import { AmortizationPostingReportRequisitionService } from '../service/amortization-posting-report-requisition.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAmortizationPeriod } from 'app/entities/prepayments/amortization-period/amortization-period.model';
import { AmortizationPeriodService } from 'app/entities/prepayments/amortization-period/service/amortization-period.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';

@Component({
  selector: 'jhi-amortization-posting-report-requisition-update',
  templateUrl: './amortization-posting-report-requisition-update.component.html',
})
export class AmortizationPostingReportRequisitionUpdateComponent implements OnInit {
  isSaving = false;

  amortizationPeriodsSharedCollection: IAmortizationPeriod[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    requestId: [null, [Validators.required]],
    timeOfRequisition: [null, [Validators.required]],
    fileChecksum: [],
    tampered: [],
    filename: [null, []],
    reportParameters: [],
    reportFile: [],
    reportFileContentType: [],
    amortizationPeriod: [null, Validators.required],
    requestedBy: [],
    lastAccessedBy: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected amortizationPostingReportRequisitionService: AmortizationPostingReportRequisitionService,
    protected amortizationPeriodService: AmortizationPeriodService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amortizationPostingReportRequisition }) => {
      if (amortizationPostingReportRequisition.id === undefined) {
        const today = dayjs().startOf('day');
        amortizationPostingReportRequisition.timeOfRequisition = today;
      }

      this.updateForm(amortizationPostingReportRequisition);

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
    const amortizationPostingReportRequisition = this.createFromForm();
    if (amortizationPostingReportRequisition.id !== undefined) {
      this.subscribeToSaveResponse(this.amortizationPostingReportRequisitionService.update(amortizationPostingReportRequisition));
    } else {
      this.subscribeToSaveResponse(this.amortizationPostingReportRequisitionService.create(amortizationPostingReportRequisition));
    }
  }

  trackAmortizationPeriodById(index: number, item: IAmortizationPeriod): number {
    return item.id!;
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAmortizationPostingReportRequisition>>): void {
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

  protected updateForm(amortizationPostingReportRequisition: IAmortizationPostingReportRequisition): void {
    this.editForm.patchValue({
      id: amortizationPostingReportRequisition.id,
      requestId: amortizationPostingReportRequisition.requestId,
      timeOfRequisition: amortizationPostingReportRequisition.timeOfRequisition
        ? amortizationPostingReportRequisition.timeOfRequisition.format(DATE_TIME_FORMAT)
        : null,
      fileChecksum: amortizationPostingReportRequisition.fileChecksum,
      tampered: amortizationPostingReportRequisition.tampered,
      filename: amortizationPostingReportRequisition.filename,
      reportParameters: amortizationPostingReportRequisition.reportParameters,
      reportFile: amortizationPostingReportRequisition.reportFile,
      reportFileContentType: amortizationPostingReportRequisition.reportFileContentType,
      amortizationPeriod: amortizationPostingReportRequisition.amortizationPeriod,
      requestedBy: amortizationPostingReportRequisition.requestedBy,
      lastAccessedBy: amortizationPostingReportRequisition.lastAccessedBy,
    });

    this.amortizationPeriodsSharedCollection = this.amortizationPeriodService.addAmortizationPeriodToCollectionIfMissing(
      this.amortizationPeriodsSharedCollection,
      amortizationPostingReportRequisition.amortizationPeriod
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      amortizationPostingReportRequisition.requestedBy,
      amortizationPostingReportRequisition.lastAccessedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.amortizationPeriodService
      .query()
      .pipe(map((res: HttpResponse<IAmortizationPeriod[]>) => res.body ?? []))
      .pipe(
        map((amortizationPeriods: IAmortizationPeriod[]) =>
          this.amortizationPeriodService.addAmortizationPeriodToCollectionIfMissing(
            amortizationPeriods,
            this.editForm.get('amortizationPeriod')!.value
          )
        )
      )
      .subscribe((amortizationPeriods: IAmortizationPeriod[]) => (this.amortizationPeriodsSharedCollection = amortizationPeriods));

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

  protected createFromForm(): IAmortizationPostingReportRequisition {
    return {
      ...new AmortizationPostingReportRequisition(),
      id: this.editForm.get(['id'])!.value,
      requestId: this.editForm.get(['requestId'])!.value,
      timeOfRequisition: this.editForm.get(['timeOfRequisition'])!.value
        ? dayjs(this.editForm.get(['timeOfRequisition'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fileChecksum: this.editForm.get(['fileChecksum'])!.value,
      tampered: this.editForm.get(['tampered'])!.value,
      filename: this.editForm.get(['filename'])!.value,
      reportParameters: this.editForm.get(['reportParameters'])!.value,
      reportFileContentType: this.editForm.get(['reportFileContentType'])!.value,
      reportFile: this.editForm.get(['reportFile'])!.value,
      amortizationPeriod: this.editForm.get(['amortizationPeriod'])!.value,
      requestedBy: this.editForm.get(['requestedBy'])!.value,
      lastAccessedBy: this.editForm.get(['lastAccessedBy'])!.value,
    };
  }
}
