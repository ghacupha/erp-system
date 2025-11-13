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
import { finalize } from 'rxjs/operators';

import { IWeeklyCounterfeitHolding, WeeklyCounterfeitHolding } from '../weekly-counterfeit-holding.model';
import { WeeklyCounterfeitHoldingService } from '../service/weekly-counterfeit-holding.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-weekly-counterfeit-holding-update',
  templateUrl: './weekly-counterfeit-holding-update.component.html',
})
export class WeeklyCounterfeitHoldingUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    reportingDate: [null, [Validators.required]],
    dateConfiscated: [null, [Validators.required]],
    serialNumber: [null, [Validators.required]],
    depositorsNames: [null, [Validators.required]],
    tellersNames: [null, [Validators.required]],
    dateSubmittedToCBK: [null, [Validators.required]],
    remarks: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected weeklyCounterfeitHoldingService: WeeklyCounterfeitHoldingService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weeklyCounterfeitHolding }) => {
      this.updateForm(weeklyCounterfeitHolding);
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
    const weeklyCounterfeitHolding = this.createFromForm();
    if (weeklyCounterfeitHolding.id !== undefined) {
      this.subscribeToSaveResponse(this.weeklyCounterfeitHoldingService.update(weeklyCounterfeitHolding));
    } else {
      this.subscribeToSaveResponse(this.weeklyCounterfeitHoldingService.create(weeklyCounterfeitHolding));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWeeklyCounterfeitHolding>>): void {
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

  protected updateForm(weeklyCounterfeitHolding: IWeeklyCounterfeitHolding): void {
    this.editForm.patchValue({
      id: weeklyCounterfeitHolding.id,
      reportingDate: weeklyCounterfeitHolding.reportingDate,
      dateConfiscated: weeklyCounterfeitHolding.dateConfiscated,
      serialNumber: weeklyCounterfeitHolding.serialNumber,
      depositorsNames: weeklyCounterfeitHolding.depositorsNames,
      tellersNames: weeklyCounterfeitHolding.tellersNames,
      dateSubmittedToCBK: weeklyCounterfeitHolding.dateSubmittedToCBK,
      remarks: weeklyCounterfeitHolding.remarks,
    });
  }

  protected createFromForm(): IWeeklyCounterfeitHolding {
    return {
      ...new WeeklyCounterfeitHolding(),
      id: this.editForm.get(['id'])!.value,
      reportingDate: this.editForm.get(['reportingDate'])!.value,
      dateConfiscated: this.editForm.get(['dateConfiscated'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      depositorsNames: this.editForm.get(['depositorsNames'])!.value,
      tellersNames: this.editForm.get(['tellersNames'])!.value,
      dateSubmittedToCBK: this.editForm.get(['dateSubmittedToCBK'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
    };
  }
}
