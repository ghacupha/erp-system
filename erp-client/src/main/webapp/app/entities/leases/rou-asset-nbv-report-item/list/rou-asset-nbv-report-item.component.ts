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

import { IRouAssetNBVReportItem } from '../rou-asset-nbv-report-item.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { RouAssetNBVReportItemService } from '../service/rou-asset-nbv-report-item.service';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-rou-asset-nbv-report-item',
  templateUrl: './rou-asset-nbv-report-item.component.html',
})
export class RouAssetNBVReportItemComponent implements OnInit {
  rouAssetNBVReportItems: IRouAssetNBVReportItem[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected rouAssetNBVReportItemService: RouAssetNBVReportItemService,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.rouAssetNBVReportItems = [];
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
      this.rouAssetNBVReportItemService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IRouAssetNBVReportItem[]>) => {
            this.isLoading = false;
            this.paginateRouAssetNBVReportItems(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.rouAssetNBVReportItemService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IRouAssetNBVReportItem[]>) => {
          this.isLoading = false;
          this.paginateRouAssetNBVReportItems(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.rouAssetNBVReportItems = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.rouAssetNBVReportItems = [];
    this.links = {
      last: 0,
    };
    this.page = 0;
    if (
      query &&
      ['modelTitle', 'description', 'rouModelReference', 'assetCategoryName', 'assetAccountNumber', 'depreciationAccountNumber'].includes(
        this.predicate
      )
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

  trackId(index: number, item: IRouAssetNBVReportItem): number {
    return item.id!;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRouAssetNBVReportItems(data: IRouAssetNBVReportItem[] | null, headers: HttpHeaders): void {
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
        this.rouAssetNBVReportItems.push(d);
      }
    }
  }
}
