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

import { AfterContentInit, ContentChild, Directive, Host, HostListener, Input, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faSort, faSortDown, faSortUp, IconDefinition } from '@fortawesome/free-solid-svg-icons';

import { SortDirective } from './sort.directive';

@Directive({
  selector: '[jhiSortBy]',
})
export class SortByDirective<T> implements AfterContentInit, OnDestroy {
  @Input() jhiSortBy!: T;

  @ContentChild(FaIconComponent, { static: false })
  iconComponent?: FaIconComponent;

  sortIcon = faSort;
  sortAscIcon = faSortUp;
  sortDescIcon = faSortDown;

  private readonly destroy$ = new Subject<void>();

  constructor(@Host() private sort: SortDirective<T>) {
    sort.predicateChange.pipe(takeUntil(this.destroy$)).subscribe(() => this.updateIconDefinition());
    sort.ascendingChange.pipe(takeUntil(this.destroy$)).subscribe(() => this.updateIconDefinition());
  }

  @HostListener('click')
  onClick(): void {
    if (this.iconComponent) {
      this.sort.sort(this.jhiSortBy);
    }
  }

  ngAfterContentInit(): void {
    this.updateIconDefinition();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private updateIconDefinition(): void {
    if (this.iconComponent) {
      let icon: IconDefinition = this.sortIcon;
      if (this.sort.predicate === this.jhiSortBy) {
        icon = this.sort.ascending ? this.sortAscIcon : this.sortDescIcon;
      }
      this.iconComponent.icon = icon.iconName;
      this.iconComponent.render();
    }
  }
}
