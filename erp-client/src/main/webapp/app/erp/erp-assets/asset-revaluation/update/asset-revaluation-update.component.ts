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

import { IAssetRevaluation, AssetRevaluation } from '../asset-revaluation.model';
import { AssetRevaluationService } from '../service/asset-revaluation.service';
import { IAssetRegistration } from '../../asset-registration/asset-registration.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { AssetRegistrationService } from '../../asset-registration/service/asset-registration.service';
import { DepreciationPeriodService } from '../../depreciation-period/service/depreciation-period.service';
import { IDepreciationPeriod } from '../../depreciation-period/depreciation-period.model';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { uuidv7 } from 'uuidv7';

@Component({
  selector: 'jhi-asset-revaluation-update',
  templateUrl: './asset-revaluation-update.component.html',
})
export class AssetRevaluationUpdateComponent implements OnInit {
  isSaving = false;

  dealersSharedCollection: IDealer[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  depreciationPeriodsSharedCollection: IDepreciationPeriod[] = [];
  assetRegistrationsSharedCollection: IAssetRegistration[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    devaluationAmount: [null, [Validators.required]],
    revaluationDate: [null, [Validators.required]],
    revaluationReferenceId: [],
    timeOfCreation: [],
    revaluer: [],
    createdBy: [],
    lastModifiedBy: [],
    lastAccessedBy: [],
    effectivePeriod: [null, Validators.required],
    revaluedAsset: [null, Validators.required],
    placeholders: [],
  });

  constructor(
    protected assetRevaluationService: AssetRevaluationService,
    protected dealerService: DealerService,
    protected applicationUserService: ApplicationUserService,
    protected depreciationPeriodService: DepreciationPeriodService,
    protected assetRegistrationService: AssetRegistrationService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetRevaluation }) => {
      if (assetRevaluation.id === undefined) {
        const today = dayjs();
        assetRevaluation.timeOfCreation = today;
        assetRevaluation.revaluationReferenceId = uuidv7();
      }

      this.updateForm(assetRevaluation);

      this.loadRelationshipsOptions();
    });
  }

  updateEffectivePeriod(update: IDepreciationPeriod): void {
    this.editForm.patchValue({
      effectivePeriod: update
    })
  }

  updateRevaluedAsset(updated: IAssetRegistration): void {
    this.editForm.patchValue({ revaluedAsset: updated });
  }

  updateRevaluer(updated: IDealer): void {
    this.editForm.patchValue( { revaluer: updated });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetRevaluation = this.createFromForm();
    if (assetRevaluation.id !== undefined) {
      this.subscribeToSaveResponse(this.assetRevaluationService.update(assetRevaluation));
    } else {
      this.subscribeToSaveResponse(this.assetRevaluationService.create(assetRevaluation));
    }
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  trackDepreciationPeriodById(index: number, item: IDepreciationPeriod): number {
    return item.id!;
  }

  trackAssetRegistrationById(index: number, item: IAssetRegistration): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  getSelectedPlaceholder(option: IPlaceholder, selectedVals?: IPlaceholder[]): IPlaceholder {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetRevaluation>>): void {
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

  protected updateForm(assetRevaluation: IAssetRevaluation): void {
    this.editForm.patchValue({
      id: assetRevaluation.id,
      description: assetRevaluation.description,
      devaluationAmount: assetRevaluation.devaluationAmount,
      revaluationDate: assetRevaluation.revaluationDate,
      revaluationReferenceId: assetRevaluation.revaluationReferenceId,
      timeOfCreation: assetRevaluation.timeOfCreation ? assetRevaluation.timeOfCreation.format(DATE_TIME_FORMAT) : null,
      revaluer: assetRevaluation.revaluer,
      createdBy: assetRevaluation.createdBy,
      lastModifiedBy: assetRevaluation.lastModifiedBy,
      lastAccessedBy: assetRevaluation.lastAccessedBy,
      effectivePeriod: assetRevaluation.effectivePeriod,
      revaluedAsset: assetRevaluation.revaluedAsset,
      placeholders: assetRevaluation.placeholders,
    });

    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      assetRevaluation.revaluer
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      assetRevaluation.createdBy,
      assetRevaluation.lastModifiedBy,
      assetRevaluation.lastAccessedBy
    );
    this.depreciationPeriodsSharedCollection = this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
      this.depreciationPeriodsSharedCollection,
      assetRevaluation.effectivePeriod
    );
    this.assetRegistrationsSharedCollection = this.assetRegistrationService.addAssetRegistrationToCollectionIfMissing(
      this.assetRegistrationsSharedCollection,
      assetRevaluation.revaluedAsset
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(assetRevaluation.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing(dealers, this.editForm.get('revaluer')!.value)))
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

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
            this.editForm.get('revaluedAsset')!.value
          )
        )
      )
      .subscribe((assetRegistrations: IAssetRegistration[]) => (this.assetRegistrationsSharedCollection = assetRegistrations));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  }

  protected createFromForm(): IAssetRevaluation {
    return {
      ...new AssetRevaluation(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      devaluationAmount: this.editForm.get(['devaluationAmount'])!.value,
      revaluationDate: this.editForm.get(['revaluationDate'])!.value,
      revaluationReferenceId: this.editForm.get(['revaluationReferenceId'])!.value,
      timeOfCreation: this.editForm.get(['timeOfCreation'])!.value
        ? dayjs(this.editForm.get(['timeOfCreation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      revaluer: this.editForm.get(['revaluer'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastAccessedBy: this.editForm.get(['lastAccessedBy'])!.value,
      effectivePeriod: this.editForm.get(['effectivePeriod'])!.value,
      revaluedAsset: this.editForm.get(['revaluedAsset'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
