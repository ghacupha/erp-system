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
import { ChartData, ChartOptions } from 'chart.js';

import { PrepaymentsDashboardService } from './prepayments-dashboard.service';
import { IPrepaymentAccountReport } from '../prepayment-account-report/prepayment-account-report.model';
import { IPrepaymentCompilationRequest } from '../prepayment-compilation-request/prepayment-compilation-request.model';
import { CompilationStatusTypes } from '../../erp-common/enumerations/compilation-status-types.model';

@Component({
  selector: 'jhi-prepayments-dashboard',
  templateUrl: './prepayments-dashboard.component.html',
  styleUrls: ['./prepayments-dashboard.component.scss'],
})
export class PrepaymentsDashboardComponent implements OnInit, OnDestroy {
  CompilationStatusTypes = CompilationStatusTypes;

  // Populated from the latest PrepaymentAccount.recognitionDate
  dataAsOf = '';

  // KPI values
  totalAccounts = 0;
  totalPrepaymentAmount = 0;
  totalAmortisedAmount = 0;
  totalOutstandingAmount = 0;
  amortisationPercent = 0;

  // Table data
  topAccounts: IPrepaymentAccountReport[] = [];
  recentCompilations: IPrepaymentCompilationRequest[] = [];

  // Amortization trend bar chart
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

  // Amortised vs Outstanding doughnut chart
  doughnutChartData: ChartData<'doughnut'> = {
    labels: ['Amortised', 'Outstanding'],
    datasets: [{
      data: [0, 0],
      backgroundColor: ['rgba(40, 167, 69, 0.8)', 'rgba(255, 193, 7, 0.8)'],
      borderColor: ['rgba(40, 167, 69, 1)', 'rgba(255, 193, 7, 1)'],
      borderWidth: 1,
    }],
  };
  doughnutChartOptions: ChartOptions<'doughnut'> = {
    responsive: true,
    plugins: {
      legend: { position: 'bottom' },
      tooltip: {
        callbacks: {
          label: ctx =>
            ` ${ctx.label as string}: ${(ctx.parsed as number).toLocaleString(undefined, { minimumFractionDigits: 2 })}`,
        },
      },
    },
  };

  isLoading = true;

  private readonly destroy$ = new Subject<void>();

  constructor(private dashboardService: PrepaymentsDashboardService) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  compilationStatusClass(status: CompilationStatusTypes | null | undefined): string {
    switch (status) {
      case CompilationStatusTypes.COMPLETE:    return 'badge-success';
      case CompilationStatusTypes.IN_PROGRESS: return 'badge-warning';
      case CompilationStatusTypes.STARTED:     return 'badge-info';
      case CompilationStatusTypes.REVERSED:    return 'badge-danger';
      default:                                 return 'badge-secondary';
    }
  }

  private loadDashboard(): void {
    this.isLoading = true;

    this.dashboardService.load()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: data => {
          this.dataAsOf = data.dataAsOf;
          // KPI totals
          this.totalAccounts          = data.totals.numberOfPrepaymentAccounts;
          this.totalPrepaymentAmount  = data.totals.totalPrepaymentAmount;
          this.totalAmortisedAmount   = data.totals.totalAmortisedAmount;
          this.totalOutstandingAmount = data.totals.totalOutstandingAmount;
          this.amortisationPercent =
            this.totalPrepaymentAmount > 0
              ? Math.round((this.totalAmortisedAmount / this.totalPrepaymentAmount) * 100)
              : 0;

          // Tables
          this.topAccounts       = data.topAccounts;
          this.recentCompilations = data.recentCompilations;

          // Doughnut — updated after totals are resolved
          this.doughnutChartData = {
            labels: ['Amortised', 'Outstanding'],
            datasets: [{
              data: [this.totalAmortisedAmount, this.totalOutstandingAmount],
              backgroundColor: ['rgba(40, 167, 69, 0.8)', 'rgba(255, 193, 7, 0.8)'],
              borderColor:     ['rgba(40, 167, 69, 1)',   'rgba(255, 193, 7, 1)'  ],
              borderWidth: 1,
            }],
          };

          // Trend bar chart
          const trend = data.trend;
          this.trendChartData = {
            labels: trend.map(t => t.label),
            datasets: [{
              data:  trend.map(t => t.amount),
              label: 'Amortization',
              backgroundColor: trend.map((_, i, arr) =>
                i === arr.length - 1 ? 'rgba(40, 167, 69, 0.85)' : 'rgba(54, 162, 235, 0.65)'
              ),
              borderColor: trend.map((_, i, arr) =>
                i === arr.length - 1 ? 'rgba(40, 167, 69, 1)' : 'rgba(54, 162, 235, 1)'
              ),
              borderWidth: 1,
            }],
          };

          this.isLoading = false;
        },
        error: () => {
          // forkJoin with catchError in the service means this path is only
          // reached on truly unexpected failures (e.g. network-level error)
          this.isLoading = false;
        },
      });
  }
}
