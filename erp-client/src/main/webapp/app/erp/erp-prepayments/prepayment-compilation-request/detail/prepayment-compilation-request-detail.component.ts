///
/// Erp System - Mark X No 12 (Kadar Series) Client 1.8.0
/// Copyright © 2021 - 2026 Edwin Njeru (mailnjeru@gmail.com)
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
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

import { IPrepaymentCompilationRequest } from '../prepayment-compilation-request.model';
import { ICompiledPrepaymentAccountReport } from '../compiled-prepayment-account-report.model';
import { CompiledPrepaymentAccountReportService } from '../service/compiled-prepayment-account-report.service';
import { ASC, DESC } from 'app/config/pagination.constants';

@Component({
  selector: 'jhi-prepayment-compilation-request-detail',
  templateUrl: './prepayment-compilation-request-detail.component.html',
})
export class PrepaymentCompilationRequestDetailComponent implements OnInit {
  prepaymentCompilationRequest: IPrepaymentCompilationRequest | null = null;

  compiledAccounts: ICompiledPrepaymentAccountReport[] = [];
  compiledAccountsLoading = false;
  compiledAccountsTotalItems = 0;
  compiledAccountsItemsPerPage = 5;
  compiledAccountsPage = 1;
  compiledAccountsPredicate = 'recognitionDate';
  compiledAccountsAscending = false;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected compiledPrepaymentAccountReportService: CompiledPrepaymentAccountReportService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prepaymentCompilationRequest }) => {
      this.prepaymentCompilationRequest = prepaymentCompilationRequest;
      if (this.prepaymentCompilationRequest?.id) {
        this.loadCompiledAccounts();
      }
    });
  }

  loadCompiledAccountsPage(page: number): void {
    this.compiledAccountsPage = page;
    this.loadCompiledAccounts();
  }

  sortCompiledAccounts(): void {
    this.compiledAccountsPage = 1;
    this.loadCompiledAccounts();
  }

  loadCompiledAccounts(): void {
    if (!this.prepaymentCompilationRequest?.id) {
      return;
    }
    this.compiledAccountsLoading = true;
    this.compiledPrepaymentAccountReportService
      .query(this.prepaymentCompilationRequest.id, {
        page: this.compiledAccountsPage - 1,
        size: this.compiledAccountsItemsPerPage,
        sort: this.compiledAccountsSort(),
      })
      .subscribe({
        next: (res: HttpResponse<ICompiledPrepaymentAccountReport[]>) => {
          this.compiledAccountsLoading = false;
          this.compiledAccountsTotalItems = Number(res.headers.get('X-Total-Count'));
          this.compiledAccounts = res.body ?? [];
        },
        error: () => {
          this.compiledAccountsLoading = false;
        },
      });
  }

  trackCompiledAccountId(index: number, item: ICompiledPrepaymentAccountReport): number {
    return item.prepaymentAccountId!;
  }

  previousState(): void {
    window.history.back();
  }

  protected compiledAccountsSort(): string[] {
    return [this.compiledAccountsPredicate + ',' + (this.compiledAccountsAscending ? ASC : DESC)];
  }
}
