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
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDepreciationEntry, DepreciationEntry } from '../depreciation-entry.model';
import { DepreciationEntryService } from '../service/depreciation-entry.service';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';
import { IAssetCategory } from '../../asset-category/asset-category.model';
import { IAssetRegistration } from '../../asset-registration/asset-registration.model';
import { IFiscalMonth } from '../../../erp-pages/fiscal-month/fiscal-month.model';
import { DepreciationPeriodService } from '../../depreciation-period/service/depreciation-period.service';
import { FiscalYearService } from '../../../erp-pages/fiscal-year/service/fiscal-year.service';
import { IFiscalYear } from '../../../erp-pages/fiscal-year/fiscal-year.model';
import { IFiscalQuarter } from '../../../erp-pages/fiscal-quarter/fiscal-quarter.model';
import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';
import { AssetRegistrationService } from '../../asset-registration/service/asset-registration.service';
import { IDepreciationMethod } from '../../depreciation-method/depreciation-method.model';
import { FiscalMonthService } from '../../../erp-pages/fiscal-month/service/fiscal-month.service';
import { IDepreciationPeriod } from '../../depreciation-period/depreciation-period.model';
import { AssetCategoryService } from '../../asset-category/service/asset-category.service';
import { FiscalQuarterService } from '../../../erp-pages/fiscal-quarter/service/fiscal-quarter.service';
import { DepreciationMethodService } from '../../depreciation-method/service/depreciation-method.service';
import { IDepreciationBatchSequence } from '../../depreciation-batch-sequence/depreciation-batch-sequence.model';
import { IDepreciationJob } from '../../depreciation-job/depreciation-job.model';
import { DepreciationJobService } from '../../depreciation-job/service/depreciation-job.service';
import { DepreciationBatchSequenceService } from '../../depreciation-batch-sequence/service/depreciation-batch-sequence.service';

@Component({
  selector: 'jhi-depreciation-entry-update',
  templateUrl: './depreciation-entry-update.component.html',
})
export class DepreciationEntryUpdateComponent implements OnInit {
  isSaving = false;

  serviceOutletsSharedCollection: IServiceOutlet[] = [];
  assetCategoriesSharedCollection: IAssetCategory[] = [];
  depreciationMethodsSharedCollection: IDepreciationMethod[] = [];
  assetRegistrationsSharedCollection: IAssetRegistration[] = [];
  depreciationPeriodsSharedCollection: IDepreciationPeriod[] = [];
  fiscalMonthsSharedCollection: IFiscalMonth[] = [];
  fiscalQuartersSharedCollection: IFiscalQuarter[] = [];
  fiscalYearsSharedCollection: IFiscalYear[] = [];
  depreciationJobsSharedCollection: IDepreciationJob[] = [];
  depreciationBatchSequencesSharedCollection: IDepreciationBatchSequence[] = [];

  editForm = this.fb.group({
    id: [],
    postedAt: [],
    depreciationAmount: [],
    assetNumber: [],
    batchSequenceNumber: [],
    processedItems: [],
    totalItemsProcessed: [],
    serviceOutlet: [],
    assetCategory: [],
    depreciationMethod: [],
    assetRegistration: [],
    depreciationPeriod: [],
    fiscalMonth: [],
    fiscalQuarter: [],
    fiscalYear: [],
    depreciationJob: [],
    depreciationBatchSequence: [],
  });

