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
import { finalize } from 'rxjs/operators';

import { IIsicEconomicActivity, IsicEconomicActivity } from '../isic-economic-activity.model';
import { IsicEconomicActivityService } from '../service/isic-economic-activity.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-isic-economic-activity-update',
  templateUrl: './isic-economic-activity-update.component.html',
})
export class IsicEconomicActivityUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    businessEconomicActivityCode: [null, [Validators.required]],
    section: [null, [Validators.required]],
    sectionLabel: [null, [Validators.required]],
    division: [null, [Validators.required]],
    divisionLabel: [null, [Validators.required]],
    groupCode: [],
    groupLabel: [null, [Validators.required]],
    classCode: [null, [Validators.required]],
    businessEconomicActivityType: [],
    businessEconomicActivityTypeDescription: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected isicEconomicActivityService: IsicEconomicActivityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isicEconomicActivity }) => {
      this.updateForm(isicEconomicActivity);
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
    const isicEconomicActivity = this.createFromForm();
    if (isicEconomicActivity.id !== undefined) {
      this.subscribeToSaveResponse(this.isicEconomicActivityService.update(isicEconomicActivity));
    } else {
      this.subscribeToSaveResponse(this.isicEconomicActivityService.create(isicEconomicActivity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIsicEconomicActivity>>): void {
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

  protected updateForm(isicEconomicActivity: IIsicEconomicActivity): void {
    this.editForm.patchValue({
      id: isicEconomicActivity.id,
      businessEconomicActivityCode: isicEconomicActivity.businessEconomicActivityCode,
      section: isicEconomicActivity.section,
      sectionLabel: isicEconomicActivity.sectionLabel,
      division: isicEconomicActivity.division,
      divisionLabel: isicEconomicActivity.divisionLabel,
      groupCode: isicEconomicActivity.groupCode,
      groupLabel: isicEconomicActivity.groupLabel,
      classCode: isicEconomicActivity.classCode,
      businessEconomicActivityType: isicEconomicActivity.businessEconomicActivityType,
      businessEconomicActivityTypeDescription: isicEconomicActivity.businessEconomicActivityTypeDescription,
    });
  }

  protected createFromForm(): IIsicEconomicActivity {
    return {
      ...new IsicEconomicActivity(),
      id: this.editForm.get(['id'])!.value,
      businessEconomicActivityCode: this.editForm.get(['businessEconomicActivityCode'])!.value,
      section: this.editForm.get(['section'])!.value,
      sectionLabel: this.editForm.get(['sectionLabel'])!.value,
      division: this.editForm.get(['division'])!.value,
      divisionLabel: this.editForm.get(['divisionLabel'])!.value,
      groupCode: this.editForm.get(['groupCode'])!.value,
      groupLabel: this.editForm.get(['groupLabel'])!.value,
      classCode: this.editForm.get(['classCode'])!.value,
      businessEconomicActivityType: this.editForm.get(['businessEconomicActivityType'])!.value,
      businessEconomicActivityTypeDescription: this.editForm.get(['businessEconomicActivityTypeDescription'])!.value,
    };
  }
}
