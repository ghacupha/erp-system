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
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAssetAdditionsReport, AssetAdditionsReport } from '../asset-additions-report.model';
import { AssetAdditionsReportService } from '../service/asset-additions-report.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';

@Component({
  selector: 'jhi-asset-additions-report-update',
  templateUrl: './asset-additions-report-update.component.html',
})
export class AssetAdditionsReportUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    timeOfRequest: [],
    reportStartDate: [],
    reportEndDate: [],
    requestId: [null, []],
    tampered: [],
    reportParameters: [],
    reportFile: [],
    reportFileContentType: [],
    requestedBy: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected assetAdditionsReportService: AssetAdditionsReportService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetAdditionsReport }) => {
      this.updateForm(assetAdditionsReport);

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
    const assetAdditionsReport = this.createFromForm();
    if (assetAdditionsReport.id !== undefined) {
      this.subscribeToSaveResponse(this.assetAdditionsReportService.update(assetAdditionsReport));
    } else {
      this.subscribeToSaveResponse(this.assetAdditionsReportService.create(assetAdditionsReport));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetAdditionsReport>>): void {
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

  protected updateForm(assetAdditionsReport: IAssetAdditionsReport): void {
    this.editForm.patchValue({
      id: assetAdditionsReport.id,
      timeOfRequest: assetAdditionsReport.timeOfRequest,
      reportStartDate: assetAdditionsReport.reportStartDate,
      reportEndDate: assetAdditionsReport.reportEndDate,
      requestId: assetAdditionsReport.requestId,
      tampered: assetAdditionsReport.tampered,
      reportParameters: assetAdditionsReport.reportParameters,
      reportFile: assetAdditionsReport.reportFile,
      reportFileContentType: assetAdditionsReport.reportFileContentType,
      requestedBy: assetAdditionsReport.requestedBy,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      assetAdditionsReport.requestedBy
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
  }

  protected createFromForm(): IAssetAdditionsReport {
    return {
      ...new AssetAdditionsReport(),
      id: this.editForm.get(['id'])!.value,
      timeOfRequest: this.editForm.get(['timeOfRequest'])!.value,
      reportStartDate: this.editForm.get(['reportStartDate'])!.value,
      reportEndDate: this.editForm.get(['reportEndDate'])!.value,
      requestId: this.editForm.get(['requestId'])!.value,
      tampered: this.editForm.get(['tampered'])!.value,
      reportParameters: this.editForm.get(['reportParameters'])!.value,
      reportFileContentType: this.editForm.get(['reportFileContentType'])!.value,
      reportFile: this.editForm.get(['reportFile'])!.value,
      requestedBy: this.editForm.get(['requestedBy'])!.value,
    };
  }
}
