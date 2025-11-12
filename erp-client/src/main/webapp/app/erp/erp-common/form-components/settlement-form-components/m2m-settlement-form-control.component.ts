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
import { concat, Observable, of, Subject } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, filter, switchMap, tap } from 'rxjs/operators';
import { ISettlement } from '../../../erp-settlements/settlement/settlement.model';
import { SettlementService } from '../../../erp-settlements/settlement/service/settlement.service';
import { SettlementSuggestionService } from '../../suggestion/settlement-suggestion.service';
import { Router } from '@angular/router';


@Component({
  selector: 'jhi-m2m-settlement-form-control',
  templateUrl: './m2m-settlement-form-control.component.html',
  styleUrls: ['./m2m-settlement-form-control.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => M2MSettlementFormControlComponent),
      multi: true
    }
  ]
})
export class M2MSettlementFormControlComponent implements OnInit, OnDestroy, ControlValueAccessor {

  @Input() inputValues: ISettlement[] = [];

  @Input() inputControlLabel = '';

  @Output() valueSelected: EventEmitter<ISettlement[]> = new EventEmitter<ISettlement[]>();

  minAccountLengthTerm = 3;
  valuesLoading = false;
  valueInputControl$ = new Subject<string>();
  valueLookups$: Observable<ISettlement[]> = of([]);

  constructor(
    protected router: Router,
    protected valueService: SettlementService,
    protected valueSuggestionService: SettlementSuggestionService
  ) {
    // if (this.router.getCurrentNavigation() !== null) {
    //   if (this.router.getCurrentNavigation().extras.state) {
    //     this.inputValues = [...this.router.getCurrentNavigation().extras.state.inputValues ?? []];
    //   }
    // }
  }

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  onChange: any = () => {
    this.getValues();
  };
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  onTouched: any = () => {};

  ngOnInit(): void {
    this.loadData();
  }

  ngOnDestroy(): void {
    this.valueLookups$ = of([]);
    this.inputValues = [];
  }

  loadData(): void {
    this.valueLookups$ = concat(
      of([]), // default items
      this.valueInputControl$.pipe(
        /* filter(res => res.length >= this.minAccountLengthTerm), */
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
        filter(res => res !== null),
        distinctUntilChanged(),
        debounceTime(800),
        tap(() => this.valuesLoading = true),
        switchMap(term => this.valueSuggestionService.search(term).pipe(
          catchError(() => of([])),
          tap(() => this.valuesLoading = false)
        ))
      )
    );
  }

  trackValueByFn(item: any): number {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return item.id!;
  }

  /**
   * Replaces the array received from the parent with the one currently in view
   *
   * @param value
   */
  writeValue(value: ISettlement[]): void {
    if (value.length !== 0) {
      this.inputValues = value
    }
  }

  /**
   * Emits updated array to parent
   */
  getValues(): void {
    this.valueSelected.emit(this.inputValues);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  createNew(): void {

    this.router.navigate(['settlement/extension/new']);
  }
}
