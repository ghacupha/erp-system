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

import { IDepreciationPeriod, DepreciationPeriod } from '../depreciation-period.model';
import { DepreciationPeriodService } from '../service/depreciation-period.service';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';
import { IFiscalYear } from 'app/entities/system/fiscal-year/fiscal-year.model';
import { FiscalYearService } from 'app/entities/system/fiscal-year/service/fiscal-year.service';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from 'app/entities/system/fiscal-month/service/fiscal-month.service';
import { IFiscalQuarter } from 'app/entities/system/fiscal-quarter/fiscal-quarter.model';
import { FiscalQuarterService } from 'app/entities/system/fiscal-quarter/service/fiscal-quarter.service';
import { DepreciationPeriodStatusTypes } from 'app/entities/enumerations/depreciation-period-status-types.model';

@Component({
  selector: 'jhi-depreciation-period-update',
  templateUrl: './depreciation-period-update.component.html',
})
export class DepreciationPeriodUpdateComponent implements OnInit {
  isSaving = false;
  depreciationPeriodStatusTypesValues = Object.keys(DepreciationPeriodStatusTypes);

  previousPeriodsCollection: IDepreciationPeriod[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  fiscalYearsSharedCollection: IFiscalYear[] = [];
  fiscalMonthsSharedCollection: IFiscalMonth[] = [];
  fiscalQuartersSharedCollection: IFiscalQuarter[] = [];

  editForm = this.fb.group({
    id: [],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    depreciationPeriodStatus: [],
    periodCode: [null, [Validators.required]],
    processLocked: [],
    previousPeriod: [],
    createdBy: [],
    fiscalYear: [null, Validators.required],
    fiscalMonth: [null, Validators.required],
    fiscalQuarter: [null, Validators.required],
  });

  constructor(
    protected depreciationPeriodService: DepreciationPeriodService,
    protected applicationUserService: ApplicationUserService,
    protected fiscalYearService: FiscalYearService,
    protected fiscalMonthService: FiscalMonthService,
    protected fiscalQuarterService: FiscalQuarterService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depreciationPeriod }) => {
      this.updateForm(depreciationPeriod);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const depreciationPeriod = this.createFromForm();
    if (depreciationPeriod.id !== undefined) {
      this.subscribeToSaveResponse(this.depreciationPeriodService.update(depreciationPeriod));
    } else {
      this.subscribeToSaveResponse(this.depreciationPeriodService.create(depreciationPeriod));
    }
  }

  trackDepreciationPeriodById(index: number, item: IDepreciationPeriod): number {
    return item.id!;
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  trackFiscalYearById(index: number, item: IFiscalYear): number {
    return item.id!;
  }

  trackFiscalMonthById(index: number, item: IFiscalMonth): number {
    return item.id!;
  }

  trackFiscalQuarterById(index: number, item: IFiscalQuarter): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepreciationPeriod>>): void {
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

  protected updateForm(depreciationPeriod: IDepreciationPeriod): void {
    this.editForm.patchValue({
      id: depreciationPeriod.id,
      startDate: depreciationPeriod.startDate,
      endDate: depreciationPeriod.endDate,
      depreciationPeriodStatus: depreciationPeriod.depreciationPeriodStatus,
      periodCode: depreciationPeriod.periodCode,
      processLocked: depreciationPeriod.processLocked,
      previousPeriod: depreciationPeriod.previousPeriod,
      createdBy: depreciationPeriod.createdBy,
      fiscalYear: depreciationPeriod.fiscalYear,
      fiscalMonth: depreciationPeriod.fiscalMonth,
      fiscalQuarter: depreciationPeriod.fiscalQuarter,
    });

    this.previousPeriodsCollection = this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
      this.previousPeriodsCollection,
      depreciationPeriod.previousPeriod
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      depreciationPeriod.createdBy
    );
    this.fiscalYearsSharedCollection = this.fiscalYearService.addFiscalYearToCollectionIfMissing(
      this.fiscalYearsSharedCollection,
      depreciationPeriod.fiscalYear
    );
    this.fiscalMonthsSharedCollection = this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(
      this.fiscalMonthsSharedCollection,
      depreciationPeriod.fiscalMonth
    );
    this.fiscalQuartersSharedCollection = this.fiscalQuarterService.addFiscalQuarterToCollectionIfMissing(
      this.fiscalQuartersSharedCollection,
      depreciationPeriod.fiscalQuarter
    );
  }

  protected loadRelationshipsOptions(): void {
    this.depreciationPeriodService
      .query({ 'nextPeriodId.specified': 'false' })
      .pipe(map((res: HttpResponse<IDepreciationPeriod[]>) => res.body ?? []))
      .pipe(
        map((depreciationPeriods: IDepreciationPeriod[]) =>
          this.depreciationPeriodService.addDepreciationPeriodToCollectionIfMissing(
            depreciationPeriods,
            this.editForm.get('previousPeriod')!.value
          )
        )
      )
      .subscribe((depreciationPeriods: IDepreciationPeriod[]) => (this.previousPeriodsCollection = depreciationPeriods));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(applicationUsers, this.editForm.get('createdBy')!.value)
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.fiscalYearService
      .query()
      .pipe(map((res: HttpResponse<IFiscalYear[]>) => res.body ?? []))
      .pipe(
        map((fiscalYears: IFiscalYear[]) =>
          this.fiscalYearService.addFiscalYearToCollectionIfMissing(fiscalYears, this.editForm.get('fiscalYear')!.value)
        )
      )
      .subscribe((fiscalYears: IFiscalYear[]) => (this.fiscalYearsSharedCollection = fiscalYears));

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
  }

  protected createFromForm(): IDepreciationPeriod {
    return {
      ...new DepreciationPeriod(),
      id: this.editForm.get(['id'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      depreciationPeriodStatus: this.editForm.get(['depreciationPeriodStatus'])!.value,
      periodCode: this.editForm.get(['periodCode'])!.value,
      processLocked: this.editForm.get(['processLocked'])!.value,
      previousPeriod: this.editForm.get(['previousPeriod'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      fiscalYear: this.editForm.get(['fiscalYear'])!.value,
      fiscalMonth: this.editForm.get(['fiscalMonth'])!.value,
      fiscalQuarter: this.editForm.get(['fiscalQuarter'])!.value,
    };
  }
}
