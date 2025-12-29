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

import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { ILiabilityEnumeration } from '../liability-enumeration.model';
import { LiabilityEnumerationService } from '../service/liability-enumeration.service';
import { AlertService } from 'app/core/util/alert.service';
import { ITEMS_PER_PAGE, ASC, DESC } from 'app/config/pagination.constants';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { Store } from '@ngrx/store';
import { setSelectedLiabilityEnumeration } from '../state/liability-enumeration.actions';

@Component({
  selector: 'jhi-liability-enumeration',
  templateUrl: './liability-enumeration.component.html',
})
export class LiabilityEnumerationComponent implements OnInit {
  liabilityEnumerations: ILiabilityEnumeration[] = [];
  isLoading = false;
  itemsPerPage = ITEMS_PER_PAGE;
  links: { [key: string]: number } = { last: 0 };
  page = 0;
  predicate = 'requestDateTime';
  ascending = false;

  constructor(
    protected liabilityEnumerationService: LiabilityEnumerationService,
    protected alertService: AlertService,
    protected router: Router,
    protected parseLinks: ParseLinks,
    protected store: Store
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.isLoading = true;
    this.liabilityEnumerationService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ILiabilityEnumeration[]>) => {
          this.isLoading = false;
          this.paginateLiabilityEnumerations(res.body, res.headers);
        },

        error: (err: HttpErrorResponse) => {
          this.isLoading = false;
          this.alertService.addHttpErrorResponse(err);
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.liabilityEnumerations = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  trackId(index: number, item: ILiabilityEnumeration): number {
    return item.id!;
  }

  viewPresentValues(item: ILiabilityEnumeration): void {
    const bookingId = item.leaseContract?.bookingId ?? (item.leaseContractId ? item.leaseContractId.toString() : undefined);
    this.store.dispatch(
      setSelectedLiabilityEnumeration({
        liabilityEnumerationId: item.id,
        bookingId,
      })
    );
    if (item.id) {
      this.router.navigate(['liability-enumeration', item.id, 'present-values']);
    }
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateLiabilityEnumerations(data: ILiabilityEnumeration[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    this.links = linkHeader ? this.parseLinks.parse(linkHeader) : { last: 0 };
    if (data) {
      this.liabilityEnumerations.push(...data);
    }
  }
}
