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

import { IPrepaymentMarshalling, PrepaymentMarshalling } from '../prepayment-marshalling.model';
import { PrepaymentMarshallingService } from '../service/prepayment-marshalling.service';
import { IPrepaymentAccount } from 'app/entities/prepayments/prepayment-account/prepayment-account.model';
import { PrepaymentAccountService } from 'app/entities/prepayments/prepayment-account/service/prepayment-account.service';
import { IAmortizationPeriod } from 'app/entities/prepayments/amortization-period/amortization-period.model';
import { AmortizationPeriodService } from 'app/entities/prepayments/amortization-period/service/amortization-period.service';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from 'app/entities/system/fiscal-month/service/fiscal-month.service';

@Component({
  selector: 'jhi-prepayment-marshalling-update',
  templateUrl: './prepayment-marshalling-update.component.html',
})
export class PrepaymentMarshallingUpdateComponent implements OnInit {
  isSaving = false;

  prepaymentAccountsSharedCollection: IPrepaymentAccount[] = [];
  amortizationPeriodsSharedCollection: IAmortizationPeriod[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  fiscalMonthsSharedCollection: IFiscalMonth[] = [];

  editForm = this.fb.group({
    id: [],
    inactive: [null, [Validators.required]],
    amortizationPeriods: [],
    processed: [],
    prepaymentAccount: [null, Validators.required],
    firstAmortizationPeriod: [null, Validators.required],
    placeholders: [],
    firstFiscalMonth: [null, Validators.required],
    lastFiscalMonth: [null, Validators.required],
  });

  constructor(
    protected prepaymentMarshallingService: PrepaymentMarshallingService,
    protected prepaymentAccountService: PrepaymentAccountService,
    protected amortizationPeriodService: AmortizationPeriodService,
    protected placeholderService: PlaceholderService,
    protected fiscalMonthService: FiscalMonthService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentMarshalling }) => {
      this.updateForm(prepaymentMarshalling);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prepaymentMarshalling = this.createFromForm();
    if (prepaymentMarshalling.id !== undefined) {
      this.subscribeToSaveResponse(this.prepaymentMarshallingService.update(prepaymentMarshalling));
    } else {
      this.subscribeToSaveResponse(this.prepaymentMarshallingService.create(prepaymentMarshalling));
    }
  }

  trackPrepaymentAccountById(index: number, item: IPrepaymentAccount): number {
    return item.id!;
  }

  trackAmortizationPeriodById(index: number, item: IAmortizationPeriod): number {
    return item.id!;
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackFiscalMonthById(index: number, item: IFiscalMonth): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrepaymentMarshalling>>): void {
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

  protected updateForm(prepaymentMarshalling: IPrepaymentMarshalling): void {
    this.editForm.patchValue({
      id: prepaymentMarshalling.id,
      inactive: prepaymentMarshalling.inactive,
      amortizationPeriods: prepaymentMarshalling.amortizationPeriods,
      processed: prepaymentMarshalling.processed,
      prepaymentAccount: prepaymentMarshalling.prepaymentAccount,
      firstAmortizationPeriod: prepaymentMarshalling.firstAmortizationPeriod,
      placeholders: prepaymentMarshalling.placeholders,
      firstFiscalMonth: prepaymentMarshalling.firstFiscalMonth,
      lastFiscalMonth: prepaymentMarshalling.lastFiscalMonth,
    });

    this.prepaymentAccountsSharedCollection = this.prepaymentAccountService.addPrepaymentAccountToCollectionIfMissing(
      this.prepaymentAccountsSharedCollection,
      prepaymentMarshalling.prepaymentAccount
    );
    this.amortizationPeriodsSharedCollection = this.amortizationPeriodService.addAmortizationPeriodToCollectionIfMissing(
      this.amortizationPeriodsSharedCollection,
      prepaymentMarshalling.firstAmortizationPeriod
    );
    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(prepaymentMarshalling.placeholders ?? [])
    );
    this.fiscalMonthsSharedCollection = this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(
      this.fiscalMonthsSharedCollection,
      prepaymentMarshalling.firstFiscalMonth,
      prepaymentMarshalling.lastFiscalMonth
    );
  }

  protected loadRelationshipsOptions(): void {
    this.prepaymentAccountService
      .query()
      .pipe(map((res: HttpResponse<IPrepaymentAccount[]>) => res.body ?? []))
      .pipe(
        map((prepaymentAccounts: IPrepaymentAccount[]) =>
          this.prepaymentAccountService.addPrepaymentAccountToCollectionIfMissing(
            prepaymentAccounts,
            this.editForm.get('prepaymentAccount')!.value
          )
        )
      )
      .subscribe((prepaymentAccounts: IPrepaymentAccount[]) => (this.prepaymentAccountsSharedCollection = prepaymentAccounts));

    this.amortizationPeriodService
      .query()
      .pipe(map((res: HttpResponse<IAmortizationPeriod[]>) => res.body ?? []))
      .pipe(
        map((amortizationPeriods: IAmortizationPeriod[]) =>
          this.amortizationPeriodService.addAmortizationPeriodToCollectionIfMissing(
            amortizationPeriods,
            this.editForm.get('firstAmortizationPeriod')!.value
          )
        )
      )
      .subscribe((amortizationPeriods: IAmortizationPeriod[]) => (this.amortizationPeriodsSharedCollection = amortizationPeriods));

    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));

    this.fiscalMonthService
      .query()
      .pipe(map((res: HttpResponse<IFiscalMonth[]>) => res.body ?? []))
      .pipe(
        map((fiscalMonths: IFiscalMonth[]) =>
          this.fiscalMonthService.addFiscalMonthToCollectionIfMissing(
            fiscalMonths,
            this.editForm.get('firstFiscalMonth')!.value,
            this.editForm.get('lastFiscalMonth')!.value
          )
        )
      )
      .subscribe((fiscalMonths: IFiscalMonth[]) => (this.fiscalMonthsSharedCollection = fiscalMonths));
  }

  protected createFromForm(): IPrepaymentMarshalling {
    return {
      ...new PrepaymentMarshalling(),
      id: this.editForm.get(['id'])!.value,
      inactive: this.editForm.get(['inactive'])!.value,
      amortizationPeriods: this.editForm.get(['amortizationPeriods'])!.value,
      processed: this.editForm.get(['processed'])!.value,
      prepaymentAccount: this.editForm.get(['prepaymentAccount'])!.value,
      firstAmortizationPeriod: this.editForm.get(['firstAmortizationPeriod'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      firstFiscalMonth: this.editForm.get(['firstFiscalMonth'])!.value,
      lastFiscalMonth: this.editForm.get(['lastFiscalMonth'])!.value,
    };
  }
}
