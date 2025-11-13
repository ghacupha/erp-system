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

import { ICountySubCountyCode, CountySubCountyCode } from '../county-sub-county-code.model';
import { CountySubCountyCodeService } from '../service/county-sub-county-code.service';

@Component({
  selector: 'jhi-county-sub-county-code-update',
  templateUrl: './county-sub-county-code-update.component.html',
})
export class CountySubCountyCodeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    subCountyCode: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(4), Validators.pattern('^\\d{4}$')]],
    subCountyName: [null, [Validators.required]],
    countyCode: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(2), Validators.pattern('^\\d{2}$')]],
    countyName: [null, [Validators.required]],
  });

  constructor(
    protected countySubCountyCodeService: CountySubCountyCodeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countySubCountyCode }) => {
      this.updateForm(countySubCountyCode);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const countySubCountyCode = this.createFromForm();
    if (countySubCountyCode.id !== undefined) {
      this.subscribeToSaveResponse(this.countySubCountyCodeService.update(countySubCountyCode));
    } else {
      this.subscribeToSaveResponse(this.countySubCountyCodeService.create(countySubCountyCode));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountySubCountyCode>>): void {
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

  protected updateForm(countySubCountyCode: ICountySubCountyCode): void {
    this.editForm.patchValue({
      id: countySubCountyCode.id,
      subCountyCode: countySubCountyCode.subCountyCode,
      subCountyName: countySubCountyCode.subCountyName,
      countyCode: countySubCountyCode.countyCode,
      countyName: countySubCountyCode.countyName,
    });
  }

  protected createFromForm(): ICountySubCountyCode {
    return {
      ...new CountySubCountyCode(),
      id: this.editForm.get(['id'])!.value,
      subCountyCode: this.editForm.get(['subCountyCode'])!.value,
      subCountyName: this.editForm.get(['subCountyName'])!.value,
      countyCode: this.editForm.get(['countyCode'])!.value,
      countyName: this.editForm.get(['countyName'])!.value,
    };
  }
}
