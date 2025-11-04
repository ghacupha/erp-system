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
import { HttpResponse } from '@angular/common/http';
import { Subscription, forkJoin, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import * as dayjs from 'dayjs';
import * as XLSX from 'xlsx';

import { LeaseLiabilityService } from '../lease-liability/service/lease-liability.service';
import { LeaseLiabilityScheduleItemService } from '../lease-liability-schedule-item/service/lease-liability-schedule-item.service';
import { LeaseRepaymentPeriodService } from '../lease-repayment-period/service/lease-repayment-period.service';
import { IFRS16LeaseContractService } from '../ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { ILeaseLiability } from '../lease-liability/lease-liability.model';
import { ILeaseLiabilityScheduleItem } from '../lease-liability-schedule-item/lease-liability-schedule-item.model';
import { ILeaseRepaymentPeriod } from '../lease-repayment-period/lease-repayment-period.model';
import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract/ifrs-16-lease-contract.model';

interface LeaseLiabilityScheduleSummary {
  cashTotal: number;
  principalTotal: number;
  interestTotal: number;
  outstandingTotal: number;
  interestPayableTotal: number;
}

@Component({
  selector: 'jhi-lease-liability-schedule-view',
  templateUrl: './lease-liability-schedule-view.component.html',
})
export class LeaseLiabilityScheduleViewComponent implements OnInit, OnDestroy {
  leaseContract: IIFRS16LeaseContract | null = null;
  leaseLiability: ILeaseLiability | null = null;
  reportingPeriods: ILeaseRepaymentPeriod[] = [];
  activePeriod: ILeaseRepaymentPeriod | null = null;
  scheduleItems: ILeaseLiabilityScheduleItem[] = [];
  visibleItems: ILeaseLiabilityScheduleItem[] = [];
  summary: LeaseLiabilityScheduleSummary = this.createEmptySummary();
  loading = false;
  loadError?: string;

  private paramSubscription?: Subscription;

  constructor(
    private activatedRoute: ActivatedRoute,
    private leaseLiabilityService: LeaseLiabilityService,
    private leaseLiabilityScheduleItemService: LeaseLiabilityScheduleItemService,
    private leaseRepaymentPeriodService: LeaseRepaymentPeriodService,
    private leaseContractService: IFRS16LeaseContractService
  ) {}

  ngOnInit(): void {
    this.paramSubscription = this.activatedRoute.paramMap
      .pipe(
        switchMap(params => {
          const contractIdParam = params.get('contractId');
          if (!contractIdParam) {
            return of(null);
          }
          const contractId = Number(contractIdParam);
          if (Number.isNaN(contractId)) {
            return of(null);
          }
          this.loading = true;
          this.loadError = undefined;
          return forkJoin({
            contract: this.leaseContractService.find(contractId),
            liability: this.leaseLiabilityService.query({ 'leaseContractId.equals': contractId, size: 1 }),
            periods: this.leaseRepaymentPeriodService.query({
              'leaseContractId.equals': contractId,
              sort: ['sequenceNumber,asc'],
              size: 500,
            }),
            scheduleItems: this.leaseLiabilityScheduleItemService.query({
              'leaseContractId.equals': contractId,
              sort: ['sequenceNumber,asc'],
              size: 1000,
            }),
          });
        })
      )
      .subscribe({
        next: responses => {
          if (!responses) {
            this.loading = false;
            return;
          }
          this.leaseContract = responses.contract.body ?? null;
          this.leaseLiability = this.extractFirst(responses.liability);
          this.reportingPeriods = this.preparePeriods(responses.periods);
          this.scheduleItems = this.prepareScheduleItems(responses.scheduleItems);
          this.visibleItems = [...this.scheduleItems];
          this.setDefaultActivePeriod();
          this.loading = false;
        },
        error: () => {
          this.loading = false;
          this.loadError = 'Unable to load lease liability dashboard data.';
        },
      });
  }

  ngOnDestroy(): void {
    if (this.paramSubscription) {
      this.paramSubscription.unsubscribe();
    }
  }

  get initialLiability(): number {
    return this.leaseLiability?.liabilityAmount ?? 0;
  }

  get startDate(): dayjs.Dayjs | undefined {
    return this.leaseLiability?.startDate ?? undefined;
  }

  get closeDate(): dayjs.Dayjs | undefined {
    if (this.activePeriod?.endDate) {
      return this.activePeriod.endDate;
    }
    return this.reportingPeriods.length > 0
      ? this.reportingPeriods[this.reportingPeriods.length - 1].endDate
      : this.leaseLiability?.endDate ?? undefined;
  }

  onPeriodChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement | null;
    const periodId = selectElement?.value;
    if (!periodId) {
      return;
    }
    const selected = this.reportingPeriods.find(period => `${period.id}` === periodId) ?? null;
    this.setActivePeriod(selected);
  }

  paymentDaysFromPrevious(index: number): number | null {
    if (index === 0) {
      return null;
    }
    const current = this.visibleItems[index];
    const previous = this.visibleItems[index - 1];
    if (!current || !previous) {
      return null;
    }
    const currentStart = current.leasePeriod?.startDate;
    const previousEnd = previous.leasePeriod?.endDate ?? previous.leasePeriod?.startDate;
    if (!currentStart || !previousEnd) {
      return null;
    }
    return currentStart.diff(previousEnd, 'day');
  }

  trackBySequence(_index: number, item: ILeaseLiabilityScheduleItem): number | string | undefined {
    return item.id ?? item.sequenceNumber ?? undefined;
  }

  exportDashboardToExcel(): void {
    if (this.loading || this.visibleItems.length === 0) {
      return;
    }

    const totalColumns = 12;
    type RowCell = { column: number; value: string | number };

    const createEmptyRow = (): (string | number)[] => Array(totalColumns).fill('');
    const worksheetRows: (string | number)[][] = [];
    const merges: XLSX.Range[] = [];

    const addRow = (cells: RowCell[] = []): number => {
      const row = createEmptyRow();
      cells.forEach(cell => {
        if (cell.column >= 0 && cell.column < totalColumns) {
          row[cell.column] = cell.value;
        }
      });
      worksheetRows.push(row);
      return worksheetRows.length - 1;
    };

    const leaseTitle = this.leaseContract?.leaseTitle ?? this.leaseContract?.bookingId ?? '';
    const leaseReference = this.leaseLiability?.leaseId ?? (this.leaseLiability?.id ? `${this.leaseLiability.id}` : '');
    const leaseContractId = this.leaseContract?.id != null ? `${this.leaseContract.id}` : '';
    const reportingPeriodLabel = this.buildReportingPeriodLabel();

    const panelWidth = 4;
    const panelStarts = [0, panelWidth, panelWidth * 2];
    const panels = [
      {
        heading: 'Lease',
        start: panelStarts[0],
        rows: [
          ['Lease title', leaseTitle],
          ['Lease contract ID', leaseContractId],
          ['Lease reference', leaseReference],
        ],
      },
      {
        heading: 'Stats',
        start: panelStarts[1],
        rows: [
          ['Initial liability', this.formatNumberForExport(this.initialLiability)],
          ['Cash payments (period)', this.formatNumberForExport(this.summary.cashTotal)],
          ['Principal settled', this.formatNumberForExport(this.summary.principalTotal)],
          ['Interest paid', this.formatNumberForExport(this.summary.interestTotal)],
        ],
      },
      {
        heading: 'Reporting',
        start: panelStarts[2],
        rows: [
          ['Reporting period', reportingPeriodLabel],
          ['Schedule start', this.formatDateForExport(this.startDate)],
          ['Reporting close', this.formatDateForExport(this.closeDate)],
          ['Outstanding balance', this.formatNumberForExport(this.summary.outstandingTotal)],
          ['Interest payable', this.formatNumberForExport(this.summary.interestPayableTotal)],
        ],
      },
    ];

    const titleRowIndex = addRow([{ column: 0, value: 'Lease Liability Schedule Dashboard' }]);
    merges.push({ s: { r: titleRowIndex, c: 0 }, e: { r: titleRowIndex, c: totalColumns - 1 } });

    addRow();

    const headingsRowIndex = addRow(
      panels.map(panel => ({ column: panel.start, value: panel.heading }))
    );
    panels.forEach(panel =>
      merges.push({ s: { r: headingsRowIndex, c: panel.start }, e: { r: headingsRowIndex, c: panel.start + panelWidth - 1 } })
    );

    const maxPanelRows = Math.max(...panels.map(panel => panel.rows.length));
    for (let rowIndex = 0; rowIndex < maxPanelRows; rowIndex++) {
      const cells: RowCell[] = [];
      const panelsWithValues: Array<(typeof panels)[number]> = [];
      panels.forEach(panel => {
        const entry = panel.rows[rowIndex];
        if (entry) {
          const [label, value] = entry;
          cells.push({ column: panel.start, value: label });
          cells.push({ column: panel.start + 1, value });
          panelsWithValues.push(panel);
        }
      });
      const currentRowIndex = addRow(cells);
      panelsWithValues.forEach(panel =>
        merges.push({ s: { r: currentRowIndex, c: panel.start + 1 }, e: { r: currentRowIndex, c: panel.start + panelWidth - 1 } })
      );
    }

    addRow();

    const scheduleHeadingRowIndex = addRow([{ column: 0, value: 'Monthly schedule' }]);
    const scheduleColumnCount = 11;
    merges.push({ s: { r: scheduleHeadingRowIndex, c: 0 }, e: { r: scheduleHeadingRowIndex, c: scheduleColumnCount - 1 } });

    const scheduleHeaders = [
      '#',
      'Period',
      'Start',
      'End',
      'Days since previous',
      'Opening',
      'Cash',
      'Principal',
      'Interest',
      'Outstanding',
      'Interest payable',
    ];
    addRow(scheduleHeaders.map((header, index) => ({ column: index, value: header })));

    this.visibleItems.forEach((item, index) => {
      const daysSincePrevious = this.paymentDaysFromPrevious(index);
      const rowValues: (string | number)[] = [
        item.sequenceNumber ?? index + 1,
        this.formatPeriodLabel(item.leasePeriod),
        this.formatDateForExport(item.leasePeriod?.startDate),
        this.formatDateForExport(item.leasePeriod?.endDate),
        daysSincePrevious ?? '—',
        this.formatNumberForExport(item.openingBalance),
        this.formatNumberForExport(item.cashPayment),
        this.formatNumberForExport(item.principalPayment),
        this.formatNumberForExport(item.interestPayment),
        this.formatNumberForExport(item.outstandingBalance),
        this.formatNumberForExport(item.interestPayableClosing),
      ];
      addRow(rowValues.map((value, column) => ({ column, value })));
    });

    const worksheet = XLSX.utils.aoa_to_sheet(worksheetRows);
    worksheet['!merges'] = merges;
    worksheet['!cols'] = [
      { wch: 18 },
      { wch: 28 },
      { wch: 18 },
      { wch: 18 },
      { wch: 20 },
      { wch: 18 },
      { wch: 18 },
      { wch: 18 },
      { wch: 18 },
      { wch: 20 },
      { wch: 20 },
      { wch: 18 },
    ];

    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Lease Liability Schedule');
    const excelBuffer = XLSX.write(workbook, { type: 'array', bookType: 'xlsx' });

    const blob = new Blob([excelBuffer], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    });
    this.triggerExcelDownload(blob);
  }

  private buildReportingPeriodLabel(): string {
    if (!this.activePeriod) {
      return 'Full schedule';
    }
    const code = this.activePeriod.periodCode;
    const range = [this.formatDateForExport(this.activePeriod.startDate), this.formatDateForExport(this.activePeriod.endDate)]
      .filter(value => !!value)
      .join(' – ');
    if (code && range) {
      return `${code} (${range})`;
    }
    if (code) {
      return code;
    }
    return range || 'Selected period';
  }

  private formatNumberForExport(value: number | null | undefined): string {
    if (value === null || value === undefined) {
      return '';
    }
    return value.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  private formatDateForExport(value: dayjs.Dayjs | undefined): string {
    if (!value) {
      return '';
    }
    return value.format('DD MMM YYYY');
  }

  private formatPeriodLabel(period: ILeaseRepaymentPeriod | undefined): string {
    if (!period) {
      return '';
    }
    return period.periodCode ?? this.formatDateForExport(period.startDate);
  }

  private triggerExcelDownload(blob: Blob): void {
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = this.buildExportFileName();
    link.click();
    URL.revokeObjectURL(url);
  }

  private buildExportFileName(): string {
    const leaseRef = this.leaseLiability?.leaseId ?? (this.leaseLiability?.id ? `lease-${this.leaseLiability.id}` : 'lease');
    const periodPart = this.activePeriod?.periodCode
      ?? this.activePeriod?.startDate?.format('YYYYMM')
      ?? 'full-schedule';
    const sanitize = (value: string): string =>
      value
        .toLowerCase()
        .replace(/[^a-z0-9]+/g, '-')
        .replace(/^-+|-+$/g, '')
        .replace(/-{2,}/g, '-');

    const safeLease = sanitize(String(leaseRef || 'lease')) || 'lease';
    const safePeriod = sanitize(String(periodPart || 'period')) || 'period';
    const timestamp = dayjs().format('YYYYMMDD-HHmmss');
    return `lease-liability-schedule-${safeLease}-${safePeriod}-${timestamp}.xlsx`;
  }

  private setDefaultActivePeriod(): void {
    if (this.reportingPeriods.length === 0) {
      this.setActivePeriod(null);
      return;
    }
    this.setActivePeriod(this.reportingPeriods[this.reportingPeriods.length - 1]);
  }

  private setActivePeriod(period: ILeaseRepaymentPeriod | null): void {
    this.activePeriod = period;
    const periodItems = this.filterItemsForPeriod(period);
    this.summary = this.computeSummary(periodItems);
  }

  private filterItemsForPeriod(period: ILeaseRepaymentPeriod | null): ILeaseLiabilityScheduleItem[] {
    if (!period) {
      return [...this.scheduleItems];
    }
    return this.scheduleItems.filter(item => item.leasePeriod && item.leasePeriod.id === period.id);
  }

  private computeSummary(items: ILeaseLiabilityScheduleItem[]): LeaseLiabilityScheduleSummary {
    return items.reduce(
      (accumulator, item) => ({
        cashTotal: accumulator.cashTotal + (item.cashPayment ?? 0),
        principalTotal: accumulator.principalTotal + (item.principalPayment ?? 0),
        interestTotal: accumulator.interestTotal + (item.interestPayment ?? 0),
        outstandingTotal: accumulator.outstandingTotal + (item.outstandingBalance ?? 0),
        interestPayableTotal: accumulator.interestPayableTotal + (item.interestPayableClosing ?? 0),
      }),
      this.createEmptySummary()
    );
  }

  private extractFirst(response: HttpResponse<ILeaseLiability[]>): ILeaseLiability | null {
    const body = response.body ?? [];
    if (body.length === 0) {
      return null;
    }
    return body[0];
  }

  private preparePeriods(response: HttpResponse<ILeaseRepaymentPeriod[]>): ILeaseRepaymentPeriod[] {
    const periods = (response.body ?? []).map(period => ({
      ...period,
      startDate: period.startDate ? dayjs(period.startDate) : undefined,
      endDate: period.endDate ? dayjs(period.endDate) : undefined,
    }));
    return periods.sort((left, right) => {
      const leftSeq = left.sequenceNumber ?? 0;
      const rightSeq = right.sequenceNumber ?? 0;
      if (leftSeq === rightSeq) {
        const leftEnd = left.endDate ? left.endDate.valueOf() : 0;
        const rightEnd = right.endDate ? right.endDate.valueOf() : 0;
        return leftEnd - rightEnd;
      }
      return leftSeq - rightSeq;
    });
  }

  private prepareScheduleItems(response: HttpResponse<ILeaseLiabilityScheduleItem[]>): ILeaseLiabilityScheduleItem[] {
    const items = (response.body ?? []).map(item => ({
      ...item,
      leasePeriod: item.leasePeriod
        ? {
            ...item.leasePeriod,
            startDate: item.leasePeriod.startDate ? dayjs(item.leasePeriod.startDate) : undefined,
            endDate: item.leasePeriod.endDate ? dayjs(item.leasePeriod.endDate) : undefined,
          }
        : undefined,
    }));
    return items.sort((left, right) => {
      const leftSeq = left.sequenceNumber ?? 0;
      const rightSeq = right.sequenceNumber ?? 0;
      return leftSeq - rightSeq;
    });
  }

  private createEmptySummary(): LeaseLiabilityScheduleSummary {
    return {
      cashTotal: 0,
      principalTotal: 0,
      interestTotal: 0,
      outstandingTotal: 0,
      interestPayableTotal: 0,
    };
  }
}
