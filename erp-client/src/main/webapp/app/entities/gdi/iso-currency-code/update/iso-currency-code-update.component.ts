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

import { IIsoCurrencyCode, IsoCurrencyCode } from '../iso-currency-code.model';
import { IsoCurrencyCodeService } from '../service/iso-currency-code.service';

@Component({
  selector: 'jhi-iso-currency-code-update',
  templateUrl: './iso-currency-code-update.component.html',
})
export class IsoCurrencyCodeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    alphabeticCode: [null, [Validators.required]],
    numericCode: [null, [Validators.required]],
    minorUnit: [null, [Validators.required]],
    currency: [null, [Validators.required]],
    country: [],
  });

  constructor(
    protected isoCurrencyCodeService: IsoCurrencyCodeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isoCurrencyCode }) => {
      this.updateForm(isoCurrencyCode);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const isoCurrencyCode = this.createFromForm();
    if (isoCurrencyCode.id !== undefined) {
      this.subscribeToSaveResponse(this.isoCurrencyCodeService.update(isoCurrencyCode));
    } else {
      this.subscribeToSaveResponse(this.isoCurrencyCodeService.create(isoCurrencyCode));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIsoCurrencyCode>>): void {
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

  protected updateForm(isoCurrencyCode: IIsoCurrencyCode): void {
    this.editForm.patchValue({
      id: isoCurrencyCode.id,
      alphabeticCode: isoCurrencyCode.alphabeticCode,
      numericCode: isoCurrencyCode.numericCode,
      minorUnit: isoCurrencyCode.minorUnit,
      currency: isoCurrencyCode.currency,
      country: isoCurrencyCode.country,
    });
  }

  protected createFromForm(): IIsoCurrencyCode {
    return {
      ...new IsoCurrencyCode(),
      id: this.editForm.get(['id'])!.value,
      alphabeticCode: this.editForm.get(['alphabeticCode'])!.value,
      numericCode: this.editForm.get(['numericCode'])!.value,
      minorUnit: this.editForm.get(['minorUnit'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      country: this.editForm.get(['country'])!.value,
    };
  }
}
