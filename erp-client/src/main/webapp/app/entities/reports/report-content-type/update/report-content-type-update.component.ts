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

import { IReportContentType, ReportContentType } from '../report-content-type.model';
import { ReportContentTypeService } from '../service/report-content-type.service';
import { ISystemContentType } from 'app/entities/system/system-content-type/system-content-type.model';
import { SystemContentTypeService } from 'app/entities/system/system-content-type/service/system-content-type.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-report-content-type-update',
  templateUrl: './report-content-type-update.component.html',
})
export class ReportContentTypeUpdateComponent implements OnInit {
  isSaving = false;

  systemContentTypesSharedCollection: ISystemContentType[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    reportTypeName: [null, [Validators.required]],
    reportFileExtension: [null, [Validators.required]],
    systemContentType: [null, Validators.required],
    placeholders: [],
  });

  constructor(
    protected reportContentTypeService: ReportContentTypeService,
    protected systemContentTypeService: SystemContentTypeService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportContentType }) => {
      this.updateForm(reportContentType);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportContentType = this.createFromForm();
    if (reportContentType.id !== undefined) {
      this.subscribeToSaveResponse(this.reportContentTypeService.update(reportContentType));
    } else {
      this.subscribeToSaveResponse(this.reportContentTypeService.create(reportContentType));
    }
  }

  trackSystemContentTypeById(index: number, item: ISystemContentType): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportContentType>>): void {
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

  protected updateForm(reportContentType: IReportContentType): void {
    this.editForm.patchValue({
      id: reportContentType.id,
      reportTypeName: reportContentType.reportTypeName,
      reportFileExtension: reportContentType.reportFileExtension,
      systemContentType: reportContentType.systemContentType,
      placeholders: reportContentType.placeholders,
    });

    this.systemContentTypesSharedCollection = this.systemContentTypeService.addSystemContentTypeToCollectionIfMissing(
      this.systemContentTypesSharedCollection,
      reportContentType.systemContentType
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(reportContentType.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.systemContentTypeService
      .query()
      .pipe(map((res: HttpResponse<ISystemContentType[]>) => res.body ?? []))
      .pipe(
        map((systemContentTypes: ISystemContentType[]) =>
          this.systemContentTypeService.addSystemContentTypeToCollectionIfMissing(
            systemContentTypes,
            this.editForm.get('systemContentType')!.value
          )
        )
      )
      .subscribe((systemContentTypes: ISystemContentType[]) => (this.systemContentTypesSharedCollection = systemContentTypes));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  }

  protected createFromForm(): IReportContentType {
    return {
      ...new ReportContentType(),
      id: this.editForm.get(['id'])!.value,
      reportTypeName: this.editForm.get(['reportTypeName'])!.value,
      reportFileExtension: this.editForm.get(['reportFileExtension'])!.value,
      systemContentType: this.editForm.get(['systemContentType'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
