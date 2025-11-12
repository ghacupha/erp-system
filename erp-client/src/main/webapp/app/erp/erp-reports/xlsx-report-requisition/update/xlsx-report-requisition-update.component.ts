///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { v4 as uuidv4 } from 'uuid';
import { IXlsxReportRequisition, XlsxReportRequisition } from '../xlsx-report-requisition.model';
import { XlsxReportRequisitionService } from '../service/xlsx-report-requisition.service';
import { DataUtils } from 'app/core/util/data-util.service';
import { ReportStatusTypes } from '../../../erp-common/enumerations/report-status-types.model';
import { ReportTemplateService } from '../../report-template/service/report-template.service';
import { IReportTemplate } from '../../report-template/report-template.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from '../../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';

@Component({
  selector: 'jhi-xlsx-report-requisition-update',
  templateUrl: './xlsx-report-requisition-update.component.html',
})
export class XlsxReportRequisitionUpdateComponent implements OnInit {
  isSaving = false;
  reportStatusTypesValues = Object.keys(ReportStatusTypes);

  reportTemplatesSharedCollection: IReportTemplate[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];

  editForm = this.fb.group({
    id: [],
    reportName: [null, [Validators.required]],
    reportDate: [],
    userPassword: [null, []],
    reportStatus: [],
    reportId: [null, [Validators.required]],
    reportTemplate: [null, Validators.required],
    placeholders: [],
    reportFileChecksum: [],
    parameters: [],
  });

  constructor(
    protected xlsxReportRequisitionService: XlsxReportRequisitionService,
    protected reportTemplateService: ReportTemplateService,
    protected placeholderService: PlaceholderService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xlsxReportRequisition }) => {
      if (!xlsxReportRequisition.id) {
        this.editForm.patchValue({
          reportStatus: ReportStatusTypes.GENERATING,
          reportId: uuidv4(),
        });
      }

      this.updateForm(xlsxReportRequisition);

      if (!xlsxReportRequisition.id) {
        this.editForm.patchValue({
          reportStatus: ReportStatusTypes.GENERATING,
          reportId: uuidv4(),
        });
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const xlsxReportRequisition = this.createFromForm();
    if (xlsxReportRequisition.id !== undefined) {
      this.subscribeToSaveResponse(this.xlsxReportRequisitionService.update(xlsxReportRequisition));
    } else {
      this.subscribeToSaveResponse(this.xlsxReportRequisitionService.create(xlsxReportRequisition));
    }
  }

  trackReportTemplateById(index: number, item: IReportTemplate): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
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

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IXlsxReportRequisition>>): void {
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

  protected updateForm(xlsxReportRequisition: IXlsxReportRequisition): void {
    this.editForm.patchValue({
      id: xlsxReportRequisition.id,
      reportName: xlsxReportRequisition.reportName,
      reportDate: xlsxReportRequisition.reportDate,
      userPassword: xlsxReportRequisition.userPassword,
      reportStatus: xlsxReportRequisition.reportStatus,
      reportId: xlsxReportRequisition.reportId,
      reportTemplate: xlsxReportRequisition.reportTemplate,
      placeholders: xlsxReportRequisition.placeholders,
      reportFileChecksum: xlsxReportRequisition.reportFileChecksum,
      parameters: xlsxReportRequisition.parameters,
    });

    this.reportTemplatesSharedCollection = this.reportTemplateService.addReportTemplateToCollectionIfMissing(
      this.reportTemplatesSharedCollection,
      xlsxReportRequisition.reportTemplate
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(xlsxReportRequisition.placeholders ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(xlsxReportRequisition.parameters ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.reportTemplateService
      .query()
      .pipe(map((res: HttpResponse<IReportTemplate[]>) => res.body ?? []))
      .pipe(
        map((reportTemplates: IReportTemplate[]) =>
          this.reportTemplateService.addReportTemplateToCollectionIfMissing(reportTemplates, this.editForm.get('reportTemplate')!.value)
        )
      )
      .subscribe((reportTemplates: IReportTemplate[]) => (this.reportTemplatesSharedCollection = reportTemplates));

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
  }

  protected createFromForm(): IXlsxReportRequisition {
    return {
      ...new XlsxReportRequisition(),
      id: this.editForm.get(['id'])!.value,
      reportName: this.editForm.get(['reportName'])!.value,
      reportDate: this.editForm.get(['reportDate'])!.value,
      userPassword: this.editForm.get(['userPassword'])!.value,
      reportFileChecksum: this.editForm.get(['reportFileChecksum'])!.value,
      reportStatus: this.editForm.get(['reportStatus'])!.value,
      reportId: this.editForm.get(['reportId'])!.value,
      reportTemplate: this.editForm.get(['reportTemplate'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      parameters: this.editForm.get(['parameters'])!.value,
    };
  }
}
