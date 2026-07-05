import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

import { IPrepaymentOutstandingOverviewReport } from '../prepayment-outstanding-overview-report.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PrepaymentOutstandingOverviewReportService } from '../service/prepayment-outstanding-overview-report.service';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-prepayment-outstanding-overview-report',
  templateUrl: './prepayment-outstanding-overview-report.component.html',
})
export class PrepaymentOutstandingOverviewReportComponent implements OnInit {
  prepaymentOutstandingOverviewReports: IPrepaymentOutstandingOverviewReport[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected prepaymentOutstandingOverviewReportService: PrepaymentOutstandingOverviewReportService,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.prepaymentOutstandingOverviewReports = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.prepaymentOutstandingOverviewReportService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IPrepaymentOutstandingOverviewReport[]>) => {
            this.isLoading = false;
            this.paginatePrepaymentOutstandingOverviewReports(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.prepaymentOutstandingOverviewReportService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IPrepaymentOutstandingOverviewReport[]>) => {
          this.isLoading = false;
          this.paginatePrepaymentOutstandingOverviewReports(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.prepaymentOutstandingOverviewReports = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.prepaymentOutstandingOverviewReports = [];
    this.links = {
      last: 0,
    };
    this.page = 0;
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPrepaymentOutstandingOverviewReport): number {
    return item.id!;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePrepaymentOutstandingOverviewReports(data: IPrepaymentOutstandingOverviewReport[] | null, headers: HttpHeaders): void {
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
        this.prepaymentOutstandingOverviewReports.push(d);
      }
    }
  }
}
