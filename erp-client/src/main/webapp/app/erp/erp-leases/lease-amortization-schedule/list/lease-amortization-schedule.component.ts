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
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaseAmortizationSchedule } from '../lease-amortization-schedule.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { LeaseAmortizationScheduleService } from '../service/lease-amortization-schedule.service';
import { LeaseAmortizationScheduleDeleteDialogComponent } from '../delete/lease-amortization-schedule-delete-dialog.component';

@Component({
  selector: 'jhi-lease-amortization-schedule',
  templateUrl: './lease-amortization-schedule.component.html',
})
export class LeaseAmortizationScheduleComponent implements OnInit {
  leaseAmortizationSchedules?: ILeaseAmortizationSchedule[];
  currentSearch: string;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  rowActionLoading: Record<number, boolean> = {};
  rowActionError: Record<number, string | undefined> = {};

  constructor(
    protected leaseAmortizationScheduleService: LeaseAmortizationScheduleService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    if (this.currentSearch) {
      this.leaseAmortizationScheduleService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<ILeaseAmortizationSchedule[]>) => {
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

    this.leaseAmortizationScheduleService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ILeaseAmortizationSchedule[]>) => {
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
    if (query && ['identifier'].includes(this.predicate)) {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadPage(1);
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: ILeaseAmortizationSchedule): number {
    return item.id!;
  }

  delete(leaseAmortizationSchedule: ILeaseAmortizationSchedule): void {
    const modalRef = this.modalService.open(LeaseAmortizationScheduleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.leaseAmortizationSchedule = leaseAmortizationSchedule;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  activateSchedule(leaseAmortizationSchedule: ILeaseAmortizationSchedule): void {
    const id = leaseAmortizationSchedule.id;
    if (id === undefined) {
      return;
    }
    this.setRowActionState(id, true, undefined);
    this.leaseAmortizationScheduleService.activate(id).subscribe({
      next: response => this.onRowActionSuccess(id, response.body, true),
      error: () => this.onRowActionError(id, 'Unable to activate schedule. Please retry.'),
    });
  }

  deactivateSchedule(leaseAmortizationSchedule: ILeaseAmortizationSchedule): void {
    const id = leaseAmortizationSchedule.id;
    if (id === undefined) {
      return;
    }
    this.setRowActionState(id, true, undefined);
    this.leaseAmortizationScheduleService.deactivate(id).subscribe({
      next: response => this.onRowActionSuccess(id, response.body, false),
      error: () => this.onRowActionError(id, 'Unable to deactivate schedule. Please retry.'),
    });
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

  protected onSuccess(data: ILeaseAmortizationSchedule[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.ngbPaginationPage = this.page;
    if (navigate) {
      this.router.navigate(['/lease-amortization-schedule'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          search: this.currentSearch,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.leaseAmortizationSchedules = data ?? [];
    this.rowActionLoading = {};
    this.rowActionError = {};
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  private setRowActionState(id: number, loading: boolean, error: string | undefined): void {
    this.rowActionLoading = { ...this.rowActionLoading, [id]: loading };
    this.rowActionError = { ...this.rowActionError, [id]: error };
  }

  private onRowActionSuccess(
    id: number,
    updated: ILeaseAmortizationSchedule | null | undefined,
    desiredActiveState?: boolean
  ): void {
    this.setRowActionState(id, false, undefined);
    if (!this.leaseAmortizationSchedules) {
      return;
    }

    if (!updated && desiredActiveState !== undefined) {
      const existing = this.leaseAmortizationSchedules.find(item => item.id === id);
      if (!existing) {
        return;
      }
      updated = { ...existing, active: desiredActiveState };
    }

    if (!updated) {
      return;
    }
    this.leaseAmortizationSchedules = this.leaseAmortizationSchedules.map(item =>{
      if (updated) {
        return (item.id === updated.id ? { ...item, ...updated } : item);
      } else {
        return {};
      }
    });
  }

  private onRowActionError(id: number, message: string): void {
    this.setRowActionState(id, false, message);
  }
}
