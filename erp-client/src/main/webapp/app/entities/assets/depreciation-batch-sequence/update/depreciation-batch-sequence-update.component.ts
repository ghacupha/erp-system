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

import { IDepreciationBatchSequence, DepreciationBatchSequence } from '../depreciation-batch-sequence.model';
import { DepreciationBatchSequenceService } from '../service/depreciation-batch-sequence.service';
import { IDepreciationJob } from 'app/entities/assets/depreciation-job/depreciation-job.model';
import { DepreciationJobService } from 'app/entities/assets/depreciation-job/service/depreciation-job.service';
import { DepreciationBatchStatusType } from 'app/entities/enumerations/depreciation-batch-status-type.model';

@Component({
  selector: 'jhi-depreciation-batch-sequence-update',
  templateUrl: './depreciation-batch-sequence-update.component.html',
})
export class DepreciationBatchSequenceUpdateComponent implements OnInit {
  isSaving = false;
  depreciationBatchStatusTypeValues = Object.keys(DepreciationBatchStatusType);

  depreciationJobsSharedCollection: IDepreciationJob[] = [];

  editForm = this.fb.group({
    id: [],
    startIndex: [],
    endIndex: [],
    createdAt: [],
    depreciationBatchStatus: [],
    batchSize: [],
    processedItems: [],
    sequenceNumber: [],
    isLastBatch: [],
    processingTime: [],
    totalItems: [],
    depreciationJob: [],
  });

  constructor(
    protected depreciationBatchSequenceService: DepreciationBatchSequenceService,
    protected depreciationJobService: DepreciationJobService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depreciationBatchSequence }) => {
      if (depreciationBatchSequence.id === undefined) {
        const today = dayjs().startOf('day');
        depreciationBatchSequence.createdAt = today;
      }

      this.updateForm(depreciationBatchSequence);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const depreciationBatchSequence = this.createFromForm();
    if (depreciationBatchSequence.id !== undefined) {
      this.subscribeToSaveResponse(this.depreciationBatchSequenceService.update(depreciationBatchSequence));
    } else {
      this.subscribeToSaveResponse(this.depreciationBatchSequenceService.create(depreciationBatchSequence));
    }
  }

  trackDepreciationJobById(index: number, item: IDepreciationJob): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepreciationBatchSequence>>): void {
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

  protected updateForm(depreciationBatchSequence: IDepreciationBatchSequence): void {
    this.editForm.patchValue({
      id: depreciationBatchSequence.id,
      startIndex: depreciationBatchSequence.startIndex,
      endIndex: depreciationBatchSequence.endIndex,
      createdAt: depreciationBatchSequence.createdAt ? depreciationBatchSequence.createdAt.format(DATE_TIME_FORMAT) : null,
      depreciationBatchStatus: depreciationBatchSequence.depreciationBatchStatus,
      batchSize: depreciationBatchSequence.batchSize,
      processedItems: depreciationBatchSequence.processedItems,
      sequenceNumber: depreciationBatchSequence.sequenceNumber,
      isLastBatch: depreciationBatchSequence.isLastBatch,
      processingTime: depreciationBatchSequence.processingTime,
      totalItems: depreciationBatchSequence.totalItems,
      depreciationJob: depreciationBatchSequence.depreciationJob,
    });

    this.depreciationJobsSharedCollection = this.depreciationJobService.addDepreciationJobToCollectionIfMissing(
      this.depreciationJobsSharedCollection,
      depreciationBatchSequence.depreciationJob
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
  }

  protected createFromForm(): IDepreciationBatchSequence {
    return {
      ...new DepreciationBatchSequence(),
      id: this.editForm.get(['id'])!.value,
      startIndex: this.editForm.get(['startIndex'])!.value,
      endIndex: this.editForm.get(['endIndex'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      depreciationBatchStatus: this.editForm.get(['depreciationBatchStatus'])!.value,
      batchSize: this.editForm.get(['batchSize'])!.value,
      processedItems: this.editForm.get(['processedItems'])!.value,
      sequenceNumber: this.editForm.get(['sequenceNumber'])!.value,
      isLastBatch: this.editForm.get(['isLastBatch'])!.value,
      processingTime: this.editForm.get(['processingTime'])!.value,
      totalItems: this.editForm.get(['totalItems'])!.value,
      depreciationJob: this.editForm.get(['depreciationJob'])!.value,
    };
  }
}
