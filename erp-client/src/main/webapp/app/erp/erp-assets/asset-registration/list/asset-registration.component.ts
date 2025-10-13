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

import { IAssetRegistration } from '../asset-registration.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { AssetRegistrationService } from '../service/asset-registration.service';
import { AssetRegistrationDeleteDialogComponent } from '../delete/asset-registration-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import {
  assetRegistrationCopyWorkflowInitiatedFromList,
  assetRegistrationCreationInitiatedFromList,
  assetRegistrationEditWorkflowInitiatedFromList
} from '../../../store/actions/fixed-assets-register-update-status.actions';

@Component({
  selector: 'jhi-asset-registration',
  templateUrl: './asset-registration.component.html',
})
export class AssetRegistrationComponent implements OnInit {
  assetRegistrations?: IAssetRegistration[];
  currentSearch: string;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = 100;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected assetRegistrationService: AssetRegistrationService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
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
      this.assetRegistrationService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IAssetRegistration[]>) => {
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

    this.assetRegistrationService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IAssetRegistration[]>) => {
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
    if (
      query &&
      ['assetNumber', 'assetTag', 'assetDetails', 'comments', 'modelNumber', 'serialNumber', 'remarks'].includes(this.predicate)
    ) {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadPage(1);
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  createButtonEvent(): void {
    this.store.dispatch(assetRegistrationCreationInitiatedFromList())
  }

  editButtonEvent(instance: IAssetRegistration): void {
    this.store.dispatch(assetRegistrationEditWorkflowInitiatedFromList({ editedInstance: instance }))
  }

  copyButtonEvent(instance: IAssetRegistration): void {
    this.store.dispatch(assetRegistrationCopyWorkflowInitiatedFromList({ copiedInstance: instance }))
  }

  trackId(index: number, item: IAssetRegistration): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(assetRegistration: IAssetRegistration): void {
    const modalRef = this.modalService.open(AssetRegistrationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.assetRegistration = assetRegistration;
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

  protected onSuccess(data: IAssetRegistration[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.ngbPaginationPage = this.page;
    if (navigate) {
      this.router.navigate(['/asset-registration'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          search: this.currentSearch,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.assetRegistrations = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
