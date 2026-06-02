import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

import { ILeaseLiabilityScheduleReportItem } from '../lease-liability-schedule-report-item.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { LeaseLiabilityScheduleReportItemService } from '../service/lease-liability-schedule-report-item.service';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-lease-liability-schedule-report-item',
  templateUrl: './lease-liability-schedule-report-item.component.html',
})
export class LeaseLiabilityScheduleReportItemComponent implements OnInit {
  leaseLiabilityScheduleReportItems: ILeaseLiabilityScheduleReportItem[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected leaseLiabilityScheduleReportItemService: LeaseLiabilityScheduleReportItemService,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.leaseLiabilityScheduleReportItems = [];
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
      this.leaseLiabilityScheduleReportItemService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<ILeaseLiabilityScheduleReportItem[]>) => {
            this.isLoading = false;
            this.paginateLeaseLiabilityScheduleReportItems(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.leaseLiabilityScheduleReportItemService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ILeaseLiabilityScheduleReportItem[]>) => {
          this.isLoading = false;
          this.paginateLeaseLiabilityScheduleReportItems(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.leaseLiabilityScheduleReportItems = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.leaseLiabilityScheduleReportItems = [];
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

  trackId(index: number, item: ILeaseLiabilityScheduleReportItem): number {
    return item.id!;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateLeaseLiabilityScheduleReportItems(data: ILeaseLiabilityScheduleReportItem[] | null, headers: HttpHeaders): void {
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
        this.leaseLiabilityScheduleReportItems.push(d);
      }
    }
  }
}
