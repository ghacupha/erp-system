import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

import { ILeaseLiabilityPostingReportItem } from '../lease-liability-posting-report-item.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { LeaseLiabilityPostingReportItemService } from '../service/lease-liability-posting-report-item.service';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-lease-liability-posting-report-item',
  templateUrl: './lease-liability-posting-report-item.component.html',
})
export class LeaseLiabilityPostingReportItemComponent implements OnInit {
  leaseLiabilityPostingReportItems: ILeaseLiabilityPostingReportItem[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected leaseLiabilityPostingReportItemService: LeaseLiabilityPostingReportItemService,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.leaseLiabilityPostingReportItems = [];
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
      this.leaseLiabilityPostingReportItemService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<ILeaseLiabilityPostingReportItem[]>) => {
            this.isLoading = false;
            this.paginateLeaseLiabilityPostingReportItems(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.leaseLiabilityPostingReportItemService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ILeaseLiabilityPostingReportItem[]>) => {
          this.isLoading = false;
          this.paginateLeaseLiabilityPostingReportItems(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.leaseLiabilityPostingReportItems = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.leaseLiabilityPostingReportItems = [];
    this.links = {
      last: 0,
    };
    this.page = 0;
    if (query && ['bookingId', 'leaseTitle', 'leaseDescription', 'accountNumber', 'posting'].includes(this.predicate)) {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILeaseLiabilityPostingReportItem): number {
    return item.id!;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateLeaseLiabilityPostingReportItems(data: ILeaseLiabilityPostingReportItem[] | null, headers: HttpHeaders): void {
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
        this.leaseLiabilityPostingReportItems.push(d);
      }
    }
  }
}
