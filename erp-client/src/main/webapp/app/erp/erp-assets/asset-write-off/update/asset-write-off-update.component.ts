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

import { IAssetWriteOff, AssetWriteOff } from '../asset-write-off.model';
import { AssetWriteOffService } from '../service/asset-write-off.service';
import { uuidv7 } from 'uuidv7';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IAssetRegistration } from '../../asset-registration/asset-registration.model';
import { AssetRegistrationService } from '../../asset-registration/service/asset-registration.service';
import { DepreciationPeriodService } from '../../depreciation-period/service/depreciation-period.service';
import { IDepreciationPeriod } from '../../depreciation-period/depreciation-period.model';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

@Component({
  selector: 'jhi-asset-write-off-update',
  templateUrl: './asset-write-off-update.component.html',
})
export class AssetWriteOffUpdateComponent implements OnInit {
  isSaving = false;

  applicationUsersSharedCollection: IApplicationUser[] = [];
  depreciationPeriodsSharedCollection: IDepreciationPeriod[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  assetWrittenOffsCollection: IAssetRegistration[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    writeOffAmount: [null, [Validators.required, Validators.min(0)]],
    writeOffDate: [null, [Validators.required]],
    writeOffReferenceId: [],
    createdBy: [],
    modifiedBy: [],
    lastAccessedBy: [],
    effectivePeriod: [null, Validators.required],
    placeholders: [],
    assetWrittenOff: [null, Validators.required],
  });

  constructor(
    protected assetWriteOffService: AssetWriteOffService,
    protected applicationUserService: ApplicationUserService,
    protected depreciationPeriodService: DepreciationPeriodService,
    protected placeholderService: PlaceholderService,
    protected assetRegistrationService: AssetRegistrationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assetWriteOff }) => {

      assetWriteOff.writeOffReferenceId = uuidv7();

      this.updateForm(assetWriteOff);

      this.loadRelationshipsOptions();
    });
  }

  updateEffectivePeriod(update: IDepreciationPeriod): void {
    this.editForm.patchValue({
      effectivePeriod: update
    })
  }

  updatePlaceholders(updated: IPlaceholder[]): void {
    this.editForm.patchValue({ placeholders: [...updated] });
  }

  updateAssetWrittenOff(updated: IAssetRegistration): void {
    this.editForm.patchValue({ assetWrittenOff: updated });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assetWriteOff = this.createFromForm();
    if (assetWriteOff.id !== undefined) {
      this.subscribeToSaveResponse(this.assetWriteOffService.update(assetWriteOff));
    } else {
      this.subscribeToSaveResponse(this.assetWriteOffService.create(assetWriteOff));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssetWriteOff>>): void {
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

  protected updateForm(assetWriteOff: IAssetWriteOff): void {
    this.editForm.patchValue({
      id: assetWriteOff.id,
      description: assetWriteOff.description,
      writeOffAmount: assetWriteOff.writeOffAmount,
      writeOffDate: assetWriteOff.writeOffDate,
      writeOffReferenceId: assetWriteOff.writeOffReferenceId,
      createdBy: assetWriteOff.createdBy,
      modifiedBy: assetWriteOff.modifiedBy,
      lastAccessedBy: assetWriteOff.lastAccessedBy,
      effectivePeriod: assetWriteOff.effectivePeriod,
      placeholders: assetWriteOff.placeholders,
      assetWrittenOff: assetWriteOff.assetWrittenOff,
    });

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      assetWriteOff.createdBy,
      assetWriteOff.modifiedBy,
      assetWriteOff.lastAccessedBy
    );
    this.depreciationPeriodsSharedCollection = this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
      this.depreciationPeriodsSharedCollection,
      assetWriteOff.effectivePeriod
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(assetWriteOff.placeholders ?? [])
    );
    this.assetWrittenOffsCollection = this.assetRegistrationService.addAssetRegistrationToCollectionIfMissing(
      this.assetWrittenOffsCollection,
      assetWriteOff.assetWrittenOff
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
      .query({ 'assetWriteOffId.specified': 'false' })
      .pipe(map((res: HttpResponse<IAssetRegistration[]>) => res.body ?? []))
      .pipe(
        map((assetRegistrations: IAssetRegistration[]) =>
          this.assetRegistrationService.addAssetRegistrationToCollectionIfMissing(
            assetRegistrations,
            this.editForm.get('assetWrittenOff')!.value
          )
        )
      )
      .subscribe((assetRegistrations: IAssetRegistration[]) => (this.assetWrittenOffsCollection = assetRegistrations));
  }

  protected createFromForm(): IAssetWriteOff {
    return {
      ...new AssetWriteOff(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      writeOffAmount: this.editForm.get(['writeOffAmount'])!.value,
      writeOffDate: this.editForm.get(['writeOffDate'])!.value,
      writeOffReferenceId: this.editForm.get(['writeOffReferenceId'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      modifiedBy: this.editForm.get(['modifiedBy'])!.value,
      lastAccessedBy: this.editForm.get(['lastAccessedBy'])!.value,
      effectivePeriod: this.editForm.get(['effectivePeriod'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      assetWrittenOff: this.editForm.get(['assetWrittenOff'])!.value,
    };
  }
}
