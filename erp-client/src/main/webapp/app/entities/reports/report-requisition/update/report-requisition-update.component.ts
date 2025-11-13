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

import { IReportRequisition, ReportRequisition } from '../report-requisition.model';
import { ReportRequisitionService } from '../service/report-requisition.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';
import { IReportTemplate } from 'app/entities/reports/report-template/report-template.model';
import { ReportTemplateService } from 'app/entities/reports/report-template/service/report-template.service';
import { IReportContentType } from 'app/entities/reports/report-content-type/report-content-type.model';
import { ReportContentTypeService } from 'app/entities/reports/report-content-type/service/report-content-type.service';
import { ReportStatusTypes } from 'app/entities/enumerations/report-status-types.model';

@Component({
  selector: 'jhi-report-requisition-update',
  templateUrl: './report-requisition-update.component.html',
})
export class ReportRequisitionUpdateComponent implements OnInit {
  isSaving = false;
  reportStatusTypesValues = Object.keys(ReportStatusTypes);

  placeholdersSharedCollection: IPlaceholder[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  reportTemplatesSharedCollection: IReportTemplate[] = [];
  reportContentTypesSharedCollection: IReportContentType[] = [];

  editForm = this.fb.group({
    id: [],
    reportName: [null, [Validators.required]],
    reportRequestTime: [null, [Validators.required]],
    reportPassword: [null, [Validators.required, Validators.minLength(6)]],
    reportStatus: [],
    reportId: [null, [Validators.required]],
    reportFileAttachment: [],
    reportFileAttachmentContentType: [],
    reportFileCheckSum: [],
    reportNotes: [],
    reportNotesContentType: [],
    placeholders: [],
    parameters: [],
    reportTemplate: [null, Validators.required],
    reportContentType: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected reportRequisitionService: ReportRequisitionService,
    protected placeholderService: PlaceholderService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected reportTemplateService: ReportTemplateService,
    protected reportContentTypeService: ReportContentTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportRequisition }) => {
      if (reportRequisition.id === undefined) {
        const today = dayjs().startOf('day');
        reportRequisition.reportRequestTime = today;
      }

      this.updateForm(reportRequisition);

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
    const reportRequisition = this.createFromForm();
    if (reportRequisition.id !== undefined) {
      this.subscribeToSaveResponse(this.reportRequisitionService.update(reportRequisition));
    } else {
      this.subscribeToSaveResponse(this.reportRequisitionService.create(reportRequisition));
    }
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  trackReportTemplateById(index: number, item: IReportTemplate): number {
    return item.id!;
  }

  trackReportContentTypeById(index: number, item: IReportContentType): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportRequisition>>): void {
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

  protected updateForm(reportRequisition: IReportRequisition): void {
    this.editForm.patchValue({
      id: reportRequisition.id,
      reportName: reportRequisition.reportName,
      reportRequestTime: reportRequisition.reportRequestTime ? reportRequisition.reportRequestTime.format(DATE_TIME_FORMAT) : null,
      reportPassword: reportRequisition.reportPassword,
      reportStatus: reportRequisition.reportStatus,
      reportId: reportRequisition.reportId,
      reportFileAttachment: reportRequisition.reportFileAttachment,
      reportFileAttachmentContentType: reportRequisition.reportFileAttachmentContentType,
      reportFileCheckSum: reportRequisition.reportFileCheckSum,
      reportNotes: reportRequisition.reportNotes,
      reportNotesContentType: reportRequisition.reportNotesContentType,
      placeholders: reportRequisition.placeholders,
      parameters: reportRequisition.parameters,
      reportTemplate: reportRequisition.reportTemplate,
      reportContentType: reportRequisition.reportContentType,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(reportRequisition.placeholders ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(reportRequisition.parameters ?? [])
    );
    this.reportTemplatesSharedCollection = this.reportTemplateService.addReportTemplateToCollectionIfMissing(
      this.reportTemplatesSharedCollection,
      reportRequisition.reportTemplate
    );
    this.reportContentTypesSharedCollection = this.reportContentTypeService.addReportContentTypeToCollectionIfMissing(
      this.reportContentTypesSharedCollection,
      reportRequisition.reportContentType
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

    this.universallyUniqueMappingService
      .query()
      .pipe(map((res: HttpResponse<IUniversallyUniqueMapping[]>) => res.body ?? []))
      .pipe(
        map((universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
            universallyUniqueMappings,
            ...(this.editForm.get('parameters')!.value ?? [])
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
      );

    this.reportTemplateService
      .query()
      .pipe(map((res: HttpResponse<IReportTemplate[]>) => res.body ?? []))
      .pipe(
        map((reportTemplates: IReportTemplate[]) =>
          this.reportTemplateService.addReportTemplateToCollectionIfMissing(reportTemplates, this.editForm.get('reportTemplate')!.value)
        )
      )
      .subscribe((reportTemplates: IReportTemplate[]) => (this.reportTemplatesSharedCollection = reportTemplates));

    this.reportContentTypeService
      .query()
      .pipe(map((res: HttpResponse<IReportContentType[]>) => res.body ?? []))
      .pipe(
        map((reportContentTypes: IReportContentType[]) =>
          this.reportContentTypeService.addReportContentTypeToCollectionIfMissing(
            reportContentTypes,
            this.editForm.get('reportContentType')!.value
          )
        )
      )
      .subscribe((reportContentTypes: IReportContentType[]) => (this.reportContentTypesSharedCollection = reportContentTypes));
  }

  protected createFromForm(): IReportRequisition {
    return {
      ...new ReportRequisition(),
      id: this.editForm.get(['id'])!.value,
      reportName: this.editForm.get(['reportName'])!.value,
      reportRequestTime: this.editForm.get(['reportRequestTime'])!.value
        ? dayjs(this.editForm.get(['reportRequestTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      reportPassword: this.editForm.get(['reportPassword'])!.value,
      reportStatus: this.editForm.get(['reportStatus'])!.value,
      reportId: this.editForm.get(['reportId'])!.value,
      reportFileAttachmentContentType: this.editForm.get(['reportFileAttachmentContentType'])!.value,
      reportFileAttachment: this.editForm.get(['reportFileAttachment'])!.value,
      reportFileCheckSum: this.editForm.get(['reportFileCheckSum'])!.value,
      reportNotesContentType: this.editForm.get(['reportNotesContentType'])!.value,
      reportNotes: this.editForm.get(['reportNotes'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      parameters: this.editForm.get(['parameters'])!.value,
      reportTemplate: this.editForm.get(['reportTemplate'])!.value,
      reportContentType: this.editForm.get(['reportContentType'])!.value,
    };
  }
}
