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

import { Injectable } from '@angular/core';
import { Observable, of, forkJoin } from 'rxjs';
import { map, catchError, switchMap } from 'rxjs/operators'; // switchMap used in fetchAccountReports fallback
import * as dayjs from 'dayjs';

import { PrepaymentAccountService } from '../prepayment-account/service/prepayment-account.service';
import { PrepaymentAccountReportService } from '../prepayment-account-report/service/prepayment-account-report.service';
import { PrepaymentAmortizationService } from '../prepayment-amortization/service/prepayment-amortization.service';
import { PrepaymentCompilationRequestService } from '../prepayment-compilation-request/service/prepayment-compilation-request.service';
import { IPrepaymentAccountReport } from '../prepayment-account-report/prepayment-account-report.model';
import { IPrepaymentCompilationRequest } from '../prepayment-compilation-request/prepayment-compilation-request.model';

export interface PrepaymentsDashboardTotals {
  totalPrepaymentAmount: number;
  totalAmortisedAmount: number;
  totalOutstandingAmount: number;
  numberOfPrepaymentAccounts: number;
}

export interface PrepaymentsTrendPoint {
  label: string;   // YYYY-MM
  amount: number;
}

export interface PrepaymentsDashboardData {
  dataAsOf: string;                          // latest recognitionDate (YYYY-MM-DD) or today
  totals: PrepaymentsDashboardTotals;
  topAccounts: IPrepaymentAccountReport[];
  trend: PrepaymentsTrendPoint[];
  recentCompilations: IPrepaymentCompilationRequest[];
}

const EMPTY_TOTALS: PrepaymentsDashboardTotals = {
  totalPrepaymentAmount: 0,
  totalAmortisedAmount: 0,
  totalOutstandingAmount: 0,
  numberOfPrepaymentAccounts: 0,
};

@Injectable({ providedIn: 'root' })
export class PrepaymentsDashboardService {
  constructor(
    private prepaymentAccountService: PrepaymentAccountService,
    private accountReportService: PrepaymentAccountReportService,
    private amortizationService: PrepaymentAmortizationService,
    private compilationRequestService: PrepaymentCompilationRequestService,
  ) {}

  /**
   * Load all dashboard panels.
   * Every branch uses catchError so forkJoin always completes — no stuck loading.
   */
  load(): Observable<PrepaymentsDashboardData> {
    // today is used for all balance calculations; it is independent of dataAsOf.
    // dataAsOf is purely informational — it tells the user when the registry was
    // last updated, but does not filter or restrict the dashboard figures.
    const today = dayjs().format('YYYY-MM-DD');

    return forkJoin({
      dataAsOf:           this.fetchLatestRecognitionDate(),
      accountReports:     this.fetchAccountReports(today),
      trend:              this.fetchTrend(),
      recentCompilations: this.fetchRecentCompilations(),
    }).pipe(
      map(({ dataAsOf, accountReports, trend, recentCompilations }) => {
        const totals: PrepaymentsDashboardTotals = {
          totalPrepaymentAmount:    accountReports.reduce((s, r) => s + (r.prepaymentAmount  ?? 0), 0),
          totalAmortisedAmount:     accountReports.reduce((s, r) => s + (r.amortisedAmount   ?? 0), 0),
          totalOutstandingAmount:   accountReports.reduce((s, r) => s + (r.outstandingAmount ?? 0), 0),
          numberOfPrepaymentAccounts: accountReports.length,
        };
        const topAccounts = [...accountReports]
          .sort((a, b) => (b.outstandingAmount ?? 0) - (a.outstandingAmount ?? 0))
          .slice(0, 10);
        return { dataAsOf, totals, topAccounts, trend, recentCompilations };
      })
    );
  }

  // ── Latest recognitionDate from PrepaymentAccount ──────────────────────────
  // This is the cut-off date the header displays so the user knows where to
  // continue updating the registry.
  private fetchLatestRecognitionDate(): Observable<string> {
    return this.prepaymentAccountService.query({
      sort: ['recognitionDate,desc'],
      size: 1,
      page: 0,
    }).pipe(
      map(res => {
        const first = res.body?.[0];
        if (first?.recognitionDate?.isValid()) {
          return first.recognitionDate.format('YYYY-MM-DD');
        }
        return dayjs().format('YYYY-MM-DD');
      }),
      catchError(() => of(dayjs().format('YYYY-MM-DD')))
    );
  }

  // ── Account reports as-of the data date (/reported endpoint) ───────────────
  private fetchAccountReports(reportDate: string): Observable<IPrepaymentAccountReport[]> {
    return this.accountReportService
      .queryByReportDate({ reportDate, size: 2000 })
      .pipe(
        map(res => res.body ?? []),
        switchMap(rows =>
          rows.length > 0
            ? of(rows)
            : this.accountReportService.query({ size: 2000 }).pipe(
                map(res => res.body ?? []),
                catchError(() => of([] as IPrepaymentAccountReport[]))
              )
        ),
        catchError(() =>
          this.accountReportService.query({ size: 2000 }).pipe(
            map(res => res.body ?? []),
            catchError(() => of([] as IPrepaymentAccountReport[]))
          )
        )
      );
  }

  // ── Amortization trend ──────────────────────────────────────────────────────
  // prepaymentPeriod is nullable on amortization entries; fall back to
  // fiscalMonth.endDate (which the backend includes in the DTO) when it is null.
  // dayjs() wraps either a dayjs object or a raw date string safely.
  private fetchTrend(): Observable<PrepaymentsTrendPoint[]> {
    return this.amortizationService.query({
      sort: ['prepaymentPeriod,desc', 'id,desc'],
      size: 2000,
      page: 0,
    }).pipe(
      map(res => {
        const grouped = new Map<string, number>();
        (res.body ?? []).forEach(a => {
          // Resolve the period: direct field → fiscalMonth.endDate → skip
          let periodKey: string | null = null;
          if (a.prepaymentPeriod?.isValid()) {
            periodKey = a.prepaymentPeriod.format('YYYY-MM');
          } else if (a.fiscalMonth?.endDate) {
            const d = dayjs(a.fiscalMonth.endDate as unknown as string | dayjs.Dayjs);
            if (d.isValid()) { periodKey = d.format('YYYY-MM'); }
          }
          if (periodKey) {
            grouped.set(periodKey, (grouped.get(periodKey) ?? 0) + (a.prepaymentAmount ?? 0));
          }
        });
        return Array.from(grouped.entries())
          .sort(([a], [b]) => a.localeCompare(b))
          .slice(-12)
          .map(([label, amount]) => ({ label, amount }));
      }),
      catchError(() => of([] as PrepaymentsTrendPoint[]))
    );
  }

  // ── Recent 5 compilation runs ───────────────────────────────────────────────
  private fetchRecentCompilations(): Observable<IPrepaymentCompilationRequest[]> {
    return this.compilationRequestService.query({
      sort: ['timeOfRequest,desc'],
      size: 5,
      page: 0,
    }).pipe(
      map(res => res.body ?? []),
      catchError(() => of([] as IPrepaymentCompilationRequest[]))
    );
  }
}
