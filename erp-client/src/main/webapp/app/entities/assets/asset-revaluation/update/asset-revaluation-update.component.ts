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
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { DealerService } from 'app/entities/people/dealer/service/dealer.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { DepreciationPeriodService } from 'app/entities/assets/depreciation-period/service/depreciation-period.service';
import { IAssetRegistration } from 'app/entities/assets/asset-registration/asset-registration.model';
import { AssetRegistrationService } from 'app/entities/assets/asset-registration/service/asset-registration.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';

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
        const today = dayjs().startOf('day');
        assetRevaluation.timeOfCreation = today;
      }

      this.updateForm(assetRevaluation);

      this.loadRelationshipsOptions();
    });
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
