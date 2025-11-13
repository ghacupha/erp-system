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

import { IAssetDisposal, AssetDisposal } from '../asset-disposal.model';
import { AssetDisposalService } from '../service/asset-disposal.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { DepreciationPeriodService } from 'app/entities/assets/depreciation-period/service/depreciation-period.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IAssetRegistration } from 'app/entities/assets/asset-registration/asset-registration.model';
import { AssetRegistrationService } from 'app/entities/assets/asset-registration/service/asset-registration.service';

@Component({
  selector: 'jhi-asset-disposal-update',
  templateUrl: './asset-disposal-update.component.html',
})
export class AssetDisposalUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUser[] = [];
  depreciationPeriodsSharedCollection: IDepreciationPeriod[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  assetDisposedsCollection: IAssetRegistration[] = [];

  editForm = this.fb.group({
    id: [],
    assetDisposalReference: [],
    description: [],
    assetCost: [],
    historicalCost: [],
    accruedDepreciation: [null, [Validators.required, Validators.min(0)]],
    netBookValue: [null, [Validators.required, Validators.min(0)]],
    decommissioningDate: [],
    disposalDate: [null, [Validators.required]],
    dormant: [],
    createdBy: [],
    modifiedBy: [],
    lastAccessedBy: [],
    effectivePeriod: [null, Validators.required],
    placeholders: [],
    assetDisposed: [null, Validators.required],
  });

  constructor(
    protected assetDisposalService: AssetDisposalService,
    protected applicationUserService: ApplicationUserService,
    protected depreciationPeriodService: DepreciationPeriodService,
    protected placeholderService: PlaceholderService,
    protected assetRegistrationService: AssetRegistrationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetDisposal }) => {
      this.updateForm(assetDisposal);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetDisposal = this.createFromForm();
    if (assetDisposal.id !== undefined) {
      this.subscribeToSaveResponse(this.assetDisposalService.update(assetDisposal));
    } else {
      this.subscribeToSaveResponse(this.assetDisposalService.create(assetDisposal));
    }
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  trackDepreciationPeriodById(index: number, item: IDepreciationPeriod): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackAssetRegistrationById(index: number, item: IAssetRegistration): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetDisposal>>): void {
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

  protected updateForm(assetDisposal: IAssetDisposal): void {
    this.editForm.patchValue({
      id: assetDisposal.id,
      assetDisposalReference: assetDisposal.assetDisposalReference,
      description: assetDisposal.description,
      assetCost: assetDisposal.assetCost,
      historicalCost: assetDisposal.historicalCost,
      accruedDepreciation: assetDisposal.accruedDepreciation,
      netBookValue: assetDisposal.netBookValue,
      decommissioningDate: assetDisposal.decommissioningDate,
      disposalDate: assetDisposal.disposalDate,
      dormant: assetDisposal.dormant,
      createdBy: assetDisposal.createdBy,
      modifiedBy: assetDisposal.modifiedBy,
      lastAccessedBy: assetDisposal.lastAccessedBy,
      effectivePeriod: assetDisposal.effectivePeriod,
      placeholders: assetDisposal.placeholders,
      assetDisposed: assetDisposal.assetDisposed,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      assetDisposal.createdBy,
      assetDisposal.modifiedBy,
      assetDisposal.lastAccessedBy
    );
    this.depreciationPeriodsSharedCollection = this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
      this.depreciationPeriodsSharedCollection,
      assetDisposal.effectivePeriod
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(assetDisposal.placeholders ?? [])
    );
    this.assetDisposedsCollection = this.assetRegistrationService.addAssetRegistrationToCollectionIfMissing(
      this.assetDisposedsCollection,
      assetDisposal.assetDisposed
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(
            applicationUsers,
            this.editForm.get('createdBy')!.value,
            this.editForm.get('modifiedBy')!.value,
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

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.assetRegistrationService
      .query({ 'assetDisposalId.specified': 'false' })
      .pipe(map((res: HttpResponse<IAssetRegistration[]>) => res.body ?? []))
      .pipe(
        map((assetRegistrations: IAssetRegistration[]) =>
          this.assetRegistrationService.addAssetRegistrationToCollectionIfMissing(
            assetRegistrations,
            this.editForm.get('assetDisposed')!.value
          )
        )
      )
      .subscribe((assetRegistrations: IAssetRegistration[]) => (this.assetDisposedsCollection = assetRegistrations));
  }

  protected createFromForm(): IAssetDisposal {
    return {
      ...new AssetDisposal(),
      id: this.editForm.get(['id'])!.value,
      assetDisposalReference: this.editForm.get(['assetDisposalReference'])!.value,
      description: this.editForm.get(['description'])!.value,
      assetCost: this.editForm.get(['assetCost'])!.value,
      historicalCost: this.editForm.get(['historicalCost'])!.value,
      accruedDepreciation: this.editForm.get(['accruedDepreciation'])!.value,
      netBookValue: this.editForm.get(['netBookValue'])!.value,
      decommissioningDate: this.editForm.get(['decommissioningDate'])!.value,
      disposalDate: this.editForm.get(['disposalDate'])!.value,
      dormant: this.editForm.get(['dormant'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      modifiedBy: this.editForm.get(['modifiedBy'])!.value,
      lastAccessedBy: this.editForm.get(['lastAccessedBy'])!.value,
      effectivePeriod: this.editForm.get(['effectivePeriod'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      assetDisposed: this.editForm.get(['assetDisposed'])!.value,
    };
  }
}
