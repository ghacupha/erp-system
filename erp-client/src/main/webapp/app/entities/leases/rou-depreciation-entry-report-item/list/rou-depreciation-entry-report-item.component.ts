import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

import { IRouDepreciationEntryReportItem } from '../rou-depreciation-entry-report-item.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { RouDepreciationEntryReportItemService } from '../service/rou-depreciation-entry-report-item.service';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-rou-depreciation-entry-report-item',
  templateUrl: './rou-depreciation-entry-report-item.component.html',
})
export class RouDepreciationEntryReportItemComponent implements OnInit {
  rouDepreciationEntryReportItems: IRouDepreciationEntryReportItem[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected rouDepreciationEntryReportItemService: RouDepreciationEntryReportItemService,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.rouDepreciationEntryReportItems = [];
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
      this.rouDepreciationEntryReportItemService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IRouDepreciationEntryReportItem[]>) => {
            this.isLoading = false;
            this.paginateRouDepreciationEntryReportItems(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.rouDepreciationEntryReportItemService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IRouDepreciationEntryReportItem[]>) => {
          this.isLoading = false;
          this.paginateRouDepreciationEntryReportItems(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.rouDepreciationEntryReportItems = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.rouDepreciationEntryReportItems = [];
    this.links = {
      last: 0,
    };
    this.page = 0;
    if (
      query &&
      [
        'leaseContractNumber',
        'fiscalPeriodCode',
        'assetCategoryName',
        'debitAccountNumber',
        'creditAccountNumber',
        'description',
        'shortTitle',
        'rouAssetIdentifier',
      ].includes(this.predicate)
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

  trackId(index: number, item: IRouDepreciationEntryReportItem): number {
    return item.id!;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRouDepreciationEntryReportItems(data: IRouDepreciationEntryReportItem[] | null, headers: HttpHeaders): void {
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
        this.rouDepreciationEntryReportItems.push(d);
      }
    }
  }
}
