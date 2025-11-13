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

import { INbvCompilationJob, NbvCompilationJob } from '../nbv-compilation-job.model';
import { NbvCompilationJobService } from '../service/nbv-compilation-job.service';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { DepreciationPeriodService } from 'app/entities/assets/depreciation-period/service/depreciation-period.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { NVBCompilationStatus } from 'app/entities/enumerations/nvb-compilation-status.model';

@Component({
  selector: 'jhi-nbv-compilation-job-update',
  templateUrl: './nbv-compilation-job-update.component.html',
})
export class NbvCompilationJobUpdateComponent implements OnInit {
  isSaving = false;
  nVBCompilationStatusValues = Object.keys(NVBCompilationStatus);

  depreciationPeriodsSharedCollection: IDepreciationPeriod[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    compilationJobIdentifier: [null, [Validators.required]],
    compilationJobTimeOfRequest: [],
    entitiesAffected: [],
    compilationStatus: [],
    jobTitle: [null, [Validators.required]],
    numberOfBatches: [],
    numberOfProcessedBatches: [],
    processingTime: [],
    activePeriod: [],
    initiatedBy: [],
  });

  constructor(
    protected nbvCompilationJobService: NbvCompilationJobService,
    protected depreciationPeriodService: DepreciationPeriodService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nbvCompilationJob }) => {
      if (nbvCompilationJob.id === undefined) {
        const today = dayjs().startOf('day');
        nbvCompilationJob.compilationJobTimeOfRequest = today;
      }

      this.updateForm(nbvCompilationJob);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nbvCompilationJob = this.createFromForm();
    if (nbvCompilationJob.id !== undefined) {
      this.subscribeToSaveResponse(this.nbvCompilationJobService.update(nbvCompilationJob));
    } else {
      this.subscribeToSaveResponse(this.nbvCompilationJobService.create(nbvCompilationJob));
    }
  }

  trackDepreciationPeriodById(index: number, item: IDepreciationPeriod): number {
    return item.id!;
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INbvCompilationJob>>): void {
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

  protected updateForm(nbvCompilationJob: INbvCompilationJob): void {
    this.editForm.patchValue({
      id: nbvCompilationJob.id,
      compilationJobIdentifier: nbvCompilationJob.compilationJobIdentifier,
      compilationJobTimeOfRequest: nbvCompilationJob.compilationJobTimeOfRequest
        ? nbvCompilationJob.compilationJobTimeOfRequest.format(DATE_TIME_FORMAT)
        : null,
      entitiesAffected: nbvCompilationJob.entitiesAffected,
      compilationStatus: nbvCompilationJob.compilationStatus,
      jobTitle: nbvCompilationJob.jobTitle,
      numberOfBatches: nbvCompilationJob.numberOfBatches,
      numberOfProcessedBatches: nbvCompilationJob.numberOfProcessedBatches,
      processingTime: nbvCompilationJob.processingTime,
      activePeriod: nbvCompilationJob.activePeriod,
      initiatedBy: nbvCompilationJob.initiatedBy,
    });

    this.depreciationPeriodsSharedCollection = this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
      this.depreciationPeriodsSharedCollection,
      nbvCompilationJob.activePeriod
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      nbvCompilationJob.initiatedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.depreciationPeriodService
      .query()
      .pipe(map((res: HttpResponse<IDepreciationPeriod[]>) => res.body ?? []))
      .pipe(
        map((depreciationPeriods: IDepreciationPeriod[]) =>
          this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
            depreciationPeriods,
            this.editForm.get('activePeriod')!.value
          )
        )
      )
      .subscribe((depreciationPeriods: IDepreciationPeriod[]) => (this.depreciationPeriodsSharedCollection = depreciationPeriods));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(applicationUsers, this.editForm.get('initiatedBy')!.value)
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }

  protected createFromForm(): INbvCompilationJob {
    return {
      ...new NbvCompilationJob(),
      id: this.editForm.get(['id'])!.value,
      compilationJobIdentifier: this.editForm.get(['compilationJobIdentifier'])!.value,
      compilationJobTimeOfRequest: this.editForm.get(['compilationJobTimeOfRequest'])!.value
        ? dayjs(this.editForm.get(['compilationJobTimeOfRequest'])!.value, DATE_TIME_FORMAT)
        : undefined,
      entitiesAffected: this.editForm.get(['entitiesAffected'])!.value,
      compilationStatus: this.editForm.get(['compilationStatus'])!.value,
      jobTitle: this.editForm.get(['jobTitle'])!.value,
      numberOfBatches: this.editForm.get(['numberOfBatches'])!.value,
      numberOfProcessedBatches: this.editForm.get(['numberOfProcessedBatches'])!.value,
      processingTime: this.editForm.get(['processingTime'])!.value,
      activePeriod: this.editForm.get(['activePeriod'])!.value,
      initiatedBy: this.editForm.get(['initiatedBy'])!.value,
    };
  }
}
