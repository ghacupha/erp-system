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

import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

import { IPrepaymentReport } from '../prepayment-report.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PrepaymentReportService } from '../service/prepayment-report.service';
import { ParseLinks } from 'app/core/util/parse-links.service';
import dayjs from 'dayjs';
import { DATE_FORMAT } from '../../../../config/input.constants';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'jhi-prepayment-report',
  templateUrl: './prepayment-report.component.html',
})
export class PrepaymentReportComponent implements OnInit {
  prepaymentReports: IPrepaymentReport[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;
  reportDate: dayjs.Dayjs = dayjs();
  selectedNavDate = dayjs().format(DATE_FORMAT);

  reportDateControlInput$ = new Subject<dayjs.Dayjs>();

  constructor(
    protected prepaymentReportService: PrepaymentReportService,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.prepaymentReports = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'catalogue_number';
    this.ascending = true;
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.prepaymentReportService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IPrepaymentReport[]>) => {
            this.isLoading = false;
            this.paginatePrepaymentReports(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.prepaymentReportService
      .queryByReportDate({
        reportDate: this.reportDate.format(DATE_FORMAT),
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IPrepaymentReport[]>) => {
          this.isLoading = false;
          this.paginatePrepaymentReports(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.prepaymentReports = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.prepaymentReports = [];
    this.links = {
      last: 0,
    };
    this.page = 0;
    if (query && ['catalogueNumber', 'particulars', 'dealerName', 'paymentNumber', 'currencyCode'].includes(this.predicate)) {
      this.predicate = 'catalogue_number';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();

    this.reportDateControlInput$.pipe(
      debounceTime(500),
      distinctUntilChanged(),
    ).subscribe(() => this.loadPage(1));
  }

  onDateInputChange(): void {
    this.reportDateControlInput$.next(this.reportDate);
    this.selectedNavDate = this.reportDate.format(DATE_FORMAT);
  }

  trackId(index: number, item: IPrepaymentReport): number {
    return item.id!;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePrepaymentReports(data: IPrepaymentReport[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.prepaymentReports.push(d);
      }
    }
  }
}
