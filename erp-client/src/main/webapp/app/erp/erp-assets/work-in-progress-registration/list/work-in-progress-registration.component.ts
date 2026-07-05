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
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkInProgressRegistration } from '../work-in-progress-registration.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { WorkInProgressRegistrationService } from '../service/work-in-progress-registration.service';
import { WorkInProgressRegistrationDeleteDialogComponent } from '../delete/work-in-progress-registration-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  wipRegistrationCopyWorkflowInitiatedFromList,
  wipRegistrationCreationWorkflowInitiatedFromList,
  wipRegistrationEditWorkflowInitiatedFromList,
} from '../../../store/actions/wip-registration-update-status.actions';
import { combineLatest } from 'rxjs';

@Component({
  selector: 'jhi-work-in-progress-registration',
  templateUrl: './work-in-progress-registration.component.html',
})
export class WorkInProgressRegistrationComponent implements OnInit {
  workInProgressRegistrations?: IWorkInProgressRegistration[];
  currentSearch: string;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected workInProgressRegistrationService: WorkInProgressRegistrationService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected parseLinks: ParseLinks,
    protected modalService: NgbModal,
    protected store: Store<State>,
  ) {
    this.workInProgressRegistrations = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;

    this.ascending = false;
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  createButtonEvent(): void {
    this.store.dispatch(wipRegistrationCreationWorkflowInitiatedFromList())
  }

  copyButtonEvent(instance: IWorkInProgressRegistration): void {
    this.store.dispatch(wipRegistrationCopyWorkflowInitiatedFromList({ copiedInstance: instance }));
  }

  editButtonEvent(instance: IWorkInProgressRegistration): void {
    this.store.dispatch(wipRegistrationEditWorkflowInitiatedFromList({editedInstance: instance}));
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    if (this.currentSearch) {
      this.workInProgressRegistrationService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IWorkInProgressRegistration[]>) => {
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

    this.workInProgressRegistrationService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IWorkInProgressRegistration[]>) => {
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
    if (query && ['sequenceNumber', 'particulars', 'comments'].includes(this.predicate)) {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadPage(1);
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: IWorkInProgressRegistration): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(workInProgressRegistration: IWorkInProgressRegistration): void {
    const modalRef = this.modalService.open(WorkInProgressRegistrationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.workInProgressRegistration = workInProgressRegistration;
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

  protected onSuccess(data: IWorkInProgressRegistration[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.ngbPaginationPage = this.page;
    if (navigate) {
      this.router.navigate(['/work-in-progress-registration'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          search: this.currentSearch,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.workInProgressRegistrations = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
