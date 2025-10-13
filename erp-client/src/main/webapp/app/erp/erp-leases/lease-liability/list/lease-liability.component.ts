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
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaseLiability } from '../lease-liability.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { LeaseLiabilityService } from '../service/lease-liability.service';
import { LeaseLiabilityDeleteDialogComponent } from '../delete/lease-liability-delete-dialog.component';
import { IIFRS16LeaseContract } from '../../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  leaseLiabilityCopyWorkflowInitiatedFromList,
  leaseLiabilityCreationInitiatedFromList, leaseLiabilityEditWorkflowInitiatedFromList
} from '../../../store/actions/lease-liability.actions';

@Component({
  selector: 'jhi-lease-liability',
  templateUrl: './lease-liability.component.html',
})
export class LeaseLiabilityComponent implements OnInit {
  leaseLiabilities?: ILeaseLiability[];
  currentSearch: string;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected leaseLiabilityService: LeaseLiabilityService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected store: Store<State>,
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    if (this.currentSearch) {
      this.leaseLiabilityService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<ILeaseLiability[]>) => {
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

    this.leaseLiabilityService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ILeaseLiability[]>) => {
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
    if (query && ['leaseId'].includes(this.predicate)) {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadPage(1);
  }

  createButtonEvent(): void {
    this.store.dispatch(leaseLiabilityCreationInitiatedFromList())
  }

  editButtonEvent(instance: ILeaseLiability): void {
    this.store.dispatch(leaseLiabilityEditWorkflowInitiatedFromList({editedInstance: instance}))
  }

  copyButtonEvent(instance: ILeaseLiability): void {
    this.store.dispatch(leaseLiabilityCopyWorkflowInitiatedFromList({copiedInstance: instance}))
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: ILeaseLiability): number {
    return item.id!;
  }

  delete(leaseLiability: ILeaseLiability): void {
    const modalRef = this.modalService.open(LeaseLiabilityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.leaseLiability = leaseLiability;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
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

  protected onSuccess(data: ILeaseLiability[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.ngbPaginationPage = this.page;
    if (navigate) {
      this.router.navigate(['/lease-liability'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          search: this.currentSearch,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.leaseLiabilities = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
