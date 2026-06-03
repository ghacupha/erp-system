/* eslint-disable @typescript-eslint/no-unnecessary-type-assertion */
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

import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import * as dayjs from 'dayjs';
import { ChartData, ChartOptions } from 'chart.js';

import { DATE_FORMAT } from 'app/config/input.constants';
import { PrepaymentAccountReportService } from '../prepayment-account-report/service/prepayment-account-report.service';
import { PrepaymentAmortizationService } from '../prepayment-amortization/service/prepayment-amortization.service';
import { IPrepaymentAccountReport } from '../prepayment-account-report/prepayment-account-report.model';

@Component({
  selector: 'jhi-prepayments-dashboard',
  templateUrl: './prepayments-dashboard.component.html',
  styleUrls: ['./prepayments-dashboard.component.scss'],
})
export class PrepaymentsDashboardComponent implements OnInit, OnDestroy {
  // KPI totals
  totalAccounts = 0;
  totalPrepaymentAmount = 0;
  totalAmortisedAmount = 0;
  totalOutstandingAmount = 0;
  amortisationPercent = 0;

  // Top accounts table (top 10 by outstanding balance)
  topAccounts: IPrepaymentAccountReport[] = [];

  // Amortization trend — last 12 fiscal months
  trendChartData: ChartData<'bar'> = { labels: [], datasets: [] };
  trendChartOptions: ChartOptions<'bar'> = {
    responsive: true,
    plugins: {
      legend: { display: false },
      tooltip: {
        callbacks: {
          label: ctx => ' ' + (ctx.parsed.y as number).toLocaleString(undefined, { minimumFractionDigits: 2 }),
        },
      },
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          callback: (value: string | number) =>
            typeof value === 'number' ? value.toLocaleString() : value,
        },
      },
    },
  };

  reportsLoading = true;
  trendLoading = true;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountReportService: PrepaymentAccountReportService,
    private amortizationService: PrepaymentAmortizationService
  ) {}

  ngOnInit(): void {
    this.loadAccountReports();
    this.loadTrend();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadAccountReports(): void {
    this.accountReportService
      .query({ size: 2000, sort: 'outstandingAmount,desc' })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: res => {
          const reports = res.body ?? [];
          this.totalAccounts = reports.length;
          this.totalPrepaymentAmount = reports.reduce((s, r) => s + (r.prepaymentAmount ?? 0), 0);
          this.totalAmortisedAmount = reports.reduce((s, r) => s + (r.amortisedAmount ?? 0), 0);
          this.totalOutstandingAmount = reports.reduce((s, r) => s + (r.outstandingAmount ?? 0), 0);
          this.amortisationPercent =
            this.totalPrepaymentAmount > 0
              ? Math.round((this.totalAmortisedAmount / this.totalPrepaymentAmount) * 100)
              : 0;
          this.topAccounts = reports.slice(0, 10);
          this.reportsLoading = false;
        },
        error: () => { this.reportsLoading = false; },
      });
  }

  private loadTrend(): void {
    // Fetch the last 13 months of active amortization entries
    const fromDate = dayjs().subtract(12, 'month').startOf('month').format(DATE_FORMAT);

    this.amortizationService
      .query({
        'prepaymentPeriod.greaterThanOrEqual': fromDate,
        'inactive.equals': false,
        sort: 'prepaymentPeriod,asc',
        size: 2000,
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: res => {
          const amortizations = res.body ?? [];
          // Aggregate amounts by period (YYYY-MM)
          const grouped = new Map<string, number>();
          amortizations.forEach(a => {
            if (a.prepaymentPeriod?.isValid()) {
              const key = a.prepaymentPeriod.format('YYYY-MM');
              grouped.set(key, (grouped.get(key) ?? 0) + (a.prepaymentAmount ?? 0));
            }
          });

          const sorted = Array.from(grouped.entries()).sort((a, b) => a[0].localeCompare(b[0]));
          this.trendChartData = {
            labels: sorted.map(([label]) => label),
            datasets: [
              {
                data: sorted.map(([, amount]) => amount),
                label: 'Amortization',
                backgroundColor: sorted.map((_, i, arr) =>
                  i === arr.length - 1
                    ? 'rgba(40, 167, 69, 0.85)'   // current month: green
                    : 'rgba(54, 162, 235, 0.65)'  // prior months: blue
                ),
                borderColor: sorted.map((_, i, arr) =>
                  i === arr.length - 1 ? 'rgba(40, 167, 69, 1)' : 'rgba(54, 162, 235, 1)'
                ),
                borderWidth: 1,
              },
            ],
          };
          this.trendLoading = false;
        },
        error: () => { this.trendLoading = false; },
      });
  }

  get isLoading(): boolean {
    return this.reportsLoading || this.trendLoading;
  }
}
