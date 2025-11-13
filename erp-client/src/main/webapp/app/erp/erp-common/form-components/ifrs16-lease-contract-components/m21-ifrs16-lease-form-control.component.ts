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
import { concat, from, Observable, of, Subject } from 'rxjs';
import {
  catchError,
  debounceTime,
  distinctUntilChanged,
  filter,
  map,
  mergeMap,
  switchMap,
  tap,
  toArray
} from 'rxjs/operators';
import { IIFRS16LeaseContract } from '../../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { Ifrs16LeaseContractSuggestionService } from './ifrs16-lease-contract-suggestion.service';
import { IFRS16LeaseContractService } from '../../../erp-leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';

/**
 * Many-to-one form control component for prepayment-account
 */
@Component({
  selector: 'jhi-m21-ifrs16-lease-form-control',
  templateUrl: './m21-ifrs16-lease-form-control.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => M21Ifrs16LeaseFormControlComponent),
      multi: true
    }
  ]
})
export class M21Ifrs16LeaseFormControlComponent implements OnInit, ControlValueAccessor, OnDestroy {

  @Input() inputValue: IIFRS16LeaseContract = {}

  @Input() inputControlLabel = '';

  @Input() disabledInput = false;

  @Output() valueSelected: EventEmitter<IIFRS16LeaseContract> = new EventEmitter<IIFRS16LeaseContract>();

  minAccountLengthTerm = 3;
  valuesLoading = false;
  valueControlInput$ = new Subject<string>();
  valueLookUps$: Observable<IIFRS16LeaseContract[]> = of([]);

  constructor(
    protected valueService: IFRS16LeaseContractService,
    protected valueSuggestionService: Ifrs16LeaseContractSuggestionService
  ) {}

  onChange: any = () => {
    this.getValues();
  };

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  onTouched: any = () => {};

  ngOnInit(): void {

    this.loadValues();

    if (this.inputValue.id != null) {
      this.valueService.find(this.inputValue.id).subscribe(inputUpdate => {
        if (inputUpdate.body) {
          this.inputValue = inputUpdate.body;
        }
      })
    }
  }

  ngOnDestroy(): void {

    this.valueLookUps$ = of([]);
    this.inputValue = {}
  }

  loadValues(): void {
    this.valueLookUps$ = concat(
      of([]), // default items
      this.valueControlInput$.pipe(
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null && res.length >= this.minAccountLengthTerm),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.valuesLoading = true),
        switchMap(term => this.valueSuggestionService.search(term).pipe(
          catchError(() => of([])),
          switchMap(searchResults => searchResults.length > 0 ?
            from(searchResults).pipe(
              mergeMap(result =>
                // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
                result?.id ? this.valueService.find(result.id).pipe(
                  // eslint-disable-next-line @typescript-eslint/no-unsafe-return
                  map((response: { body: any; }) => response.body), // Assuming response is HttpResponse<IServiceOutlet>
                  catchError(() => of(null))
                ) : of(null)
              ),
              filter((result): result is IIFRS16LeaseContract => result !== null),
              toArray()
            ) : of([])
          ),
          tap(() => this.valuesLoading = false)
        ))
      )
    );
  }

  trackValueByFn(item: any): number {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return item.id!;
  }

  writeValue(value: IIFRS16LeaseContract): void {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (value) {
      this.inputValue = value;
    }
  }

  getValues(): void {
    // TODO check service on this
    this.valueSelected.emit(this.inputValue);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }
}
