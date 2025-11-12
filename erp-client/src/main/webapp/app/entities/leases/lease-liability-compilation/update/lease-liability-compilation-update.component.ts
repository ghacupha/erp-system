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
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILeaseLiabilityCompilation, LeaseLiabilityCompilation } from '../lease-liability-compilation.model';
import { LeaseLiabilityCompilationService } from '../service/lease-liability-compilation.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';

@Component({
  selector: 'jhi-lease-liability-compilation-update',
  templateUrl: './lease-liability-compilation-update.component.html',
})
export class LeaseLiabilityCompilationUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    requestId: [null, [Validators.required]],
    timeOfRequest: [null, [Validators.required]],
    requestedBy: [],
  });

  constructor(
    protected leaseLiabilityCompilationService: LeaseLiabilityCompilationService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseLiabilityCompilation }) => {
      if (leaseLiabilityCompilation.id === undefined) {
        const today = dayjs().startOf('day');
        leaseLiabilityCompilation.timeOfRequest = today;
      }

      this.updateForm(leaseLiabilityCompilation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaseLiabilityCompilation = this.createFromForm();
    if (leaseLiabilityCompilation.id !== undefined) {
      this.subscribeToSaveResponse(this.leaseLiabilityCompilationService.update(leaseLiabilityCompilation));
    } else {
      this.subscribeToSaveResponse(this.leaseLiabilityCompilationService.create(leaseLiabilityCompilation));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaseLiabilityCompilation>>): void {
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

  protected updateForm(leaseLiabilityCompilation: ILeaseLiabilityCompilation): void {
    this.editForm.patchValue({
      id: leaseLiabilityCompilation.id,
      requestId: leaseLiabilityCompilation.requestId,
      timeOfRequest: leaseLiabilityCompilation.timeOfRequest ? leaseLiabilityCompilation.timeOfRequest.format(DATE_TIME_FORMAT) : null,
      requestedBy: leaseLiabilityCompilation.requestedBy,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      leaseLiabilityCompilation.requestedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(applicationUsers, this.editForm.get('requestedBy')!.value)
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }

  protected createFromForm(): ILeaseLiabilityCompilation {
    return {
      ...new LeaseLiabilityCompilation(),
      id: this.editForm.get(['id'])!.value,
      requestId: this.editForm.get(['requestId'])!.value,
      timeOfRequest: this.editForm.get(['timeOfRequest'])!.value
        ? dayjs(this.editForm.get(['timeOfRequest'])!.value, DATE_TIME_FORMAT)
        : undefined,
      requestedBy: this.editForm.get(['requestedBy'])!.value,
    };
  }
}
