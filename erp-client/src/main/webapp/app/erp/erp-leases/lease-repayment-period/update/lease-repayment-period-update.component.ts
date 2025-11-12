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

import { ILeaseRepaymentPeriod, LeaseRepaymentPeriod } from '../lease-repayment-period.model';
import { LeaseRepaymentPeriodService } from '../service/lease-repayment-period.service';
import { IFiscalMonth } from '../../../erp-pages/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from '../../../erp-pages/fiscal-month/service/fiscal-month.service';

@Component({
  selector: 'jhi-lease-repayment-period-update',
  templateUrl: './lease-repayment-period-update.component.html',
})
export class LeaseRepaymentPeriodUpdateComponent implements OnInit {
  isSaving = false;

  fiscalMonthsCollection: IFiscalMonth[] = [];

  editForm = this.fb.group({
    id: [],
    sequenceNumber: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    periodCode: [null, [Validators.required]],
    fiscalMonth: [null, Validators.required],
  });

  constructor(
    protected leaseRepaymentPeriodService: LeaseRepaymentPeriodService,
    protected fiscalMonthService: FiscalMonthService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseRepaymentPeriod }) => {
      this.updateForm(leaseRepaymentPeriod);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaseRepaymentPeriod = this.createFromForm();
    if (leaseRepaymentPeriod.id !== undefined) {
      this.subscribeToSaveResponse(this.leaseRepaymentPeriodService.update(leaseRepaymentPeriod));
    } else {
      this.subscribeToSaveResponse(this.leaseRepaymentPeriodService.create(leaseRepaymentPeriod));
    }
  }

  trackFiscalMonthById(index: number, item: IFiscalMonth): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaseRepaymentPeriod>>): void {
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

  protected updateForm(leaseRepaymentPeriod: ILeaseRepaymentPeriod): void {
    this.editForm.patchValue({
      id: leaseRepaymentPeriod.id,
      sequenceNumber: leaseRepaymentPeriod.sequenceNumber,
      startDate: leaseRepaymentPeriod.startDate,
      endDate: leaseRepaymentPeriod.endDate,
      periodCode: leaseRepaymentPeriod.periodCode,
      fiscalMonth: leaseRepaymentPeriod.fiscalMonth,
    });

    this.fiscalMonthsCollection = this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(
      this.fiscalMonthsCollection,
      leaseRepaymentPeriod.fiscalMonth
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fiscalMonthService
      .query({ 'leaseRepaymentPeriodId.specified': 'false' })
      .pipe(map((res: HttpResponse<IFiscalMonth[]>) => res.body ?? []))
      .pipe(
        map((fiscalMonths: IFiscalMonth[]) =>
          this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(fiscalMonths, this.editForm.get('fiscalMonth')!.value)
        )
      )
      .subscribe((fiscalMonths: IFiscalMonth[]) => (this.fiscalMonthsCollection = fiscalMonths));
  }

  protected createFromForm(): ILeaseRepaymentPeriod {
    return {
      ...new LeaseRepaymentPeriod(),
      id: this.editForm.get(['id'])!.value,
      sequenceNumber: this.editForm.get(['sequenceNumber'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      periodCode: this.editForm.get(['periodCode'])!.value,
      fiscalMonth: this.editForm.get(['fiscalMonth'])!.value,
    };
  }
}
