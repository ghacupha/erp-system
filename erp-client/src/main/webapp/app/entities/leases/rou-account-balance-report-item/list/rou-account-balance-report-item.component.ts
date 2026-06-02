import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

import { IRouAccountBalanceReportItem } from '../rou-account-balance-report-item.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { RouAccountBalanceReportItemService } from '../service/rou-account-balance-report-item.service';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-rou-account-balance-report-item',
  templateUrl: './rou-account-balance-report-item.component.html',
})
export class RouAccountBalanceReportItemComponent implements OnInit {
  rouAccountBalanceReportItems: IRouAccountBalanceReportItem[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected rouAccountBalanceReportItemService: RouAccountBalanceReportItemService,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.rouAccountBalanceReportItems = [];
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
      this.rouAccountBalanceReportItemService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IRouAccountBalanceReportItem[]>) => {
            this.isLoading = false;
            this.paginateRouAccountBalanceReportItems(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.rouAccountBalanceReportItemService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IRouAccountBalanceReportItem[]>) => {
          this.isLoading = false;
          this.paginateRouAccountBalanceReportItems(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.rouAccountBalanceReportItems = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.rouAccountBalanceReportItems = [];
    this.links = {
      last: 0,
    };
    this.page = 0;
    if (query && ['assetAccountName', 'assetAccountNumber', 'depreciationAccountNumber'].includes(this.predicate)) {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRouAccountBalanceReportItem): number {
    return item.id!;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRouAccountBalanceReportItems(data: IRouAccountBalanceReportItem[] | null, headers: HttpHeaders): void {
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
        this.rouAccountBalanceReportItems.push(d);
      }
    }
  }
}
