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

import { IEmploymentTerms, EmploymentTerms } from '../employment-terms.model';
import { EmploymentTermsService } from '../service/employment-terms.service';

@Component({
  selector: 'jhi-employment-terms-update',
  templateUrl: './employment-terms-update.component.html',
})
export class EmploymentTermsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    employmentTermsCode: [null, [Validators.required]],
    employmentTermsStatus: [null, [Validators.required]],
  });

  constructor(
    protected employmentTermsService: EmploymentTermsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employmentTerms }) => {
      this.updateForm(employmentTerms);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employmentTerms = this.createFromForm();
    if (employmentTerms.id !== undefined) {
      this.subscribeToSaveResponse(this.employmentTermsService.update(employmentTerms));
    } else {
      this.subscribeToSaveResponse(this.employmentTermsService.create(employmentTerms));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmploymentTerms>>): void {
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

  protected updateForm(employmentTerms: IEmploymentTerms): void {
    this.editForm.patchValue({
      id: employmentTerms.id,
      employmentTermsCode: employmentTerms.employmentTermsCode,
      employmentTermsStatus: employmentTerms.employmentTermsStatus,
    });
  }

  protected createFromForm(): IEmploymentTerms {
    return {
      ...new EmploymentTerms(),
      id: this.editForm.get(['id'])!.value,
      employmentTermsCode: this.editForm.get(['employmentTermsCode'])!.value,
      employmentTermsStatus: this.editForm.get(['employmentTermsStatus'])!.value,
    };
  }
}
