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

import { IPrepaymentMarshalling, PrepaymentMarshalling } from '../prepayment-marshalling.model';
import { PrepaymentMarshallingService } from '../service/prepayment-marshalling.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { PrepaymentAccountService } from '../../prepayment-account/service/prepayment-account.service';
import { IFiscalMonth } from '../../../erp-pages/fiscal-month/fiscal-month.model';
import { FiscalMonthService } from '../../../erp-pages/fiscal-month/service/fiscal-month.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IPrepaymentAccount } from '../../prepayment-account/prepayment-account.model';
import { select, Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  copyingPrepaymentMarshallingStatus,
  creatingPrepaymentMarshallingStatus,
  editingPrepaymentMarshallingStatus,
  prepaymentMarshallingUpdateSelectedInstance
} from '../../../store/selectors/prepayment-marshalling-workflow-status.selector';
import { IAmortizationPeriod } from '../../amortization-period/amortization-period.model';
import { AmortizationPeriodService } from '../../amortization-period/service/amortization-period.service';
import dayjs from 'dayjs';

@Component({
  selector: 'jhi-prepayment-marshalling-update',
  templateUrl: './prepayment-marshalling-update.component.html'
})
export class PrepaymentMarshallingUpdateComponent implements OnInit {
  isSaving = false;

  amortizationPeriodsSharedCollection: IAmortizationPeriod[] = [];
  prepaymentAccountsSharedCollection: IPrepaymentAccount[] = [];
  placeholdersSharedCollection: IPlaceholder[] = [];
  fiscalMonthsSharedCollection: IFiscalMonth[] = [];

  // Setting up default form states
  weAreCopying = false;
  weAreEditing = false;
  weAreCreating = false;
  selectedItem = { ...new PrepaymentMarshalling() };

  todDate = dayjs();

  editForm = this.fb.group({
    id: [],
    inactive: [],
    amortizationPeriods: [],
    processed: [],
    prepaymentAccount: [null, Validators.required],
    firstAmortizationPeriod: [null, Validators.required],
    placeholders: [],
    firstFiscalMonth: [null, Validators.required],
    lastFiscalMonth: [null, Validators.required]
  });

  disableAmortizationPeriodControl = false;

  constructor(
    protected prepaymentMarshallingService: PrepaymentMarshallingService,
    protected prepaymentAccountService: PrepaymentAccountService,
    protected amortizationPeriodService: AmortizationPeriodService,
    protected placeholderService: PlaceholderService,
    protected fiscalMonthService: FiscalMonthService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected store: Store<State>
  ) {
    this.store.pipe(select(copyingPrepaymentMarshallingStatus)).subscribe(stat => this.weAreCopying = stat);
    this.store.pipe(select(editingPrepaymentMarshallingStatus)).subscribe(stat => this.weAreEditing = stat);
    this.store.pipe(select(creatingPrepaymentMarshallingStatus)).subscribe(stat => this.weAreCreating = stat);
    this.store.pipe(select(prepaymentMarshallingUpdateSelectedInstance)).subscribe(copied => this.selectedItem = copied);
  }

  ngOnInit(): void {

    if (this.weAreEditing) {
      this.disableAmortizationPeriodControl = true;
      this.updateForm(this.selectedItem);
    }

    if (this.weAreCopying) {
      this.disableAmortizationPeriodControl = true;
      this.copyForm(this.selectedItem);
    }

    if (this.weAreCreating) {
      this.loadRelationshipsOptions();
    }

    this.updateFirstPeriod();

    this.updateFirstFiscalMonthGivenFirstPeriod();

    this.updateLastFiscalMonthGivenPeriods();

    this.updateLastFiscalMonthGivenAmortizationPeriod();

  }

  updateFirstPeriod(): void {
    if(!this.weAreCopying) {
      this.amortizationPeriodService.findByDate(this.todDate).subscribe(periodResponse => {
        if (periodResponse.body) {
          this.editForm.patchValue({
            firstAmortizationPeriod: periodResponse.body
          });
        }
      });
    }
  }

  updateFirstFiscalMonthGivenFirstPeriod(): void {
    this.editForm.get(['firstAmortizationPeriod'])?.valueChanges.subscribe((amortizationPeriodChange) => {

      this.fiscalMonthService.find(amortizationPeriodChange.fiscalMonth.id).subscribe(fiscalMonthResponse => {
        if (fiscalMonthResponse.body) {
          const fiscalMonth = fiscalMonthResponse.body;
          this.editForm.patchValue({
            firstFiscalMonth: fiscalMonth
          });
        }
      });
    });
  }

