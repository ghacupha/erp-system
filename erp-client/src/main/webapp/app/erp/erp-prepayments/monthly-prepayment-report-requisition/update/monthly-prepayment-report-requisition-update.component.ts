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
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import {
  IMonthlyPrepaymentReportRequisition,
  MonthlyPrepaymentReportRequisition
} from '../monthly-prepayment-report-requisition.model';
import { MonthlyPrepaymentReportRequisitionService } from '../service/monthly-prepayment-report-requisition.service';
import { ASC, DESC, ITEMS_PER_PAGE } from '../../../../config/pagination.constants';
import { FiscalYearService } from '../../../erp-pages/fiscal-year/service/fiscal-year.service';
import { IFiscalYear } from '../../../erp-pages/fiscal-year/fiscal-year.model';
import * as dayjs from 'dayjs';
import { uuidv7 } from 'uuidv7';

@Component({
  selector: 'jhi-monthly-prepayment-report-requisition-update',
  templateUrl: './monthly-prepayment-report-requisition-update.component.html',
})
export class MonthlyPrepaymentReportRequisitionUpdateComponent implements OnInit {
  isSaving = false;

  fiscalYearsSharedCollection: IFiscalYear[] = [];

  itemsPerPage = ITEMS_PER_PAGE;
  predicate!: string;
  ascending!: boolean;

  editForm = this.fb.group({
    id: [],
    requestId: [],
    timeOfRequisition: [null, [Validators.required]],
    fileChecksum: [],
    filename: [],
    reportParameters: [],
    reportFile: [],
    reportFileContentType: [],
    tampered: [],
    fiscalYear: [null, Validators.required],
  });

  constructor(
    protected monthlyPrepaymentReportRequisitionService: MonthlyPrepaymentReportRequisitionService,
    protected fiscalYearService: FiscalYearService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ monthlyPrepaymentReportRequisition }) => {

      monthlyPrepaymentReportRequisition.timeOfRequisition = dayjs();
      monthlyPrepaymentReportRequisition.requestId = uuidv7();

      this.updateForm(monthlyPrepaymentReportRequisition);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {

    const { fiscalYear } = this.createFromForm();

    // NAV to "monthly-prepayment-outstanding-report-item"
    this.router.navigate(['/monthly-prepayment-outstanding-report-item'], {
      queryParams: {
        fiscalYearId: fiscalYear?.id,
        page: 0,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
      },
    });
  }

  updateFiscalYear(update: IFiscalYear): void {
    this.editForm.patchValue({
      fiscalYear: update
    });
  }

  trackFiscalYearById(index: number, item: IFiscalYear): number {
    return item.id!;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'end_date') {
      result.push('end_date');
    }
    return result;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMonthlyPrepaymentReportRequisition>>): void {
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

  protected updateForm(monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition): void {
    this.editForm.patchValue({
      id: monthlyPrepaymentReportRequisition.id,
      fiscalYear: monthlyPrepaymentReportRequisition.fiscalYear,
    });

    this.fiscalYearsSharedCollection = this.fiscalYearService.addFiscalYearToCollectionIfMissing(
      this.fiscalYearsSharedCollection,
      monthlyPrepaymentReportRequisition.fiscalYear
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fiscalYearService
      .query()
      .pipe(map((res: HttpResponse<IFiscalYear[]>) => res.body ?? []))
      .pipe(
        map((fiscalYears: IFiscalYear[]) =>
          this.fiscalYearService.addFiscalYearToCollectionIfMissing(fiscalYears, this.editForm.get('fiscalYear')!.value)
        )
      )
      .subscribe((fiscalYears: IFiscalYear[]) => (this.fiscalYearsSharedCollection = fiscalYears));
  }

  protected createFromForm(): IMonthlyPrepaymentReportRequisition {
    return {
      ...new MonthlyPrepaymentReportRequisition(),
      id: this.editForm.get(['id'])!.value,
      fiscalYear: this.editForm.get(['fiscalYear'])!.value,
    };
  }
}
