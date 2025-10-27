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

import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { ReportMetadataService } from '../report-metadata/report-metadata.service';
import { IReportMetadata, ReportSummaryRecord } from '../report-metadata/report-metadata.model';
import { ReportSummaryDataService } from './report-summary-data.service';
import { LeasePeriodService } from 'app/entities/leases/lease-period/service/lease-period.service';
import { ILeasePeriod } from 'app/entities/leases/lease-period/lease-period.model';
import { LeaseLiabilityService } from 'app/entities/leases/lease-liability/service/lease-liability.service';
import { ILeaseLiability } from 'app/entities/leases/lease-liability/lease-liability.model';
import { HttpResponse } from '@angular/common/http';

import { buildCsvContent, buildExcelArrayBuffer, deriveOrderedColumns } from './report-summary-export.util';

@Component({
  selector: 'jhi-report-summary-view',
  templateUrl: './report-summary-view.component.html',
  styleUrls: ['./report-summary-view.component.scss'],
})
export class ReportSummaryViewComponent implements OnInit, OnDestroy {
  metadata?: IReportMetadata | null;
  summaryItems: ReportSummaryRecord[] = [];
  displayedColumns: string[] = [];
  loading = false;
  loadingLeasePeriods = false;
  loadingLeaseLiabilities = false;
  errorMessage?: string | null;
  exportErrorMessage?: string | null;
  exportingCsv = false;
  exportingExcel = false;

  leasePeriods: ILeasePeriod[] = [];
  selectedLeasePeriod?: ILeasePeriod | null;
  private leasePeriodSearchTerm?: string;

  leaseLiabilities: ILeaseLiability[] = [];
  selectedLeaseLiability?: ILeaseLiability | null;

