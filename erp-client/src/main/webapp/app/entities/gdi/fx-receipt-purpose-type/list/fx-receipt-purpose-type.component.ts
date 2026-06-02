import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFxReceiptPurposeType } from '../fx-receipt-purpose-type.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { FxReceiptPurposeTypeService } from '../service/fx-receipt-purpose-type.service';
import { FxReceiptPurposeTypeDeleteDialogComponent } from '../delete/fx-receipt-purpose-type-delete-dialog.component';

@Component({
  selector: 'jhi-fx-receipt-purpose-type',
  templateUrl: './fx-receipt-purpose-type.component.html',
})
export class FxReceiptPurposeTypeComponent implements OnInit {
  fxReceiptPurposeTypes?: IFxReceiptPurposeType[];
  currentSearch: string;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected fxReceiptPurposeTypeService: FxReceiptPurposeTypeService,
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
      this.fxReceiptPurposeTypeService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IFxReceiptPurposeType[]>) => {
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

    this.fxReceiptPurposeTypeService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IFxReceiptPurposeType[]>) => {
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
      [
        'itemCode',
        'attribute1ReceiptPaymentPurposeCode',
        'attribute1ReceiptPaymentPurposeType',
        'attribute2ReceiptPaymentPurposeCode',
        'attribute2ReceiptPaymentPurposeDescription',
        'attribute3ReceiptPaymentPurposeCode',
        'attribute3ReceiptPaymentPurposeDescription',
        'attribute4ReceiptPaymentPurposeCode',
        'attribute4ReceiptPaymentPurposeDescription',
        'attribute5ReceiptPaymentPurposeCode',
        'attribute5ReceiptPaymentPurposeDescription',
        'lastChild',
      ].includes(this.predicate)
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

  trackId(index: number, item: IFxReceiptPurposeType): number {
    return item.id!;
  }

  delete(fxReceiptPurposeType: IFxReceiptPurposeType): void {
    const modalRef = this.modalService.open(FxReceiptPurposeTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fxReceiptPurposeType = fxReceiptPurposeType;
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

  protected onSuccess(data: IFxReceiptPurposeType[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.ngbPaginationPage = this.page;
    if (navigate) {
      this.router.navigate(['/fx-receipt-purpose-type'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          search: this.currentSearch,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.fxReceiptPurposeTypes = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
