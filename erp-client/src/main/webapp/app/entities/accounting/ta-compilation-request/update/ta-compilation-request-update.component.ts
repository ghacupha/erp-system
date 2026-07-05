import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITACompilationRequest, TACompilationRequest } from '../ta-compilation-request.model';
import { TACompilationRequestService } from '../service/ta-compilation-request.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { compilationProcessStatusTypes } from 'app/entities/enumerations/compilation-process-status-types.model';

@Component({
  selector: 'jhi-ta-compilation-request-update',
  templateUrl: './ta-compilation-request-update.component.html',
})
export class TACompilationRequestUpdateComponent implements OnInit {
  isSaving = false;
  compilationProcessStatusTypesValues = Object.keys(compilationProcessStatusTypes);

  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    requisitionId: [null, [Validators.required]],
    timeOfRequest: [],
    compilationProcessStatus: [],
    numberOfEnumeratedItems: [],
    batchJobIdentifier: [null, [Validators.required]],
    compilationTime: [],
    invalidated: [],
    initiatedBy: [],
  });

  constructor(
    protected tACompilationRequestService: TACompilationRequestService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tACompilationRequest }) => {
      if (tACompilationRequest.id === undefined) {
        const today = dayjs().startOf('day');
        tACompilationRequest.timeOfRequest = today;
        tACompilationRequest.compilationTime = today;
      }

      this.updateForm(tACompilationRequest);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tACompilationRequest = this.createFromForm();
    if (tACompilationRequest.id !== undefined) {
      this.subscribeToSaveResponse(this.tACompilationRequestService.update(tACompilationRequest));
    } else {
      this.subscribeToSaveResponse(this.tACompilationRequestService.create(tACompilationRequest));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITACompilationRequest>>): void {
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

  protected updateForm(tACompilationRequest: ITACompilationRequest): void {
    this.editForm.patchValue({
      id: tACompilationRequest.id,
      requisitionId: tACompilationRequest.requisitionId,
      timeOfRequest: tACompilationRequest.timeOfRequest ? tACompilationRequest.timeOfRequest.format(DATE_TIME_FORMAT) : null,
      compilationProcessStatus: tACompilationRequest.compilationProcessStatus,
      numberOfEnumeratedItems: tACompilationRequest.numberOfEnumeratedItems,
      batchJobIdentifier: tACompilationRequest.batchJobIdentifier,
      compilationTime: tACompilationRequest.compilationTime ? tACompilationRequest.compilationTime.format(DATE_TIME_FORMAT) : null,
      invalidated: tACompilationRequest.invalidated,
      initiatedBy: tACompilationRequest.initiatedBy,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      tACompilationRequest.initiatedBy
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

  protected createFromForm(): ITACompilationRequest {
    return {
      ...new TACompilationRequest(),
      id: this.editForm.get(['id'])!.value,
      requisitionId: this.editForm.get(['requisitionId'])!.value,
      timeOfRequest: this.editForm.get(['timeOfRequest'])!.value
        ? dayjs(this.editForm.get(['timeOfRequest'])!.value, DATE_TIME_FORMAT)
        : undefined,
      compilationProcessStatus: this.editForm.get(['compilationProcessStatus'])!.value,
      numberOfEnumeratedItems: this.editForm.get(['numberOfEnumeratedItems'])!.value,
      batchJobIdentifier: this.editForm.get(['batchJobIdentifier'])!.value,
      compilationTime: this.editForm.get(['compilationTime'])!.value
        ? dayjs(this.editForm.get(['compilationTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      invalidated: this.editForm.get(['invalidated'])!.value,
      initiatedBy: this.editForm.get(['initiatedBy'])!.value,
    };
  }
}
