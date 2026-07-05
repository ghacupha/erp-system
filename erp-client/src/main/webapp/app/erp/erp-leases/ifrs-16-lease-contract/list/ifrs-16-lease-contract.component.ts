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

import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { IFRS16LeaseContractService } from '../service/ifrs-16-lease-contract.service';
import { IFRS16LeaseContractDeleteDialogComponent } from '../delete/ifrs-16-lease-contract-delete-dialog.component';
import {
  ifrs16LeaseContractCopyWorkflowInitiatedFromList,
  ifrs16LeaseContractCopyWorkflowInitiatedFromView,
  ifrs16LeaseContractCreationWorkflowInitiatedFromList,
  ifrs16LeaseContractEditWorkflowInitiatedFromList,
  ifrs16LeaseContractEditWorkflowInitiatedFromView
} from '../../../store/actions/ifrs16-lease-model-update-status.actions';
import {
  leaseTemplateCreationFromLeaseContractInitiatedFromList
} from '../../../store/actions/lease-template-update-status.actions';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';

@Component({
  selector: 'jhi-ifrs-16-lease-contract',
  templateUrl: './ifrs-16-lease-contract.component.html',
})
export class IFRS16LeaseContractComponent implements OnInit {
  iFRS16LeaseContracts?: IIFRS16LeaseContract[];
  currentSearch: string;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected iFRS16LeaseContractService: IFRS16LeaseContractService,
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
      this.iFRS16LeaseContractService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IIFRS16LeaseContract[]>) => {
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

    this.iFRS16LeaseContractService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IIFRS16LeaseContract[]>) => {
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
    if (query && ['bookingId', 'leaseTitle', 'shortTitle', 'description', 'serialNumber'].includes(this.predicate)) {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadPage(1);
  }

  createButtonEvent(): void {
    this.store.dispatch(ifrs16LeaseContractCreationWorkflowInitiatedFromList())
  }

  editButtonEvent(instance: IIFRS16LeaseContract): void {
    this.store.dispatch(ifrs16LeaseContractEditWorkflowInitiatedFromList({editedInstance: instance}))
  }

  copyButtonEvent(instance: IIFRS16LeaseContract): void {
    this.store.dispatch(ifrs16LeaseContractCopyWorkflowInitiatedFromList({copiedInstance: instance}))
  }

  createTemplateButtonEvent(instance: IIFRS16LeaseContract): void {
    this.store.dispatch(leaseTemplateCreationFromLeaseContractInitiatedFromList({ sourceLeaseContract: instance }));
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: IIFRS16LeaseContract): number {
    return item.id!;
  }

  delete(iFRS16LeaseContract: IIFRS16LeaseContract): void {
    const modalRef = this.modalService.open(IFRS16LeaseContractDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.iFRS16LeaseContract = iFRS16LeaseContract;
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

  protected onSuccess(data: IIFRS16LeaseContract[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.ngbPaginationPage = this.page;
    if (navigate) {
      this.router.navigate(['/ifrs-16-lease-contract'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          search: this.currentSearch,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.iFRS16LeaseContracts = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
