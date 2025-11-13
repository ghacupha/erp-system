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

import { INetBookValueEntry, NetBookValueEntry } from '../net-book-value-entry.model';
import { NetBookValueEntryService } from '../service/net-book-value-entry.service';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';
import { IAssetRegistration } from '../../asset-registration/asset-registration.model';
import { IAssetCategory } from '../../asset-category/asset-category.model';
import { IFiscalMonth } from '../../../erp-pages/fiscal-month/fiscal-month.model';
import { DepreciationPeriodService } from '../../depreciation-period/service/depreciation-period.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { AssetRegistrationService } from '../../asset-registration/service/asset-registration.service';
import { IDepreciationMethod } from '../../depreciation-method/depreciation-method.model';
import { FiscalMonthService } from '../../../erp-pages/fiscal-month/service/fiscal-month.service';
import { IDepreciationPeriod } from '../../depreciation-period/depreciation-period.model';
import { AssetCategoryService } from '../../asset-category/service/asset-category.service';
import { DepreciationMethodService } from '../../depreciation-method/service/depreciation-method.service';
import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';

@Component({
  selector: 'jhi-net-book-value-entry-update',
  templateUrl: './net-book-value-entry-update.component.html',
})
export class NetBookValueEntryUpdateComponent implements OnInit {
  isSaving = false;

  serviceOutletsSharedCollection: IServiceOutlet[] = [];
  depreciationPeriodsSharedCollection: IDepreciationPeriod[] = [];
  fiscalMonthsSharedCollection: IFiscalMonth[] = [];
  depreciationMethodsSharedCollection: IDepreciationMethod[] = [];
  assetRegistrationsSharedCollection: IAssetRegistration[] = [];
  assetCategoriesSharedCollection: IAssetCategory[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];

  editForm = this.fb.group({
    id: [],
    assetNumber: [],
    assetTag: [],
    assetDescription: [],
    nbvIdentifier: [null, [Validators.required]],
    compilationJobIdentifier: [],
    compilationBatchIdentifier: [],
    elapsedMonths: [],
    priorMonths: [],
    usefulLifeYears: [],
    netBookValueAmount: [],
    previousNetBookValueAmount: [],
    historicalCost: [],
    serviceOutlet: [],
    depreciationPeriod: [],
    fiscalMonth: [],
    depreciationMethod: [],
    assetRegistration: [],
    assetCategory: [],
    placeholders: [],
  });

