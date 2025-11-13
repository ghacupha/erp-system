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

import { Directive, EventEmitter, Input, Output } from '@angular/core';

@Directive({
  selector: '[jhiSort]',
})
export class SortDirective<T> {
  @Input()
  get predicate(): T | undefined {
    return this._predicate;
  }
  set predicate(predicate: T | undefined) {
    this._predicate = predicate;
    this.predicateChange.emit(predicate);
  }

  @Input()
  get ascending(): boolean | undefined {
    return this._ascending;
  }
  set ascending(ascending: boolean | undefined) {
    this._ascending = ascending;
    this.ascendingChange.emit(ascending);
  }

  @Output() predicateChange = new EventEmitter<T>();
  @Output() ascendingChange = new EventEmitter<boolean>();
  @Output() sortChange = new EventEmitter<{ predicate: T; ascending: boolean }>();

  private _predicate?: T;
  private _ascending?: boolean;

  sort(field: T): void {
    this.ascending = field !== this.predicate ? true : !this.ascending;
    this.predicate = field;
    this.predicateChange.emit(field);
    this.ascendingChange.emit(this.ascending);
    this.sortChange.emit({ predicate: this.predicate, ascending: this.ascending });
  }
}
