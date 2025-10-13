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

import { IMonthlyPrepaymentOutstandingReportItem } from '../monthly-prepayment-outstanding-report-item.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { MonthlyPrepaymentOutstandingReportItemService } from '../service/monthly-prepayment-outstanding-report-item.service';
import { ParseLinks } from 'app/core/util/parse-links.service';
import dayjs from 'dayjs';
import { FiscalYearService } from '../../../erp-pages/fiscal-year/service/fiscal-year.service';

@Component({
  selector: 'jhi-monthly-prepayment-outstanding-report-item',
  templateUrl: './monthly-prepayment-outstanding-report-item.component.html'
})
export class MonthlyPrepaymentOutstandingReportItemComponent implements OnInit {
  monthlyPrepaymentOutstandingReportItems: IMonthlyPrepaymentOutstandingReportItem[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;
  fiscalYearId: number;

  constructor(
    protected monthlyPrepaymentOutstandingReportItemService: MonthlyPrepaymentOutstandingReportItemService,
    protected fiscalYearService: FiscalYearService,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.monthlyPrepaymentOutstandingReportItems = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'end_date';
    this.ascending = true;
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';

    this.fiscalYearId = Number(this.activatedRoute.snapshot.queryParams['fiscalYearId'] ?? '');

  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.monthlyPrepaymentOutstandingReportItemService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe(
          (res: HttpResponse<IMonthlyPrepaymentOutstandingReportItem[]>) => {
            this.isLoading = false;
            this.paginateMonthlyPrepaymentOutstandingReportItems(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    if (!this.fiscalYearId) {
      // Like just in case somehow someone refreshed the view without parameters, may be
      this.monthlyPrepaymentOutstandingReportItemService
        .queryByFiscalPeriod(
          dayjs().startOf('year'),
          dayjs().endOf('year'),
          {
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort()
          })
        .subscribe(
          (res: HttpResponse<IMonthlyPrepaymentOutstandingReportItem[]>) => {
            this.isLoading = false;
            this.paginateMonthlyPrepaymentOutstandingReportItems(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    }

    this.fiscalYearService.find(Number(this.fiscalYearId)).subscribe(fiscalYear => {

      if (fiscalYear.body) {
        this.monthlyPrepaymentOutstandingReportItemService
          .queryByFiscalPeriod(
            fiscalYear.body.startDate,
            fiscalYear.body.endDate,
            {
              page: this.page,
              size: this.itemsPerPage,
              sort: this.sort()
            })
          .subscribe(
            (res: HttpResponse<IMonthlyPrepaymentOutstandingReportItem[]>) => {
              this.isLoading = false;
              this.paginateMonthlyPrepaymentOutstandingReportItems(res.body, res.headers);
            },
            () => {
              this.isLoading = false;
            }
          );

      } else {

        // TODO
      }
    })




  }

  reset(): void {
    this.page = 0;
    this.monthlyPrepaymentOutstandingReportItems = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.monthlyPrepaymentOutstandingReportItems = [];
    this.links = {
      last: 0
    };
    this.page = 0;
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMonthlyPrepaymentOutstandingReportItem): number {
    return item.id!;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'end_date') {
      result.push('end_date');
    }
    return result;
  }

  protected paginateMonthlyPrepaymentOutstandingReportItems(
    data: IMonthlyPrepaymentOutstandingReportItem[] | null,
    headers: HttpHeaders
  ): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0
      };
    }
    if (data) {
      for (const d of data) {
        this.monthlyPrepaymentOutstandingReportItems.push(d);
      }
    }
  }
}
