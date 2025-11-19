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

import { Component, EventEmitter, forwardRef, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { concat, Observable, of, Subject } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, filter, switchMap, tap } from 'rxjs/operators';
import { ILeaseAmortizationSchedule } from '../../../erp-leases/lease-amortization-schedule/lease-amortization-schedule.model';
import { LeaseAmortizationScheduleSuggestionService } from './lease-amortization-schedule-suggestion.service';

@Component({
  selector: 'jhi-m21-lease-amortization-schedule-form-control',
  templateUrl: './m21-lease-amortization-schedule-form-control.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => M21LeaseAmortizationScheduleFormControlComponent),
      multi: true,
    },
  ],
})
export class M21LeaseAmortizationScheduleFormControlComponent implements OnInit, ControlValueAccessor, OnDestroy {
  @Input() inputValue: ILeaseAmortizationSchedule = {};
  @Input() inputControlLabel = '';
  @Input() disabledInput = false;
  @Output() valueSelected = new EventEmitter<ILeaseAmortizationSchedule>();

  minAccountLengthTerm = 2;
  valuesLoading = false;
  valueControlInput$ = new Subject<string>();
  valueLookUps$: Observable<ILeaseAmortizationSchedule[]> = of([]);

  constructor(private valueSuggestionService: LeaseAmortizationScheduleSuggestionService) {}

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  onTouched: any = () => {};
  onChange: any = () => {
    this.getValues();
  };

  ngOnInit(): void {
    this.loadValues();
  }

  ngOnDestroy(): void {
    this.valueLookUps$ = of([]);
    this.inputValue = {};
  }

  loadValues(): void {
    this.valueLookUps$ = concat(
      of([]),
      this.valueControlInput$.pipe(
        filter(term => term !== null),
        distinctUntilChanged(),
        debounceTime(500),
        tap(() => (this.valuesLoading = true)),
        switchMap(term =>
          this.valueSuggestionService.search(term).pipe(
            catchError(() => of([])),
            tap(() => (this.valuesLoading = false))
          )
        )
      )
    );
  }

  trackValueByFn(item: ILeaseAmortizationSchedule): number {
    return item.id!;
  }

  writeValue(value: ILeaseAmortizationSchedule): void {
    if (value) {
      this.inputValue = value;
    }
  }

  getValues(): void {
    this.valueSelected.emit(this.inputValue);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }
}
