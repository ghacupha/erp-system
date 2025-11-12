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

import { Component, EventEmitter, forwardRef, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { concat, from, Observable, of, Subject } from 'rxjs';
import {
  catchError,
  debounceTime,
  distinctUntilChanged,
  filter, map,
  mergeMap,
  switchMap,
  tap,
  toArray
} from 'rxjs/operators';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';
import {
  ServiceOutletService
} from '../../../erp-granular/service-outlet/service/service-outlet.service';
import { ServiceOutletSuggestionService } from './service-outlet-suggestion.service';

/**
 * Service to fetch suggestion values that coincide with the search terms input by the user
 * at any form controls that fills an object of the UUM entity
 */
@Component({
  selector: 'jhi-m2m-service-outlet-form-control',
  templateUrl: './m2m-service-outlet-mapping-form-control.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => M2mServiceOutletFormControlComponent),
      multi: true
    }
  ]
})
export class M2mServiceOutletFormControlComponent implements OnInit, OnDestroy, ControlValueAccessor {

  @Input() inputValues: IServiceOutlet[] = [];
  @Input() inputControlLabel = '';
  @Output() selectedValues: EventEmitter<IServiceOutlet[]> = new EventEmitter<IServiceOutlet[]>();

  minAccountLengthTerm = 3;
  valuesLoading = false;
  valueInputControl$ = new Subject<string>();
  valueLookUps$: Observable<IServiceOutlet[]> = of([]);

  constructor(
    protected valueService: ServiceOutletService,
    protected valueSuggestionService: ServiceOutletSuggestionService
  ) {}

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  onChange: any = () => {
    this.getValues();
  };
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  onTouched: any = () => {};

  ngOnInit(): void {
    this.loadValues();
  }

  ngOnDestroy(): void {
    this.valueLookUps$ = of([]);
    this.inputValues = [];
  }

  loadValues(): void {
    this.valueLookUps$ = concat(
      of([]), // default items
      this.valueInputControl$.pipe(
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
              filter((result): result is IServiceOutlet => result !== null),
              toArray()
            ) : of([])
          ),
          tap(() => this.valuesLoading = false)
        ))
      )
    );
  }

  trackValuesByFn(item: IServiceOutlet): number {
    return item.id!;
  }

  /**
   * Replaces the array received from the parent with the one currently in view
   *
   * @param value
   */
  writeValue(value: IServiceOutlet[]): void {
    if (value.length !== 0) {
      this.inputValues = value;
    }
  }

  /**
   * Emits updated array to parent
   */
  getValues(): void {
    this.selectedValues.emit(this.inputValues);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }
}
