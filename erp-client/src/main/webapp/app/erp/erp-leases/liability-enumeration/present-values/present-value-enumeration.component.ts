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
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { forkJoin, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import * as XLSX from 'xlsx';
import { LiabilityEnumerationService } from '../service/liability-enumeration.service';
import { AlertService } from 'app/core/util/alert.service';
import { ILiabilityEnumeration, IPresentValueEnumeration } from '../liability-enumeration.model';
import { Store } from '@ngrx/store';
import { selectSelectedLiabilityEnumerationBookingId } from '../state/liability-enumeration.selectors';
import { formatCurrency } from '@angular/common';

@Component({
  selector: 'jhi-present-value-enumeration',
  templateUrl: './present-value-enumeration.component.html',
})
export class PresentValueEnumerationComponent implements OnInit, OnDestroy {
  values: IPresentValueEnumeration[] = [];
  liabilityEnumerationId?: number;
  liabilityEnumeration?: ILiabilityEnumeration;
  selectedBookingId?: string | null;
  isLoading = false;
  private readonly destroy$ = new Subject<void>();
  exportingCsv = false;
  exportingExcel = false;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected liabilityEnumerationService: LiabilityEnumerationService,
    protected alertService: AlertService,
    protected store: Store
  ) {}

  ngOnInit(): void {
    this.store
      .select(selectSelectedLiabilityEnumerationBookingId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(bookingId => (this.selectedBookingId = bookingId));

    this.activatedRoute.params.pipe(takeUntil(this.destroy$)).subscribe(params => {
      this.liabilityEnumerationId = Number(params['id']);
      this.load();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  load(): void {
    if (!this.liabilityEnumerationId) {
      return;
    }
    this.isLoading = true;
    forkJoin({
      enumeration: this.liabilityEnumerationService.find(this.liabilityEnumerationId),
      values: this.liabilityEnumerationService.presentValues(this.liabilityEnumerationId, {
        page: 0,
        size: 2000,
        sort: ['sequenceNumber,asc'],
      }),
    }).subscribe({
      next: ({ enumeration, values }: { enumeration: HttpResponse<ILiabilityEnumeration>; values: HttpResponse<IPresentValueEnumeration[]> }) => {
        this.liabilityEnumeration = enumeration.body ?? undefined;
        this.values = values.body ?? [];
        this.isLoading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.isLoading = false;
        this.alertService.addHttpErrorResponse(err);
      },
    });
  }

  get contractLabel(): string {
    if (this.selectedBookingId) {
      return this.selectedBookingId;
    }
    if (this.liabilityEnumeration?.leaseContract?.bookingId) {
      return this.liabilityEnumeration.leaseContract.bookingId;
    }
    if (this.liabilityEnumeration?.leaseContractId) {
      return this.liabilityEnumeration.leaseContractId.toString();
    }
    const valueContractId = this.values.find(value => value.leaseContractId)?.leaseContractId;
    return valueContractId ? valueContractId.toString() : '';
  }

  exportCsv(): void {
    if (!this.values.length) {
      return;
    }
    if (this.exportingCsv) {
      return;
    }
    this.exportingCsv = true;
    const worksheet = this.buildWorksheet();
    const csvContent = XLSX.utils.sheet_to_csv(worksheet);
    this.downloadFile(csvContent, `${this.buildFilename()}.csv`, 'text/csv;charset=utf-8;');
    this.exportingCsv = false;
  }

  exportXlsx(): void {
    if (!this.values.length) {
      return;
    }
    if (this.exportingExcel) {
      return;
    }
    this.exportingExcel = true;
    const worksheet = this.buildWorksheet();
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Present Values');
    XLSX.writeFile(workbook, `${this.buildFilename()}.xlsx`);
    this.exportingExcel = false;
  }

  private buildWorksheet(): XLSX.WorkSheet {
    const rows = this.values.map(value => ({
      Sequence: value.sequenceNumber,
      'Payment date': value.paymentDate,
      'Payment amount': value.paymentAmount,
      'Discount rate': value.discountRate,
      'Present value': value.presentValue,
    }));
    return XLSX.utils.json_to_sheet(rows);
  }

  private buildFilename(): string {
    const base = this.contractLabel || `contract-${this.liabilityEnumerationId ?? 'unknown'}`;
    return `present-values-${base}`;
  }

  private downloadFile(content: string, filename: string, mimeType: string): void {
    const blob = new Blob([content], { type: mimeType });
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = filename;
    link.click();
    window.URL.revokeObjectURL(link.href);
  }

  protected readonly formatCurrency = formatCurrency;
}
