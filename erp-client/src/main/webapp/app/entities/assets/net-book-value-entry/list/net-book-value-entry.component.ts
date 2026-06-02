import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { INetBookValueEntry } from '../net-book-value-entry.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { NetBookValueEntryService } from '../service/net-book-value-entry.service';
import { NetBookValueEntryDeleteDialogComponent } from '../delete/net-book-value-entry-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-net-book-value-entry',
  templateUrl: './net-book-value-entry.component.html',
})
export class NetBookValueEntryComponent implements OnInit {
  netBookValueEntries: INetBookValueEntry[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected netBookValueEntryService: NetBookValueEntryService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.netBookValueEntries = [];
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
      this.netBookValueEntryService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<INetBookValueEntry[]>) => {
            this.isLoading = false;
            this.paginateNetBookValueEntries(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.netBookValueEntryService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<INetBookValueEntry[]>) => {
          this.isLoading = false;
          this.paginateNetBookValueEntries(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.netBookValueEntries = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.netBookValueEntries = [];
    this.links = {
      last: 0,
    };
    this.page = 0;
    if (
      query &&
      ['assetNumber', 'assetTag', 'assetDescription', 'nbvIdentifier', 'compilationJobIdentifier', 'compilationBatchIdentifier'].includes(
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

  trackId(index: number, item: INetBookValueEntry): number {
    return item.id!;
  }

  delete(netBookValueEntry: INetBookValueEntry): void {
    const modalRef = this.modalService.open(NetBookValueEntryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.netBookValueEntry = netBookValueEntry;
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

  protected paginateNetBookValueEntries(data: INetBookValueEntry[] | null, headers: HttpHeaders): void {
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
        this.netBookValueEntries.push(d);
      }
    }
  }
}
