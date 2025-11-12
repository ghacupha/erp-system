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

import { ICurrencyAuthenticityFlag, CurrencyAuthenticityFlag } from '../currency-authenticity-flag.model';
import { CurrencyAuthenticityFlagService } from '../service/currency-authenticity-flag.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { CurrencyAuthenticityFlags } from 'app/entities/enumerations/currency-authenticity-flags.model';
import { CurrencyAuthenticityTypes } from 'app/entities/enumerations/currency-authenticity-types.model';

@Component({
  selector: 'jhi-currency-authenticity-flag-update',
  templateUrl: './currency-authenticity-flag-update.component.html',
})
export class CurrencyAuthenticityFlagUpdateComponent implements OnInit {
  isSaving = false;
  currencyAuthenticityFlagsValues = Object.keys(CurrencyAuthenticityFlags);
  currencyAuthenticityTypesValues = Object.keys(CurrencyAuthenticityTypes);

  editForm = this.fb.group({
    id: [],
    currencyAuthenticityFlag: [null, [Validators.required]],
    currencyAuthenticityType: [null, [Validators.required]],
    currencyAuthenticityTypeDetails: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected currencyAuthenticityFlagService: CurrencyAuthenticityFlagService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ currencyAuthenticityFlag }) => {
      this.updateForm(currencyAuthenticityFlag);
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
    const currencyAuthenticityFlag = this.createFromForm();
    if (currencyAuthenticityFlag.id !== undefined) {
      this.subscribeToSaveResponse(this.currencyAuthenticityFlagService.update(currencyAuthenticityFlag));
    } else {
      this.subscribeToSaveResponse(this.currencyAuthenticityFlagService.create(currencyAuthenticityFlag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICurrencyAuthenticityFlag>>): void {
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

  protected updateForm(currencyAuthenticityFlag: ICurrencyAuthenticityFlag): void {
    this.editForm.patchValue({
      id: currencyAuthenticityFlag.id,
      currencyAuthenticityFlag: currencyAuthenticityFlag.currencyAuthenticityFlag,
      currencyAuthenticityType: currencyAuthenticityFlag.currencyAuthenticityType,
      currencyAuthenticityTypeDetails: currencyAuthenticityFlag.currencyAuthenticityTypeDetails,
    });
  }

  protected createFromForm(): ICurrencyAuthenticityFlag {
    return {
      ...new CurrencyAuthenticityFlag(),
      id: this.editForm.get(['id'])!.value,
      currencyAuthenticityFlag: this.editForm.get(['currencyAuthenticityFlag'])!.value,
      currencyAuthenticityType: this.editForm.get(['currencyAuthenticityType'])!.value,
      currencyAuthenticityTypeDetails: this.editForm.get(['currencyAuthenticityTypeDetails'])!.value,
    };
  }
}
