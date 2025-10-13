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

import { INbvReport, NbvReport } from '../nbv-report.model';
import { NbvReportService } from '../service/nbv-report.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { DepreciationPeriodService } from 'app/entities/assets/depreciation-period/service/depreciation-period.service';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ServiceOutletService } from 'app/entities/system/service-outlet/service/service-outlet.service';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { AssetCategoryService } from 'app/entities/assets/asset-category/service/asset-category.service';

@Component({
  selector: 'jhi-nbv-report-update',
  templateUrl: './nbv-report-update.component.html',
})
export class NbvReportUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUser[] = [];
  depreciationPeriodsSharedCollection: IDepreciationPeriod[] = [];
  serviceOutletsSharedCollection: IServiceOutlet[] = [];
  assetCategoriesSharedCollection: IAssetCategory[] = [];

  editForm = this.fb.group({
    id: [],
    reportName: [null, [Validators.required]],
    timeOfReportRequest: [null, [Validators.required]],
    fileChecksum: [],
    tampered: [],
    filename: [],
    reportParameters: [],
    reportFile: [],
    reportFileContentType: [],
    requestedBy: [],
    depreciationPeriod: [null, Validators.required],
    serviceOutlet: [],
    assetCategory: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected nbvReportService: NbvReportService,
    protected applicationUserService: ApplicationUserService,
    protected depreciationPeriodService: DepreciationPeriodService,
    protected serviceOutletService: ServiceOutletService,
    protected assetCategoryService: AssetCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nbvReport }) => {
      if (nbvReport.id === undefined) {
        const today = dayjs().startOf('day');
        nbvReport.timeOfReportRequest = today;
      }

      this.updateForm(nbvReport);

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
    const nbvReport = this.createFromForm();
    if (nbvReport.id !== undefined) {
      this.subscribeToSaveResponse(this.nbvReportService.update(nbvReport));
    } else {
      this.subscribeToSaveResponse(this.nbvReportService.create(nbvReport));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  trackDepreciationPeriodById(index: number, item: IDepreciationPeriod): number {
    return item.id!;
  }

  trackServiceOutletById(index: number, item: IServiceOutlet): number {
    return item.id!;
  }

  trackAssetCategoryById(index: number, item: IAssetCategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INbvReport>>): void {
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

  protected updateForm(nbvReport: INbvReport): void {
    this.editForm.patchValue({
      id: nbvReport.id,
      reportName: nbvReport.reportName,
      timeOfReportRequest: nbvReport.timeOfReportRequest ? nbvReport.timeOfReportRequest.format(DATE_TIME_FORMAT) : null,
      fileChecksum: nbvReport.fileChecksum,
      tampered: nbvReport.tampered,
      filename: nbvReport.filename,
      reportParameters: nbvReport.reportParameters,
      reportFile: nbvReport.reportFile,
      reportFileContentType: nbvReport.reportFileContentType,
      requestedBy: nbvReport.requestedBy,
      depreciationPeriod: nbvReport.depreciationPeriod,
      serviceOutlet: nbvReport.serviceOutlet,
      assetCategory: nbvReport.assetCategory,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      nbvReport.requestedBy
    );
    this.depreciationPeriodsSharedCollection = this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
      this.depreciationPeriodsSharedCollection,
      nbvReport.depreciationPeriod
    );
    this.serviceOutletsSharedCollection = this.serviceOutletService.addServiceOutletToCollectionIfMissing(
      this.serviceOutletsSharedCollection,
      nbvReport.serviceOutlet
    );
    this.assetCategoriesSharedCollection = this.assetCategoryService.addAssetCategoryToCollectionIfMissing(
      this.assetCategoriesSharedCollection,
      nbvReport.assetCategory
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

    this.serviceOutletService
      .query()
      .pipe(map((res: HttpResponse<IServiceOutlet[]>) => res.body ?? []))
      .pipe(
        map((serviceOutlets: IServiceOutlet[]) =>
          this.serviceOutletService.addServiceOutletToCollectionIfMissing(serviceOutlets, this.editForm.get('serviceOutlet')!.value)
        )
      )
      .subscribe((serviceOutlets: IServiceOutlet[]) => (this.serviceOutletsSharedCollection = serviceOutlets));

    this.assetCategoryService
      .query()
      .pipe(map((res: HttpResponse<IAssetCategory[]>) => res.body ?? []))
      .pipe(
        map((assetCategories: IAssetCategory[]) =>
          this.assetCategoryService.addAssetCategoryToCollectionIfMissing(assetCategories, this.editForm.get('assetCategory')!.value)
        )
      )
      .subscribe((assetCategories: IAssetCategory[]) => (this.assetCategoriesSharedCollection = assetCategories));
  }

  protected createFromForm(): INbvReport {
    return {
      ...new NbvReport(),
      id: this.editForm.get(['id'])!.value,
      reportName: this.editForm.get(['reportName'])!.value,
      timeOfReportRequest: this.editForm.get(['timeOfReportRequest'])!.value
        ? dayjs(this.editForm.get(['timeOfReportRequest'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fileChecksum: this.editForm.get(['fileChecksum'])!.value,
      tampered: this.editForm.get(['tampered'])!.value,
      filename: this.editForm.get(['filename'])!.value,
      reportParameters: this.editForm.get(['reportParameters'])!.value,
      reportFileContentType: this.editForm.get(['reportFileContentType'])!.value,
      reportFile: this.editForm.get(['reportFile'])!.value,
      requestedBy: this.editForm.get(['requestedBy'])!.value,
      depreciationPeriod: this.editForm.get(['depreciationPeriod'])!.value,
      serviceOutlet: this.editForm.get(['serviceOutlet'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
    };
  }
}
