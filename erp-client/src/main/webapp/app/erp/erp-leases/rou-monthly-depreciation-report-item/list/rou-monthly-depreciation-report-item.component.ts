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
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRouMonthlyDepreciationReportItem } from '../rou-monthly-depreciation-report-item.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { RouMonthlyDepreciationReportItemService } from '../service/rou-monthly-depreciation-report-item.service';
import { RouMonthlyDepreciationReportItemDeleteDialogComponent } from '../delete/rou-monthly-depreciation-report-item-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-rou-monthly-depreciation-report-item',
  templateUrl: './rou-monthly-depreciation-report-item.component.html',
})
export class RouMonthlyDepreciationReportItemComponent implements OnInit {
  rouMonthlyDepreciationReportItems: IRouMonthlyDepreciationReportItem[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected rouMonthlyDepreciationReportItemService: RouMonthlyDepreciationReportItemService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.rouMonthlyDepreciationReportItems = [];
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
      this.rouMonthlyDepreciationReportItemService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IRouMonthlyDepreciationReportItem[]>) => {
            this.isLoading = false;
            this.paginateRouMonthlyDepreciationReportItems(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.rouMonthlyDepreciationReportItemService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IRouMonthlyDepreciationReportItem[]>) => {
          this.isLoading = false;
          this.paginateRouMonthlyDepreciationReportItems(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.rouMonthlyDepreciationReportItems = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.rouMonthlyDepreciationReportItems = [];
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

  trackId(index: number, item: IRouMonthlyDepreciationReportItem): number {
    return item.id!;
  }

  delete(rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem): void {
    const modalRef = this.modalService.open(RouMonthlyDepreciationReportItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.rouMonthlyDepreciationReportItem = rouMonthlyDepreciationReportItem;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRouMonthlyDepreciationReportItems(data: IRouMonthlyDepreciationReportItem[] | null, headers: HttpHeaders): void {
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
        this.rouMonthlyDepreciationReportItems.push(d);
      }
    }
  }
}
