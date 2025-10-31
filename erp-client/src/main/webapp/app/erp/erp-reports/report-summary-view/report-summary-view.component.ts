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
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { Store } from '@ngrx/store';

import { ReportMetadataService } from '../report-metadata/report-metadata.service';
import {
  IReportFilterDefinition,
  IReportMetadata,
  ReportSummaryRecord,
} from '../report-metadata/report-metadata.model';
import { ReportSummaryDataService } from './report-summary-data.service';
import { ReportFilterOption, ReportFilterOptionService } from './report-filter-option.service';

import { buildCsvContent, buildExcelArrayBuffer, deriveOrderedColumns } from './report-summary-export.util';
import { State } from 'app/erp/store/global-store.definition';
import { selectedLeaseContractIdForReport } from 'app/erp/store/selectors/ifrs16-lease-contract-report.selectors';
import { ifrs16LeaseContractReportReset } from 'app/erp/store/actions/ifrs16-lease-contract-report.actions';

interface ReportFilterState {
  definition: IReportFilterDefinition;
  options: ReportFilterOption[];
  selected: ReportFilterOption | null;
  loading: boolean;
  error: string | null;
  lastSearchTerm?: string;
}

@Component({
  selector: 'jhi-report-summary-view',
  templateUrl: './report-summary-view.component.html',
  styleUrls: ['./report-summary-view.component.scss'],
})
export class ReportSummaryViewComponent implements OnInit, OnDestroy {
  private static readonly LEASE_LIABILITY_SCHEDULE_SLUG = 'lease-liability-schedule-report';

  metadata?: IReportMetadata | null;
  summaryItems: ReportSummaryRecord[] = [];
  displayedColumns: string[] = [];
  loading = false;
  errorMessage?: string | null;
  exportErrorMessage?: string | null;
  exportingCsv = false;
  exportingExcel = false;

  filters: ReportFilterState[] = [];