  private routeSub?: Subscription;
  private pendingFilterRequests = 0;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly titleService: Title,
    private readonly reportMetadataService: ReportMetadataService,
    private readonly summaryDataService: ReportSummaryDataService,
    private readonly leasePeriodService: LeasePeriodService,
    private readonly leaseLiabilityService: LeaseLiabilityService
  ) {}

  ngOnInit(): void {
    this.routeSub = this.activatedRoute.paramMap.subscribe(params => {
      const slug = params.get('slug');
      if (slug) {
        this.fetchMetadata(slug);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
  }

  trackLeasePeriodById(index: number, item: ILeasePeriod): number {
    return item.id ?? index;
  }

  trackLeaseLiabilityById(index: number, item: ILeaseLiability): number {
    return item.id ?? index;
  }

  onLeasePeriodChange(): void {
    this.loadSummaryData();
  }

  onLeasePeriodSearch(term: string): void {
    const normalizedTerm = term?.trim();
    if (normalizedTerm === this.leasePeriodSearchTerm) {
      return;
    }
    this.leasePeriodSearchTerm = normalizedTerm;
    this.fetchLeasePeriods(normalizedTerm, false);
  }

  onLeaseLiabilityChange(): void {
    this.loadSummaryData();
  }

  formatValue(value: unknown): string {
    if (value === null || value === undefined) {
      return '';
    }
    if (dayjs.isDayjs(value)) {
      return value.format('YYYY-MM-DD');
    }
    if (typeof value === 'number') {
      return value.toLocaleString();
    }
    if (typeof value === 'object') {
      try {
        return JSON.stringify(value);
      } catch (err) {
        return String(value);
      }
    }
    return String(value);
  }

  private fetchMetadata(slug: string): void {
    this.errorMessage = null;
    this.metadata = null;
    this.summaryItems = [];
    this.displayedColumns = [];
    this.leasePeriods = [];
    this.selectedLeasePeriod = null;
    this.leasePeriodSearchTerm = undefined;
    this.leaseLiabilities = [];
    this.selectedLeaseLiability = null;
    const pagePath = `reports/view/${slug}`;
    this.reportMetadataService.findByPagePath(pagePath).subscribe({
      next: metadata => {
        if (!metadata) {
          this.errorMessage = 'The requested report configuration was not found.';
          return;
        }
        this.metadata = metadata;
        this.titleService.setTitle(`ERP | ${metadata.reportTitle ?? 'Dynamic report'}`);
        this.prepareFilters(metadata);
      },
      error: () => {
        this.errorMessage = 'Unable to load report metadata. Please try again later.';
      },
    });
  }

  private prepareFilters(metadata: IReportMetadata): void {
    this.pendingFilterRequests = 0;
    let initiated = false;
    if (metadata.displayLeasePeriod) {
      initiated = true;
      this.selectedLeasePeriod = null;
      this.fetchLeasePeriods(undefined, true);
    } else {
      this.leasePeriods = [];
      this.selectedLeasePeriod = null;
    }

    if (metadata.displayLeaseContract) {
      initiated = true;
      this.loadingLeaseLiabilities = true;
      this.selectedLeaseLiability = null;
      this.leaseLiabilityService
        .query({ size: 100, sort: ['id,desc'] })
        .pipe(finalize(() => {
          this.loadingLeaseLiabilities = false;
          this.handleFilterRequestCompletion();
        }))
        .subscribe({
          next: (res: HttpResponse<ILeaseLiability[]>) => {
            this.leaseLiabilities = res.body ?? [];
            if (this.leaseLiabilities.length > 0 && !this.selectedLeaseLiability) {
              this.selectedLeaseLiability = this.leaseLiabilities[0];
            }
          },
          error: () => {
            this.errorMessage = 'Failed to load lease liabilities for the report.';
          },
        });
      this.pendingFilterRequests++;
    } else {
      this.leaseLiabilities = [];
      this.selectedLeaseLiability = null;
    }

    if (!initiated) {
      this.loadSummaryData();
    }
  }

  private loadSummaryData(): void {
    if (!this.metadata) {
      return;
    }
    if (!this.metadata.backendApi) {
      this.summaryItems = [];
      this.displayedColumns = [];
      this.errorMessage = 'This report is not yet linked to a data endpoint.';
      return;
    }
    this.errorMessage = null;
    this.loading = true;
    this.summaryDataService
      .fetchSummary(this.metadata.backendApi, this.buildQueryParams())
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: data => {
          this.summaryItems = data;
          this.updateDisplayedColumns(data);
          if (!data.length) {
            this.errorMessage = 'No summary data was returned for the selected parameters.';
          } else {
            this.errorMessage = null;
          }
        },
        error: () => {
          this.summaryItems = [];
          this.displayedColumns = [];
          this.errorMessage = 'Unable to load report data. Please verify your selections and try again.';
        },
      });
  }

  private fetchLeasePeriods(term?: string, trackCompletion = false): void {
    const request: Record<string, unknown> = { size: 100, sort: ['id,desc'] };
    if (term) {
      request['periodCode.contains'] = term;
    }

    if (trackCompletion) {
      this.pendingFilterRequests++;
    }

    this.loadingLeasePeriods = true;
    this.leasePeriodService
      .query(request)
      .pipe(
        finalize(() => {
          this.loadingLeasePeriods = false;
          if (trackCompletion) {
            this.handleFilterRequestCompletion();
          }
        })
      )
      .subscribe({
        next: (res: HttpResponse<ILeasePeriod[]>) => {
          const previousSelectionId = this.selectedLeasePeriod?.id;
          this.leasePeriods = res.body ?? [];
          const selectionStillPresent = previousSelectionId
            ? this.leasePeriods.some(period => period.id === previousSelectionId)
            : false;

          if (!selectionStillPresent) {
            if (trackCompletion && !this.selectedLeasePeriod && this.leasePeriods.length > 0) {
              this.selectedLeasePeriod = this.leasePeriods[0];
            } else if (!trackCompletion) {
              this.selectedLeasePeriod = null;
            }
          }
        },
        error: () => {
          this.errorMessage = 'Failed to load lease periods for the report.';
        },
      });
  }

  private updateDisplayedColumns(items: ReportSummaryRecord[]): void {
    if (!items.length) {
      this.displayedColumns = [];
      return;
    }
    this.displayedColumns = deriveOrderedColumns(items, this.displayedColumns);
  }

  private buildQueryParams(): Record<string, unknown> {
    const params: Record<string, unknown> = {};
    if (this.metadata?.displayLeasePeriod && this.selectedLeasePeriod) {
      const key = this.metadata.leasePeriodQueryParam ?? 'leasePeriodId.equals';
      const value = this.resolveLeasePeriodValue(key, this.selectedLeasePeriod);
      if (value !== undefined) {
        params[key] = value;
      }
    }
    if (this.metadata?.displayLeaseContract && this.selectedLeaseLiability) {
      const key = this.metadata.leaseContractQueryParam ?? 'leaseLiabilityId.equals';
      const identifier = this.selectedLeaseLiability.id ?? this.selectedLeaseLiability.leaseId;
      if (identifier !== undefined && identifier !== null) {
        params[key] = identifier;
      }
    }
    return params;
  }

  private resolveLeasePeriodValue(paramKey: string, period: ILeasePeriod): string | number | undefined {
    if (paramKey.toLowerCase().includes('date')) {
      if (period.endDate) {
        return period.endDate.format('YYYY-MM-DD');
      }
      if (period.startDate) {
        return period.startDate.format('YYYY-MM-DD');
      }
    }
    if (period.id !== undefined) {
      return period.id;
    }
    if (period.periodCode) {
      return period.periodCode;
    }
    return undefined;
  }

  private handleFilterRequestCompletion(): void {
    if (this.pendingFilterRequests > 0) {
      this.pendingFilterRequests--;
    }
    if (this.pendingFilterRequests === 0) {
      this.loadSummaryData();
    }
  }

  trackRow(index: number): number {
    return index;
  }

  exportToCsv(): void {
    this.exportSummary('csv');
  }

  exportToExcel(): void {
    this.exportSummary('xlsx');
  }

  private exportSummary(format: 'csv' | 'xlsx'): void {
    if (!this.metadata?.backendApi) {
      this.exportErrorMessage = 'This report is not linked to a data endpoint, so it cannot be exported.';
      return;
    }

    if (this.exportingCsv || this.exportingExcel) {
      return;
    }

    this.exportErrorMessage = null;
    if (format === 'csv') {
      this.exportingCsv = true;
    } else {
      this.exportingExcel = true;
    }

    this.summaryDataService
      .fetchAllSummary(this.metadata.backendApi, this.buildQueryParams())
      .pipe(
        finalize(() => {
          if (format === 'csv') {
            this.exportingCsv = false;
          } else {
            this.exportingExcel = false;
          }
        })
      )
      .subscribe({
        next: data => {
          if (!data.length) {
            this.exportErrorMessage = 'No data is available for export with the current filters.';
            return;
          }

          const orderedColumns = deriveOrderedColumns(data, this.displayedColumns);

          if (format === 'csv') {
            const csvContent = buildCsvContent(data, orderedColumns, value => this.formatValue(value));
            const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
            this.triggerDownload(blob, 'csv');
          } else {
            const excelBuffer = buildExcelArrayBuffer(data, orderedColumns, value => this.formatValue(value));
            const blob = new Blob([excelBuffer], {
              type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
            });
            this.triggerDownload(blob, 'xlsx');
          }
        },
        error: () => {
          this.exportErrorMessage = 'Unable to export report data. Please try again later.';
        },
      });
  }

  private triggerDownload(blob: Blob, extension: 'csv' | 'xlsx'): void {
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = this.buildExportFileName(extension);
    link.click();
    URL.revokeObjectURL(url);
  }

  private buildExportFileName(extension: string): string {
    const baseTitle = this.metadata?.reportTitle?.trim() ?? 'report-summary';
    const normalizedTitle = baseTitle
      .toLowerCase()
      .replace(/[^a-z0-9]+/g, '-')
      .replace(/^-+|-+$/g, '');
    const safeTitle = normalizedTitle || 'report-summary';
    const timestamp = dayjs().format('YYYYMMDD-HHmmss');
    return `${safeTitle}-${timestamp}.${extension}`;
  }
}
