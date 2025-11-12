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
import { finalize } from 'rxjs/operators';

import { ICurrencyServiceabilityFlag, CurrencyServiceabilityFlag } from '../currency-serviceability-flag.model';
import { CurrencyServiceabilityFlagService } from '../service/currency-serviceability-flag.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { CurrencyServiceabilityFlagTypes } from 'app/entities/enumerations/currency-serviceability-flag-types.model';
import { CurrencyServiceability } from 'app/entities/enumerations/currency-serviceability.model';

@Component({
  selector: 'jhi-currency-serviceability-flag-update',
  templateUrl: './currency-serviceability-flag-update.component.html',
})
export class CurrencyServiceabilityFlagUpdateComponent implements OnInit {
  isSaving = false;
  currencyServiceabilityFlagTypesValues = Object.keys(CurrencyServiceabilityFlagTypes);
  currencyServiceabilityValues = Object.keys(CurrencyServiceability);

  editForm = this.fb.group({
    id: [],
    currencyServiceabilityFlag: [null, [Validators.required]],
    currencyServiceability: [null, [Validators.required]],
    currencyServiceabilityFlagDetails: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected currencyServiceabilityFlagService: CurrencyServiceabilityFlagService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ currencyServiceabilityFlag }) => {
      this.updateForm(currencyServiceabilityFlag);
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
    const currencyServiceabilityFlag = this.createFromForm();
    if (currencyServiceabilityFlag.id !== undefined) {
      this.subscribeToSaveResponse(this.currencyServiceabilityFlagService.update(currencyServiceabilityFlag));
    } else {
      this.subscribeToSaveResponse(this.currencyServiceabilityFlagService.create(currencyServiceabilityFlag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICurrencyServiceabilityFlag>>): void {
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

  protected updateForm(currencyServiceabilityFlag: ICurrencyServiceabilityFlag): void {
    this.editForm.patchValue({
      id: currencyServiceabilityFlag.id,
      currencyServiceabilityFlag: currencyServiceabilityFlag.currencyServiceabilityFlag,
      currencyServiceability: currencyServiceabilityFlag.currencyServiceability,
      currencyServiceabilityFlagDetails: currencyServiceabilityFlag.currencyServiceabilityFlagDetails,
    });
  }

  protected createFromForm(): ICurrencyServiceabilityFlag {
    return {
      ...new CurrencyServiceabilityFlag(),
      id: this.editForm.get(['id'])!.value,
      currencyServiceabilityFlag: this.editForm.get(['currencyServiceabilityFlag'])!.value,
      currencyServiceability: this.editForm.get(['currencyServiceability'])!.value,
      currencyServiceabilityFlagDetails: this.editForm.get(['currencyServiceabilityFlagDetails'])!.value,
    };
  }
}
