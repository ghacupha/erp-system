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

import { ISnaSectorCode, SnaSectorCode } from '../sna-sector-code.model';
import { SnaSectorCodeService } from '../service/sna-sector-code.service';

@Component({
  selector: 'jhi-sna-sector-code-update',
  templateUrl: './sna-sector-code-update.component.html',
})
export class SnaSectorCodeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    sectorTypeCode: [null, [Validators.required]],
    mainSectorCode: [],
    mainSectorTypeName: [],
    subSectorCode: [],
    subSectorName: [],
    subSubSectorCode: [],
    subSubSectorName: [],
  });

  constructor(protected snaSectorCodeService: SnaSectorCodeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ snaSectorCode }) => {
      this.updateForm(snaSectorCode);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const snaSectorCode = this.createFromForm();
    if (snaSectorCode.id !== undefined) {
      this.subscribeToSaveResponse(this.snaSectorCodeService.update(snaSectorCode));
    } else {
      this.subscribeToSaveResponse(this.snaSectorCodeService.create(snaSectorCode));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISnaSectorCode>>): void {
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

  protected updateForm(snaSectorCode: ISnaSectorCode): void {
    this.editForm.patchValue({
      id: snaSectorCode.id,
      sectorTypeCode: snaSectorCode.sectorTypeCode,
      mainSectorCode: snaSectorCode.mainSectorCode,
      mainSectorTypeName: snaSectorCode.mainSectorTypeName,
      subSectorCode: snaSectorCode.subSectorCode,
      subSectorName: snaSectorCode.subSectorName,
      subSubSectorCode: snaSectorCode.subSubSectorCode,
      subSubSectorName: snaSectorCode.subSubSectorName,
    });
  }

  protected createFromForm(): ISnaSectorCode {
    return {
      ...new SnaSectorCode(),
      id: this.editForm.get(['id'])!.value,
      sectorTypeCode: this.editForm.get(['sectorTypeCode'])!.value,
      mainSectorCode: this.editForm.get(['mainSectorCode'])!.value,
      mainSectorTypeName: this.editForm.get(['mainSectorTypeName'])!.value,
      subSectorCode: this.editForm.get(['subSectorCode'])!.value,
      subSectorName: this.editForm.get(['subSectorName'])!.value,
      subSubSectorCode: this.editForm.get(['subSubSectorCode'])!.value,
      subSubSectorName: this.editForm.get(['subSubSectorName'])!.value,
    };
  }
}