  private routeSub?: Subscription;
  private pendingFilterRequests = 0;
  private leaseContractSelectionSub?: Subscription;
  private selectedLeaseContractId?: number;
  private leaseContractFilterKey?: string;
  private activeSlug?: string;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly titleService: Title,
    private readonly reportMetadataService: ReportMetadataService,
    private readonly summaryDataService: ReportSummaryDataService,
    private readonly filterOptionService: ReportFilterOptionService,
    private readonly router: Router,
    private readonly store: Store<State>
  ) {}

  ngOnInit(): void {
    this.routeSub = this.activatedRoute.paramMap.subscribe(params => {
      const slug = params.get('slug');
      if (slug) {
        this.handleSlugChange(slug);
        this.fetchMetadata(slug);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
    this.teardownLeaseContractSelection();
    this.store.dispatch(ifrs16LeaseContractReportReset());
  }

  trackFilterByQueryKey(index: number, filter: ReportFilterState): string {
    return filter.definition.queryParameterKey ?? String(index);
  }

  trackFilterOptionByValue(option: ReportFilterOption): unknown {
    return option?.value ?? option;
  }

  onFilterSelectionChange(filter: ReportFilterState, option: ReportFilterOption | null): void {
    filter.selected = option;
    this.loadSummaryData();
  }

  onFilterSearch(filter: ReportFilterState, term: string | undefined): void {
    const normalizedTerm = term?.trim();
    if (normalizedTerm === filter.lastSearchTerm) {
      return;
    }
    filter.lastSearchTerm = normalizedTerm;
    this.fetchFilterOptions(filter, normalizedTerm ?? undefined, false);
  }

  buildFilterControlId(filter: ReportFilterState): string {
    const base = filter.definition.queryParameterKey || filter.definition.label || 'filter';
    return base
      .toLowerCase()
      .replace(/[^a-z0-9]+/g, '-')
      .replace(/^-+|-+$/g, '')
      .replace(/-{2,}/g, '-');
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

  private handleSlugChange(slug: string): void {
    this.activeSlug = slug;
    if (slug === ReportSummaryViewComponent.LEASE_LIABILITY_SCHEDULE_SLUG) {
      if (!this.leaseContractSelectionSub) {
        this.leaseContractSelectionSub = this.store
          .select(selectedLeaseContractIdForReport)
          .subscribe(id => {
            if (typeof id === 'number') {
              this.selectedLeaseContractId = id;
              this.applyLeaseContractSelectionToFilters();
            } else {
              this.selectedLeaseContractId = undefined;
              void this.redirectToLeaseReportNav();
            }
          });
      }
    } else {
      this.teardownLeaseContractSelection();
    }
  }

  private teardownLeaseContractSelection(): void {
    this.leaseContractSelectionSub?.unsubscribe();
    this.leaseContractSelectionSub = undefined;
    this.selectedLeaseContractId = undefined;
  }

  private redirectToLeaseReportNav(): Promise<boolean> {
    if (this.activeSlug !== ReportSummaryViewComponent.LEASE_LIABILITY_SCHEDULE_SLUG) {
      return Promise.resolve(false);
    }
    const currentUrl = this.router.url ?? '';
    if (currentUrl.startsWith('/lease-liability-schedule-report/report-nav')) {
      return Promise.resolve(false);
    }
    if (currentUrl.startsWith('/reports/view/lease-liability-schedule-report')) {
      return this.router.navigate(['/lease-liability-schedule-report/report-nav']);
    }
    return Promise.resolve(false);
  }

  private applyLeaseContractSelectionToFilters(target?: ReportFilterState): void {
    if (this.activeSlug !== ReportSummaryViewComponent.LEASE_LIABILITY_SCHEDULE_SLUG) {
      return;
    }
    if (this.selectedLeaseContractId === undefined || this.pendingFilterRequests > 0) {
      return;
    }
    const filterKey = this.leaseContractFilterKey ?? 'leaseLiabilityId.equals';
    const filter = target ?? this.filters.find(f => f.definition.queryParameterKey === filterKey);
    if (!filter) {
      return;
    }
    if (filter.selected?.value === this.selectedLeaseContractId) {
      return;
    }
    const existingOption = filter.options.find(option => option.value === this.selectedLeaseContractId);
    if (existingOption) {
      filter.selected = existingOption;
      this.loadSummaryData();
      return;
    }
    const placeholder: ReportFilterOption = {
      value: this.selectedLeaseContractId,
      label: `Lease contract #${this.selectedLeaseContractId}`,
    };
    filter.options = [placeholder, ...filter.options];
    filter.selected = placeholder;
    this.loadSummaryData();
  }

  private fetchMetadata(slug: string): void {
    const loadMetadata = () => {
      this.errorMessage = null;
      this.metadata = null;
      this.summaryItems = [];
      this.displayedColumns = [];
      this.filters = [];
      this.pendingFilterRequests = 0;
      this.leaseContractFilterKey = undefined;
      const pagePath = `reports/view/${slug}`;
      this.reportMetadataService.findByPagePath(pagePath).subscribe({
        next: metadata => {
          if (!metadata) {
            this.errorMessage = 'The requested report configuration was not found.';
            return;
          }
          this.metadata = metadata;
          this.titleService.setTitle(`ERP | ${metadata.reportTitle ?? 'Dynamic report'}`);
          this.initializeFilters(this.deriveFilterDefinitions(metadata));
        },
        error: () => {
          this.errorMessage = 'Unable to load report metadata. Please try again later.';
        },
      });
    };

    if (slug === ReportSummaryViewComponent.LEASE_LIABILITY_SCHEDULE_SLUG) {
      this.redirectToLeaseReportNav()
        .then(navigated => {
          if (!navigated) {
            loadMetadata();
          }
        })
        .catch(() => {
          loadMetadata();
        });
      return;
    }

    loadMetadata();
  }

  private initializeFilters(definitions: IReportFilterDefinition[]): void {
    this.filters = definitions.map(definition => ({
      definition,
      options: [],
      selected: null,
      loading: false,
      error: null,
      lastSearchTerm: undefined,
    }));
    this.pendingFilterRequests = 0;

    if (!this.filters.length) {
      this.loadSummaryData();
      return;
    }

    for (const filter of this.filters) {
      this.fetchFilterOptions(filter, undefined, true);
    }
  }

  private deriveFilterDefinitions(metadata: IReportMetadata): IReportFilterDefinition[] {
    this.leaseContractFilterKey = undefined;
    if (metadata.filters && metadata.filters.length) {
      const contractFilter = metadata.filters.find(
        filter => filter?.valueSource === 'leaseContracts' && !!filter.queryParameterKey
      );
      if (contractFilter?.queryParameterKey) {
        this.leaseContractFilterKey = contractFilter.queryParameterKey;
      }
      return metadata.filters.filter(
        (filter): filter is IReportFilterDefinition =>
          !!filter && !!filter.queryParameterKey && !!filter.valueSource && !!filter.label
      );
    }

    const definitions: IReportFilterDefinition[] = [];

    if (metadata.displayLeasePeriod) {
      definitions.push({
        label: 'Lease period',
        queryParameterKey: metadata.leasePeriodQueryParam ?? 'leasePeriodId.equals',
        valueSource: 'leasePeriods',
        uiHint: 'dropdown',
      });
    }

    if (metadata.displayLeaseContract) {
      this.leaseContractFilterKey = metadata.leaseContractQueryParam ?? 'leaseLiabilityId.equals';
      definitions.push({
        label: 'Lease liability',
        queryParameterKey: metadata.leaseContractQueryParam ?? 'leaseLiabilityId.equals',
        valueSource: 'leaseContracts',
        uiHint: 'typeahead',
      });
    }

    return definitions;
  }

  private loadSummaryData(): void {
    if (!this.metadata || this.pendingFilterRequests > 0) {
      return;
    }
    if (this.filters.some(filter => filter.error)) {
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
    const queryParams = this.buildQueryParams();
    this.summaryDataService
      .fetchSummary(this.metadata.backendApi, queryParams)
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

  private fetchFilterOptions(filter: ReportFilterState, term?: string, trackCompletion = false): void {
    const searchTerm = term?.trim();
    if (trackCompletion) {
      this.pendingFilterRequests++;
    }

    filter.lastSearchTerm = searchTerm;
    filter.loading = true;
    filter.error = null;

    this.filterOptionService
      .loadOptions(filter.definition, searchTerm)
      .pipe(
        finalize(() => {
          filter.loading = false;
          if (trackCompletion) {
            this.handleFilterRequestCompletion();
          }
        })
      )
      .subscribe({
        next: options => {
          filter.options = options;
          const previousValue = filter.selected?.value;
          const matchingOption =
            previousValue !== undefined && previousValue !== null
              ? options.find(option => option.value === previousValue)
              : undefined;

          if (matchingOption) {
            filter.selected = matchingOption;
          } else if (trackCompletion && options.length > 0) {
            filter.selected = options[0];
          } else {
            filter.selected = null;
          }

          this.applyLeaseContractSelectionToFilters(filter);
        },
        error: () => {
          filter.options = [];
          filter.selected = null;
          filter.error = `Failed to load options for ${filter.definition.label}.`;
          if (!this.errorMessage) {
            this.errorMessage = filter.error;
          }
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

    for (const filter of this.filters) {
      const key = filter.definition.queryParameterKey?.trim();
      if (!key) {
        continue;
      }
      const value = filter.selected?.value;
      if (value === undefined || value === null) {
        continue;
      }
      if (typeof value === 'string' && value.trim() === '') {
        continue;
      }
      params[key] = value;
    }

    return params;
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