  constructor(
    protected netBookValueEntryService: NetBookValueEntryService,
    protected serviceOutletService: ServiceOutletService,
    protected depreciationPeriodService: DepreciationPeriodService,
    protected fiscalMonthService: FiscalMonthService,
    protected depreciationMethodService: DepreciationMethodService,
    protected assetRegistrationService: AssetRegistrationService,
    protected assetCategoryService: AssetCategoryService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ netBookValueEntry }) => {
      this.updateForm(netBookValueEntry);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const netBookValueEntry = this.createFromForm();
    if (netBookValueEntry.id !== undefined) {
      this.subscribeToSaveResponse(this.netBookValueEntryService.update(netBookValueEntry));
    } else {
      this.subscribeToSaveResponse(this.netBookValueEntryService.create(netBookValueEntry));
    }
  }

  trackServiceOutletById(index: number, item: IServiceOutlet): number {
    return item.id!;
  }

  trackDepreciationPeriodById(index: number, item: IDepreciationPeriod): number {
    return item.id!;
  }

  trackFiscalMonthById(index: number, item: IFiscalMonth): number {
    return item.id!;
  }

  trackDepreciationMethodById(index: number, item: IDepreciationMethod): number {
    return item.id!;
  }

  trackAssetRegistrationById(index: number, item: IAssetRegistration): number {
    return item.id!;
  }

  trackAssetCategoryById(index: number, item: IAssetCategory): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INetBookValueEntry>>): void {
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

  protected updateForm(netBookValueEntry: INetBookValueEntry): void {
    this.editForm.patchValue({
      id: netBookValueEntry.id,
      assetNumber: netBookValueEntry.assetNumber,
      assetTag: netBookValueEntry.assetTag,
      assetDescription: netBookValueEntry.assetDescription,
      nbvIdentifier: netBookValueEntry.nbvIdentifier,
      compilationJobIdentifier: netBookValueEntry.compilationJobIdentifier,
      compilationBatchIdentifier: netBookValueEntry.compilationBatchIdentifier,
      elapsedMonths: netBookValueEntry.elapsedMonths,
      priorMonths: netBookValueEntry.priorMonths,
      usefulLifeYears: netBookValueEntry.usefulLifeYears,
      netBookValueAmount: netBookValueEntry.netBookValueAmount,
      previousNetBookValueAmount: netBookValueEntry.previousNetBookValueAmount,
      historicalCost: netBookValueEntry.historicalCost,
      serviceOutlet: netBookValueEntry.serviceOutlet,
      depreciationPeriod: netBookValueEntry.depreciationPeriod,
      fiscalMonth: netBookValueEntry.fiscalMonth,
      depreciationMethod: netBookValueEntry.depreciationMethod,
      assetRegistration: netBookValueEntry.assetRegistration,
      assetCategory: netBookValueEntry.assetCategory,
      placeholders: netBookValueEntry.placeholders,
    });

    this.serviceOutletsSharedCollection = this.serviceOutletService.addServiceOutletToCollectionIfMissing(
      this.serviceOutletsSharedCollection,
      netBookValueEntry.serviceOutlet
    );
    this.depreciationPeriodsSharedCollection = this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
      this.depreciationPeriodsSharedCollection,
      netBookValueEntry.depreciationPeriod
    );
    this.fiscalMonthsSharedCollection = this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(
      this.fiscalMonthsSharedCollection,
      netBookValueEntry.fiscalMonth
    );
    this.depreciationMethodsSharedCollection = this.depreciationMethodService.addDepreciationMethodToCollectionIfMissing(
      this.depreciationMethodsSharedCollection,
      netBookValueEntry.depreciationMethod
    );
    this.assetRegistrationsSharedCollection = this.assetRegistrationService.addAssetRegistrationToCollectionIfMissing(
      this.assetRegistrationsSharedCollection,
      netBookValueEntry.assetRegistration
    );
    this.assetCategoriesSharedCollection = this.assetCategoryService.addAssetCategoryToCollectionIfMissing(
      this.assetCategoriesSharedCollection,
      netBookValueEntry.assetCategory
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(netBookValueEntry.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.serviceOutletService
      .query()
      .pipe(map((res: HttpResponse<IServiceOutlet[]>) => res.body ?? []))
      .pipe(
        map((serviceOutlets: IServiceOutlet[]) =>
          this.serviceOutletService.addServiceOutletToCollectionIfMissing(serviceOutlets, this.editForm.get('serviceOutlet')!.value)
        )
      )
      .subscribe((serviceOutlets: IServiceOutlet[]) => (this.serviceOutletsSharedCollection = serviceOutlets));

    this.depreciationPeriodService
      .query()
      .pipe(map((res: HttpResponse<IDepreciationPeriod[]>) => res.body ?? []))
      .pipe(
        map((depreciationPeriods: IDepreciationPeriod[]) =>
          this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
            depreciationPeriods,
            this.editForm.get('depreciationPeriod')!.value
          )
        )
      )
      .subscribe((depreciationPeriods: IDepreciationPeriod[]) => (this.depreciationPeriodsSharedCollection = depreciationPeriods));

    this.fiscalMonthService
      .query()
      .pipe(map((res: HttpResponse<IFiscalMonth[]>) => res.body ?? []))
      .pipe(
        map((fiscalMonths: IFiscalMonth[]) =>
          this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(fiscalMonths, this.editForm.get('fiscalMonth')!.value)
        )
      )
      .subscribe((fiscalMonths: IFiscalMonth[]) => (this.fiscalMonthsSharedCollection = fiscalMonths));

    this.depreciationMethodService
      .query()
      .pipe(map((res: HttpResponse<IDepreciationMethod[]>) => res.body ?? []))
      .pipe(
        map((depreciationMethods: IDepreciationMethod[]) =>
          this.depreciationMethodService.addDepreciationMethodToCollectionIfMissing(
            depreciationMethods,
            this.editForm.get('depreciationMethod')!.value
          )
        )
      )
      .subscribe((depreciationMethods: IDepreciationMethod[]) => (this.depreciationMethodsSharedCollection = depreciationMethods));

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

    this.assetCategoryService
      .query()
      .pipe(map((res: HttpResponse<IAssetCategory[]>) => res.body ?? []))
      .pipe(
        map((assetCategories: IAssetCategory[]) =>
          this.assetCategoryService.addAssetCategoryToCollectionIfMissing(assetCategories, this.editForm.get('assetCategory')!.value)
        )
      )
      .subscribe((assetCategories: IAssetCategory[]) => (this.assetCategoriesSharedCollection = assetCategories));

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

  protected createFromForm(): INetBookValueEntry {
    return {
      ...new NetBookValueEntry(),
      id: this.editForm.get(['id'])!.value,
      assetNumber: this.editForm.get(['assetNumber'])!.value,
      assetTag: this.editForm.get(['assetTag'])!.value,
      assetDescription: this.editForm.get(['assetDescription'])!.value,
      nbvIdentifier: this.editForm.get(['nbvIdentifier'])!.value,
      compilationJobIdentifier: this.editForm.get(['compilationJobIdentifier'])!.value,
      compilationBatchIdentifier: this.editForm.get(['compilationBatchIdentifier'])!.value,
      elapsedMonths: this.editForm.get(['elapsedMonths'])!.value,
      priorMonths: this.editForm.get(['priorMonths'])!.value,
      usefulLifeYears: this.editForm.get(['usefulLifeYears'])!.value,
      netBookValueAmount: this.editForm.get(['netBookValueAmount'])!.value,
      previousNetBookValueAmount: this.editForm.get(['previousNetBookValueAmount'])!.value,
      historicalCost: this.editForm.get(['historicalCost'])!.value,
      serviceOutlet: this.editForm.get(['serviceOutlet'])!.value,
      depreciationPeriod: this.editForm.get(['depreciationPeriod'])!.value,
      fiscalMonth: this.editForm.get(['fiscalMonth'])!.value,
      depreciationMethod: this.editForm.get(['depreciationMethod'])!.value,
      assetRegistration: this.editForm.get(['assetRegistration'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
    };
  }
}
