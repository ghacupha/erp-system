import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

import { IRouDepreciationPostingReportItem } from '../rou-depreciation-posting-report-item.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { RouDepreciationPostingReportItemService } from '../service/rou-depreciation-posting-report-item.service';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-rou-depreciation-posting-report-item',
  templateUrl: './rou-depreciation-posting-report-item.component.html',
})
export class RouDepreciationPostingReportItemComponent implements OnInit {
  rouDepreciationPostingReportItems: IRouDepreciationPostingReportItem[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected rouDepreciationPostingReportItemService: RouDepreciationPostingReportItemService,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.rouDepreciationPostingReportItems = [];
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
      this.rouDepreciationPostingReportItemService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IRouDepreciationPostingReportItem[]>) => {
            this.isLoading = false;
            this.paginateRouDepreciationPostingReportItems(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.rouDepreciationPostingReportItemService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IRouDepreciationPostingReportItem[]>) => {
          this.isLoading = false;
          this.paginateRouDepreciationPostingReportItems(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.rouDepreciationPostingReportItems = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.rouDepreciationPostingReportItems = [];
    this.links = {
      last: 0,
    };
    this.page = 0;
    if (
      query &&
      ['leaseContractNumber', 'leaseDescription', 'fiscalMonthCode', 'accountForCredit', 'accountForDebit'].includes(this.predicate)
    ) {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRouDepreciationPostingReportItem): number {
    return item.id!;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRouDepreciationPostingReportItems(data: IRouDepreciationPostingReportItem[] | null, headers: HttpHeaders): void {
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
        this.rouDepreciationPostingReportItems.push(d);
      }
    }
  }
}
