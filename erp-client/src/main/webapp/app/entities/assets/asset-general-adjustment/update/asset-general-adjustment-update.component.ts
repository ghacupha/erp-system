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
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAssetGeneralAdjustment, AssetGeneralAdjustment } from '../asset-general-adjustment.model';
import { AssetGeneralAdjustmentService } from '../service/asset-general-adjustment.service';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { DepreciationPeriodService } from 'app/entities/assets/depreciation-period/service/depreciation-period.service';
import { IAssetRegistration } from 'app/entities/assets/asset-registration/asset-registration.model';
import { AssetRegistrationService } from 'app/entities/assets/asset-registration/service/asset-registration.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-asset-general-adjustment-update',
  templateUrl: './asset-general-adjustment-update.component.html',
})
export class AssetGeneralAdjustmentUpdateComponent implements OnInit {
  isSaving = false;

  depreciationPeriodsSharedCollection: IDepreciationPeriod[] = [];
  assetRegistrationsSharedCollection: IAssetRegistration[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    devaluationAmount: [null, [Validators.required]],
    adjustmentDate: [null, [Validators.required]],
    timeOfCreation: [null, [Validators.required]],
    adjustmentReferenceId: [null, [Validators.required]],
    effectivePeriod: [null, Validators.required],
    assetRegistration: [null, Validators.required],
    createdBy: [],
    lastModifiedBy: [],
    lastAccessedBy: [],
    placeholder: [],
  });

  constructor(
    protected assetGeneralAdjustmentService: AssetGeneralAdjustmentService,
    protected depreciationPeriodService: DepreciationPeriodService,
    protected assetRegistrationService: AssetRegistrationService,
    protected applicationUserService: ApplicationUserService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetGeneralAdjustment }) => {
      if (assetGeneralAdjustment.id === undefined) {
        const today = dayjs().startOf('day');
        assetGeneralAdjustment.timeOfCreation = today;
      }

      this.updateForm(assetGeneralAdjustment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetGeneralAdjustment = this.createFromForm();
    if (assetGeneralAdjustment.id !== undefined) {
      this.subscribeToSaveResponse(this.assetGeneralAdjustmentService.update(assetGeneralAdjustment));
    } else {
      this.subscribeToSaveResponse(this.assetGeneralAdjustmentService.create(assetGeneralAdjustment));
    }
  }

  trackDepreciationPeriodById(index: number, item: IDepreciationPeriod): number {
    return item.id!;
  }

  trackAssetRegistrationById(index: number, item: IAssetRegistration): number {
    return item.id!;
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetGeneralAdjustment>>): void {
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

  protected updateForm(assetGeneralAdjustment: IAssetGeneralAdjustment): void {
    this.editForm.patchValue({
      id: assetGeneralAdjustment.id,
      description: assetGeneralAdjustment.description,
      devaluationAmount: assetGeneralAdjustment.devaluationAmount,
      adjustmentDate: assetGeneralAdjustment.adjustmentDate,
      timeOfCreation: assetGeneralAdjustment.timeOfCreation ? assetGeneralAdjustment.timeOfCreation.format(DATE_TIME_FORMAT) : null,
      adjustmentReferenceId: assetGeneralAdjustment.adjustmentReferenceId,
      effectivePeriod: assetGeneralAdjustment.effectivePeriod,
      assetRegistration: assetGeneralAdjustment.assetRegistration,
      createdBy: assetGeneralAdjustment.createdBy,
      lastModifiedBy: assetGeneralAdjustment.lastModifiedBy,
      lastAccessedBy: assetGeneralAdjustment.lastAccessedBy,
      placeholder: assetGeneralAdjustment.placeholder,
    });

    this.depreciationPeriodsSharedCollection = this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
      this.depreciationPeriodsSharedCollection,
      assetGeneralAdjustment.effectivePeriod
    );
    this.assetRegistrationsSharedCollection = this.assetRegistrationService.addAssetRegistrationToCollectionIfMissing(
      this.assetRegistrationsSharedCollection,
      assetGeneralAdjustment.assetRegistration
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      assetGeneralAdjustment.createdBy,
      assetGeneralAdjustment.lastModifiedBy,
      assetGeneralAdjustment.lastAccessedBy
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      assetGeneralAdjustment.placeholder
    );
  }

  protected loadRelationshipsOptions(): void {
    this.depreciationPeriodService
      .query()
      .pipe(map((res: HttpResponse<IDepreciationPeriod[]>) => res.body ?? []))
      .pipe(
        map((depreciationPeriods: IDepreciationPeriod[]) =>
          this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
            depreciationPeriods,
            this.editForm.get('effectivePeriod')!.value
          )
        )
      )
      .subscribe((depreciationPeriods: IDepreciationPeriod[]) => (this.depreciationPeriodsSharedCollection = depreciationPeriods));

    this.assetRegistrationService
      .query()
      .pipe(map((res: HttpResponse<IAssetRegistration[]>) => res.body ?? []))
      .pipe(
        map((assetRegistrations: IAssetRegistration[]) =>
          this.assetRegistrationService.addAssetRegistrationToCollectionIfMissing(
            assetRegistrations,
            this.editForm.get('assetRegistration')!.value
          )
        )
      )
      .subscribe((assetRegistrations: IAssetRegistration[]) => (this.assetRegistrationsSharedCollection = assetRegistrations));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(
            applicationUsers,
            this.editForm.get('createdBy')!.value,
            this.editForm.get('lastModifiedBy')!.value,
            this.editForm.get('lastAccessedBy')!.value
          )
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, this.editForm.get('placeholder')!.value)
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  }

  protected createFromForm(): IAssetGeneralAdjustment {
    return {
      ...new AssetGeneralAdjustment(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      devaluationAmount: this.editForm.get(['devaluationAmount'])!.value,
      adjustmentDate: this.editForm.get(['adjustmentDate'])!.value,
      timeOfCreation: this.editForm.get(['timeOfCreation'])!.value
        ? dayjs(this.editForm.get(['timeOfCreation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      adjustmentReferenceId: this.editForm.get(['adjustmentReferenceId'])!.value,
      effectivePeriod: this.editForm.get(['effectivePeriod'])!.value,
      assetRegistration: this.editForm.get(['assetRegistration'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastAccessedBy: this.editForm.get(['lastAccessedBy'])!.value,
      placeholder: this.editForm.get(['placeholder'])!.value,
    };
  }
}
