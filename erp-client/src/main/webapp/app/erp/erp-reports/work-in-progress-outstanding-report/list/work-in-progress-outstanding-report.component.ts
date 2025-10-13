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
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, Subject } from 'rxjs';

import { IWorkInProgressOutstandingReport } from '../work-in-progress-outstanding-report.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { WorkInProgressOutstandingReportService } from '../service/work-in-progress-outstanding-report.service';
import * as dayjs from 'dayjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { DATE_FORMAT } from '../../../../config/input.constants';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-work-in-progress-outstanding-report',
  templateUrl: './work-in-progress-outstanding-report.component.html'
})
export class WorkInProgressOutstandingReportComponent implements OnInit {
  workInProgressOutstandingReports?: IWorkInProgressOutstandingReport[];
  currentSearch: string;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  outstandingAmount = 0;
  selectedReportDate: dayjs.Dayjs = dayjs();
  selectedNavDate = dayjs().format(DATE_FORMAT);

  reportDateControlInput$ = new Subject<dayjs.Dayjs>();

  constructor(
    protected workInProgressOutstandingReportService: WorkInProgressOutstandingReportService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected parseLinks: ParseLinks,
    protected route: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';

  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    if (this.currentSearch) {
      this.workInProgressOutstandingReportService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe(
          (res: HttpResponse<IWorkInProgressOutstandingReport[]>) => {
            this.isLoading = false;
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          () => {
            this.isLoading = false;
            this.onError();
          }
        );
      return;
    }

    this.workInProgressOutstandingReportService
      .queryByReportDate(this.selectedReportDate,
        {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        })
      .subscribe(
        (res: HttpResponse<IWorkInProgressOutstandingReport[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  search(query: string): void {
    if (query && ['sequenceNumber', 'particulars', 'dealerName', 'instalmentTransactionNumber', 'iso4217Code'].includes(this.predicate)) {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadPage(1);
  }

  ngOnInit(): void {
    this.handleNavigation();

    this.reportDateControlInput$.pipe(
      debounceTime(500),
      distinctUntilChanged(),
    ).subscribe(() => this.loadPage(1));

  }

  onDateInputChange(): void {
    this.reportDateControlInput$.next(this.selectedReportDate);
    this.selectedNavDate = this.selectedReportDate.format(DATE_FORMAT);
  }

  trackId(index: number, item: IWorkInProgressOutstandingReport): number {
    return item.id!;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IWorkInProgressOutstandingReport[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.ngbPaginationPage = this.page;
    if (navigate) {
      this.router.navigate(['/work-in-progress-outstanding-report'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          search: this.currentSearch,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC)
        }
      });
    }
    this.workInProgressOutstandingReports = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
