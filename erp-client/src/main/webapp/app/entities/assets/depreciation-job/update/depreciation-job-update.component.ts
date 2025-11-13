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
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDepreciationJob, DepreciationJob } from '../depreciation-job.model';
import { DepreciationJobService } from '../service/depreciation-job.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { DepreciationPeriodService } from 'app/entities/assets/depreciation-period/service/depreciation-period.service';
import { DepreciationJobStatusType } from 'app/entities/enumerations/depreciation-job-status-type.model';

@Component({
  selector: 'jhi-depreciation-job-update',
  templateUrl: './depreciation-job-update.component.html',
})
export class DepreciationJobUpdateComponent implements OnInit {
  isSaving = false;
  depreciationJobStatusTypeValues = Object.keys(DepreciationJobStatusType);

  applicationUsersSharedCollection: IApplicationUser[] = [];
  depreciationPeriodsCollection: IDepreciationPeriod[] = [];

  editForm = this.fb.group({
    id: [],
    timeOfCommencement: [],
    depreciationJobStatus: [],
    description: [],
    numberOfBatches: [],
    processedBatches: [],
    lastBatchSize: [],
    processedItems: [],
    processingTime: [],
    totalItems: [],
    createdBy: [],
    depreciationPeriod: [],
  });

  constructor(
    protected depreciationJobService: DepreciationJobService,
    protected applicationUserService: ApplicationUserService,
    protected depreciationPeriodService: DepreciationPeriodService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depreciationJob }) => {
      if (depreciationJob.id === undefined) {
        const today = dayjs().startOf('day');
        depreciationJob.timeOfCommencement = today;
      }

      this.updateForm(depreciationJob);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const depreciationJob = this.createFromForm();
    if (depreciationJob.id !== undefined) {
      this.subscribeToSaveResponse(this.depreciationJobService.update(depreciationJob));
    } else {
      this.subscribeToSaveResponse(this.depreciationJobService.create(depreciationJob));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  trackDepreciationPeriodById(index: number, item: IDepreciationPeriod): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepreciationJob>>): void {
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

  protected updateForm(depreciationJob: IDepreciationJob): void {
    this.editForm.patchValue({
      id: depreciationJob.id,
      timeOfCommencement: depreciationJob.timeOfCommencement ? depreciationJob.timeOfCommencement.format(DATE_TIME_FORMAT) : null,
      depreciationJobStatus: depreciationJob.depreciationJobStatus,
      description: depreciationJob.description,
      numberOfBatches: depreciationJob.numberOfBatches,
      processedBatches: depreciationJob.processedBatches,
      lastBatchSize: depreciationJob.lastBatchSize,
      processedItems: depreciationJob.processedItems,
      processingTime: depreciationJob.processingTime,
      totalItems: depreciationJob.totalItems,
      createdBy: depreciationJob.createdBy,
      depreciationPeriod: depreciationJob.depreciationPeriod,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      depreciationJob.createdBy
    );
    this.depreciationPeriodsCollection = this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
      this.depreciationPeriodsCollection,
      depreciationJob.depreciationPeriod
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(applicationUsers, this.editForm.get('createdBy')!.value)
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.depreciationPeriodService
      .query({ 'depreciationJobId.specified': 'false' })
      .pipe(map((res: HttpResponse<IDepreciationPeriod[]>) => res.body ?? []))
      .pipe(
        map((depreciationPeriods: IDepreciationPeriod[]) =>
          this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
            depreciationPeriods,
            this.editForm.get('depreciationPeriod')!.value
          )
        )
      )
      .subscribe((depreciationPeriods: IDepreciationPeriod[]) => (this.depreciationPeriodsCollection = depreciationPeriods));
  }

  protected createFromForm(): IDepreciationJob {
    return {
      ...new DepreciationJob(),
      id: this.editForm.get(['id'])!.value,
      timeOfCommencement: this.editForm.get(['timeOfCommencement'])!.value
        ? dayjs(this.editForm.get(['timeOfCommencement'])!.value, DATE_TIME_FORMAT)
        : undefined,
      depreciationJobStatus: this.editForm.get(['depreciationJobStatus'])!.value,
      description: this.editForm.get(['description'])!.value,
      numberOfBatches: this.editForm.get(['numberOfBatches'])!.value,
      processedBatches: this.editForm.get(['processedBatches'])!.value,
      lastBatchSize: this.editForm.get(['lastBatchSize'])!.value,
      processedItems: this.editForm.get(['processedItems'])!.value,
      processingTime: this.editForm.get(['processingTime'])!.value,
      totalItems: this.editForm.get(['totalItems'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      depreciationPeriod: this.editForm.get(['depreciationPeriod'])!.value,
    };
  }
}
