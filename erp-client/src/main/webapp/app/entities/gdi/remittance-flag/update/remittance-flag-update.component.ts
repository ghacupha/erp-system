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

import { IRemittanceFlag, RemittanceFlag } from '../remittance-flag.model';
import { RemittanceFlagService } from '../service/remittance-flag.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { RemittanceTypeFlag } from 'app/entities/enumerations/remittance-type-flag.model';
import { RemittanceType } from 'app/entities/enumerations/remittance-type.model';

@Component({
  selector: 'jhi-remittance-flag-update',
  templateUrl: './remittance-flag-update.component.html',
})
export class RemittanceFlagUpdateComponent implements OnInit {
  isSaving = false;
  remittanceTypeFlagValues = Object.keys(RemittanceTypeFlag);
  remittanceTypeValues = Object.keys(RemittanceType);

  editForm = this.fb.group({
    id: [],
    remittanceTypeFlag: [null, [Validators.required]],
    remittanceType: [null, [Validators.required]],
    remittanceTypeDetails: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected remittanceFlagService: RemittanceFlagService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ remittanceFlag }) => {
      this.updateForm(remittanceFlag);
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
    const remittanceFlag = this.createFromForm();
    if (remittanceFlag.id !== undefined) {
      this.subscribeToSaveResponse(this.remittanceFlagService.update(remittanceFlag));
    } else {
      this.subscribeToSaveResponse(this.remittanceFlagService.create(remittanceFlag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRemittanceFlag>>): void {
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

  protected updateForm(remittanceFlag: IRemittanceFlag): void {
    this.editForm.patchValue({
      id: remittanceFlag.id,
      remittanceTypeFlag: remittanceFlag.remittanceTypeFlag,
      remittanceType: remittanceFlag.remittanceType,
      remittanceTypeDetails: remittanceFlag.remittanceTypeDetails,
    });
  }

  protected createFromForm(): IRemittanceFlag {
    return {
      ...new RemittanceFlag(),
      id: this.editForm.get(['id'])!.value,
      remittanceTypeFlag: this.editForm.get(['remittanceTypeFlag'])!.value,
      remittanceType: this.editForm.get(['remittanceType'])!.value,
      remittanceTypeDetails: this.editForm.get(['remittanceTypeDetails'])!.value,
    };
  }
}
