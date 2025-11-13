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
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRouDepreciationRequest, RouDepreciationRequest } from '../rou-depreciation-request.model';
import { RouDepreciationRequestService } from '../service/rou-depreciation-request.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { depreciationProcessStatusTypes } from 'app/entities/enumerations/depreciation-process-status-types.model';

@Component({
  selector: 'jhi-rou-depreciation-request-update',
  templateUrl: './rou-depreciation-request-update.component.html',
})
export class RouDepreciationRequestUpdateComponent implements OnInit {
  isSaving = false;
  depreciationProcessStatusTypesValues = Object.keys(depreciationProcessStatusTypes);

  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    requisitionId: [null, [Validators.required]],
    timeOfRequest: [],
    depreciationProcessStatus: [],
    numberOfEnumeratedItems: [],
    invalidated: [],
    initiatedBy: [],
  });

  constructor(
    protected rouDepreciationRequestService: RouDepreciationRequestService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rouDepreciationRequest }) => {
      if (rouDepreciationRequest.id === undefined) {
        const today = dayjs().startOf('day');
        rouDepreciationRequest.timeOfRequest = today;
      }

      this.updateForm(rouDepreciationRequest);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rouDepreciationRequest = this.createFromForm();
    if (rouDepreciationRequest.id !== undefined) {
      this.subscribeToSaveResponse(this.rouDepreciationRequestService.update(rouDepreciationRequest));
    } else {
      this.subscribeToSaveResponse(this.rouDepreciationRequestService.create(rouDepreciationRequest));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRouDepreciationRequest>>): void {
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

  protected updateForm(rouDepreciationRequest: IRouDepreciationRequest): void {
    this.editForm.patchValue({
      id: rouDepreciationRequest.id,
      requisitionId: rouDepreciationRequest.requisitionId,
      timeOfRequest: rouDepreciationRequest.timeOfRequest ? rouDepreciationRequest.timeOfRequest.format(DATE_TIME_FORMAT) : null,
      depreciationProcessStatus: rouDepreciationRequest.depreciationProcessStatus,
      numberOfEnumeratedItems: rouDepreciationRequest.numberOfEnumeratedItems,
      invalidated: rouDepreciationRequest.invalidated,
      initiatedBy: rouDepreciationRequest.initiatedBy,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      rouDepreciationRequest.initiatedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(applicationUsers, this.editForm.get('initiatedBy')!.value)
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }

  protected createFromForm(): IRouDepreciationRequest {
    return {
      ...new RouDepreciationRequest(),
      id: this.editForm.get(['id'])!.value,
      requisitionId: this.editForm.get(['requisitionId'])!.value,
      timeOfRequest: this.editForm.get(['timeOfRequest'])!.value
        ? dayjs(this.editForm.get(['timeOfRequest'])!.value, DATE_TIME_FORMAT)
        : undefined,
      depreciationProcessStatus: this.editForm.get(['depreciationProcessStatus'])!.value,
      numberOfEnumeratedItems: this.editForm.get(['numberOfEnumeratedItems'])!.value,
      invalidated: this.editForm.get(['invalidated'])!.value,
      initiatedBy: this.editForm.get(['initiatedBy'])!.value,
    };
  }
}
