import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { ILiabilityEnumeration } from '../liability-enumeration.model';
import { LiabilityEnumerationService } from '../service/liability-enumeration.service';
import { AlertService } from 'app/core/util/alert.service';
import { ITEMS_PER_PAGE, ASC, DESC } from 'app/config/pagination.constants';
import { ParseLinks } from 'app/core/util/parse-links.service';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-liability-enumeration',
  templateUrl: './liability-enumeration.component.html',
})
export class LiabilityEnumerationComponent implements OnInit {
  liabilityEnumerations: ILiabilityEnumeration[] = [];
  isLoading = false;
  itemsPerPage = ITEMS_PER_PAGE;
  links: { [key: string]: number } = { last: 0 };
  page = 0;
  predicate = 'requestDateTime';
  ascending = false;

  constructor(
    protected liabilityEnumerationService: LiabilityEnumerationService,
    protected alertService: AlertService,
    protected router: Router,
    protected parseLinks: ParseLinks
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.isLoading = true;
    this.liabilityEnumerationService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ILiabilityEnumeration[]>) => {
          this.isLoading = false;
          this.paginateLiabilityEnumerations(res.body, res.headers);
        },

        error: (err: HttpErrorResponse) => {
          this.isLoading = false;
          this.alertService.addHttpErrorResponse(err);
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.liabilityEnumerations = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  trackId(index: number, item: ILiabilityEnumeration): number {
    return item.id!;
  }

  viewPresentValues(item: ILiabilityEnumeration): void {
    if (item.id) {
      this.router.navigate(['liability-enumeration', item.id, 'present-values']);
    }
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateLiabilityEnumerations(data: ILiabilityEnumeration[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    this.links = linkHeader ? this.parseLinks.parse(linkHeader) : { last: 0 };
    if (data) {
      this.liabilityEnumerations.push(
        ...data.map(item => ({ ...item, requestDateTime: item.requestDateTime ? dayjs(item.requestDateTime) : undefined }))
      );
    }
  }
}
