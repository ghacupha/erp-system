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

import { IAmortizationPeriod, AmortizationPeriod } from '../amortization-period.model';
import { AmortizationPeriodService } from '../service/amortization-period.service';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from 'app/entities/system/fiscal-month/service/fiscal-month.service';

@Component({
  selector: 'jhi-amortization-period-update',
  templateUrl: './amortization-period-update.component.html',
})
export class AmortizationPeriodUpdateComponent implements OnInit {
  isSaving = false;

  fiscalMonthsSharedCollection: IFiscalMonth[] = [];
  amortizationPeriodsSharedCollection: IAmortizationPeriod[] = [];

  editForm = this.fb.group({
    id: [],
    sequenceNumber: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    periodCode: [null, [Validators.required]],
    fiscalMonth: [null, Validators.required],
    amortizationPeriod: [],
  });

  constructor(
    protected amortizationPeriodService: AmortizationPeriodService,
    protected fiscalMonthService: FiscalMonthService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amortizationPeriod }) => {
      this.updateForm(amortizationPeriod);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const amortizationPeriod = this.createFromForm();
    if (amortizationPeriod.id !== undefined) {
      this.subscribeToSaveResponse(this.amortizationPeriodService.update(amortizationPeriod));
    } else {
      this.subscribeToSaveResponse(this.amortizationPeriodService.create(amortizationPeriod));
    }
  }

  trackFiscalMonthById(index: number, item: IFiscalMonth): number {
    return item.id!;
  }

  trackAmortizationPeriodById(index: number, item: IAmortizationPeriod): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAmortizationPeriod>>): void {
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

  protected updateForm(amortizationPeriod: IAmortizationPeriod): void {
    this.editForm.patchValue({
      id: amortizationPeriod.id,
      sequenceNumber: amortizationPeriod.sequenceNumber,
      startDate: amortizationPeriod.startDate,
      endDate: amortizationPeriod.endDate,
      periodCode: amortizationPeriod.periodCode,
      fiscalMonth: amortizationPeriod.fiscalMonth,
      amortizationPeriod: amortizationPeriod.amortizationPeriod,
    });

    this.fiscalMonthsSharedCollection = this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(
      this.fiscalMonthsSharedCollection,
      amortizationPeriod.fiscalMonth
    );
    this.amortizationPeriodsSharedCollection = this.amortizationPeriodService.addAmortizationPeriodToCollectionIfMissing(
      this.amortizationPeriodsSharedCollection,
      amortizationPeriod.amortizationPeriod
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fiscalMonthService
      .query()
      .pipe(map((res: HttpResponse<IFiscalMonth[]>) => res.body ?? []))
      .pipe(
        map((fiscalMonths: IFiscalMonth[]) =>
          this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(fiscalMonths, this.editForm.get('fiscalMonth')!.value)
        )
      )
      .subscribe((fiscalMonths: IFiscalMonth[]) => (this.fiscalMonthsSharedCollection = fiscalMonths));

    this.amortizationPeriodService
      .query()
      .pipe(map((res: HttpResponse<IAmortizationPeriod[]>) => res.body ?? []))
      .pipe(
        map((amortizationPeriods: IAmortizationPeriod[]) =>
          this.amortizationPeriodService.addAmortizationPeriodToCollectionIfMissing(
            amortizationPeriods,
            this.editForm.get('amortizationPeriod')!.value
          )
        )
      )
      .subscribe((amortizationPeriods: IAmortizationPeriod[]) => (this.amortizationPeriodsSharedCollection = amortizationPeriods));
  }

  protected createFromForm(): IAmortizationPeriod {
    return {
      ...new AmortizationPeriod(),
      id: this.editForm.get(['id'])!.value,
      sequenceNumber: this.editForm.get(['sequenceNumber'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      periodCode: this.editForm.get(['periodCode'])!.value,
      fiscalMonth: this.editForm.get(['fiscalMonth'])!.value,
      amortizationPeriod: this.editForm.get(['amortizationPeriod'])!.value,
    };
  }
}
