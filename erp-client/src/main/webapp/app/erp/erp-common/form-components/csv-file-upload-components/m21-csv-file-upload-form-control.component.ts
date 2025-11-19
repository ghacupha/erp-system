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
import { ICsvFileUpload } from '../../../erp-files/csv-file-upload/csv-file-upload.model';
import { CsvFileUploadSuggestionService } from './csv-file-upload-suggestion.service';

@Component({
  selector: 'jhi-m21-csv-file-upload-form-control',
  templateUrl: './m21-csv-file-upload-form-control.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => M21CsvFileUploadFormControlComponent),
      multi: true,
    },
  ],
})
export class M21CsvFileUploadFormControlComponent implements OnInit, ControlValueAccessor, OnDestroy {
  @Input() inputValue: ICsvFileUpload = {};
  @Input() inputControlLabel = '';
  @Input() disabledInput = false;
  @Output() valueSelected = new EventEmitter<ICsvFileUpload>();

  minAccountLengthTerm = 2;
  valuesLoading = false;
  valueControlInput$ = new Subject<string>();
  valueLookUps$: Observable<ICsvFileUpload[]> = of([]);

  constructor(private valueSuggestionService: CsvFileUploadSuggestionService) {}

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

  trackValueByFn(item: ICsvFileUpload): number {
    return item.id!;
  }

  writeValue(value: ICsvFileUpload): void {
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
