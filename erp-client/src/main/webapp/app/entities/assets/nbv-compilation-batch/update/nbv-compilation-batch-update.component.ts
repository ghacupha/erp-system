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

import { INbvCompilationBatch, NbvCompilationBatch } from '../nbv-compilation-batch.model';
import { NbvCompilationBatchService } from '../service/nbv-compilation-batch.service';
import { INbvCompilationJob } from 'app/entities/assets/nbv-compilation-job/nbv-compilation-job.model';
import { NbvCompilationJobService } from 'app/entities/assets/nbv-compilation-job/service/nbv-compilation-job.service';
import { CompilationBatchStatusTypes } from 'app/entities/enumerations/compilation-batch-status-types.model';

@Component({
  selector: 'jhi-nbv-compilation-batch-update',
  templateUrl: './nbv-compilation-batch-update.component.html',
})
export class NbvCompilationBatchUpdateComponent implements OnInit {
  isSaving = false;
  compilationBatchStatusTypesValues = Object.keys(CompilationBatchStatusTypes);

  nbvCompilationJobsSharedCollection: INbvCompilationJob[] = [];

  editForm = this.fb.group({
    id: [],
    startIndex: [],
    endIndex: [],
    compilationBatchStatus: [],
    compilationBatchIdentifier: [],
    compilationJobidentifier: [],
    depreciationPeriodIdentifier: [],
    fiscalMonthIdentifier: [],
    batchSize: [],
    processedItems: [],
    sequenceNumber: [],
    isLastBatch: [],
    processingTime: [],
    totalItems: [],
    nbvCompilationJob: [],
  });

  constructor(
    protected nbvCompilationBatchService: NbvCompilationBatchService,
    protected nbvCompilationJobService: NbvCompilationJobService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nbvCompilationBatch }) => {
      this.updateForm(nbvCompilationBatch);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nbvCompilationBatch = this.createFromForm();
    if (nbvCompilationBatch.id !== undefined) {
      this.subscribeToSaveResponse(this.nbvCompilationBatchService.update(nbvCompilationBatch));
    } else {
      this.subscribeToSaveResponse(this.nbvCompilationBatchService.create(nbvCompilationBatch));
    }
  }

  trackNbvCompilationJobById(index: number, item: INbvCompilationJob): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INbvCompilationBatch>>): void {
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

  protected updateForm(nbvCompilationBatch: INbvCompilationBatch): void {
    this.editForm.patchValue({
      id: nbvCompilationBatch.id,
      startIndex: nbvCompilationBatch.startIndex,
      endIndex: nbvCompilationBatch.endIndex,
      compilationBatchStatus: nbvCompilationBatch.compilationBatchStatus,
      compilationBatchIdentifier: nbvCompilationBatch.compilationBatchIdentifier,
      compilationJobidentifier: nbvCompilationBatch.compilationJobidentifier,
      depreciationPeriodIdentifier: nbvCompilationBatch.depreciationPeriodIdentifier,
      fiscalMonthIdentifier: nbvCompilationBatch.fiscalMonthIdentifier,
      batchSize: nbvCompilationBatch.batchSize,
      processedItems: nbvCompilationBatch.processedItems,
      sequenceNumber: nbvCompilationBatch.sequenceNumber,
      isLastBatch: nbvCompilationBatch.isLastBatch,
      processingTime: nbvCompilationBatch.processingTime,
      totalItems: nbvCompilationBatch.totalItems,
      nbvCompilationJob: nbvCompilationBatch.nbvCompilationJob,
    });

    this.nbvCompilationJobsSharedCollection = this.nbvCompilationJobService.addNbvCompilationJobToCollectionIfMissing(
      this.nbvCompilationJobsSharedCollection,
      nbvCompilationBatch.nbvCompilationJob
    );
  }

  protected loadRelationshipsOptions(): void {
    this.nbvCompilationJobService
      .query()
      .pipe(map((res: HttpResponse<INbvCompilationJob[]>) => res.body ?? []))
      .pipe(
        map((nbvCompilationJobs: INbvCompilationJob[]) =>
          this.nbvCompilationJobService.addNbvCompilationJobToCollectionIfMissing(
            nbvCompilationJobs,
            this.editForm.get('nbvCompilationJob')!.value
          )
        )
      )
      .subscribe((nbvCompilationJobs: INbvCompilationJob[]) => (this.nbvCompilationJobsSharedCollection = nbvCompilationJobs));
  }

  protected createFromForm(): INbvCompilationBatch {
    return {
      ...new NbvCompilationBatch(),
      id: this.editForm.get(['id'])!.value,
      startIndex: this.editForm.get(['startIndex'])!.value,
      endIndex: this.editForm.get(['endIndex'])!.value,
      compilationBatchStatus: this.editForm.get(['compilationBatchStatus'])!.value,
      compilationBatchIdentifier: this.editForm.get(['compilationBatchIdentifier'])!.value,
      compilationJobidentifier: this.editForm.get(['compilationJobidentifier'])!.value,
      depreciationPeriodIdentifier: this.editForm.get(['depreciationPeriodIdentifier'])!.value,
      fiscalMonthIdentifier: this.editForm.get(['fiscalMonthIdentifier'])!.value,
      batchSize: this.editForm.get(['batchSize'])!.value,
      processedItems: this.editForm.get(['processedItems'])!.value,
      sequenceNumber: this.editForm.get(['sequenceNumber'])!.value,
      isLastBatch: this.editForm.get(['isLastBatch'])!.value,
      processingTime: this.editForm.get(['processingTime'])!.value,
      totalItems: this.editForm.get(['totalItems'])!.value,
      nbvCompilationJob: this.editForm.get(['nbvCompilationJob'])!.value,
    };
  }
}