  constructor(
    protected depreciationEntryService: DepreciationEntryService,
    protected serviceOutletService: ServiceOutletService,
    protected assetCategoryService: AssetCategoryService,
    protected depreciationMethodService: DepreciationMethodService,
    protected assetRegistrationService: AssetRegistrationService,
    protected depreciationPeriodService: DepreciationPeriodService,
    protected fiscalMonthService: FiscalMonthService,
    protected fiscalQuarterService: FiscalQuarterService,
    protected fiscalYearService: FiscalYearService,
    protected depreciationJobService: DepreciationJobService,
    protected depreciationBatchSequenceService: DepreciationBatchSequenceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depreciationEntry }) => {
      if (depreciationEntry.id === undefined) {
        const today = dayjs().startOf('day');
        depreciationEntry.postedAt = today;
      }

      this.updateForm(depreciationEntry);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const depreciationEntry = this.createFromForm();
    if (depreciationEntry.id !== undefined) {
      this.subscribeToSaveResponse(this.depreciationEntryService.update(depreciationEntry));
    } else {
      this.subscribeToSaveResponse(this.depreciationEntryService.create(depreciationEntry));
    }
  }

  trackServiceOutletById(index: number, item: IServiceOutlet): number {
    return item.id!;
  }

  trackAssetCategoryById(index: number, item: IAssetCategory): number {
    return item.id!;
  }

  trackDepreciationMethodById(index: number, item: IDepreciationMethod): number {
    return item.id!;
  }

  trackAssetRegistrationById(index: number, item: IAssetRegistration): number {
    return item.id!;
  }

  trackDepreciationPeriodById(index: number, item: IDepreciationPeriod): number {
    return item.id!;
  }

  trackFiscalMonthById(index: number, item: IFiscalMonth): number {
    return item.id!;
  }

  trackFiscalQuarterById(index: number, item: IFiscalQuarter): number {
    return item.id!;
  }

  trackFiscalYearById(index: number, item: IFiscalYear): number {
    return item.id!;
  }

  trackDepreciationJobById(index: number, item: IDepreciationJob): number {
    return item.id!;
  }

  trackDepreciationBatchSequenceById(index: number, item: IDepreciationBatchSequence): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepreciationEntry>>): void {
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

  protected updateForm(depreciationEntry: IDepreciationEntry): void {
    this.editForm.patchValue({
      id: depreciationEntry.id,
      postedAt: depreciationEntry.postedAt ? depreciationEntry.postedAt.format(DATE_TIME_FORMAT) : null,
      depreciationAmount: depreciationEntry.depreciationAmount,
      assetNumber: depreciationEntry.assetNumber,
      batchSequenceNumber: depreciationEntry.batchSequenceNumber,
      processedItems: depreciationEntry.processedItems,
      totalItemsProcessed: depreciationEntry.totalItemsProcessed,
      serviceOutlet: depreciationEntry.serviceOutlet,
      assetCategory: depreciationEntry.assetCategory,
      depreciationMethod: depreciationEntry.depreciationMethod,
      assetRegistration: depreciationEntry.assetRegistration,
      depreciationPeriod: depreciationEntry.depreciationPeriod,
      fiscalMonth: depreciationEntry.fiscalMonth,
      fiscalQuarter: depreciationEntry.fiscalQuarter,
      fiscalYear: depreciationEntry.fiscalYear,
      depreciationJob: depreciationEntry.depreciationJob,
      depreciationBatchSequence: depreciationEntry.depreciationBatchSequence,
    });

    this.serviceOutletsSharedCollection = this.serviceOutletService.addServiceOutletToCollectionIfMissing(
      this.serviceOutletsSharedCollection,
      depreciationEntry.serviceOutlet
    );
    this.assetCategoriesSharedCollection = this.assetCategoryService.addAssetCategoryToCollectionIfMissing(
      this.assetCategoriesSharedCollection,
      depreciationEntry.assetCategory
    );
    this.depreciationMethodsSharedCollection = this.depreciationMethodService.addDepreciationMethodToCollectionIfMissing(
      this.depreciationMethodsSharedCollection,
      depreciationEntry.depreciationMethod
    );
    this.assetRegistrationsSharedCollection = this.assetRegistrationService.addAssetRegistrationToCollectionIfMissing(
      this.assetRegistrationsSharedCollection,
      depreciationEntry.assetRegistration
    );
    this.depreciationPeriodsSharedCollection = this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
      this.depreciationPeriodsSharedCollection,
      depreciationEntry.depreciationPeriod
    );
    this.fiscalMonthsSharedCollection = this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(
      this.fiscalMonthsSharedCollection,
      depreciationEntry.fiscalMonth
    );
    this.fiscalQuartersSharedCollection = this.fiscalQuarterService.addFiscalQuarterToCollectionIfMissing(
      this.fiscalQuartersSharedCollection,
      depreciationEntry.fiscalQuarter
    );
    this.fiscalYearsSharedCollection = this.fiscalYearService.addFiscalYearToCollectionIfMissing(
      this.fiscalYearsSharedCollection,
      depreciationEntry.fiscalYear
    );
    this.depreciationJobsSharedCollection = this.depreciationJobService.addDepreciationJobToCollectionIfMissing(
      this.depreciationJobsSharedCollection,
      depreciationEntry.depreciationJob
    );
    this.depreciationBatchSequencesSharedCollection =
      this.depreciationBatchSequenceService.addDepreciationBatchSequenceToCollectionIfMissing(
        this.depreciationBatchSequencesSharedCollection,
        depreciationEntry.depreciationBatchSequence
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

    this.assetCategoryService
      .query()
      .pipe(map((res: HttpResponse<IAssetCategory[]>) => res.body ?? []))
      .pipe(
        map((assetCategories: IAssetCategory[]) =>
          this.assetCategoryService.addAssetCategoryToCollectionIfMissing(assetCategories, this.editForm.get('assetCategory')!.value)
        )
      )
      .subscribe((assetCategories: IAssetCategory[]) => (this.assetCategoriesSharedCollection = assetCategories));

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

    this.fiscalQuarterService
      .query()
      .pipe(map((res: HttpResponse<IFiscalQuarter[]>) => res.body ?? []))
      .pipe(
        map((fiscalQuarters: IFiscalQuarter[]) =>
          this.fiscalQuarterService.addFiscalQuarterToCollectionIfMissing(fiscalQuarters, this.editForm.get('fiscalQuarter')!.value)
        )
      )
      .subscribe((fiscalQuarters: IFiscalQuarter[]) => (this.fiscalQuartersSharedCollection = fiscalQuarters));

    this.fiscalYearService
      .query()
      .pipe(map((res: HttpResponse<IFiscalYear[]>) => res.body ?? []))
      .pipe(
        map((fiscalYears: IFiscalYear[]) =>
          this.fiscalYearService.addFiscalYearToCollectionIfMissing(fiscalYears, this.editForm.get('fiscalYear')!.value)
        )
      )
      .subscribe((fiscalYears: IFiscalYear[]) => (this.fiscalYearsSharedCollection = fiscalYears));

    this.depreciationJobService
      .query()
      .pipe(map((res: HttpResponse<IDepreciationJob[]>) => res.body ?? []))
      .pipe(
        map((depreciationJobs: IDepreciationJob[]) =>
          this.depreciationJobService.addDepreciationJobToCollectionIfMissing(depreciationJobs, this.editForm.get('depreciationJob')!.value)
        )
      )
      .subscribe((depreciationJobs: IDepreciationJob[]) => (this.depreciationJobsSharedCollection = depreciationJobs));

    this.depreciationBatchSequenceService
      .query()
      .pipe(map((res: HttpResponse<IDepreciationBatchSequence[]>) => res.body ?? []))
      .pipe(
        map((depreciationBatchSequences: IDepreciationBatchSequence[]) =>
          this.depreciationBatchSequenceService.addDepreciationBatchSequenceToCollectionIfMissing(
            depreciationBatchSequences,
            this.editForm.get('depreciationBatchSequence')!.value
          )
        )
      )
      .subscribe(
        (depreciationBatchSequences: IDepreciationBatchSequence[]) =>
          (this.depreciationBatchSequencesSharedCollection = depreciationBatchSequences)
      );
  }

  protected createFromForm(): IDepreciationEntry {
    return {
      ...new DepreciationEntry(),
      id: this.editForm.get(['id'])!.value,
      postedAt: this.editForm.get(['postedAt'])!.value ? dayjs(this.editForm.get(['postedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      depreciationAmount: this.editForm.get(['depreciationAmount'])!.value,
      assetNumber: this.editForm.get(['assetNumber'])!.value,
      batchSequenceNumber: this.editForm.get(['batchSequenceNumber'])!.value,
      processedItems: this.editForm.get(['processedItems'])!.value,
      totalItemsProcessed: this.editForm.get(['totalItemsProcessed'])!.value,
      serviceOutlet: this.editForm.get(['serviceOutlet'])!.value,
      assetCategory: this.editForm.get(['assetCategory'])!.value,
      depreciationMethod: this.editForm.get(['depreciationMethod'])!.value,
      assetRegistration: this.editForm.get(['assetRegistration'])!.value,
      depreciationPeriod: this.editForm.get(['depreciationPeriod'])!.value,
      fiscalMonth: this.editForm.get(['fiscalMonth'])!.value,
      fiscalQuarter: this.editForm.get(['fiscalQuarter'])!.value,
      fiscalYear: this.editForm.get(['fiscalYear'])!.value,
      depreciationJob: this.editForm.get(['depreciationJob'])!.value,
      depreciationBatchSequence: this.editForm.get(['depreciationBatchSequence'])!.value,
    };
  }
}
