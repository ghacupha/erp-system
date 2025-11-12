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

import { IManagementMemberType, ManagementMemberType } from '../management-member-type.model';
import { ManagementMemberTypeService } from '../service/management-member-type.service';

@Component({
  selector: 'jhi-management-member-type-update',
  templateUrl: './management-member-type-update.component.html',
})
export class ManagementMemberTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    managementMemberTypeCode: [null, [Validators.required]],
    managementMemberType: [null, [Validators.required]],
  });

  constructor(
    protected managementMemberTypeService: ManagementMemberTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ managementMemberType }) => {
      this.updateForm(managementMemberType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const managementMemberType = this.createFromForm();
    if (managementMemberType.id !== undefined) {
      this.subscribeToSaveResponse(this.managementMemberTypeService.update(managementMemberType));
    } else {
      this.subscribeToSaveResponse(this.managementMemberTypeService.create(managementMemberType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManagementMemberType>>): void {
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

  protected updateForm(managementMemberType: IManagementMemberType): void {
    this.editForm.patchValue({
      id: managementMemberType.id,
      managementMemberTypeCode: managementMemberType.managementMemberTypeCode,
      managementMemberType: managementMemberType.managementMemberType,
    });
  }

  protected createFromForm(): IManagementMemberType {
    return {
      ...new ManagementMemberType(),
      id: this.editForm.get(['id'])!.value,
      managementMemberTypeCode: this.editForm.get(['managementMemberTypeCode'])!.value,
      managementMemberType: this.editForm.get(['managementMemberType'])!.value,
    };
  }
}
