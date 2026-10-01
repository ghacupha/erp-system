///
/// Erp System - Mark X No 12 (Kadar Series) Client 1.8.0
/// Copyright © 2021 - 2026 Edwin Njeru (mailnjeru@gmail.com)
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
import { BehaviorSubject, merge, Observable, of, Subject } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, filter, switchMap, tap } from 'rxjs/operators';
import { SettlementSuggestionService } from '../../suggestion/settlement-suggestion.service';
import { ISettlement } from '../../../erp-settlements/settlement/settlement.model';

@Component({
  selector: 'jhi-m21-settlement-form-control',
  templateUrl: './m21-settlement-form-control.component.html',
  styleUrls: ['./m21-settlement-form-control.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => M21SettlementFormControlComponent),
      multi: true
    }
  ]
})
export class M21SettlementFormControlComponent implements OnInit, ControlValueAccessor, OnDestroy {

  @Input() inputValue: ISettlement = {}

  @Input() inputControlLabel = '';

  @Output() valueSelected: EventEmitter<ISettlement> = new EventEmitter<ISettlement>();

  // Purely informational to the owning form - this component has no opinion on what "create
  // new" should do (navigate where, capture what state to come back to, etc). The owning form
  // handles that itself; see e.g. PrepaymentAccountUpdateComponent's handling of this output and
  // inlineCreateStarted in store/actions/inline-create-stack.actions.ts.
  @Output() createNewRequested: EventEmitter<void> = new EventEmitter<void>();

  minAccountLengthTerm = 3;
  valuesLoading = false;
  valueControlInput$ = new Subject<string>();
  valueLookUps$: Observable<ISettlement[]> = of([]);

  // ng-select only renders inputValue's bindLabel correctly when that exact object is present in
  // the bound [items] list. The typeahead-driven valueLookUps$ below only ever contains whatever
  // the user last searched for, so a value assigned externally via writeValue() (e.g. restoring a
  // settlement created through the inline-create workflow, well after ngOnInit already ran) was
  // never actually in [items] - ng-select silently rendered blank despite inputValue being
  // correct. This subject always re-injects the current value into the merged items stream.
  private selectedValue$ = new BehaviorSubject<ISettlement[]>([]);

  constructor(
    protected valueSuggestionService: SettlementSuggestionService
  ) {}

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
    this.inputValue = {}
  }

  loadValues(): void {
    this.valueLookUps$ = merge(
      this.selectedValue$,
      this.valueControlInput$.pipe(
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
      ),
    );
  }

  trackValueByFn(item: any): number {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return item.id!;
  }

  writeValue(value: ISettlement): void {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (value) {
      this.inputValue = value;
      if (value.id) {
        this.selectedValue$.next([value]);
      }
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

  createNew(): void {
    this.createNewRequested.emit();
  }
}
