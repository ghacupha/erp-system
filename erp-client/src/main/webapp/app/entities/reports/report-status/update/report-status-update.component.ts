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

import { IReportStatus, ReportStatus } from '../report-status.model';
import { ReportStatusService } from '../service/report-status.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IProcessStatus } from 'app/entities/system/process-status/process-status.model';
import { ProcessStatusService } from 'app/entities/system/process-status/service/process-status.service';

@Component({
  selector: 'jhi-report-status-update',
  templateUrl: './report-status-update.component.html',
})
export class ReportStatusUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];
  processStatusesSharedCollection: IProcessStatus[] = [];

  editForm = this.fb.group({
    id: [],
    reportName: [null, [Validators.required]],
    reportId: [null, [Validators.required]],
    placeholders: [],
    processStatus: [],
  });

  constructor(
    protected reportStatusService: ReportStatusService,
    protected placeholderService: PlaceholderService,
    protected processStatusService: ProcessStatusService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportStatus }) => {
      this.updateForm(reportStatus);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportStatus = this.createFromForm();
    if (reportStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.reportStatusService.update(reportStatus));
    } else {
      this.subscribeToSaveResponse(this.reportStatusService.create(reportStatus));
    }
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackProcessStatusById(index: number, item: IProcessStatus): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportStatus>>): void {
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

  protected updateForm(reportStatus: IReportStatus): void {
    this.editForm.patchValue({
      id: reportStatus.id,
      reportName: reportStatus.reportName,
      reportId: reportStatus.reportId,
      placeholders: reportStatus.placeholders,
      processStatus: reportStatus.processStatus,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(reportStatus.placeholders ?? [])
    );
    this.processStatusesSharedCollection = this.processStatusService.addProcessStatusToCollectionIfMissing(
      this.processStatusesSharedCollection,
      reportStatus.processStatus
    );
  }

  protected loadRelationshipsOptions(): void {
    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.processStatusService
      .query()
      .pipe(map((res: HttpResponse<IProcessStatus[]>) => res.body ?? []))
      .pipe(
        map((processStatuses: IProcessStatus[]) =>
          this.processStatusService.addProcessStatusToCollectionIfMissing(processStatuses, this.editForm.get('processStatus')!.value)
        )
      )
      .subscribe((processStatuses: IProcessStatus[]) => (this.processStatusesSharedCollection = processStatuses));
  }

  protected createFromForm(): IReportStatus {
    return {
      ...new ReportStatus(),
      id: this.editForm.get(['id'])!.value,
      reportName: this.editForm.get(['reportName'])!.value,
      reportId: this.editForm.get(['reportId'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      processStatus: this.editForm.get(['processStatus'])!.value,
    };
  }
}
