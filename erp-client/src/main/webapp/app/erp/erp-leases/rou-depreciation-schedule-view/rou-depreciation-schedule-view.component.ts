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

import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { IFRS16LeaseContractService } from '../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import {
  RouDepreciationScheduleRow,
  RouDepreciationScheduleViewService,
} from './rou-depreciation-schedule-view.service';
import { buildCsvContent, buildExcelArrayBuffer } from 'app/erp/erp-reports/report-summary-view/report-summary-export.util';
import { ReportSummaryRecord } from 'app/erp/erp-reports/report-metadata/report-metadata.model';

@Component({
  selector: 'jhi-rou-depreciation-schedule-view',
  templateUrl: './rou-depreciation-schedule-view.component.html',
})
export class RouDepreciationScheduleViewComponent implements OnInit, OnDestroy {
  selectedContract: IIFRS16LeaseContract = {};
  selectedContractId?: number;
  scheduleRows: RouDepreciationScheduleRow[] = [];
  loading = false;
  loadError?: string;
  exportingCsv = false;
  exportingExcel = false;
  exportError?: string;

  private routeSubscription?: Subscription;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private leaseContractService: IFRS16LeaseContractService,
    private scheduleService: RouDepreciationScheduleViewService
  ) {}

  ngOnInit(): void {
    this.routeSubscription = this.activatedRoute.paramMap.subscribe(params => {
      const idParam = params.get('leaseContractId');
      if (!idParam) {
        this.selectedContractId = undefined;
        this.selectedContract = {};
        this.scheduleRows = [];
        return;
      }
      const parsedId = Number(idParam);
      if (Number.isNaN(parsedId)) {
        this.selectedContractId = undefined;
        this.selectedContract = {};
        this.scheduleRows = [];
        this.loadError = 'The provided lease contract identifier is invalid.';
        return;
      }
      if (this.selectedContractId !== parsedId) {
        this.selectedContractId = parsedId;
        this.ensureSelectedContractLoaded(parsedId);
        this.fetchSchedule(parsedId);
      }
    });
  }

  ngOnDestroy(): void {
    this.routeSubscription?.unsubscribe();
  }

  trackRowById(_index: number, row: RouDepreciationScheduleRow): number | string | undefined {
    return row.entryId ?? row.sequenceNumber ?? row.periodCode;
  }

  onContractSelected(contract: IIFRS16LeaseContract | null): void {
    const id = contract?.id;
    if (id === undefined || id === null) {
      return;
    }
    this.selectedContract = contract;
    if (this.selectedContractId !== id) {
      void this.router.navigate(['/rou-depreciation-schedule-view', id]);
    }
  }

  get initialAmount(): number {
    return this.scheduleRows.length > 0 ? this.scheduleRows[0].initialAmount ?? 0 : 0;
  }

  get totalDepreciation(): number {
    return this.scheduleRows.reduce((sum, row) => sum + (row.depreciationAmount ?? 0), 0);
  }

  get closingBalance(): number {
    if (this.scheduleRows.length === 0) {
      return 0;
    }
    return this.scheduleRows[this.scheduleRows.length - 1].outstandingAmount ?? 0;
  }

  formatDate(value?: dayjs.Dayjs): string {
    return value ? value.format('DD MMM YYYY') : '';
  }

  exportToCsv(): void {
    this.exportSchedule('csv');
  }

  exportToExcel(): void {
    this.exportSchedule('xlsx');
  }

  private ensureSelectedContractLoaded(contractId: number): void {
    if (this.selectedContract?.id === contractId) {
      return;
    }
    this.leaseContractService
      .find(contractId)
      .pipe(catchError(() => of({ body: undefined })))
      .subscribe(contractResponse => {
        if (contractResponse.body) {
          this.selectedContract = contractResponse.body;
        }
      });
  }

  private fetchSchedule(contractId: number): void {
    this.loading = true;
    this.loadError = undefined;
    this.exportError = undefined;
    this.scheduleService
      .loadSchedule(contractId)
      .pipe(
        catchError(() => {
          this.loadError = 'Unable to load depreciation schedule for the selected lease contract.';
          this.loading = false;
          return of([] as RouDepreciationScheduleRow[]);
        })
      )
      .subscribe(rows => {
        this.scheduleRows = rows;
        this.loading = false;
      });
  }

  private exportSchedule(format: 'csv' | 'xlsx'): void {
    if (!this.scheduleRows.length) {
      this.exportError = 'No depreciation schedule data is available to export.';
      return;
    }

    if (this.exportingCsv || this.exportingExcel) {
      return;
    }

    this.exportError = undefined;
    if (format === 'csv') {
      this.exportingCsv = true;
    } else {
      this.exportingExcel = true;
    }

    try {
      const columns = ['Period', 'Start date', 'End date', 'Initial amount', 'Depreciation', 'Outstanding'];
      const rows = this.buildExportRows();

      if (format === 'csv') {
        const csvContent = buildCsvContent(rows, columns, value => this.formatExportValue(value));
        const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
        this.triggerDownload(blob, 'csv');
      } else {
        const excelBuffer = buildExcelArrayBuffer(rows, columns, value => this.formatExportValue(value));
        const blob = new Blob([excelBuffer], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        });
        this.triggerDownload(blob, 'xlsx');
      }
    } catch (error) {
      this.exportError = 'Unable to export the depreciation schedule right now.';
    } finally {
      if (format === 'csv') {
        this.exportingCsv = false;
      } else {
        this.exportingExcel = false;
      }
    }
  }

  private buildExportRows(): ReportSummaryRecord[] {
    return this.scheduleRows.map(row => ({
      Period: row.periodCode ?? '',
      'Start date': row.periodStartDate ? row.periodStartDate.format('YYYY-MM-DD') : '',
      'End date': row.periodEndDate ? row.periodEndDate.format('YYYY-MM-DD') : '',
      'Initial amount': row.initialAmount ?? 0,
      Depreciation: row.depreciationAmount ?? 0,
      Outstanding: row.outstandingAmount ?? 0,
    }));
  }

  private formatExportValue(value: unknown): string {
    if (value === null || value === undefined) {
      return '';
    }
    if (typeof value === 'number') {
      return value.toFixed(2);
    }
    if (dayjs.isDayjs(value)) {
      return value.format('YYYY-MM-DD');
    }
    return String(value);
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
    const contractSegment = this.selectedContractId ? `-${this.selectedContractId}` : '';
    const timestamp = dayjs().format('YYYYMMDD');
    return `rou-depreciation-schedule${contractSegment}-${timestamp}.${extension}`;
  }
}
