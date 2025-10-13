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

import { Component, EventEmitter, forwardRef, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { concat, Observable, of, Subject } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, filter, switchMap, tap } from 'rxjs/operators';
import { IAssetWarranty } from '../../../erp-assets/asset-warranty/asset-warranty.model';
import { AssetWarrantyService } from '../../../erp-assets/asset-warranty/service/asset-warranty.service';
import { AssetWarrantySuggestionService } from '../../suggestion/asset-warranty-suggestion.service';

@Component({
  selector: 'jhi-m2m-asset-warranty-form-control',
  templateUrl: './m2m-asset-warranty-form-control.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => M2mAssetWarrantyFormComponent),
      multi: true
    }
  ]
})
export class M2mAssetWarrantyFormComponent implements OnInit, OnDestroy, ControlValueAccessor {

  @Input() inputValues: IAssetWarranty[] = [];

  @Input() inputControlLabel = '';

  @Output() selectedValues: EventEmitter<IAssetWarranty[]> = new EventEmitter<IAssetWarranty[]>();

  minAccountLengthTerm = 3;
  valuesLoading = false;
  valueInputControl$ = new Subject<string>();
  valueLookUps$: Observable<IAssetWarranty[]> = of([]);

  constructor(
    protected valueService: AssetWarrantyService,
    protected valueSuggestionService: AssetWarrantySuggestionService
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

  trackValuesByFn(item: any): number {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return item.id!;
  }

  /**
   * Replaces the array received from the parent with the one currently in view
   *
   * @param value
   */
  writeValue(value: IAssetWarranty[]): void {
    if (value.length !== 0) {
      this.inputValues = value
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