  updateLastFiscalMonthGivenPeriods(): void {
    this.editForm.get(['amortizationPeriods'])?.valueChanges.subscribe((amortizationPeriodsChange) => {
      const firstFiscalMonth = this.editForm.get(['firstFiscalMonth'])!.value;
      if (firstFiscalMonth) {
        this.fiscalMonthService.findFiscalMonthAfterGivenPeriods(firstFiscalMonth.id, amortizationPeriodsChange).subscribe(fiscalMonthResponse => {
          if (fiscalMonthResponse.body) {
            const fiscalMonth = fiscalMonthResponse.body;
            this.editForm.patchValue({
              lastFiscalMonth: fiscalMonth
            });
          }
        });
      }
    });
  }

  /**
   * Reacts to changes in firstAmortizationPeriod given the periods are provided
   */
  updateLastFiscalMonthGivenAmortizationPeriod(): void {
    this.editForm.get(['firstAmortizationPeriod'])?.valueChanges.subscribe((amortizationPeriodChange) => {

      const amortizationPeriods = this.editForm.get(['amortizationPeriods'])!.value;

      if(amortizationPeriods) {
        this.fiscalMonthService.find(amortizationPeriodChange.fiscalMonth.id).subscribe(fiscalMonthProposedChange => {
          if (fiscalMonthProposedChange.body) {
            const firstFiscalMonth = fiscalMonthProposedChange.body;
            if (firstFiscalMonth.id != null) {
              this.fiscalMonthService.findFiscalMonthAfterGivenPeriods(firstFiscalMonth.id, amortizationPeriods).subscribe(lastFiscalMonthResponse => {
                if (lastFiscalMonthResponse.body) {
                  this.editForm.patchValue({
                    lastFiscalMonth: lastFiscalMonthResponse.body
                  });
                }
              });
            }
          }
        });
      }
    });
  }

  updatePrepaymentAccount(update: IPrepaymentAccount): void {
    this.editForm.patchValue({
      prepaymentAccount: update
    });
  }

  updateFirstAmortizationPeriod(update: IAmortizationPeriod): void {
    this.editForm.patchValue({
      firstAmortizationPeriod: update
    });
  }

  updatePlaceholders(update: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [...update]
    });
  }

  updateFirstFiscalMonth(update: IFiscalMonth): void {
    this.editForm.patchValue({
      firstFiscalMonth: update
    });
  }

  updateLastFiscalMonth(update: IFiscalMonth): void {
    this.editForm.patchValue({
      lastFiscalMonth: update
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.prepaymentMarshallingService.create(this.createFromForm()));
  }

  edit(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.prepaymentMarshallingService.update(this.createFromForm()));
  }

  copy(): void {
    this.isSaving = true;
    this.subscribeToSaveResponse(this.prepaymentMarshallingService.create(this.copyFromForm()));
  }

  trackAmortizationPeriodById(index: number, item: IAmortizationPeriod): number {
    return item.id!;
  }

  trackPrepaymentAccountById(index: number, item: IPrepaymentAccount): number {
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
      placeholders: prepaymentMarshalling.placeholders,
      firstFiscalMonth: prepaymentMarshalling.firstFiscalMonth,
      lastFiscalMonth: prepaymentMarshalling.lastFiscalMonth,
      firstAmortizationPeriod: prepaymentMarshalling.firstAmortizationPeriod
    });

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

  protected copyForm(prepaymentMarshalling: IPrepaymentMarshalling): void {
    this.editForm.patchValue({
      id: prepaymentMarshalling.id,
      inactive: prepaymentMarshalling.inactive,
      amortizationPeriods: prepaymentMarshalling.amortizationPeriods,
      processed: false,
      prepaymentAccount: prepaymentMarshalling.prepaymentAccount,
      placeholders: prepaymentMarshalling.placeholders,
      firstFiscalMonth: prepaymentMarshalling.firstFiscalMonth,
      lastFiscalMonth: prepaymentMarshalling.lastFiscalMonth,
      firstAmortizationPeriod: prepaymentMarshalling.firstAmortizationPeriod
    });

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
      inactive: false,
      amortizationPeriods: this.editForm.get(['amortizationPeriods'])!.value,
      processed: false,
      prepaymentAccount: this.editForm.get(['prepaymentAccount'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      firstFiscalMonth: this.editForm.get(['firstFiscalMonth'])!.value,
      lastFiscalMonth: this.editForm.get(['lastFiscalMonth'])!.value,
      firstAmortizationPeriod: this.editForm.get(['firstAmortizationPeriod'])!.value
    };
  }

  protected copyFromForm(): IPrepaymentMarshalling {
    return {
      ...new PrepaymentMarshalling(),
      // id: this.editForm.get(['id'])!.value,
      inactive: false,
      amortizationPeriods: this.editForm.get(['amortizationPeriods'])!.value,
      processed: false,
      prepaymentAccount: this.editForm.get(['prepaymentAccount'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      firstFiscalMonth: this.editForm.get(['firstFiscalMonth'])!.value,
      lastFiscalMonth: this.editForm.get(['lastFiscalMonth'])!.value,
      firstAmortizationPeriod: this.editForm.get(['firstAmortizationPeriod'])!.value
    };
  }
}
