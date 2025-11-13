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

import { IDepreciationJobNotice, DepreciationJobNotice } from '../depreciation-job-notice.model';
import { DepreciationJobNoticeService } from '../service/depreciation-job-notice.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IDepreciationJob } from 'app/entities/assets/depreciation-job/depreciation-job.model';
import { DepreciationJobService } from 'app/entities/assets/depreciation-job/service/depreciation-job.service';
import { IDepreciationBatchSequence } from 'app/entities/assets/depreciation-batch-sequence/depreciation-batch-sequence.model';
import { DepreciationBatchSequenceService } from 'app/entities/assets/depreciation-batch-sequence/service/depreciation-batch-sequence.service';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { DepreciationPeriodService } from 'app/entities/assets/depreciation-period/service/depreciation-period.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { DepreciationNoticeStatusType } from 'app/entities/enumerations/depreciation-notice-status-type.model';

@Component({
  selector: 'jhi-depreciation-job-notice-update',
  templateUrl: './depreciation-job-notice-update.component.html',
})
export class DepreciationJobNoticeUpdateComponent implements OnInit {
  isSaving = false;
  depreciationNoticeStatusTypeValues = Object.keys(DepreciationNoticeStatusType);

  depreciationJobsSharedCollection: IDepreciationJob[] = [];
  depreciationBatchSequencesSharedCollection: IDepreciationBatchSequence[] = [];
  depreciationPeriodsSharedCollection: IDepreciationPeriod[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    eventNarrative: [null, [Validators.required]],
    eventTimeStamp: [null, [Validators.required]],
    depreciationNoticeStatus: [null, [Validators.required]],
    sourceModule: [],
    sourceEntity: [],
    errorCode: [],
    errorMessage: [],
    userAction: [],
    technicalDetails: [],
    depreciationJob: [],
    depreciationBatchSequence: [],
    depreciationPeriod: [],
    placeholders: [],
    universallyUniqueMappings: [],
    superintended: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected depreciationJobNoticeService: DepreciationJobNoticeService,
    protected depreciationJobService: DepreciationJobService,
    protected depreciationBatchSequenceService: DepreciationBatchSequenceService,
    protected depreciationPeriodService: DepreciationPeriodService,
    protected placeholderService: PlaceholderService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depreciationJobNotice }) => {
      if (depreciationJobNotice.id === undefined) {
        const today = dayjs().startOf('day');
        depreciationJobNotice.eventTimeStamp = today;
      }

      this.updateForm(depreciationJobNotice);

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
    const depreciationJobNotice = this.createFromForm();
    if (depreciationJobNotice.id !== undefined) {
      this.subscribeToSaveResponse(this.depreciationJobNoticeService.update(depreciationJobNotice));
    } else {
      this.subscribeToSaveResponse(this.depreciationJobNoticeService.create(depreciationJobNotice));
    }
  }

  trackDepreciationJobById(index: number, item: IDepreciationJob): number {
    return item.id!;
  }

  trackDepreciationBatchSequenceById(index: number, item: IDepreciationBatchSequence): number {
    return item.id!;
  }

  trackDepreciationPeriodById(index: number, item: IDepreciationPeriod): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  getSelectedPlaceholder(option: IPlaceholder, selectedVals?: IPlaceholder[]): IPlaceholder {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedUniversallyUniqueMapping(
    option: IUniversallyUniqueMapping,
    selectedVals?: IUniversallyUniqueMapping[]
  ): IUniversallyUniqueMapping {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepreciationJobNotice>>): void {
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

  protected updateForm(depreciationJobNotice: IDepreciationJobNotice): void {
    this.editForm.patchValue({
      id: depreciationJobNotice.id,
      eventNarrative: depreciationJobNotice.eventNarrative,
      eventTimeStamp: depreciationJobNotice.eventTimeStamp ? depreciationJobNotice.eventTimeStamp.format(DATE_TIME_FORMAT) : null,
      depreciationNoticeStatus: depreciationJobNotice.depreciationNoticeStatus,
      sourceModule: depreciationJobNotice.sourceModule,
      sourceEntity: depreciationJobNotice.sourceEntity,
      errorCode: depreciationJobNotice.errorCode,
      errorMessage: depreciationJobNotice.errorMessage,
      userAction: depreciationJobNotice.userAction,
      technicalDetails: depreciationJobNotice.technicalDetails,
      depreciationJob: depreciationJobNotice.depreciationJob,
      depreciationBatchSequence: depreciationJobNotice.depreciationBatchSequence,
      depreciationPeriod: depreciationJobNotice.depreciationPeriod,
      placeholders: depreciationJobNotice.placeholders,
      universallyUniqueMappings: depreciationJobNotice.universallyUniqueMappings,
      superintended: depreciationJobNotice.superintended,
    });

    this.depreciationJobsSharedCollection = this.depreciationJobService.addDepreciationJobToCollectionIfMissing(
      this.depreciationJobsSharedCollection,
      depreciationJobNotice.depreciationJob
    );
    this.depreciationBatchSequencesSharedCollection =
      this.depreciationBatchSequenceService.addDepreciationBatchSequenceToCollectionIfMissing(
        this.depreciationBatchSequencesSharedCollection,
        depreciationJobNotice.depreciationBatchSequence
      );
    this.depreciationPeriodsSharedCollection = this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
      this.depreciationPeriodsSharedCollection,
      depreciationJobNotice.depreciationPeriod
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(depreciationJobNotice.placeholders ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(depreciationJobNotice.universallyUniqueMappings ?? [])
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      depreciationJobNotice.superintended
    );
  }

  protected loadRelationshipsOptions(): void {
    this.depreciationJobService
      .query()
      .pipe(map((res: HttpResponse<IDepreciationJob[]>) => res.body ?? []))
      .pipe(
        map((depreciationJobs: IDepreciationJob[]) =>
          this.depreciationJobService.addDepreciationJobToCollectionIfMissing(depreciationJobs, this.editForm.get('depreciationJob')!.value)
        )
      )
      .subscribe((depreciationJobs: IDepreciationJob[]) => (this.depreciationJobsSharedCollection = depreciationJobs));

    this.depreciationBatchSequenceService
      .query()
      .pipe(map((res: HttpResponse<IDepreciationBatchSequence[]>) => res.body ?? []))
      .pipe(
        map((depreciationBatchSequences: IDepreciationBatchSequence[]) =>
          this.depreciationBatchSequenceService.addDepreciationBatchSequenceToCollectionIfMissing(
            depreciationBatchSequences,
            this.editForm.get('depreciationBatchSequence')!.value
          )
        )
      )
      .subscribe(
        (depreciationBatchSequences: IDepreciationBatchSequence[]) =>
          (this.depreciationBatchSequencesSharedCollection = depreciationBatchSequences)
      );

    this.depreciationPeriodService
      .query()
      .pipe(map((res: HttpResponse<IDepreciationPeriod[]>) => res.body ?? []))
      .pipe(
        map((depreciationPeriods: IDepreciationPeriod[]) =>
          this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
            depreciationPeriods,
            this.editForm.get('depreciationPeriod')!.value
          )
        )
      )
      .subscribe((depreciationPeriods: IDepreciationPeriod[]) => (this.depreciationPeriodsSharedCollection = depreciationPeriods));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.universallyUniqueMappingService
      .query()
      .pipe(map((res: HttpResponse<IUniversallyUniqueMapping[]>) => res.body ?? []))
      .pipe(
        map((universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
            universallyUniqueMappings,
            ...(this.editForm.get('universallyUniqueMappings')!.value ?? [])
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
      );

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(applicationUsers, this.editForm.get('superintended')!.value)
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }

  protected createFromForm(): IDepreciationJobNotice {
    return {
      ...new DepreciationJobNotice(),
      id: this.editForm.get(['id'])!.value,
      eventNarrative: this.editForm.get(['eventNarrative'])!.value,
      eventTimeStamp: this.editForm.get(['eventTimeStamp'])!.value
        ? dayjs(this.editForm.get(['eventTimeStamp'])!.value, DATE_TIME_FORMAT)
        : undefined,
      depreciationNoticeStatus: this.editForm.get(['depreciationNoticeStatus'])!.value,
      sourceModule: this.editForm.get(['sourceModule'])!.value,
      sourceEntity: this.editForm.get(['sourceEntity'])!.value,
      errorCode: this.editForm.get(['errorCode'])!.value,
      errorMessage: this.editForm.get(['errorMessage'])!.value,
      userAction: this.editForm.get(['userAction'])!.value,
      technicalDetails: this.editForm.get(['technicalDetails'])!.value,
      depreciationJob: this.editForm.get(['depreciationJob'])!.value,
      depreciationBatchSequence: this.editForm.get(['depreciationBatchSequence'])!.value,
      depreciationPeriod: this.editForm.get(['depreciationPeriod'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      universallyUniqueMappings: this.editForm.get(['universallyUniqueMappings'])!.value,
      superintended: this.editForm.get(['superintended'])!.value,
    };
  }
}
